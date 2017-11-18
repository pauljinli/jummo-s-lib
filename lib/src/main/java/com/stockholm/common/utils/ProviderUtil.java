package com.stockholm.common.utils;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.stockholm.common.Constant;

public final class ProviderUtil {

    private ProviderUtil() {
    }

    public static boolean showUserGuide(Context context) {
        boolean showUserGuide = false;
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.stockholm.launcher.provider/GuideProviderModel"),
                null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            showUserGuide = cursor.getInt(cursor.getColumnIndex("showUserGuide")) > 0;
        }
        if (cursor != null) cursor.close();
        return showUserGuide;
    }

    public static void updateShowGuide(Context context, boolean showGuide) {
        int guide = showGuide ? 1 : 0;
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("showUserGuide", guide);
        resolver.update(Uri.parse("content://com.stockholm.launcher.provider/GuideProviderModel"), contentValues,
                "packageName=?" , new String[]{Constant.APP_PACKAGE_NAME_LAUNCHER});
    }

}