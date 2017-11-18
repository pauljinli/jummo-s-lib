package com.stockholm.common.api;


public class EnvData {

    private String apiUrl;
    private String wsUrl;

    public EnvData(String apiUrl, String wsUrl) {
        this.apiUrl = apiUrl;
        this.wsUrl = wsUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getWsUrl() {
        return wsUrl;
    }
}
