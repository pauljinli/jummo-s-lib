package com.stockholm.common.guide;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.stockholm.common.IntentExtraKey;
import com.stockholm.common.R;
import com.stockholm.common.utils.WeakHandler;

public final class UserGuideHelper {

    public static UserGuideHelper helper;

    private WindowManager windowManager;
    private GuideView guideView;
    private GuideStep guideStep;
    private WeakHandler handler = new WeakHandler();

    private UserGuideHelper() {
    }

    public static UserGuideHelper getInstance() {
        if (helper == null) {
            synchronized (UserGuideHelper.class) {
                if (helper == null) {
                    helper = new UserGuideHelper();
                }
            }
        }
        return helper;
    }

    public void showGuide(Context context, GuideStep stepNo) {
        this.guideStep = stepNo;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (guideView == null) {
            guideView = new GuideView(context);
            guideView.setStepNo(stepNo);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.format = PixelFormat.TRANSPARENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.windowAnimations = R.style.UserGuideAnimation;
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            guideView.setLayoutParams(params);
            handler.postDelayed(() -> windowManager.addView(guideView, params), 1000);
        }
    }

    public void dismissGuide(Context context) {
        dismissGuide(context, guideStep);
    }

    private void dismissGuide(Context context, GuideStep step) {
        if (guideView != null && guideView.isAttachedToWindow()) {
            windowManager.removeView(guideView);
            guideView = null;
            if (step == GuideStep.STEP_END) {
                sendGuideFinishBroadcast(context);
            }
            this.guideStep = null;
        }
    }

    public void stopGuide(Context context) {
        dismissGuide(context, GuideStep.STEP_END);
    }

    private void sendGuideFinishBroadcast(Context context) {
        Intent intent = new Intent(IntentExtraKey.ACTION_USER_GUIDE_FINISH);
        context.sendBroadcast(intent);
    }

}