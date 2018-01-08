package com.stockholm.common;

import android.os.Environment;

public final class Constant {

    public static final String PLATFORM_DEVICE = "0";

    public static final String CLOCK_THEME_PATH = Environment.getExternalStorageDirectory() + "/meow/clock/theme";

    public static final String LAUNCHER_CLOCK_THEME_PATH = Environment.getExternalStorageDirectory() + "/meow/clock/theme/launcher";

    public static final String ACTION_PUSH_BROADCAST = "com.stockholm.meow.push.broadcast.action";

    public static final String JPUSH_ORDER = "jpush_order";

    public static final String JPUSH_ADDITION = "jpush_addition";

    public static final String SOCKET_ACTION = "jpush_action";

    public static final String OPEN_IN_LAUNCHER_PERMISSION = "com.stockholm.OPEN_IN_LAUNCHER";

    public static final String APP_PACKAGE_NAME_LAUNCHER = "com.stockholm.launcher";

    public static final String APP_PACKAGE_NAME_NEWS = "com.stockholm.news";

    public static final String APP_PACKAGE_NAME_BIND = "com.stockholm.bind";

    public static final String APP_PACKAGE_NAME_MUSIC = "com.stockholm.music";

    public static final String APP_PACKAGE_NAME_FM = "com.stockholm.fm";

    public static final String APP_PACKAGE_NAME_SPEECH = "com.stockholm.speech";

    public static final String APP_PACKAGE_NAME_WEATHER = "com.stockholm.weather";

    public static final String APP_PACKAGE_NAME_CLOCK = "com.stockholm.clock";

    public static final String APP_PACKAGE_NAME_SPEAKER = "com.stockholm.speaker";

    public static final String APP_PACKAGE_NAME_CALENDAR = "com.stockholm.calendar";

    public static final String APP_PACKAGE_NAME_FOTA = "com.stockholm.fota";

    public static final String APP_PACKAGE_NAME_DISPLAY = "com.stockholm.display";

    public static final String APP_PACKAGE_NAME_MOZIK = "com.stockholm.mozik";

    public static final String ACTION_DEVELOP_ENV = "com.stockholm.develop.env";

    public static final String KEY_DEV_ENV = "key_dev_env";

    public static final String ACTION_VOLUME_CHANGE = "android.media.VOLUME_CHANGED_ACTION";

    public static final String KEY_STATUS_PACKAGE = "key_status_package";

    public static final String KEY_STATUS_ENTITY = "key_status_entity";

    public static final String APP_SERVICE_NAME_FOTA = "com.stockholm.fota.ota.FotaService";

    public static final String KEY_IS_LOCAL = "key_is_local";

    private Constant() {

    }
}
