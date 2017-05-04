package com.github.maxcriser.qrscanner.database.models;

import com.github.maxcriser.qrscanner.database.annotations.Table;
import com.github.maxcriser.qrscanner.database.annotations.typeInteger;
import com.github.maxcriser.qrscanner.database.annotations.typePrimaryKey;
import com.github.maxcriser.qrscanner.database.annotations.typeString;

@Table(name = "itemTable")
public final class ItemModel {

    @typePrimaryKey
    @typeInteger
    public static final String ID = "_id";

    @typeString
    public static final String TEXT = "text";

    @typeString
    public static final String CODE_FORMAT = "code_format";
}