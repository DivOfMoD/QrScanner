package com.github.maxcriser.qrscanner.constants;

public class DatabaseConst {

    public interface Database {

        String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s%s";
        String mDatabaseName = "database.cards.thecriser";
        int dbVersion = 1;
    }
}