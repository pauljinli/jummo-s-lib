/*
 * Copyright (c) 2014 Badoo Trading Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Portions of documentation in this code are modifications based on work created and
 * shared by Android Open Source Project and used according to terms described in the
 * Apache License, Version 2.0
 */
package com.stockholm.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Memory safer implementation of android.os.Handler
 * <p/>
 * Original implementation of Handlers always keeps hard reference to handler in queue of execution.
 * If you create anonymous handler and post delayed message into it, it will keep all parent class
 * for that time in memory even if it could be cleaned.
 * <p/>
 * This implementation is trickier, it will keep WeakReferences to runnables and messages,
 * and GC could collect them once WeakHandler instance is not referenced any more
 * <p/>
 *
 * @see Handler
 * <p>
 * Created by Dmytro Voronkevych on 17/06/2014.
 * Optimized by Septenary on 8/15/2015
 * https://github.com/Ryfthink/android-weak-handler
 * http://dk-exp.com/2015/11/11/weak-handler/  WeakHanlder使用须知:需要将weakHanlder定义为activity或者fragment等的实例变量
 */

public class WeakHandler {
    private final Handler.Callback mCallback;
    private final ExecHandler mExec;

    private Lock mLock = new ReentrantLock();

    @SuppressWarnings("ConstantConditions")
    @VisibleForTesting
    private final ChainedRef mRunnables = new ChainedRef(mLock, null);


    public WeakHandler() {
        mCallback = null;
        mExec = new ExecHandler(this);
    }

    public WeakHandler(@Nullable Handler.Callback callback) {
        mCallback = callback;
        mExec = new ExecHandler(this);
    }

    public WeakHandler(@NonNull Looper looper) {
        mCallback = null;
        mExec = new ExecHandler(this, looper);
    }

    public WeakHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {
        mCallback = callback;
        mExec = new ExecHandler(this, looper);
    }

    public final boolean post(@NonNull Runnable r) {
        return mExec.post(wrapRunnable(r));
    }

    public final boolean postAtTime(@NonNull Runnable r, long uptimeMillis) {
        return mExec.postAtTime(wrapRunnable(r), uptimeMillis);
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return mExec.postAtTime(wrapRunnable(r), token, uptimeMillis);
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return mExec.postDelayed(wrapRunnable(r), delayMillis);
    }

    public final boolean postAtFrontOfQueue(Runnable r) {
        return mExec.postAtFrontOfQueue(wrapRunnable(r));
    }

    public final void removeCallbacks(Runnable r) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeCallbacks(runnable);
        }
    }

    public final void removeCallbacks(Runnable r, Object token) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeCallbacks(runnable, token);
        }
    }

    public final boolean sendMessage(Message msg) {
        return mExec.sendMessage(msg);
    }

    public final boolean sendEmptyMessage(int what) {
        return mExec.sendEmptyMessage(what);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return mExec.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return mExec.sendEmptyMessageAtTime(what, uptimeMillis);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return mExec.sendMessageDelayed(msg, delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return mExec.sendMessageAtTime(msg, uptimeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return mExec.sendMessageAtFrontOfQueue(msg);
    }

    public final void removeMessages(int what) {
        mExec.removeMessages(what);
    }

    public final void removeMessages(int what, Object object) {
        mExec.removeMessages(what, object);
    }

    public final void removeCallbacksAndMessages(Object token) {
        mExec.removeCallbacksAndMessages(token);
    }

    public final boolean hasMessages(int what) {
        return mExec.hasMessages(what);
    }

    public final boolean hasMessages(int what, Object object) {
        return mExec.hasMessages(what, object);
    }

    public final Message obtainMessage() {
        return mExec.obtainMessage();
    }

    public final Message obtainMessage(int what) {
        return mExec.obtainMessage(what);
    }

    public final Message obtainMessage(int what, Object obj) {
        return mExec.obtainMessage(what, obj);
    }

    public final Message obtainMessage(int what, int arg1, int arg2) {
        return mExec.obtainMessage(what, arg1, arg2);
    }

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return mExec.obtainMessage(what, arg1, arg2, obj);
    }

    public final Looper getLooper() {
        return mExec.getLooper();
    }


    public void handleMessage(Message msg) {

    }

    private WeakRunnable wrapRunnable(@NonNull Runnable r) {
        //noinspection ConstantConditions
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        final ChainedRef hardRef = new ChainedRef(mLock, r);
        mRunnables.insertAfter(hardRef);
        return hardRef.wrapper;
    }

    private static class ExecHandler extends Handler {
        private final WeakReference<WeakHandler> mBase;

        ExecHandler(WeakHandler base) {
            super();
            mBase = new WeakReference<>(base);
        }

        ExecHandler(WeakHandler base, Looper looper) {
            super(looper);
            mBase = new WeakReference<>(base);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            WeakHandler base = mBase.get();
            if (base != null) {
                if (base.mCallback != null) {
                    base.mCallback.handleMessage(msg);
                } else {
                    base.handleMessage(msg);
                }
            }
        }
    }

    private static final class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            mDelegate = delegate;
            mReference = reference;
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            final ChainedRef reference = mReference.get();
            if (reference != null) {
                reference.remove();
            }
            if (delegate != null) {
                delegate.run();
            }
        }
    }

    private static final class ChainedRef {
        @Nullable
        ChainedRef next;
        @Nullable
        ChainedRef prev;
        @NonNull
        final Runnable runnable;
        @NonNull
        final WeakRunnable wrapper;

        @NonNull
        Lock lock;

        public ChainedRef(@NonNull Lock lock, @NonNull Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WeakRunnable(new WeakReference<>(r), new WeakReference<>(this));
        }

        public WeakRunnable remove() {
            lock.lock();
            try {
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                prev = null;
                next = null;
            } finally {
                lock.unlock();
            }
            return wrapper;
        }

        public void insertAfter(@NonNull ChainedRef candidate) {
            lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }

                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                lock.unlock();
            }
        }

        @Nullable
        public WeakRunnable remove(Runnable obj) {
            lock.lock();
            try {
                ChainedRef curr = this.next; // Skipping head
                while (curr != null) {
                    if (curr.runnable == obj) { // We do comparison exactly how Handler does inside
                        return curr.remove();
                    }
                    curr = curr.next;
                }
            } finally {
                lock.unlock();
            }
            return null;
        }
    }
}