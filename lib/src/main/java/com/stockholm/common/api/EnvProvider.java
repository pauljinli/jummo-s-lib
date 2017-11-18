package com.stockholm.common.api;


import android.content.Context;

import com.stockholm.common.BuildConfig;
import com.stockholm.common.R;
import com.stockholm.common.utils.PreferenceFactory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class EnvProvider implements Provider<EnvData> {

    private final Map<Env, EnvData> envDataMap = new HashMap<>();
    private PreferenceFactory factory;
    private Env env;

    @Inject
    public EnvProvider(Context context, PreferenceFactory factory, Env env) {
        this.factory = factory;
        this.env = env;
        envDataMap.put(Env.DEV, new EnvData(context.getString(R.string.dev_url),
            context.getString(R.string.dev_ws_url)));
        envDataMap.put(Env.STG, new EnvData(context.getString(R.string.stag_url),
            context.getString(R.string.stag_ws_url)));
        envDataMap.put(Env.PROD, new EnvData(context.getString(R.string.prod_url),
            context.getString(R.string.prod_ws_url)));
    }

    @Override
    public EnvData get() {
        ApiConfig apiConfig = factory.create(ApiConfig.class);
        Env defaultEnv = Env.DEV;
        switch (this.env.ordinal()) {
            case 1:
                defaultEnv = Env.DEV;
                break;
            case 2:
                defaultEnv = Env.STG;
                break;
            case 3:
                defaultEnv = Env.PROD;
                break;
            default:
                break;
        }
        return envDataMap.get(apiConfig.getEnv(defaultEnv));
    }

}
