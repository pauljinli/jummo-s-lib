package com.stockholm.common.widget;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.stockholm.common.R;

import java.lang.ref.WeakReference;

public final class ProgressDialog {

    private static Dialog dialog;
    private static ProgressDialog progressDialog;
    private WeakReference<Context> weakReference;

    private ImageView ivLoading;
    private TextView tvInfo;

    private RotateAnimation rotate;

    private ProgressDialog(Context context) {
        weakReference = new WeakReference<>(context);
        dialog = new Dialog(weakReference.get());
        dialog.setContentView(R.layout.dialog_progress);
        ivLoading = (ImageView) dialog.findViewById(R.id.iv_progress);
        tvInfo = (TextView) dialog.findViewById(R.id.tv_progress);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            dismiss();
            return true;
        });

        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1300);
        rotate.setRepeatCount(-1);
        rotate.setInterpolator(new LinearInterpolator());
    }

    public ProgressDialog(Context context, @StringRes int text) {
        this(context);
        if (text != 0) {
            tvInfo.setText(text);
            tvInfo.setVisibility(View.VISIBLE);
        }
    }

    public static ProgressDialog create(Context context) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context);
        }
        return progressDialog;
    }

    public static ProgressDialog create(Context context, @StringRes int text) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context, text);
        }
        return progressDialog;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            ivLoading.startAnimation(rotate);
            dialog.show();
        }
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
            progressDialog = null;
        }
    }
}
