package com.stockholm.common.sound.remote;

import com.google.gson.Gson;

public class AudioContent {

    private int playState;

    private String title;

    private String subTitle;

    private String CoverImg;

    private String extra;

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCoverImg() {
        return CoverImg;
    }

    public void setCoverImg(String coverImg) {
        CoverImg = coverImg;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
