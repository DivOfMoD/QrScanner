package com.github.maxcriser.qrscanner.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.adapter.SampleFragmentPagerAdapter;
import com.github.maxcriser.qrscanner.constants.Constants;
import com.github.maxcriser.qrscanner.database.DatabaseHelper;
import com.github.maxcriser.qrscanner.utils.DatabaseUtils;
import com.github.maxcriser.qrscanner.utils.NetworkUtils;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    SharedPreferences mSharedPreferences;
    private DatabaseHelper dbHelper;

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

    public void Toast(final CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void onSettingsClicked(final View view) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_settings, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final EditText server = (EditText) dialogView.findViewById(R.id.server);
        final CheckBox useAltServer = (CheckBox) dialogView.findViewById(R.id.use_alt_server);
        final EditText username = (EditText) dialogView.findViewById(R.id.username);
        final EditText password = (EditText) dialogView.findViewById(R.id.password);
        final CheckBox useAltUsernameAndPassword = (CheckBox) dialogView.findViewById(R.id.use_alt_username_and_password);
        final AppCompatImageView eyePassword = (AppCompatImageView) dialogView.findViewById(R.id.eye_password);
        final CheckBox useSound = (CheckBox) dialogView.findViewById(R.id.use_sound);

        mSharedPreferences = getSharedPreferences(Constants.Shared.SHARED_NAME, MODE_PRIVATE);
        server.setText(mSharedPreferences.getString(Constants.Shared.SERVER, Constants.AlternativeData.SERVER));

        final Boolean serverBoolean = mSharedPreferences.getBoolean(Constants.Shared.USE_ALT_SERVER, true);
        useAltServer.setChecked(serverBoolean);
        server.setEnabled(!serverBoolean);

        final Boolean usernameAndPasswordBoolean = mSharedPreferences.getBoolean(Constants.Shared.USE_ALT_PASS_AND_USERNMAE, true);
        useAltUsernameAndPassword.setChecked(usernameAndPasswordBoolean);
        username.setEnabled(!usernameAndPasswordBoolean);
        password.setEnabled(!usernameAndPasswordBoolean);

        username.setText(mSharedPreferences.getString(Constants.Shared.USERNAME, Constants.AlternativeData.USERNAME));
        password.setText(mSharedPreferences.getString(Constants.Shared.PASSWORD, Constants.AlternativeData.PASSWORD));
        useSound.setChecked(mSharedPreferences.getBoolean(Constants.Shared.SOUND, true));

        useAltServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                server.setEnabled(!isChecked);

                if (isChecked) {
                    server.setText(Constants.AlternativeData.SERVER);
                } else {
                    server.setText(null);
                }
            }
        });

        useAltUsernameAndPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                username.setEnabled(!isChecked);
                password.setEnabled(!isChecked);

                if (isChecked) {
                    username.setText(Constants.AlternativeData.USERNAME);
                    password.setText(Constants.AlternativeData.PASSWORD);
                } else {
                    username.setText(null);
                    password.setText(null);
                }
            }
        });

        dialogBuilder.setTitle(R.string.settings);
        dialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final String serverStr = server.getText().toString();
                final String usernameStr = username.getText().toString();
                final String passwordStr = password.getText().toString();
                final Boolean useSoundBoolean = useSound.isChecked();
                final Boolean useAltServerBoolean = useAltServer.isChecked();
                final Boolean useAltPasswordAndUsernameBoolean = useAltUsernameAndPassword.isChecked();

                if (serverStr.isEmpty() || usernameStr.isEmpty() || passwordStr.isEmpty()) {
                    Toast(getString(R.string.please_fill_all_fields));
                } else {
                    mSharedPreferences = getSharedPreferences(Constants.Shared.SHARED_NAME, MODE_PRIVATE);
                    final SharedPreferences.Editor editor = mSharedPreferences.edit();

                    editor.putBoolean(Constants.Shared.SOUND, useSoundBoolean);
                    editor.putBoolean(Constants.Shared.USE_ALT_SERVER, useAltServerBoolean);
                    editor.putBoolean(Constants.Shared.USE_ALT_PASS_AND_USERNMAE, useAltPasswordAndUsernameBoolean);
                    editor.putString(Constants.Shared.SERVER, serverStr);
                    editor.putString(Constants.Shared.USERNAME, usernameStr);
                    editor.putString(Constants.Shared.PASSWORD, passwordStr);

                    editor.apply();
                    dialog.dismiss();
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });

        eyePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (password.getInputType() != TYPE_TEXT_VARIATION_PASSWORD) {
                    password.setInputType(TYPE_TEXT_VARIATION_PASSWORD);
                    eyePassword.setImageResource(R.drawable.eye_off);
                } else {
                    password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                    eyePassword.setImageResource(R.drawable.eye_on);
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void onSendingClicked(final View view) {
        if(NetworkUtils.isOnline(this)) {
            DatabaseUtils.sendItemsToServer(this, getApplication(), true, getLayoutInflater());
        } else {
            Toast.makeText(this, R.string.no_connection_try_again, Toast.LENGTH_LONG).show();
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (NetworkUtils.isOnline(this)) {
            DatabaseUtils.sendItemsToServer(this, getApplication(), true, getLayoutInflater());
        }
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
}
