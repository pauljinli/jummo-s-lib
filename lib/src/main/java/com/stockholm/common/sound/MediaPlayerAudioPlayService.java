package com.stockholm.common.sound;


import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;

import com.devbrackets.android.exomedia.AudioPlayer;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.stockholm.common.bus.RxEventBus;
import com.stockholm.common.sound.event.AudioPlayStatusEvent;
import com.stockholm.common.utils.StockholmLogger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayerAudioPlayService extends AbstractAudioPlayService implements OnCompletionListener,
        OnPreparedListener, OnSeekCompletionListener, OnErrorListener {

    private static final String TAG = MediaPlayerAudioPlayService.class.getSimpleName();

    private AudioPlayer audioPlayer;

    //play progress.
    private Timer progressTimer;
    private long progress;
    private float volume;
    private boolean timerStart;

    //record position of pause & restart to use.
    private long currentPosition;

    @Override
    public RxEventBus initEventBus() {
        return null;
    }

    @Override
    public void initAudioPlayer() {
        audioPlayer = new AudioPlayer(this);
        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        audioPlayer.setOnCompletionListener(this);
        audioPlayer.setOnPreparedListener(this);
        audioPlayer.setOnSeekCompletionListener(this);
        audioPlayer.setOnErrorListener(this);
    }

    @Override
    public void releaseAudioPlayer() {
        if (audioPlayer != null) {
            audioPlayer.reset();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    @Override
    public List<? extends AudioPlayModel> initPlayList() {
        return null;
    }

    @Override
    public List<? extends AudioPlayModel> updatePlayList() {
        return null;
    }

    @Override
    public List<? extends AudioPlayModel> appendPlayList() {
        return null;
    }

    @Override
    public void onPrepared() {
        StockholmLogger.d(TAG, "onPrepared: " + historyProgress);
        startProgressTimer();
        if (historyProgress == 0) {
            audioPlayer.start();
            broadcastPlayStatus(AudioPlayStatusEvent.PLAYING);
        } else audioPlayer.seekTo(historyProgress * audioPlayer.getDuration() / 100);
    }

    @Override
    public void onCompletion() {
        StockholmLogger.d(TAG, "onCompletion");
        if (!playNext()) eventBus.post(new AudioPlayStatusEvent(AudioPlayStatusEvent.NO_NEXT));
    }

    @Override
    public void onSeekComplete() {
        StockholmLogger.d(TAG, "onSeekComplete");
        audioPlayer.start();
        broadcastPlayStatus(AudioPlayStatusEvent.PLAYING);
        startProgressTimer();
    }

    @Override
    public boolean onError(Exception e) {
        StockholmLogger.e(TAG, "onError: " + e.toString());
        return true;
    }

    @Override
    public boolean play() {
        super.play();
        AudioPlayModel model = playList.getCurrent();
        if (model == null) {
            StockholmLogger.d(TAG, "play| model is null.");
            return false;
        }
        audioPlayer.reset();
        String url = model.getAudioUrl();
        if (TextUtils.isEmpty(url)) {
            StockholmLogger.d(TAG, "");
            return false;
        }
        try {
            audioPlayer.setDataSource(Uri.parse(model.getAudioUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            StockholmLogger.e(TAG, "play| e:" + e.getMessage());
            return false;
        }
        audioPlayer.prepareAsync();
        broadcastPlayStatus(AudioPlayStatusEvent.PREPARE);
        broadcastPlayModel(model, playList.getIndex());
        return true;
    }

    @Override
    public boolean pause() {
        super.pause();
        try {
            if (null != audioPlayer) {
                audioPlayer.pause();
                currentPosition = audioPlayer.getCurrentPosition();
                broadcastPlayStatus(AudioPlayStatusEvent.PAUSE);
            }
            return true;
        } catch (IllegalStateException e) {
            StockholmLogger.e(TAG, "pause| e:" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restart() {
        super.restart();
        audioPlayer.seekTo(currentPosition);
        audioPlayer.start();
        broadcastPlayStatus(AudioPlayStatusEvent.RESTART);
        return true;
    }

    @Override
    public boolean toggle() {
        if (audioPlayer.isPlaying()) {
            return pause();
        } else {
            return restart();
        }
    }

    @Override
    public boolean stop() {
        super.stop();
        audioPlayer.stopPlayback();
        audioPlayer.reset();
        stopProgressTimer();
        broadcastPlayStatus(AudioPlayStatusEvent.STOP);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopProgressTimer();
    }

    //audio play progress start
    private void startProgressTimer() {
        if (!timerStart) {
            timerStart = true;
            progressTimer = new Timer(true);
            ProgressTimerTask progressTimerTask = new ProgressTimerTask();
            VolumeTimerTask volumeTimerTask = new VolumeTimerTask();
            progressTimer.schedule(progressTimerTask, 0, 1000);
            progressTimer.schedule(volumeTimerTask, 0, 80);
        }
    }

    private void stopProgressTimer() {
        if (timerStart && progressTimer != null) {
            progressTimer.cancel();
            progressTimer = null;
        }
        timerStart = false;
    }

    private class VolumeTimerTask extends TimerTask {

        @Override
        public void run() {
            if (audioPlayer.isPlaying()) {
                float current = (float) Math.random();
                if (volume == current) return;
                volume = current;
                broadcastVolume(volume);
            }
        }
    }

    private class ProgressTimerTask extends TimerTask {
        private long getProgress() {
            long progress = 0;
            if (audioPlayer.getDuration() != 0)
                progress = (audioPlayer.getCurrentPosition() * 100 / audioPlayer.getDuration());
            if (progress == 99) progress += 1;
            return progress;
        }

        public void run() {
            if (audioPlayer.isPlaying()) {
                long current = getProgress();
                if (progress == current) return;
                progress = current;
                broadcastProgress((int) progress);
            }
        }
    }
    //audio play progress end

}