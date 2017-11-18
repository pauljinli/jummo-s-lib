package com.stockholm.common.rxjava;


import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public interface RxServiceExecutor {

    <T> void execute(Observable<T> observable, Subscriber<T> subscriber, boolean noTimeOut);

    <T> void execute(Observable<T> observable, Action1<T> onNext, boolean noTimeOut);

    <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, boolean noTimeOut);

    <T> void execute(Observable<T> observable, Action1<T> onNext, Action1<Throwable> onError, Action0 onComplete, boolean noTimeOut);

    <T> void execute(Single<T> single, Subscriber<T> subscriber, boolean noTimeOut);

    <T> void execute(Single<T> single, Action1<T> onSuccess, boolean noTimeOut);

    <T> void execute(Single<T> single, Action1<T> onSuccess, Action1<Throwable> onError, boolean noTimeOut);
}
