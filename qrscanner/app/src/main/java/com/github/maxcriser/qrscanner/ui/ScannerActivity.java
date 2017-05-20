package com.github.maxcriser.qrscanner.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.Core;
import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.constants.Constants;
import com.github.maxcriser.qrscanner.database.DatabaseHelper;
import com.github.maxcriser.qrscanner.database.models.ItemModel;
import com.github.maxcriser.qrscanner.manager.GPSManager;
import com.github.maxcriser.qrscanner.utils.DialogUtils;
import com.github.maxcriser.qrscanner.utils.NetworkUtils;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.github.maxcriser.qrscanner.constants.Constants.GPS.API_LATITUDE;
import static com.github.maxcriser.qrscanner.constants.Constants.GPS.API_LONGITUDE;
import static com.github.maxcriser.qrscanner.constants.Constants.GPS.HTTP_IP_API_COM_JSON;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final String SPLIT_CHAR = "/";
    private ZXingScannerView mZXingScannerView;
    private DatabaseHelper dbHelper;
    private Boolean isSound;
    private SharedPreferences mSharedPreferences;
    private String login;
    private static final String GET = "GET";
    private String password;

    private void loadGPSFromAPI(final android.support.v7.app.AlertDialog dialog, final String result, final String format, final String currentDate) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPostExecute(final String pS) {
                super.onPostExecute(pS);
                if (pS != null) {
                    final String[] array = pS.split(SPLIT_CHAR);
                    sendToServer(dialog, result, format, currentDate, array[0], array[1]);
                } else {
                    // TODO: 20.05.2017 what's next when gps not recognize
                    sendToServer(dialog, result, format, currentDate, "null", "null");
                    Toast.makeText(ScannerActivity.this, R.string.gps_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(final String... params) {
                try {
                    final URL url = new URL(params[0]);

                    final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(GET);
                    urlConnection.connect();

                    final InputStream inputStream = urlConnection.getInputStream();
                    final StringBuilder buffer = new StringBuilder();

                    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    final String resultJson = buffer.toString();
                    inputStream.close();

                    final JSONObject dataJsonObj;
                    dataJsonObj = new JSONObject(resultJson);

                    final String latitude = dataJsonObj.getString(API_LATITUDE);
                    final String longitude = dataJsonObj.getString(API_LONGITUDE);

                    // TODO: 20.05.2017 check why null? 
                    return latitude + SPLIT_CHAR + longitude;
                } catch (final Exception e) {
                    return null;
                }
            }
        }.execute(HTTP_IP_API_COM_JSON);
    }

    public void sendToServer(final android.support.v7.app.AlertDialog dialog, final String result, final String format, final String currentDate, final String lat, final String lon) {
        if (NetworkUtils.isOnline(this)) {
            // ALEX SEND - >>>
            // if(ok) { dialog.dismiss }
            // if(not ok)
            // Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            // addItem(result, format, login, password, currentDate, GPSManager.gpsFormatter(lat, lon));
            dialog.dismiss();
            mZXingScannerView.resumeCameraPreview(this);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            addItem(result, format, login, password, currentDate, GPSManager.gpsFormatter(lat, lon));
            dialog.dismiss();
        }
    }

    private void loadGPS(final android.support.v7.app.AlertDialog dialog, final String result, final String resultCode, final String date) {
        new AsyncTask<Void, Void, Location>() {

            @Override
            protected void onPostExecute(final Location pS) {
                super.onPostExecute(pS);
                if (pS != null) {
                    sendToServer(dialog, result, resultCode, date, String.valueOf(pS.getLatitude()), String.valueOf(pS.getLongitude()));
                } else {
                    loadGPSFromAPI(dialog, result, resultCode, date);
                }
            }

            @Override
            protected Location doInBackground(final Void... params) {
                try {
                    return GPSManager.getGPS(ScannerActivity.this);
                } catch (final Exception e) {
                    return null;
                }
            }
        }.execute();
    }

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
                        final String format = pResult.getBarcodeFormat().toString();

                        final android.support.v7.app.AlertDialog progressDialog = DialogUtils.showProgressDialog(ScannerActivity.this, getLayoutInflater());

                        // TODO: 20.05.2017 check if Location "ON"
                        loadGPS(progressDialog, result, format, currentDate);
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void addItem(final String result, final String resultCode, final String login, final String password, final String date, final String gps) {
        final ContentValues newItem = new ContentValues();
        newItem.put(ItemModel.ID, (Integer) null);
        newItem.put(ItemModel.LOGIN, login);
        newItem.put(ItemModel.PASSWORD, password);
        newItem.put(ItemModel.DATA, result);
        newItem.put(ItemModel.DATE_INFO, date);
        newItem.put(ItemModel.GPS, gps);
        newItem.put(ItemModel.CODE_FORMAT, resultCode);

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