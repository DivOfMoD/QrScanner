package com.github.maxcriser.qrscanner.utils;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.github.maxcriser.qrscanner.Core;
import com.github.maxcriser.qrscanner.async.OnResultCallback;
import com.github.maxcriser.qrscanner.database.DatabaseHelper;
import com.github.maxcriser.qrscanner.database.models.ItemModel;

public final class DatabaseUtils {

    public static void sendItemsToServer(final Context pContext, final Application pApplication, final Boolean showResults, final LayoutInflater pLayoutInflater) {
        AlertDialog progressDialog = null;
        if (showResults) {
            progressDialog = DialogUtils.showProgressDialog(pContext, pLayoutInflater);
        }

        final DatabaseHelper dbHelper;
        dbHelper = ((Core) pApplication).getDatabaseHelper(pContext);
        final AlertDialog finalProgressDialog = progressDialog;
        dbHelper.query(new OnResultCallback<Cursor, Void>() {

            @Override
            public void onSuccess(final Cursor pCursor) {
                final Integer countSended = 0;
                final Integer countNotSended = 0;
                final Integer countOfItems = pCursor.getCount();

                for (int i = 0; i < countOfItems; i++) {
                    if (pCursor.moveToNext()) {
                        // TODO: 15.05.2017 try send to server
                        final String id = pCursor.getString(pCursor.getColumnIndex(ItemModel.ID));
                        final String login = pCursor.getString(pCursor.getColumnIndex(ItemModel.LOGIN));
                        final String password = pCursor.getString(pCursor.getColumnIndex(ItemModel.PASSWORD));
                        final String data = pCursor.getString(pCursor.getColumnIndex(ItemModel.DATA));
                        final String dateInfo = pCursor.getString(pCursor.getColumnIndex(ItemModel.DATE_INFO));
                        final String gps = pCursor.getString(pCursor.getColumnIndex(ItemModel.GPS));
                        final String codeFormat = pCursor.getString(pCursor.getColumnIndex(ItemModel.CODE_FORMAT));

//                         onSuccess()
//                        dbHelper.delete(ItemModel.class, null, ItemModel.ID + " = ?", String.valueOf(id));
//                        countSended++;

//                         onFailed() nothing
//                        countNotSended++;
                    }
                }

                if (showResults) {
                    DialogUtils.showResultsDialog(pContext, pLayoutInflater, countNotSended + countSended, countSended, countNotSended, finalProgressDialog);
                }
            }

            @Override
            public void onError(final Exception pE) {

            }

            @Override
            public void onProgressChanged(final Void pVoid) {

            }
        }, "*", ItemModel.class, "");
    }

}