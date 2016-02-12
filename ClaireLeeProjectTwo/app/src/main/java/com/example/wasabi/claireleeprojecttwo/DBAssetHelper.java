package com.example.wasabi.claireleeprojecttwo;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Wasabi on 2/8/2016.
 */
public class DBAssetHelper extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "NEIGHBOR_DB.db";
    private static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
