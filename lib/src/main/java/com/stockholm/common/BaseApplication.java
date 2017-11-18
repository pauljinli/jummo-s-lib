package com.stockholm.common;

import android.app.Application;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseApplication extends Application {

    public abstract void initializeThirdService();

    public abstract void initializeInjector();

    public String initUMengAppKey() {
        return "";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        initializeThirdService();
        if (!TextUtils.isEmpty(initUMengAppKey())) {
            MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, initUMengAppKey(),
                    "0", MobclickAgent.EScenarioType.E_UM_NORMAL, false));
            MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        }
    }

}
