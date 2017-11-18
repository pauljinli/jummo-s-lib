package com.stockholm.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.stockholm.common.R;


public class VolumeAdjustView extends View {

    private Paint paint;
    private Bitmap volumeIcon;
    private Bitmap volumeItemIcon;
    private int iconWidth;
    private int iconOffsetY;
    private int itemIconWidth;
    private int itemOffsetY;

    private int streamType = AudioManager.STREAM_MUSIC;
    private int maxVolume;
    private int currentVolume;
    private AudioManager audioManager;

    public VolumeAdjustView(Context context) {
        this(context, null);
    }

    public VolumeAdjustView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeAdjustView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(streamType);
        currentVolume = audioManager.getStreamVolume(streamType);
        init();
    }

    private void init() {
        volumeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_volume);
        volumeItemIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_volume_item);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setFilterBitmap(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public void updateCurrentVolume() {
        this.currentVolume = audioManager.getStreamVolume(streamType);
        invalidate();
    }

    public void raiseCurrentVolume() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        int volume = audioManager.getStreamVolume(streamType);
        setCurrentVolume(volume);
    }

    public void lowerCurrentVolume() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        int volume = audioManager.getStreamVolume(streamType);
        setCurrentVolume(volume);
    }

    private void setCurrentVolume(int currentVolume) {
        this.currentVolume = currentVolume;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int viewHeight = getMeasuredHeight();
        if (volumeIcon != null) {
            iconWidth = volumeIcon.getWidth();
            int iconHeight = volumeIcon.getHeight();
            iconOffsetY = (viewHeight + paddingBottom + paddingTop - iconHeight) / 2;
        }
        if (volumeItemIcon != null) {
            itemIconWidth = volumeItemIcon.getWidth();
            int itemIconHeight = volumeItemIcon.getHeight();
            itemOffsetY = (viewHeight + paddingTop + paddingBottom - itemIconHeight) / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(volumeIcon, 0, iconOffsetY, paint);
        for (int i = 0; i < currentVolume; i++) {
            canvas.drawBitmap(volumeItemIcon, itemIconWidth * i + iconWidth, itemOffsetY, paint);
        }
    }

}