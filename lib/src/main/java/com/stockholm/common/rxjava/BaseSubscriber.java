package com.stockholm.common.rxjava;

public class BaseSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        onFinal();
    }

    @Override
    public void onNext(T t) {
        onFinal();
    }

    protected void onFinal() {
    }

    public static <T> BaseSubscriber<T> empty() {
        return new BaseSubscriber<>();
    }
}
