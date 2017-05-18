package com.github.maxcriser.qrscanner.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.Core;
import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.constants.Constants;
import com.github.maxcriser.qrscanner.database.DatabaseHelper;
import com.github.maxcriser.qrscanner.database.models.ItemModel;
import com.github.maxcriser.qrscanner.utils.DialogUtils;
import com.github.maxcriser.qrscanner.utils.NetworkUtils;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private ZXingScannerView mZXingScannerView;
    private DatabaseHelper dbHelper;
    private Boolean isSound;
    private SharedPreferences mSharedPreferences;
    private String login;
    private String password;

    @Override
    public void handleResult(final Result pResult) {
        if (isSound) {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.scan_sound);
            mp.start();
        }

        final String result = pResult.getText();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.detected)
                .setMessage(result)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                                mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                        final String currentDate = sdf.format(new Date());
                        final String gps = null; // // TODO: 5/15/17
                        final String format = pResult.getBarcodeFormat().toString();

                        if (NetworkUtils.isOnline(ScannerActivity.this)) {
                            final android.support.v7.app.AlertDialog progressDialog = DialogUtils.showProgressDialog(ScannerActivity.this, getLayoutInflater());
                            // TODO: 15.05.2017 if sending success or denied progressDialog.dismiss();
                            // TODO: 5/15/17 login pass result gps date format

                            // TODO: 13.05.2017 Try load to server, if sending are not applied then save to database // addItem(...);
//                            Toast.makeText(ScannerActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                            addItem(result, format, login, password, currentDate, gps);
                            mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                        } else {
                            Toast.makeText(ScannerActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                            addItem(result, format, login, password, currentDate, gps);
                            mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                        }
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void addItem(final String result, final String resultCode, final String login, final String password, final String date, final String gps) {
        final ContentValues newItem = new ContentValues();
        newItem.put(ItemModel.ID, (Integer) null);
        newItem.put(ItemModel.DATA, result);
        newItem.put(ItemModel.CODE_FORMAT, resultCode);
        newItem.put(ItemModel.LOGIN, login);
        newItem.put(ItemModel.PASSWORD, password);
        newItem.put(ItemModel.DATE_INFO, date);
        newItem.put(ItemModel.GPS, gps);

        dbHelper.insert(ItemModel.class, newItem, null);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        mZXingScannerView = (ZXingScannerView) findViewById(R.id.zxingscanner_id);
        mZXingScannerView.startCamera();
        mZXingScannerView.setResultHandler(this);
        dbHelper = ((Core) getApplication()).getDatabaseHelper(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mZXingScannerView != null) {
            mZXingScannerView.stopCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mZXingScannerView != null) {
            mZXingScannerView.startCamera();
        }

        mSharedPreferences = getSharedPreferences(Constants.Shared.SHARED_NAME, MODE_PRIVATE);
        isSound = mSharedPreferences.getBoolean(Constants.Shared.SOUND, true);
        login = mSharedPreferences.getString(Constants.Shared.USERNAME, Constants.AlternativeData.USERNAME);
        password = mSharedPreferences.getString(Constants.Shared.PASSWORD, Constants.AlternativeData.PASSWORD);
    }
}