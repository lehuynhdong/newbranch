package com.example.dongle.location.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class PlaceSqliteHelper extends SQLiteOpenHelper {
    private static final String DB_Name="PLACE";
    private static final int DB_VESION=1;

    private static final String CREATE_PLACE_TBL_SQL =
            "CREATE TABLE " + DataUlits.PLACE_TBL_NAME + "("
                    + DataUlits.COLUMN_PLACE_ID + " " + DataUlits.TEXT + " " + DataUlits.PRIMARY_KEY
                    + DataUlits.COLUMN_PLACE_CATEGORY_ID + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL+ ", "
                    + DataUlits.COLUMN_PLACE_NAME + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_ADDRESS + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_PRICE + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_TIME + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_DESCRIPITION + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_IMGAE + " " + DataUlits.BOLB + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_LAT + " " + DataUlits.REAL + " " + DataUlits.NOT_NULL + ", "
                    + DataUlits.COLUMN_PLACE_LNG + " " + DataUlits.REAL + " " + DataUlits.NOT_NULL
                    +")";
    private static final String CREATE_CATEGORY_TBL_SQL =
            "CREATE TABLE " + DataUlits.CATEGORY_TBL_NAME + "("
                    + DataUlits.COLUMN_CATEGORY_ID + " " + DataUlits.TEXT + " " + DataUlits.PRIMARY_KEY
                    + DataUlits.COLUMN_CATEGORY_NAME + " " + DataUlits.TEXT + " " + DataUlits.NOT_NULL
                    +")";

    private static final String INSERT_CATEGORY_SQL =
            "INSERT INTO " + DataUlits.CATEGORY_TBL_NAME + "(" + DataUlits.COLUMN_CATEGORY_ID + ", " + DataUlits.COLUMN_CATEGORY_NAME + ")"
                    + " VALUES  "
                    + "('1', 'Coffee'), "
                    + "('2', 'Restaurant'), "
                    + "('3', 'Cinema'), "
                    + "('4', 'ATM') ";

    public PlaceSqliteHelper(Context context) {
        super(context, DB_Name, null, DB_VESION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PLACE_TBL_SQL);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TBL_SQL);
        sqLiteDatabase.execSQL(INSERT_CATEGORY_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
