package com.github.maxcriser.qrscanner.database;

import com.github.maxcriser.qrscanner.database.models.ItemModel;

import java.lang.reflect.AnnotatedElement;

public final class Sql {

    public static String getSqlWithQuery(final AnnotatedElement model) {
        return "SELECT * FROM " + DatabaseHelperImpl.getTableName(model) + " WHERE " + ItemModel.TEXT + " LIKE ? ";
    }

    public static String getSqlAllItems(final AnnotatedElement model) {
        return "SELECT * FROM " + DatabaseHelperImpl.getTableName(model);
    }

}
