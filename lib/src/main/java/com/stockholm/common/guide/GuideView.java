package com.stockholm.common.guide;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stockholm.common.R;

import butterknife.ButterKnife;


public class GuideView extends LinearLayout {
    private static final String TAG = "GuideView";

    private TextView topTip;
    private TextView keyTip;
    private TextView centerTip;
    private TextView ketHint;
    private LinearLayout layoutPre;
    private LinearLayout layoutNext;
    private LinearLayout layoutOk;
    private LinearLayout layoutLine;
    private LinearLayout layoutHome;
    private ImageView guideArrow;

    private Context context;
    private GuideStep stepNo;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = inflate(context, R.layout.layout_user_guide, this);
        ButterKnife.bind(this, view);
        topTip = ButterKnife.findById(view, R.id.tv_top_tip);
        keyTip = ButterKnife.findById(view, R.id.tv_key_tip);
        centerTip = ButterKnife.findById(view, R.id.tv_center_tip);
        ketHint = ButterKnife.findById(view, R.id.tv_key_hint);
        layoutPre = ButterKnife.findById(view, R.id.layout_pre);
        layoutNext = ButterKnife.findById(view, R.id.layout_next);
        layoutOk = ButterKnife.findById(view, R.id.layout_ok);
        layoutLine = ButterKnife.findById(view, R.id.layout_line);
        layoutHome = ButterKnife.findById(view, R.id.layout_home);
        guideArrow = ButterKnife.findById(view, R.id.iv_arrow);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    public void setStepNo(GuideStep stepNo) {
        this.stepNo = stepNo;
        updateView();
    }

    private void updateView() {
        switch (stepNo) {
            case STEP_START:
                layoutHome.setVisibility(VISIBLE);
                layoutLine.setVisibility(INVISIBLE);
                break;
            case STEP_ONE:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_1);
                break;
            case TEP_TWO:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_2);
                topTip.setText(R.string.guide_step_2_tip);
                layoutPre.setVisibility(VISIBLE);
                layoutNext.setVisibility(VISIBLE);
                layoutOk.setVisibility(VISIBLE);
                break;
            case STEP_THREE:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_3);
                topTip.setText(R.string.guide_step_3_tip);
                guideArrow.setVisibility(VISIBLE);
                startAnim();
                break;
            case STEP_FOUR:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_4);
                topTip.setText(R.string.guide_step_4_tip);
                break;
            case STEP_FIVE:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_5);
                topTip.setText(R.string.guide_step_5_tip);
                break;
            case STEP_SIX:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(VISIBLE);
                keyTip.setText(R.string.guide_step_6);
                topTip.setText(R.string.guide_step_6_tip);
                guideArrow.setVisibility(VISIBLE);
                startAnim();
                break;
            case STEP_END:
                layoutHome.setVisibility(INVISIBLE);
                layoutLine.setVisibility(INVISIBLE);
                centerTip.setVisibility(VISIBLE);
                ketHint.setVisibility(INVISIBLE);
                keyTip.setText(R.string.guide_step_end);
                break;
            default:
                break;
        }
    }

    private void startAnim() {
        guideArrow.setImageResource(R.drawable.anim_guide_arrow);
        AnimationDrawable animationDrawable = (AnimationDrawable) guideArrow.getDrawable();
        animationDrawable.start();
    }

    private void stopAnim() {
        guideArrow.setVisibility(INVISIBLE);
        guideArrow.setImageResource(R.drawable.anim_guide_arrow);
        AnimationDrawable animationDrawable = (AnimationDrawable) guideArrow.getDrawable();
        animationDrawable.stop();
    }

}