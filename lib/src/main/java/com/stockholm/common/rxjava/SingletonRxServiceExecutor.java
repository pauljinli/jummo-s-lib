package com.stockholm.common.rxjava;


import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

@Singleton
public class SingletonRxServiceExecutor implements RxServiceExecutor {

    private static final String TAG = "SingletonRxServiceExecutor";
    private static final int TIME_OUT_MILLISECONDS = 10 * 1000;

    private PostExecutionSchedulers postExecutionScheduler;

    @Inject
    public SingletonRxServiceExecutor(RxThread postExecutionScheduler) {
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public <T> void execute(Observable<T> observable, Subscriber<T> subscriber, boolean noTimeOut) {
        if (noTimeOut) {
            observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(subscriber);
        } else {
            observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(subscriber);
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, boolean noTimeOut) {
        if (noTimeOut) {
            observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, getDefaultOnError());
        } else {
            observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, getDefaultOnError());
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, boolean noTimeOut) {
        if (noTimeOut) {
            observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, onError);
        } else {
            observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, onError);
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, Action0 onComplete, boolean noTimeOut) {
        if (noTimeOut) {
            observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, onError, onComplete);
        } else {
            observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onNext, onError, onComplete);
        }
    }

    public <T> void execute(Single<T> single, Subscriber<T> subscriber, boolean noTimeOut) {
        if (noTimeOut) {
            single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(subscriber);
        } else {
            single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(subscriber);
        }
    }

    public <T> void execute(Single<T> single, Action1<T> onSuccess, boolean noTimeOut) {
        if (noTimeOut) {
            single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onSuccess, getDefaultOnError());
        } else {
            single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onSuccess, getDefaultOnError());
        }
    }

    public <T> void execute(Single<T> single, Action1<T> onSuccess, Action1<Throwable> onError, boolean noTimeOut) {
        if (noTimeOut) {
            single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onSuccess, onError);
        } else {
            single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                    .observeOn(postExecutionScheduler.getObserveScheduler())
                    .subscribe(onSuccess, onError);
        }
    }

    @NonNull
    private Action1<Throwable> getDefaultOnError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }
}
