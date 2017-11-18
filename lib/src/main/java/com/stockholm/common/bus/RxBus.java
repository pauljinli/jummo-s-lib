package com.stockholm.common.bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class RxBus {

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    @Inject
    public RxBus() {

    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public <T extends Object> Observable<T> toObservable(final Class<T> eventType) {
        return _bus.ofType(eventType);
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}