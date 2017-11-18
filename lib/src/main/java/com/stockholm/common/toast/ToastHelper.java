package com.stockholm.common.toast;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.stockholm.common.R;
import com.stockholm.common.utils.WeakHandler;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public final class ToastHelper {

    private static final long LENGTH_LONG = 3500;
    private static final long LENGTH_SHORT = 2000;

    private static ToastHelper helper;

    private final WeakReference<Context> weakReference;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private View toastView;
    private TextView tvContent;
    private String content;
    private WeakHandler handler = new WeakHandler();

    private ToastHelper(Context context) {
        weakReference = new WeakReference<>(context);
        initWindow();
    }

    public static synchronized ToastHelper getInstance(Context context) {
        if (helper == null) {
            helper = new ToastHelper(context);
        } else if (helper.weakReference.get() != context) {
            helper = new ToastHelper(context);
        }
        return helper;
    }

    private void initWindow() {
        windowManager = (WindowManager) weakReference.get().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.y = 365;
        params.gravity = Gravity.CENTER;
        params.windowAnimations = R.style.ToastAnimation;
        toastView = View.inflate(weakReference.get(), R.layout.layout_toast, null);
        tvContent = ButterKnife.findById(toastView, R.id.tv_content);
    }

    public void showShort(String text) {
        this.showToast(text, LENGTH_SHORT);
    }

    public void showShort(int resId) {
        this.showShort(weakReference.get().getString(resId));
    }

    public void showLong(String text) {
        this.showToast(text, LENGTH_LONG);
    }

    public void showLong(int resId) {
        this.showLong(weakReference.get().getString(resId));
    }

    private void showToast(String text, long duration) {
        if (toastView != null && toastView.getParent() != null) {
            if (text != null && text.equals(content)) {
                handler.removeCallbacksAndMessages(null);
                duration *= 2;
            } else {
                windowManager.removeView(toastView);
                this.content = text;
                tvContent.setText(text);
                windowManager.addView(toastView, params);
            }
        } else {
            this.content = text;
            tvContent.setText(text);
            windowManager.addView(toastView, params);
        }
        dismiss(duration);
    }

    private void dismiss(long duration) {
        handler.postDelayed(() -> {
            if (toastView.getParent() != null) {
                windowManager.removeView(toastView);
            }
        }, duration);
    }

}