package com.stockholm.common.rxjava;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class RxThread implements PostExecutionSchedulers {

    @Inject
    public RxThread() {
    }

    @Override
    public Scheduler getObserveScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getSubscribeScheduler() {
        return Schedulers.io();
    }
}
