package com.stockholm.common.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;


public final class OsUtils {

    private static final String TAG = "OsUtils";

    private OsUtils() {

    }

    public static void setSystemTime(Context context, LocalDateTime time) {
        setSystemTime(context, time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
    }

    public static void setSystemTime(Context context, int year, int month, int day,
                                     int hour, int minute, int second) {
        StockholmLogger.d(TAG, "set system time:" + year + "/" + month + "/" + day + "/" + hour + "/" + minute + "/" + second);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    public static boolean runCommand(boolean root, String... commandline) {
        if (EmulatorDetector.isEmulator()) {
            return false;
        }
        Process process = null;
        try {
            process = runCommandAndGetProcess(root, commandline);
            if (process != null) {
                return process.waitFor() == 0;
            }
        } catch (InterruptedException ignore) {
            StockholmLogger.e(TAG, commandline + " :Command is interrupted");
        } catch (Exception e) {
            StockholmLogger.e(TAG, commandline + " :Cannot run command line", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static Process runCommandAndGetProcess(boolean root, String... commandline) {
        Process process = null;

        StringBuilder builder = new StringBuilder();
        if (commandline != null) {
            for (String cmd : commandline) {
                builder.append(cmd).append(", ");
            }

            StockholmLogger.d(TAG, "run command line: " + builder.toString());
        }

        try {
            process = Runtime.getRuntime().exec(root ? "su" : "sh");
            try (PrintWriter out = new PrintWriter(process.getOutputStream())) {
                if (commandline != null) {
                    for (String cmd : commandline) {
                        out.println(cmd);
                    }
                }

                out.append("exit");
                out.flush();
            }
        } catch (Exception e) {
            StockholmLogger.e(TAG, "run command line failed: " + builder.toString(), e);
        }
        return process;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            for (int i = 0; i < networkInfo.length; i++) {
                NetworkInfo.State state = networkInfo[i].getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return null != wifiNetworkInfo && wifiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI && wifiNetworkInfo.isConnected();
    }

    public static String getCommonLogPath(Context context) {
        return context.getExternalFilesDir(null) + File.separator + context.getPackageName();
    }

    private static boolean returnResult(int value) {
        StockholmLogger.d(TAG, "returnResult: " + value);
        if (value == 0) {
            return true;
        } else if (value == 1) {
            return false;
        } else {
            return false;
        }
    }

    public static boolean isTestDevice() {
        StockholmLogger.d(TAG, "Build.MODEL:" + Build.MODEL);
        return !"meow".equals(Build.MODEL);
    }

    public static void clearAppUserData(String packageName) {
        boolean success = runCommand(false, "pm clear " + packageName);
        if (success) {
            StockholmLogger.d(TAG, "Clear app data packageName:" + packageName
                    + ", success !");
        } else {
            StockholmLogger.d(TAG, "Clear app data packageName:" + packageName
                    + ", fail !");
        }
    }

    public static void rebootDevice(Context context) {
        Intent intent2 = new Intent("android.intent.action.REBOOT");
        intent2.putExtra("nowait", 1);
        intent2.putExtra("interval", 1);
        intent2.putExtra("window", 0);
        context.sendBroadcast(intent2);
    }

    public static String getAppVersionName(Context context, String packageName) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
            return versionName;
        } catch (Exception e) {
            StockholmLogger.e(TAG, "get version name error");
        }
        return null;
    }
}