package com.github.maxcriser.qrscanner;

import android.app.Application;
import android.content.Context;

import com.github.maxcriser.qrscanner.database.DatabaseHelper;

public class Core extends Application {

    private DatabaseHelper mDatabaseHelper;

    public DatabaseHelper getDatabaseHelper(final Context pContext) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = DatabaseHelper.Impl.newInstance(pContext);
        }
        return mDatabaseHelper;
    }

    public Core() {
        ContextHolder.set(this);
    }

}