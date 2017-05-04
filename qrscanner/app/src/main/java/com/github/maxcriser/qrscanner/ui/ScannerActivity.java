package com.github.maxcriser.qrscanner.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.maxcriser.qrscanner.Core;
import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.async.OnResultCallback;
import com.github.maxcriser.qrscanner.database.DatabaseHelper;
import com.github.maxcriser.qrscanner.database.models.ItemModel;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mZXingScannerView;
    private DatabaseHelper dbHelper;

    @Override
    public void handleResult(final Result pResult) {
        final String result = pResult.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detected")
                .setMessage(result)
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItem(result, pResult.getBarcodeFormat().toString());
                        mZXingScannerView.resumeCameraPreview(ScannerActivity.this);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addItem(String result, String resultCode) {
        final ContentValues newItem = new ContentValues();
        newItem.put(ItemModel.ID, (Integer) null);
        newItem.put(ItemModel.TEXT, result);
        newItem.put(ItemModel.CODE_FORMAT, resultCode);

        dbHelper.insert(ItemModel.class, newItem, new OnResultCallback<Long, Void>() {

            @Override
            public void onSuccess(final Long pLong) {
                Toast.makeText(ScannerActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(final Exception pE) {
                Toast.makeText(ScannerActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(final Void pVoid) {

            }
        });
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
    }
}