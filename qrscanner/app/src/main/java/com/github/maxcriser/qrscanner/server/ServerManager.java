package com.github.maxcriser.qrscanner.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ServerManager {

    private static final String PASSW = "passw";
    private static final String LOGIN = "login";
    private static final String DATA = "data";
    private static final String DATE_GET_INFO = "date_get_info";
    private static final String GPS = "gps";
    private String mLogin = "";
    private String mPassword = "";
    private String mServer = "http://mob.dvigenie.pro/walker.php";

    public ServerManager() {
    }

    public ServerManager(final String login, final String password, final String server) {
        mLogin = login;
        mPassword = password;
        mServer = server;
    }

    public ServerManager(final String server) {
        mServer = server;
    }

    public ServerManager(final String login, final String password) {
        mLogin = login;
        mPassword = password;
    }

    public ResponseStatus signIn(final String login, final String password) {
        if (!ConnectionChecker.isEnabled()) {
            return ResponseStatus.NoInternet;
        }
        final Map<String, String> dictionary = new HashMap<>();
        dictionary.put(LOGIN, mLogin);
        dictionary.put(PASSW, mPassword);
        final ResponseStatus[] responseStatus = new ResponseStatus[1];
        call(dictionary, new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                responseStatus[0] = ResponseStatus.Failure;
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                responseStatus[0] = ResponseStatus.Success;
            }
        });
        return responseStatus[0];
    }

    public ResponseStatus send(final String data, final String time, final double lat, final double lon) {
        if (!ConnectionChecker.isEnabled()) {
            return ResponseStatus.NoInternet;
        }
        final Map<String, String> dictionary = new HashMap<>();
        dictionary.put(LOGIN, mLogin);
        dictionary.put(PASSW, mPassword);
        dictionary.put(DATA, data);
        dictionary.put(DATE_GET_INFO, time);
        // TODO: 5/10/17 process gps lat & lon
//            dictionary.put(GPS, gps);
        final ResponseStatus[] responseStatus = new ResponseStatus[1];
        call(dictionary, new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                responseStatus[0] = ResponseStatus.Failure;
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                responseStatus[0] = ResponseStatus.Success;
                // TODO: 5/10/17 process callback data
//                final String temp = response.body().string();
//                final String[] res = temp.substring(1, temp.length() - 1).split(";");
//                for (final String re : res) {
//                    if ("0".equals(re)) {
//                        return;
//                    }
//                }
            }
        });
        return responseStatus[0];
    }

    private void call(final Map<String, String> dictionary, final Callback callback) {
        final Call.Factory client = new OkHttpClient();
        client.newCall(new RequestFormer().create(dictionary, mServer)).enqueue(callback);
    }
}
