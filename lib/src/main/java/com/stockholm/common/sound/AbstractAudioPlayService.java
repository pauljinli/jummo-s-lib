package com.stockholm.common.sound;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.stockholm.common.bus.RxEventBus;
import com.stockholm.common.sound.event.AudioPlayControlEvent;
import com.stockholm.common.sound.event.AudioPlayHistoryEvent;
import com.stockholm.common.sound.event.AudioPlayListEvent;
import com.stockholm.common.sound.event.AudioPlayModelEvent;
import com.stockholm.common.sound.event.AudioPlayModelUpdateEvent;
import com.stockholm.common.sound.event.AudioPlayProgressEvent;
import com.stockholm.common.sound.event.AudioPlayStatusEvent;
import com.stockholm.common.sound.event.AudioPlayVolumeEvent;
import com.stockholm.common.utils.StockholmLogger;

import java.util.List;

public abstract class AbstractAudioPlayService extends Service implements AudioManager.OnAudioFocusChangeListener {

    protected static final String TAG = "AudioPlayService";

    protected AudioPlayList playList;
    protected RxEventBus eventBus;
    protected int historyProgress;
    private AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        playList = new AudioPlayList();
        playList.init(initPlayList());
        eventBus = initEventBus();
        subscribeEventBus();
        initAudioPlayer();
        initAudioManger();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract RxEventBus initEventBus();

    public abstract void initAudioPlayer();

    public abstract void releaseAudioPlayer();

    public abstract List<? extends AudioPlayModel> initPlayList();

    public abstract List<? extends AudioPlayModel> updatePlayList();

    public abstract List<? extends AudioPlayModel> appendPlayList();

    public void setPlayListLooper(boolean loop) {
        playList.setLoop(loop);
    }

    public abstract boolean toggle();

    public boolean play() {
        requestFocus();
        return true;
    }

    public boolean pause() {
        abandonFocus();
        return true;
    }

    public boolean restart() {
        requestFocus();
        return true;
    }

    public boolean stop() {
        abandonFocus();
        return true;
    }

    public boolean playNext() {
        if (playList.next()) {
            this.historyProgress = 0;
            play();
            return true;
        }
        return false;
    }

    public boolean playPrevious() {
        if (playList.previous()) {
            this.historyProgress = 0;
            play();
            return true;
        }
        return false;
    }

    private void subscribeEventBus() {
        eventBus.subscribe(AudioPlayListEvent.class, this::handlePlaylistEvent);
        eventBus.subscribe(AudioPlayControlEvent.class, this::handlePlayControlEvent);
        eventBus.subscribe(AudioPlayHistoryEvent.class, this::handlePlayHistoryEvent);
        eventBus.subscribe(AudioPlayModelUpdateEvent.class, this::handlePlayModelUpdateEvent);
    }

    private void initAudioManger() {
        audioManager = (AudioManager) getApplicationContext().getSystemService(Service.AUDIO_SERVICE);
    }

    private void handlePlaylistEvent(AudioPlayListEvent event) {
        switch (event.getCmd()) {
            case AudioPlayListEvent.INIT:
                playList.init(initPlayList());
                break;
            case AudioPlayListEvent.UPDATE:
                playList.init(updatePlayList());
                play();
                break;
            case AudioPlayListEvent.APPEND:
                playList.append(appendPlayList());
                // TODO: 2017/6/22 keep current play state
//                playNext();
                break;
            case AudioPlayListEvent.TEMP_INIT:
                List<? extends AudioPlayModel> list = event.getPlayModelList();
                playList.init(list);
                play();
            default:
        }
    }

    private void handlePlayControlEvent(AudioPlayControlEvent event) {
        switch (event.getCmd()) {
            case AudioPlayControlEvent.PLAY:
                play();
                break;
            case AudioPlayControlEvent.TOGGLE:
                toggle();
                break;
            case AudioPlayControlEvent.NEXT:
                boolean next = playNext();
                if (!next) eventBus.post(new AudioPlayStatusEvent(AudioPlayStatusEvent.NO_NEXT));
                break;
            case AudioPlayControlEvent.PREVIOUS:
                boolean previous = playPrevious();
                if (!previous)
                    eventBus.post(new AudioPlayStatusEvent(AudioPlayStatusEvent.NO_PREVIOUS));
                break;
            case AudioPlayControlEvent.PAUSE:
                pause();
                break;
            case AudioPlayControlEvent.RESTART:
                restart();
                break;
            case AudioPlayControlEvent.STOP:
                stop();
                break;
            default:
        }
    }

    private void handlePlayHistoryEvent(AudioPlayHistoryEvent event) {
        StockholmLogger.d(TAG, "handlePlayHistoryEvent index: " + event.getIndex() + "-- progress: " + event.getHistoryProgress());
        this.historyProgress = event.getHistoryProgress();
        playList.setIndex(event.getIndex());
    }

    private void handlePlayModelUpdateEvent(AudioPlayModelUpdateEvent event) {
        playList.update(event.getIndex(), event.getAudioPlayModel());
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                restart();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pause();
                break;
            default:
                break;
        }
    }

    public void requestFocus() {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        StockholmLogger.i(TAG, "audio focus by " + getPackageName());
    }

    public void abandonFocus() {
        audioManager.abandonAudioFocus(this);
        StockholmLogger.i(TAG, "audio abandon by " + getPackageName());
    }

    protected void broadcastVolume(float volume) {
        eventBus.post(new AudioPlayVolumeEvent(volume));
    }

    protected void broadcastProgress(int progress) {
        eventBus.post(new AudioPlayProgressEvent(progress));
    }

    protected void broadcastProgress(int currentPosition, int totalDuration) {
        eventBus.post(new AudioPlayProgressEvent(currentPosition, totalDuration));
    }

    protected void broadcastPlayStatus(int status) {
        eventBus.post(new AudioPlayStatusEvent(status));
    }

    protected void broadcastPlayModel(AudioPlayModel playModel) {
        eventBus.post(new AudioPlayModelEvent(playModel));
    }

    protected void broadcastPlayModel(AudioPlayModel playModel, int index) {
        eventBus.post(new AudioPlayModelEvent(playModel, index));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unsubscribe();
        releaseAudioPlayer();
    }
}
