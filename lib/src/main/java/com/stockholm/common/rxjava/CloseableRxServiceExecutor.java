package com.stockholm.common.rxjava;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class CloseableRxServiceExecutor implements RxServiceExecutor, Closeable {

    private static final String TAG = "CloseableRxServiceExecutor";

    private static final int TIME_OUT_MILLISECONDS = 10 * 1000;

    private PostExecutionSchedulers postExecutionScheduler;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    public CloseableRxServiceExecutor(RxThread postExecutionScheduler) {
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public <T> void execute(Observable<T> observable, Subscriber<T> subscriber, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(subscriber)
            );
        } else {
            subscriptions.add(
                    observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(subscriber)
            );
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, getDefaultOnError())
            );
        } else {
            subscriptions.add(
                    observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, getDefaultOnError())
            );
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, onError)
            );
        } else {
            subscriptions.add(
                    observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, onError)
            );
        }
    }

    public <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, Action0 onComplete, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    observable.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, onError, onComplete)
            );
        } else {
            subscriptions.add(
                    observable.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onNext, onError, onComplete)
            );
        }
    }

    public <T> void execute(Single<T> single, Subscriber<T> subscriber, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(subscriber)
            );
        } else {
            subscriptions.add(
                    single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(subscriber)
            );
        }

    }

    public <T> void execute(Single<T> single, Action1<T> onSuccess, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onSuccess, getDefaultOnError())
            );
        } else {
            subscriptions.add(
                    single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onSuccess, getDefaultOnError())
            );
        }
    }

    public <T> void execute(Single<T> single, Action1<T> onSuccess, Action1<Throwable> onError, boolean noTimeOut) {
        if (noTimeOut) {
            subscriptions.add(
                    single.subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onSuccess, onError)
            );
        } else {
            subscriptions.add(
                    single.timeout(TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                            .subscribeOn(postExecutionScheduler.getSubscribeScheduler())
                            .observeOn(postExecutionScheduler.getObserveScheduler())
                            .subscribe(onSuccess, onError)
            );
        }
    }

    @Override
    public void close() {
        subscriptions.clear();
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
