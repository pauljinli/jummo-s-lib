package com.stockholm.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.stockholm.common.IntentExtraKey;
import com.stockholm.common.ReleaseSystemCommandInterface;
import com.stockholm.common.develop.DeveloperDialog;
import com.stockholm.common.nightmode.NightModeStateManager;
import com.stockholm.common.notification.NotificationStateManager;
import com.stockholm.common.toast.VolumeToastHelper;
import com.stockholm.common.utils.StockholmLogger;
import com.stockholm.common.utils.WeakHandler;
import com.stockholm.common.widget.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public abstract class ReleaseBaseActivity extends AppCompatActivity implements ReleaseSystemCommandInterface {
    private static final int MSG_POWER = 1001;
    private static final int TIME_AUTO_TO_MAIN_VIEW = 10 * 60 * 1000;
    private final String TAG = this.getClass().getSimpleName();
    private KillProcessReceiver killProcessReceiver;
    private PowerEventReceiver powerEventReceiver;

    protected enum ViewState {
        MAIN, DETAIL
    }

    private WeakHandler handler = new WeakHandler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_POWER) {
                onPowerClick();
            }
        }
    };
    private CountDownTimer countdownTimer;
    private ViewState viewState;
    private ReleaseSystemKeyEventHandler systemKeyEventHandler;
    private NightModeStateManager nightModeStateManager;
    private NotificationStateManager notificationStateManager;

    private DeveloperDialog developerDialog;

    //use for UMeng
    private long enterMainView;
    private long enterDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        hideNavBar();
        initView();
        initSystemKeyHandler();
        nightModeStateManager = new NightModeStateManager(this);
        notificationStateManager = new NotificationStateManager(this);
        initCountdown();
        registerReceiver();
        lazyLoad();
    }
    private void hideNavBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    protected abstract void initInject();

    protected abstract int getLayoutResource();

    protected abstract void init();

    private void initView() {
        viewState = ViewState.MAIN;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (getLayoutResource() != 0) {
            View contentView = inflater.inflate(getLayoutResource(), null);
            setContentView(contentView);
            ButterKnife.bind(this);
        }
    }

    private void initSystemKeyHandler() {
        systemKeyEventHandler = new ReleaseSystemKeyEventHandler(this);
        systemKeyEventHandler.setCommandInterface(this);
    }

    private void initCountdown() {
        countdownTimer = new CountDownTimer(TIME_AUTO_TO_MAIN_VIEW, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                enterMainView();
            }
        };
    }

    private void lazyLoad() {
        getWindow().getDecorView().post(() -> handler.post(this::init));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (viewState == ViewState.MAIN) {
            enterMainView = System.currentTimeMillis();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (viewState == ViewState.DETAIL && needCountdown()) {
            countdownTimer.start();
            enterDetailView = System.currentTimeMillis();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        enterMainView = enterDetailView = 0;
        if (viewState == ViewState.DETAIL && needCountdown()) {
            countdownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    protected boolean needCountdown() {
        return true;
    }

    protected void registerReceiver() {
        killProcessReceiver = new KillProcessReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentExtraKey.ACTION_KILL_PROCESS);
        registerReceiver(killProcessReceiver, filter);

        powerEventReceiver = new PowerEventReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(powerEventReceiver, intentFilter);

        nightModeStateManager.registerNightModeReceiver();
        notificationStateManager.registerNightModeReceiver();
    }

    protected void unregisterReceiver() {
        unregisterReceiver(killProcessReceiver);
        unregisterReceiver(powerEventReceiver);
        nightModeStateManager.unregisterNightModeReceiver();
        notificationStateManager.unregisterNightModeReceiver();
    }

    protected void showProgressDialog() {
        ProgressDialog.create(this).show();
    }

    protected void dismissProgressDialog() {
        ProgressDialog.dismiss();
    }

    protected void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }

    protected boolean isMainView() {
        return viewState == ViewState.MAIN;
    }

    protected boolean isDetailView() {
        return viewState == ViewState.DETAIL;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return systemKeyEventHandler.onKeyDown(keyCode, event, notificationStateManager.isShowing());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return systemKeyEventHandler.onKeyUp(keyCode, event, notificationStateManager.isShowing());
    }

    protected void enterDetailView() {
        int duration = (int) (System.currentTimeMillis() - enterMainView) / 1000;
        MobclickAgent.onEventValue(this, "page1", null, duration);
        enterDetailView = System.currentTimeMillis();
        viewState = ViewState.DETAIL;
        countdownTimer.start();
    }

    protected void enterMainView() {
        int duration = (int) (System.currentTimeMillis() - enterDetailView) / 1000;
        MobclickAgent.onEventValue(this, "page2", null, duration);
        enterDetailView = System.currentTimeMillis();
        viewState = ViewState.MAIN;
        countdownTimer.cancel();
    }

    @Override
    public boolean onAllKeyEvent(KeyEvent event) {
        sendBroadcast(new Intent(IntentExtraKey.ACTION_DISMISS_AUTO_DISPLAY));
        if (nightModeStateManager.isShowing()) {
            sendBroadcast(new Intent(IntentExtraKey.ACTION_ANY_KEY_PRESSED));
            return true;
        }
        if (isDetailView()) {
            countdownTimer.start();
        }
        return false;
    }

    @Override
    public void onControlOkLongClick() {
        MobclickAgent.onEvent(this, "click5");
        StockholmLogger.d(TAG, "onControlOkLongClick");
        if (isMainView()) {
            enterDetailView();
        } else if (isDetailView()) {
            enterMainView();
        }
    }

    @Override
    public void onLineLongDrag() {
        MobclickAgent.onEvent(this, "click11");
        StockholmLogger.d(TAG, "onLineLongDrag");
    }

    @Override
    public void onLineShortDrag() {
        MobclickAgent.onEvent(this, "click10");
        StockholmLogger.d(TAG, "onLineShortDrag");
        sendBroadcast(new Intent(IntentExtraKey.ACTION_OPEN_NEXT_APP));
    }

    @Override
    public void onControlUpClick() {
        MobclickAgent.onEvent(this, "click1");
        StockholmLogger.d(TAG, "onControlUpClick");
    }

    @Override
    public void onControlUpLongPress() {
        MobclickAgent.onEvent(this, "click4");
        StockholmLogger.d(TAG, "onControlUpLongPress");
        VolumeToastHelper.getInstance().showToast(this);
    }

    @Override
    public void onLongPressRaiseVolume() {
        MobclickAgent.onEvent(this, "click7");
        StockholmLogger.d(TAG, "onLongPressRaiseVolume: ");
        VolumeToastHelper.getInstance().updateToast(AudioManager.ADJUST_RAISE);
    }

    @Override
    public void onControlDownClick() {
        MobclickAgent.onEvent(this, "click3");
        StockholmLogger.d(TAG, "onControlDownClick");
    }

    @Override
    public void onControlDownLongPress() {
        MobclickAgent.onEvent(this, "click6");
        StockholmLogger.d(TAG, "onControlDownLongPress");
        VolumeToastHelper.getInstance().showToast(this);
    }

    @Override
    public void onLongPressLowerVolume() {
        MobclickAgent.onEvent(this, "click9");
        StockholmLogger.d(TAG, "onLongPressLowerVolume: ");
        VolumeToastHelper.getInstance().updateToast(AudioManager.ADJUST_LOWER);
    }

    @Override
    public void onControlOKClick() {
        MobclickAgent.onEvent(this, "click2");
        StockholmLogger.d(TAG, "onControlOKClick");
    }

    @Override
    public void onReverseSwitchAppClick() {
        StockholmLogger.d(TAG, "onReverseSwitchAppClick.");
        sendBroadcast(new Intent(IntentExtraKey.ACTION_OPEN_PRE_APP));
    }

    @Override
    public void onPowerClick() {
        StockholmLogger.d(TAG, "onPowerClick: ");
    }

    @Override
    public void onFactoryModeButtonClick() {
        StockholmLogger.d(TAG, "onFactoryModeButtonClick: ");
    }

    @Override
    public void onTestButtonClick() {
        StockholmLogger.d(TAG, "onTestButtonClick.");
    }

    @Override
    public void onNotificationButtonClick() {
        StockholmLogger.d(TAG, "onNotificationButtonClick.");
    }

    @Override
    public void onConnectWifiButtonClick() {
        StockholmLogger.d(TAG, "onConnectWifiButtonClick");
        sendBroadcast(new Intent(IntentExtraKey.ACTION_OPEN_BIND_APP_CONNECT_WIFI));
    }

    @Override
    public void onDebugButtonClick() {
        if (developerDialog == null) {
            developerDialog = new DeveloperDialog(this);
            developerDialog.show();
        } else {
            developerDialog.dismiss();
            developerDialog = null;
        }
    }

    @Override
    public void onVersionButtonClick() {
        sendBroadcast(new Intent(IntentExtraKey.ACTION_SHOW_VERSION_NAME));
    }

    private class KillProcessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentExtraKey.ACTION_KILL_PROCESS)) {
                String packageName = intent.getStringExtra(IntentExtraKey.KEY_PACKAGE_NAME);
                if (context.getPackageName().equals(packageName)) {
                    MobclickAgent.onKillProcess(context);
                    System.exit(0);
                }
            }
        }
    }

    private class PowerEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            StockholmLogger.d("PowerEventReceiver", "onReceive: " + action);
            if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_SCREEN_ON)) {
                handler.sendEmptyMessage(MSG_POWER);
            }
        }
    }

}