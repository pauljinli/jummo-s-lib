package com.stockholm.common.sound.remote;

import com.google.gson.Gson;

public class AudioControlBean {

    private int controlType;
    private AudioControlBean.ActionBean actionBean;
    private AudioControlBean.ContentBean contentBean;

    public AudioControlBean(int controlType, AudioControlBean.ActionBean actionBean, AudioControlBean.ContentBean contentBean) {
        this.controlType = controlType;
        this.actionBean = actionBean;
        this.contentBean = contentBean;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static AudioControlBean fromString(String json) {
        return new Gson().fromJson(json, AudioControlBean.class);
    }

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    public AudioControlBean.ActionBean getActionBean() {
        return actionBean;
    }

    public void setActionBean(AudioControlBean.ActionBean ationBean) {
        this.actionBean = actionBean;
    }

    public AudioControlBean.ContentBean getContentBean() {
        return contentBean;
    }

    public void setContentBean(AudioControlBean.ContentBean contentBean) {
        this.contentBean = contentBean;
    }

    public static class ActionBean {
        /**
         * action : 1
         */

        private int action;

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }
    }

    public static class ContentBean {

        private int audioType;
        private int audioIndex;
        private String audioId;
        private String packageName;

        public int getAudioType() {
            return audioType;
        }

        public void setAudioType(int audioType) {
            this.audioType = audioType;
        }

        public String getAudioId() {
            return audioId;
        }

        public void setAudioId(String audioId) {
            this.audioId = audioId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setAudioIndex(int audioIndex) {
            this.audioIndex = audioIndex;
        }

        public int getAudioIndex() {
            return audioIndex;
        }
    }
}
