package com.stockholm.common.utils;

import android.content.Context;

import net.orange_box.storebox.StoreBox;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceFactory {

    private Context context;

    @Inject
    public PreferenceFactory(Context context) {
        this.context = context;
    }

    public <T> T create(Class<T> interfaceType) {
        return StoreBox.create(context, interfaceType);
    }
}
