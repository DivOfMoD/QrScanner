package com.github.maxcriser.qrscanner.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.preferences.SettingsSharedPreferences;

public class SettingsFragment extends Fragment {

    private static Context sContext;
    private static View ourInstance;
    private static EditText mServerEditText;
    private static EditText mLoginEditText;
    private static EditText mPasswordEditText;
    private static CheckedTextView mAltServerCheckable;
    private static CheckedTextView mAltPasswordCheckable;
    private static CheckedTextView mScanSoundCheckable;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             final Bundle savedInstanceState) {
        sContext = inflater.getContext();
        ourInstance = inflater.inflate(R.layout.fragment_settings, null);
        mServerEditText = (EditText) ourInstance.findViewById(R.id.edit_text_server);
        mLoginEditText = (EditText) ourInstance.findViewById(R.id.edit_text_login);
        mPasswordEditText = (EditText) ourInstance.findViewById(R.id.edit_text_password);
        final SettingsSharedPreferences preferences = new SettingsSharedPreferences(sContext);
        mAltServerCheckable = initCheckedTextView(R.id.checkable_alt_server, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAltServerClicked(v);
            }
        });
        onAltServerClicked(preferences.getAltServerChecked());
        mAltPasswordCheckable = initCheckedTextView(R.id.checkable_alt_password, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAltPasswordClicked(v);
            }
        });
        onAltPasswordClicked(preferences.getAltPasswordChecked());
        mScanSoundCheckable = initCheckedTextView(R.id.checkable_scan_sound, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onScanSoundClicked(v);
            }
        });
        onScanSoundClicked(preferences.getScanSoundChecked());
        mServerEditText.setText(preferences.getServer());
        mLoginEditText.setText(preferences.getLogin());
        mPasswordEditText.setText(preferences.getPassword());
        return ourInstance;
    }

    private CheckedTextView initCheckedTextView(final int id, final View.OnClickListener listener) {
        final CheckedTextView textView = (CheckedTextView) ourInstance.findViewById(id);
        textView.setOnClickListener(listener);
        return textView;
    }

    public DialogInterface.OnClickListener getOnPositiveClick() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final SettingsSharedPreferences preferences = new SettingsSharedPreferences(sContext);
                preferences.setServer(mServerEditText.getText().toString());
                preferences.setLogin(mLoginEditText.getText().toString());
                preferences.setPassword(mPasswordEditText.getText().toString());
                Toast.makeText(sContext, "OK", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void onAltServerClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onAltServerClicked(current);
    }

    private void onAltServerClicked(final boolean current) {
        mAltServerCheckable.setChecked(current);
        mServerEditText.setEnabled(current);
        new SettingsSharedPreferences(sContext).setAltServerChecked(current);
        mServerEditText.setEnabled(current);
    }

    private void onAltPasswordClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onAltPasswordClicked(current);
    }

    private void onAltPasswordClicked(final boolean current) {
        mAltPasswordCheckable.setChecked(current);
        mLoginEditText.setEnabled(current);
        mPasswordEditText.setEnabled(current);
        new SettingsSharedPreferences(sContext).setAltPasswordChecked(current);
        mLoginEditText.setEnabled(current);
        mPasswordEditText.setEnabled(current);
    }

    private void onScanSoundClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onScanSoundClicked(current);
    }

    private void onScanSoundClicked(final boolean current) {
        mScanSoundCheckable.setChecked(current);
        new SettingsSharedPreferences(sContext).setScanSoundChecked(current);
    }
}
