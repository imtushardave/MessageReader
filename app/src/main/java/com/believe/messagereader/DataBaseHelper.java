package com.believe.messagereader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by HP on 23-Nov-17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "event";
    private static final int DATABASE_VERSION = 1;
    private HashMap hp;
    public String table_name = "event";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + table_name + "(id integer primary key, name VARCHAR, " +
                        "coaching VARCHAR, batch VARCHAR, mobileNumber VARCHAR UNIQUE,regId VARCHAR UNIQUE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    /*
    **  Insert data into table of database
     */
    public void insertData(String name, String coaching, String batch, String mobileNumber, String regId) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("coaching", coaching);
        contentValues.put("batch", batch);
        contentValues.put("mobileNumber", mobileNumber);
        contentValues.put("regId", regId);
        db1.insertWithOnConflict(table_name, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /* Get cursor ready to read data from database
    ** Return Cursor res
     */
    public Cursor getuser() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name + " ", null);
        return res;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, table_name);
        db.close();
        return cnt;
    }


    public boolean checkDataNumber(String mobileNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name + " WHERE mobileNumber = '"
                                   + mobileNumber +"' ", null);
        if (res.moveToFirst()) {
            return false;
        }
        if (res != null) {
            res.close();
        }
        db.close();
        return true;
    }
    public boolean checkDataId(String regId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name + " WHERE regId = '"
                + regId +"' ", null);
        if (res.moveToFirst()) {
            return false;
        }
        if (res != null) {
            res.close();
        }
        db.close();
        return true;
    }

}
