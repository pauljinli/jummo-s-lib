package com.stockholm.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) {
        this(context, null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spec = MeasureSpec.getSize(widthMeasureSpec) > MeasureSpec.getSize(heightMeasureSpec) ? heightMeasureSpec : widthMeasureSpec;
        super.onMeasure(spec, spec);
    }
}
