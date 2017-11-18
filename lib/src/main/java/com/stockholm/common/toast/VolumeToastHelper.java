package com.stockholm.common.toast;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.stockholm.common.R;
import com.stockholm.common.utils.WeakHandler;
import com.stockholm.common.widget.VolumeAdjustView;

import butterknife.ButterKnife;

public class VolumeToastHelper {

    private static final long LENGTH_SHORT = 1500;
    private static VolumeToastHelper helper;

    private WindowManager windowManager;
    private WeakHandler handler = new WeakHandler();
    private View toastView;
    private VolumeAdjustView volumeAdjustView;

    private boolean isShowing;
    private WindowManager.LayoutParams params;

    public static VolumeToastHelper getInstance() {
        if (helper == null) {
            synchronized (VolumeToastHelper.class) {
                if (helper == null) {
                    helper = new VolumeToastHelper();
                }
            }
        }
        return helper;
    }

    private void initWindow(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (toastView == null) {
            params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.y = 300;
            params.gravity = Gravity.CENTER;
            toastView = View.inflate(context, R.layout.layout_toast_volume, null);
            volumeAdjustView = ButterKnife.findById(toastView, R.id.volume);
        }
    }

    public void showToast(Context context) {
        initWindow(context);
        if (toastView != null) {
            if (isShowing) {
                handler.removeCallbacksAndMessages(null);
            } else {
                windowManager.addView(toastView, params);
                isShowing = true;
            }
            volumeAdjustView.updateCurrentVolume();
            autoDismiss();
        }
    }

    public void updateToast(int direction) {
        handler.removeCallbacksAndMessages(null);
        if (direction == AudioManager.ADJUST_RAISE) {
            volumeAdjustView.raiseCurrentVolume();
        } else if (direction == AudioManager.ADJUST_LOWER) {
            volumeAdjustView.lowerCurrentVolume();
        }
        autoDismiss();
    }

    private void autoDismiss() {
        handler.postDelayed(() -> {
            if (toastView.getParent() != null) {
                windowManager.removeView(toastView);
                isShowing = false;
            }
        }, LENGTH_SHORT);
    }

    public void onDestroy() {
        if (toastView != null && toastView.isAttachedToWindow()) {
            handler.removeCallbacksAndMessages(null);
            windowManager.removeView(toastView);
            toastView = null;
        }
    }

}