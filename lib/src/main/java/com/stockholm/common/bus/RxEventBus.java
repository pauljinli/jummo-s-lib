package com.stockholm.common.bus;

import javax.inject.Inject;

import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class RxEventBus {

    private RxBus rxBus;
    private CompositeSubscription subscriptions;

    @Inject
    public RxEventBus(RxBus rxBus) {
        this.rxBus = rxBus;
        this.subscriptions = new CompositeSubscription();
    }

    public void post(Object msg) {
        if (rxBus.hasObservers()) {
            rxBus.send(msg);
        }
    }

    public <T> void subscribe(Class<T> dtoType, Action1<T> onSuccess) {
        subscriptions.add(rxBus.toObservable(dtoType).subscribe(onSuccess));
    }

    public <T> void subscribe(Class<T> dtoType, Action1<T> onSuccess, Action1<Throwable> onError) {
        subscriptions.add(rxBus.toObservable(dtoType).subscribe(onSuccess, onError));
    }

    public <T> void subscribe(Class<T> dtoType, Action1<T> onSuccess, Action1<Throwable> onError, Action0 onCompleted) {
        subscriptions.add(rxBus.toObservable(dtoType).subscribe(onSuccess, onError, onCompleted));
    }

    public <T> void subscribe(Class<T> dtoType, Observer<T> observer) {
        subscriptions.add(rxBus.toObservable(dtoType).subscribe(observer));
    }

    public <T> void subscribe(Class<T> dtoType, Subscriber<T> subscriber) {
        subscriptions.add(rxBus.toObservable(dtoType).subscribe(subscriber));
    }

    public <T> void subscribe(Class<T> dtoType, Scheduler scheduler, Action1<T> onSuccess) {
        subscriptions.add(rxBus.toObservable(dtoType).observeOn(scheduler).subscribe(onSuccess));
    }

    public void unsubscribe() {
        subscriptions.unsubscribe();
    }
}
