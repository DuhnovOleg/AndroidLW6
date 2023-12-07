package com.example.lw6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());

        db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public List<User> getAllUsers()
    {
        List<User> usersList = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setPass(cursor.getString(2));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        return usersList;
    }

    public boolean DeleteUser(String userLogin)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<User> userList = getAllUsers();

        for (int i = 0; i < userList.size(); i++)
        {
            User tempUser = userList.get(i);

            if (Objects.equals(tempUser._login, userLogin.toString()))
            {
                String ID = Integer.toString(tempUser.getID());
                db.delete(DBContract.UserEntry.TABLE_NAME, DBContract.UserEntry.COLUMN_NAME_KEY_ID + "=" + ID, null);
                db.close();
                return true;
            }
        }
        return false;
    }

    public void ChangePassword(User user, String newPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String ID = Integer.toString(user.getID());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, newPassword);
        db.update(DBContract.UserEntry.TABLE_NAME, values, DBContract.UserEntry.COLUMN_NAME_KEY_ID + "=" + ID, null);
        db.close();
    }
}
