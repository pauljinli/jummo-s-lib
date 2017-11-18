package com.stockholm.common.backoff;

import com.stockholm.common.utils.StockholmLogger;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class ExponentialBackoff implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

    private static final String TAG = "ExponentialBackoff";
    private static final int CRITICAL_POINT_RETRY_COUNT = 7;

    private int maxRetries;
    private final long interval;
    private final TimeUnit timeUnit;
    private int retryCount;

    private ExponentialBackoff(int maxRetries, long interval, TimeUnit timeUnit) {
        this.maxRetries = maxRetries;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.retryCount = 0;
    }

    public static ExponentialBackoff infinite(long interval, TimeUnit timeUnit) {
        return new ExponentialBackoff(Integer.MAX_VALUE, interval, timeUnit);
    }

    public static ExponentialBackoff create(int attempts, long interval, TimeUnit timeUnit) {
        return new ExponentialBackoff(attempts, interval, timeUnit);
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap(throwable -> {
            if (maxRetries < CRITICAL_POINT_RETRY_COUNT) {
                if (retryCount++ < maxRetries) {
                    StockholmLogger.d(TAG, "retry " + retryCount + " times");
                    return Observable.timer((long) Math.pow(interval, retryCount), timeUnit, Schedulers.immediate());
                }
            } else {
                if (retryCount++ < CRITICAL_POINT_RETRY_COUNT) {
                    StockholmLogger.d(TAG, "retry " + retryCount + " times");
                    return Observable.timer((long) Math.pow(interval, retryCount), timeUnit, Schedulers.immediate());
                } else {
                    StockholmLogger.d(TAG, "retry " + retryCount + " times");
                    return Observable.timer((long) Math.pow(interval, CRITICAL_POINT_RETRY_COUNT), timeUnit, Schedulers.immediate());
                }
            }
            return Observable.error(throwable);
        });
    }
}