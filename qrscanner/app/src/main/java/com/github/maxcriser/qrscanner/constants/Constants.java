package com.github.maxcriser.qrscanner.constants;

public class Constants {

    public interface Database {

        String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s%s";
        String mDatabaseName = "database.cards.thecriser";
        int dbVersion = 1;
    }

    public interface AlternativeData {

        String SERVER = "http://mob.dvigenie.pro/walker.php";
        String USERNAME = "test";
        String PASSWORD = "test";
    }

    public interface Shared {

        String SHARED_NAME = "shared_preferences_walker";
        String SERVER = "shared_server_walker";
        String USE_ALT_SERVER = "shared_alt_server_walker";
        String PASSWORD = "shared_password_walker";
        String USERNAME = "shared_username_walker";
        String USE_ALT_PASS_AND_USERNMAE = "shared_alt_p_a_u_walker";
        String SOUND = "shared_sound_walker";
    }
}