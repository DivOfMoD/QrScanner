package com.github.maxcriser.qrscanner.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsSharedPreferences {

    private static final String TABLE_NAME = "settings";
    private static final String ALT_SERVER = "alt_server";
    private static final String ALT_PASSWORD = "alt_password";
    private static final String SERVER = "server";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String SCAN_SOUND = "scan_sound";
    private final SharedPreferences mPreferences;

    public SettingsSharedPreferences(final Context context) {
        mPreferences = context.getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE);
    }

    public void setAltServerChecked(final boolean state) {
        mPreferences.edit().putBoolean(ALT_SERVER, state).apply();
    }

    public void setAltPasswordChecked(final boolean state) {
        mPreferences.edit().putBoolean(ALT_PASSWORD, state).apply();
    }

    public void setScanSoundChecked(final boolean state) {
        mPreferences.edit().putBoolean(SCAN_SOUND, state).apply();
    }

    public void setServer(final String s) {
        mPreferences.edit().putString(SERVER, s).apply();
    }

    public void setLogin(final String s) {
        mPreferences.edit().putString(LOGIN, s).apply();
    }

    public void setPassword(final String s) {
        mPreferences.edit().putString(PASSWORD, s).apply();
    }

    public boolean getAltServerChecked() {
        return mPreferences.getBoolean(ALT_SERVER, false);
    }

    public boolean getAltPasswordChecked() {
        return mPreferences.getBoolean(ALT_PASSWORD, false);
    }

    public boolean getScanSoundChecked() {
        return mPreferences.getBoolean(SCAN_SOUND, false);
    }

    public String getServer() {
        return mPreferences.getString(SERVER, "");
    }

    public String getLogin() {
        return mPreferences.getString(LOGIN, "");
    }

    public String getPassword() {
        return mPreferences.getString(PASSWORD, "");
    }
}
