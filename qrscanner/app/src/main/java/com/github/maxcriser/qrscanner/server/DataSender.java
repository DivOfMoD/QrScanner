package com.github.maxcriser.qrscanner.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

public class DataSender {

    private String mLogin = "";
    private String mPassword = "";
    private String mServer = "http://mob.dvigenie.pro/walker.php";

    public DataSender() {
    }

    public DataSender(final String login, final String password, final String server) {
        mLogin = login;
        mPassword = password;
        mServer = server;
    }

    public DataSender(final String server) {
        mServer = server;
    }

    public DataSender(final String login, final String password) {
        mLogin = login;
        mPassword = password;
    }

    public boolean send(final String data, final String time, final double lat, final double lon) {
        try {
            final URL url = new URL(mServer);
            final URLConnection con = url.openConnection();
            con.setDoOutput(true);
            final PrintStream ps = new PrintStream(con.getOutputStream());
            ps.print("login=" + mLogin);
            ps.print("&passw=" + mPassword);
            ps.print("&data=" + data);
            ps.print("&date_get_info=" + time);
//            ps.print("&gps=" + time);

            con.getInputStream();
            ps.close();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }
}
