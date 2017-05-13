package com.github.maxcriser.qrscanner.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.maxcriser.qrscanner.R;

public class SettingsView {

    private static View ourInstance;

    public static View getInstance(final Context context, final LayoutInflater layoutInflater) {
        if (ourInstance == null) {
            ourInstance = layoutInflater.inflate(R.layout.fragment_settings, null);
        }
        return ourInstance;
    }
}
