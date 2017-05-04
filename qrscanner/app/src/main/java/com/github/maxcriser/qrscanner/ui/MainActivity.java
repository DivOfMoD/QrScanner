package com.github.maxcriser.qrscanner.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.adapter.SampleFragmentPagerAdapter;
import com.github.maxcriser.qrscanner.preferences.SettingsSharedPreferences;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private EditText mServerEditText;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public final void handleResult(final Result pResult) {
        Toast.makeText(this, pResult.getText(), Toast.LENGTH_SHORT).show();
    }

    public void onScanClicked(final View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            startScanner();
        }
    }

    private void startScanner() {
        startActivity(new Intent(this, ScannerActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initViews() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final SampleFragmentPagerAdapter sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this, getApplication(), getSupportLoaderManager());
        viewPager.setAdapter(sampleFragmentPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.need_access_to_the_camera, Toast.LENGTH_SHORT).show();
        } else {
            startScanner();
        }
    }

    public void onAltServerClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onAltServerClicked(checkable, current);
    }

    private void onAltServerClicked(final Checkable checkable, final boolean current) {
        checkable.setChecked(current);
        mServerEditText.setEnabled(current);
        new SettingsSharedPreferences(this).setAltServerChecked(current);
        mServerEditText.setEnabled(current);
    }

    public void onAltPasswordClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onAltPasswordClicked(checkable, current);
    }

    private void onAltPasswordClicked(final Checkable checkable, final boolean current) {
        checkable.setChecked(current);
        mLoginEditText.setEnabled(current);
        mPasswordEditText.setEnabled(current);
        new SettingsSharedPreferences(this).setAltPasswordChecked(current);
        mLoginEditText.setEnabled(current);
        mPasswordEditText.setEnabled(current);
    }

    public void onScanSoundClicked(final View view) {
        final Checkable checkable = (Checkable) view;
        final boolean current = !checkable.isChecked();
        onScanSoundClicked(checkable, current);
    }

    public void onScanSoundClicked(final Checkable checkable, final boolean current) {
        checkable.setChecked(current);
        new SettingsSharedPreferences(this).setScanSoundChecked(current);
    }

    public void onSettingsClicked(final View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Login");
        final View modifyView = getLayoutInflater().inflate(R.layout.fragment_settings, null);
        alert.setView(modifyView);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        final Context context = this;
        alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final SettingsSharedPreferences preferences = new SettingsSharedPreferences(context);
                preferences.setServer(mServerEditText.getText().toString());
                preferences.setLogin(mLoginEditText.getText().toString());
                preferences.setPassword(mPasswordEditText.getText().toString());
                Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog dialog = alert.create();
        mServerEditText = (EditText) modifyView.findViewById(R.id.edit_text_server);
        mLoginEditText = (EditText) modifyView.findViewById(R.id.edit_text_login);
        mPasswordEditText = (EditText) modifyView.findViewById(R.id.edit_text_password);
        final SettingsSharedPreferences preferences = new SettingsSharedPreferences(this);
        onAltServerClicked((Checkable) modifyView.findViewById(R.id.checkable_alt_server),
                preferences.getAltServerChecked());
        onAltPasswordClicked((Checkable) modifyView.findViewById(R.id.checkable_alt_password),
                preferences.getAltPasswordChecked());
        onScanSoundClicked((Checkable) modifyView.findViewById(R.id.checkable_scan_sound),
                preferences.getScanSoundChecked());
        mServerEditText.setText(preferences.getServer());
        mLoginEditText.setText(preferences.getLogin());
        mPasswordEditText.setText(preferences.getPassword());
        dialog.show();
    }
}
