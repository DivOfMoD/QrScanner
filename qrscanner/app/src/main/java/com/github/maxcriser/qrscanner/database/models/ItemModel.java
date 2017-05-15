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
    public static final String LOGIN = "login";

    @typeString
    public static final String PASSWORD = "password";

    @typeString
    public static final String DATA = "data";

    @typeString
    public static final String DATE_INFO = "date_info";

    @typeString
    public static final String GPS = "gps";

    @typeString
    public static final String CODE_FORMAT = "code_format";
}