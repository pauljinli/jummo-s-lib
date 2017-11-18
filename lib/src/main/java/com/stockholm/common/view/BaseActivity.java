package com.stockholm.common.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.stockholm.common.IntentExtraKey;
import com.stockholm.common.SystemCommandInterface;
import com.stockholm.common.develop.DeveloperDialog;
import com.stockholm.common.nightmode.NightModeStateManager;
import com.stockholm.common.notification.NotificationStateManager;
import com.stockholm.common.toast.VolumeToastHelper;
import com.stockholm.common.utils.StockholmLogger;
import com.stockholm.common.utils.WeakHandler;
import com.stockholm.common.widget.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements SystemCommandInterface {

    private static final int TIME_AUTO_TO_MAIN_VIEW = 10 * 60 * 1000;
    private final String TAG = this.getClass().getSimpleName();
    private KillProcessReceiver killProcessReceiver;

    private enum ViewState {
        MAIN, DETAIL
    }

    private WeakHandler handler;
    private CountDownTimer countdownTimer;
    private ViewState viewState;
    private AudioManager audioManager;
    private SystemKeyEventHandler systemKeyEventHandler;
    private NightModeStateManager nightModeStateManager;
    private NotificationStateManager notificationStateManager;

    private DeveloperDialog developerDialog;

    private long enterMainView;
    private long enterDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        initView();
        initSystemKeyHandler();
        nightModeStateManager = new NightModeStateManager(this);
        notificationStateManager = new NotificationStateManager(this);
        initAudioManager();
        initCountdown();
        registerReceiver();
        lazyLoad();
    }

    protected abstract void initInject();

    protected abstract int getLayoutResource();

    protected abstract void init();

    private void initView() {
        viewState = ViewState.MAIN;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (getLayoutResource() != 0 && inflater != null) {
            View contentView = inflater.inflate(getLayoutResource(), null);
            setContentView(contentView);
            ButterKnife.bind(this);
        }
    }

    private void initSystemKeyHandler() {
        systemKeyEventHandler = new SystemKeyEventHandler(this);
        systemKeyEventHandler.setCommandInterface(this);
    }

    private void initAudioManager() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
        handler = new WeakHandler();
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
        dismissProgressDialog();
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
        registerReceiver(killProcessReceiver, new IntentFilter(IntentExtraKey.ACTION_KILL_PROCESS));

        nightModeStateManager.registerNightModeReceiver();
        notificationStateManager.registerNightModeReceiver();
    }

    protected void unregisterReceiver() {
        unregisterReceiver(killProcessReceiver);
        nightModeStateManager.unregisterNightModeReceiver();
        notificationStateManager.unregisterNightModeReceiver();
    }

    protected void showProgressDialog() {
        ProgressDialog.create(this).show();
    }

    protected void dismissProgressDialog() {
        ProgressDialog.dismiss();
    }

    protected boolean isMainView() {
        return viewState == ViewState.MAIN;
    }

    protected boolean isDetailView() {
        return viewState == ViewState.DETAIL;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return systemKeyEventHandler.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return systemKeyEventHandler.onKeyDown(keyCode, event);
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
    }

    @Override
    public void onControlOKClick() {
        MobclickAgent.onEvent(this, "click2");
        StockholmLogger.d(TAG, "onControlOKClick");
    }

    @Override
    public void onControlOkDoubleClick() {
        StockholmLogger.d(TAG, "onControlOkDoubleClick");
    }

    @Override
    public void onKeyVolumeUpClick() {
        MobclickAgent.onEvent(this, "click7");
        StockholmLogger.d(TAG, "onKeyVolumeUpClick");
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        VolumeToastHelper.getInstance().showToast(this);
    }

    @Override
    public void onKeyVolumeDownClick() {
        MobclickAgent.onEvent(this, "click9");
        StockholmLogger.d(TAG, "onKeyVolumeDownClick");
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        VolumeToastHelper.getInstance().showToast(this);
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

    @Override
    public void onDeviceTestButtonClock() {
        StockholmLogger.d(TAG, "onDeviceTestButtonClock");
    }

    @Override
    public void onFactoryButtonClick() {
        StockholmLogger.d(TAG, "onFactoryButtonClick");
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

}