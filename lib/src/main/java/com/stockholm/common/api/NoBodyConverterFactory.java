package com.stockholm.common.api;


import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class NoBodyConverterFactory extends Converter.Factory {

    private NoBodyConverterFactory() {
    }

    public static final NoBodyConverterFactory create() {
        return new NoBodyConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations,
                                                            @NonNull Retrofit retrofit) {
        if (NoBodyResp.class.equals(type)) {
            return (Converter<ResponseBody, NoBodyResp>) value -> null;
        }
        return null;
    }

}