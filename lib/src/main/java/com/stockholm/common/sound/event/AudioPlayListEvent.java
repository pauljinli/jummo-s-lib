package com.stockholm.common.sound.event;


import com.stockholm.common.sound.AudioPlayModel;

import java.util.List;

public class AudioPlayListEvent {

    /**
     * initial play list.
     */
    public static final int INIT = 1;

    /**
     * clear current play list, update.
     */
    public static final int UPDATE = 2;

    /**
     * keep current play list, append a list to it.
     */
    public static final int APPEND = 3;

    public static final int TEMP_INIT = 4;

    /**
     * clear current play list
     */
    public static final int CLEAR = 5;

    private int cmd;

    private List<? extends AudioPlayModel> playModelList;

    public AudioPlayListEvent(int cmd) {
        this.cmd = cmd;
    }

    public AudioPlayListEvent(List<? extends AudioPlayModel> playModelList) {
        this.cmd = TEMP_INIT;
        this.playModelList = playModelList;
    }

    public int getCmd() {
        return cmd;
    }

    public List<? extends AudioPlayModel> getPlayModelList() {
        return playModelList;
    }
}
