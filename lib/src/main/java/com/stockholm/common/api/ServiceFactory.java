package com.stockholm.common.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.stockholm.common.Constant;
import com.stockholm.common.utils.DeviceUUIDFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private final Retrofit retrofit;

    public ServiceFactory(BaseUrl baseUrl) {
        retrofit = createRetrofit(baseUrl.getUrl(), true);
    }

    public ServiceFactory(BaseUrl baseUrl, boolean addHeader) {
        retrofit = createRetrofit(baseUrl.getUrl(), addHeader);
    }

    public <T> T create(Class<T> interfaceType) {
        return retrofit.create(interfaceType);
    }

    private Retrofit createRetrofit(String baseUrl, boolean addHeader) {
        OkHttpClient client;
        if (addHeader) {
            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {
                        DeviceUUIDFactory deviceUUIDFactory = new DeviceUUIDFactory();
                        Request newReq = chain.request().newBuilder()
                                .addHeader("UUID", deviceUUIDFactory.getDeviceId())
                                .addHeader("Platform", Constant.PLATFORM_DEVICE)
                                .build();
                        return chain.proceed(newReq);
                    }).build();
        } else {
            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(NoBodyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
    }

}
