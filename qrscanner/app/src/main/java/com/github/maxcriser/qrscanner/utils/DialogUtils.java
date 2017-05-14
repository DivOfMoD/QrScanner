package com.github.maxcriser.qrscanner.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.maxcriser.qrscanner.R;

public final class DialogUtils {

    public static AlertDialog showProgressDialog(final Context pContext, final LayoutInflater pLayoutInflater) {
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(pContext);
        final View dialogView = pLayoutInflater.inflate(R.layout.fragment_progress, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static void showResultsDialog(final Context pContext, final LayoutInflater pLayoutInflater, final Integer all, final Integer sended, final Integer notSended, final AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null) {
            progressAlertDialog.dismiss();
        }

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(pContext);
        final View dialogView = pLayoutInflater.inflate(R.layout.fragment_results_of_sending, null);
        dialogBuilder.setView(dialogView);

        final TextView allView = (TextView) dialogView.findViewById(R.id.all);
        final TextView sendedView = (TextView) dialogView.findViewById(R.id.sended);
        final TextView notSendedView = (TextView) dialogView.findViewById(R.id.not_sended);

        allView.setText(String.valueOf(all));
        sendedView.setText(String.valueOf(sended));
        notSendedView.setText(String.valueOf(notSended));

        dialogBuilder.setTitle(pContext.getString(R.string.results_of_sending));
        dialogBuilder.setPositiveButton(pContext.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
