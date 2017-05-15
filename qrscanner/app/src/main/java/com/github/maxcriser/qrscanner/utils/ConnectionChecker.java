package com.github.maxcriser.qrscanner.utils;

import java.net.HttpURLConnection;
import java.net.URL;

public final class ConnectionChecker {

    private static final int SUCCESS = 200;

    public static boolean isEnabled() {
        try {
            final URL url = new URL("http://www.google.com");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == SUCCESS) {
                return true;
            }
        } catch (final Exception e) {
            return false;
        }
        return false;
    }
}
