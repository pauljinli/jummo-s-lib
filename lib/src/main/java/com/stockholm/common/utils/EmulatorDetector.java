package com.stockholm.common.utils;

import android.os.Build;

public final class EmulatorDetector {

    private static final String TAG = "EmulatorDetector";

    private static int rating = -1;

    private static Boolean isEmulator;

    private EmulatorDetector() {
    }

    /**
     * Detects if app is currenly running on emulator, or real device.
     *
     * @return true for emulator, false for real devices
     */
    public static boolean isEmulator() {
        if (isEmulator == null) {
            if (rating < 0) { // rating is not calculated yet
                int newRating = 0;

                if (Build.PRODUCT.equals("sdk")
                        || Build.PRODUCT.equals("google_sdk")
                        || Build.PRODUCT.equals("sdk_x86")
                        || Build.PRODUCT.equals("vbox86p")
                        || Build.PRODUCT.equals("sdk_google_phone_x86_64")) {
                    newRating++;
                }

                if (Build.MANUFACTURER.equals("unknown") || Build.MANUFACTURER.equals("Genymotion")) {
                    newRating++;
                }

                if (Build.BRAND.equals("generic") || Build.BRAND.equals("generic_x86")) {
                    newRating++;
                }

                if (Build.DEVICE.equals("generic") || Build.DEVICE.equals("generic_x86")
                        || Build.DEVICE.equals("vbox86p")) {
                    newRating++;
                }

                if (Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk")
                        || Build.MODEL.equals("Android SDK built for x86")
                        || Build.MODEL.equals("Android SDK built for x86_64")) {
                    newRating++;
                }

                if (Build.HARDWARE.equals("goldfish") || Build.HARDWARE.equals("vbox86")
                        || Build.HARDWARE.equals("ranchu")) {
                    newRating++;
                }

                if (Build.FINGERPRINT.contains("generic/sdk/generic")
                        || Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86")
                        || Build.FINGERPRINT.contains("generic/google_sdk/generic")
                        || Build.FINGERPRINT.contains("generic/vbox86p/vbox86p")
                        || Build.FINGERPRINT.contains("sdk_google_phone_x86_64")) {
                    newRating++;
                }
                rating = newRating;
            }
            isEmulator = rating > 4;
        }
        return isEmulator;
    }

    /**
     * Returns string with human-readable listing of Build.* parameters used in {@link #isEmulator()} method.
     *
     * @return all involved Build.* parameters and its values
     */
    public static String getDeviceListing() {
        return "Build.PRODUCT: " + Build.PRODUCT + "\n"
                + "Build.MANUFACTURER: " + Build.MANUFACTURER + "\n"
                + "Build.BRAND: " + Build.BRAND + "\n"
                + "Build.DEVICE: " + Build.DEVICE + "\n"
                + "Build.MODEL: " + Build.MODEL + "\n"
                + "Build.HARDWARE: " + Build.HARDWARE + "\n"
                + "Build.FINGERPRINT: " + Build.FINGERPRINT;
    }

    /**
     * Prints all Build.* parameters used in {@link #isEmulator()} method to logcat.
     */
    public static void logcat() {
        StockholmLogger.d(TAG, getDeviceListing());
    }

}
