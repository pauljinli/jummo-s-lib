package com.stockholm.common.api;


import android.content.Context;

import com.stockholm.common.R;
import com.stockholm.common.utils.PreferenceFactory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnvProvider {

    private final Map<Env, EnvData> envDataMap = new HashMap<>();
    private PreferenceFactory factory;

    @Inject
    public EnvProvider(Context context, PreferenceFactory factory) {
        this.factory = factory;
        envDataMap.put(Env.DEV, new EnvData(context.getString(R.string.dev_url),
            context.getString(R.string.dev_ws_url)));
        envDataMap.put(Env.STG, new EnvData(context.getString(R.string.stag_url),
            context.getString(R.string.stag_ws_url)));
        envDataMap.put(Env.PROD, new EnvData(context.getString(R.string.prod_url),
            context.getString(R.string.prod_ws_url)));
    }

    public EnvData get(Env env) {
        ApiConfig apiConfig = factory.create(ApiConfig.class);
        Env defaultEnv = Env.DEV;
        switch (env.ordinal()) {
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
