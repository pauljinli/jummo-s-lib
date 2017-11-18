package com.stockholm.common.sound;


import com.stockholm.common.utils.StockholmLogger;

import java.util.ArrayList;
import java.util.List;

import static com.stockholm.common.sound.AbstractAudioPlayService.TAG;

public class AudioPlayList<T extends AudioPlayModel> {

    private List<T> modelList = new ArrayList<>();
    private int index;
    private boolean loop = true;

    public boolean init(List<T> list) {
        if (list != null && list.size() > 0) {
            index = 0;
            modelList.clear();
            return modelList.addAll(list);
        }
        return false;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isLoop() {
        return loop;
    }

    public void reset() {
        modelList.clear();
    }

    public boolean append(List<T> list) {
        int size = modelList.size();
        return modelList.addAll(size, list);
    }

    public void append(T t) {
        int size = modelList.size();
        modelList.add(size, t);
    }

    public boolean insert(int index, List<T> list) {
        return modelList.addAll(index, list);
    }

    public void insert(int index, T t) {
        modelList.add(index, t);
    }

    public boolean play() {
        if (modelList != null && modelList.size() != 0)
            return true;
        return false;
    }

    public boolean next() {
        StockholmLogger.d(TAG, "next index: " + index);
        index++;
        if (loop) {
            index %= modelList.size();
            return true;
        } else {
            if (index < modelList.size()) {
                return true;
            } else {
                index--;
                return false;
            }
        }
    }

    public boolean previous() {
        StockholmLogger.d(TAG, "previous index: " + index);
        index--;
        if (loop) {
            index += modelList.size();
            index %= modelList.size();
            return true;
        } else {
            if (index < 0) {
                index++;
                return false;
            }
            return true;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getCurrent() {
        if (modelList.isEmpty()) return null;
        if (index >= modelList.size()) return null;
        return modelList.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T t : modelList) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }
}
