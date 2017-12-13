package com.stockholm.common.utils;


import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {

    private static final String TAG = "FileUtil";

    private FileUtil() {

    }

    public static boolean copyFileFromAsset(Context app, String fileName, String targetPath) {
        try {
            InputStream srcStream = app.getAssets().open(fileName);
            OutputStream destStream = new FileOutputStream(targetPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = srcStream.read(buffer)) > 0) {
                destStream.write(buffer, 0, length);
            }

            destStream.flush();
            destStream.close();
            srcStream.close();

            return true;

        } catch (IOException e) {
            StockholmLogger.e(TAG, "copyFileFromAsset: " + e.toString());
        }

        return false;
    }

    public static boolean isDirectoryEmpty(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (file.isDirectory() && file.listFiles().length > 0) {
            return false;
        }
        return true;
    }

    public static List<String> listFiles(String directoryPath) {
        List<String> paths = new ArrayList<>();
        File file = new File(directoryPath);
        for (String fileName : file.list()) {
            paths.add(directoryPath + fileName);
        }
        return paths;
    }

    public static void saveBitmap(Bitmap bitmap, String path, String fileName) {
        saveBitmap(bitmap, path, fileName, 100);
    }

    public static void saveBitmap(Bitmap bitmap, String path, String fileName, int quality) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(path, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
            StockholmLogger.i(TAG, "save image error-->" + e.getMessage());
        }
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String[] listDirFileNames(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory() || !dir.exists()) {
            return null;
        }
        return dir.list();
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            StockholmLogger.e("Delete File", "Delete file not exists");
            return;
        }
        if (file.isDirectory()) {
            StockholmLogger.e("Delete File", "Delete target is a directory");
            return;
        }
        if (file.delete()) {
            StockholmLogger.i("Delete File", "Delete file success");
        } else {
            StockholmLogger.e("Delete File", "Delete file error");
        }
    }

    public static void clearDir(String path) {
        File file = new File(path);
        if (!file.isDirectory() || !file.exists()) {
            return;
        }
        File[] childFiles = file.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }
        for (int i = 0; i < childFiles.length; i++) {
            if (childFiles[i].delete()) {
                StockholmLogger.i("Delete File", "Delete file success");
            } else {
                StockholmLogger.e("Delete File", "Delete file error");
            }
        }
    }

}
