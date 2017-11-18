package com.stockholm.common.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class NetworkTestUtil {

    private static final String TAG = "NetworkTestUtil";

    private NetworkTestUtil() {

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean httpsTest(String url) {
        boolean result = false;
        try {
            URL constructedUrl = new URL(url);
            URLConnection conn = constructedUrl.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Connection", "close");
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection urlConnection = (HttpURLConnection) conn;
                int code = urlConnection.getResponseCode();
                StockholmLogger.d(TAG, "code:" + code);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean socketTest(String host, int port) {
        boolean result = false;
        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);
            sslsocket.setEnabledProtocols(new String[]{"TLSv1.2"});
            sslsocket.setSoTimeout(3000);
            InputStream in = sslsocket.getInputStream();
            OutputStream out = sslsocket.getOutputStream();
            out.write(1);
            while (in.available() > 0) {
                StockholmLogger.d(TAG, "read:" + in.read());
            }
            StockholmLogger.d(TAG, "Successfully connected");
            result = true;
            in.close();
            out.close();
            sslsocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
