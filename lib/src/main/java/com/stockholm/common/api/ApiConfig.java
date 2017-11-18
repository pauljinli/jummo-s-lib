package com.stockholm.common.api;


import net.orange_box.storebox.annotations.method.KeyByString;

public interface ApiConfig {

    @KeyByString("env")
    Env getEnv(Env defaultEnv);

    @KeyByString("env")
    void setEnv(Env env);
}
