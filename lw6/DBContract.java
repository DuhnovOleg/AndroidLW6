package com.example.lw6;
import android.provider.BaseColumns;

public final class DBContract {

    private DBContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_KEY_ID = "id";
        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_PASS = "pass";
    }
}
