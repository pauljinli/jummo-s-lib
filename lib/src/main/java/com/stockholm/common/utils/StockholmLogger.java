package com.stockholm.common.utils;


import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;

public final class StockholmLogger {

    private StockholmLogger() {

    }

    public static void init(Application application) {
        try {
            LogConfiguration config = new LogConfiguration.Builder()
                    .logLevel(LogLevel.ALL).tag("Meow").t().st(3).b().build();
            Printer androidPrinter = new AndroidPrinter();
            XLog.init(config, androidPrinter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String tag, String msg) {
        XLog.tag(tag).v(msg);
    }

    public static void i(String tag, String msg) {
        XLog.tag(tag).i(msg);
    }

    public static void d(String tag, String msg) {
        XLog.tag(tag).d(msg);
    }

    public static void w(String tag, String msg) {
        XLog.tag(tag).w(msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        XLog.tag(tag).w(msg, tr);
    }

    public static void e(String tag, String msg) {
        XLog.tag(tag).e(msg);
    }

    public static void e(String tag, Throwable tr) {
        XLog.tag(tag).e(tr.getMessage(), tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        XLog.tag(tag).e(msg, tr);
    }

    public static void json(String tag, String json) {
        XLog.tag(tag).json(json);
    }

    public static void xml(String tag, String xml) {
        XLog.tag(tag).xml(xml);
    }
}
