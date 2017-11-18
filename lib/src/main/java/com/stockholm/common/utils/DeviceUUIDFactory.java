package com.stockholm.common.utils;

import android.os.Build;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeviceUUIDFactory {

    private static final String TAG = "DeviceUUIDFactory";
    private static final String SALT = "8f6d5w2e";

    @Inject
    public DeviceUUIDFactory() {

    }

    public String getDeviceId() {
        String serial = Build.SERIAL;
        return serial + getMD5(serial + SALT).substring(0, 8);
    }

    private String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashText = bigInt.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

