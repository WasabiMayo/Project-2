package com.example.wasabi.claireleeprojecttwo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Wasabi on 2/8/2016.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NEIGHBOR_DB.db";
    public static final String TABLE_NAME = "NEIGHBOR_LIST";

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_NEIGHBOR = "NEIGHBOR";
    public static final String COL_ADDRESS = "ADDRESS";
    public static final String COL_PRICE = "PRICE";
    public static final String COL_GREAT_FOR = "GREATE_FOR";
    public static final String COL_TYPE = "TYPE";
    public static final String COL_FAVORITE = "FAVORITE";
    public static final String COL_CREDITCARD = "CREDITCARD";
    public static final String COL_WIFI = "WIFI";
    public static final String COL_RATING = "RATING";
    public static final String COL_KEYWORD = "KEYWORD";
    public static final String COL_COMMENT = "COMMENT";
    public static final String COL_PHOTO_ID = "PHOTO_ID";
    public static final String COL_VOTES = "VOTES";

    public static final String[] NEIGHBOR_COLUMNS = {COL_ID, COL_NAME, COL_NEIGHBOR, COL_ADDRESS, COL_PRICE, COL_GREAT_FOR, COL_TYPE, COL_FAVORITE, COL_CREDITCARD, COL_WIFI, COL_RATING, COL_KEYWORD, COL_COMMENT, COL_PHOTO_ID, COL_VOTES};

    public static final String CREATE_NEIGHBOR_LIST_TABLE =
            "CREATE TABLE" + TABLE_NAME +
                    "(" +
                    COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT, " +
                    COL_NEIGHBOR + " TEXT, " +
                    COL_ADDRESS + " TEXT, " +
                    COL_PRICE + " TEXT, " +
                    COL_GREAT_FOR + " TEXT, " +
                    COL_TYPE + " TEXT, " +
                    COL_FAVORITE + " INTEGER, " +
                    COL_CREDITCARD + " INTEGER, " +
                    COL_WIFI + "INTEGER, " +
                    COL_RATING + " INTEGER, " +
                    COL_KEYWORD + " TEXT, " +
                    COL_COMMENT + " TEXT, " +
                    COL_PHOTO_ID + " TEXT, " +
                    COL_VOTES + " INTEGER) ";

    private MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MySQLiteOpenHelper instance;

    public static MySQLiteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new MySQLiteOpenHelper(context.getApplicationContext());
        }return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEIGHBOR_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public Cursor getNeighborList(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, null,null,null,null,null,null);

        return cursor;
    }

    public Cursor getNeighborListByGreatFor(String greatfor, boolean isFilterApplied, String price,int wifi, int creditcard){
        SQLiteDatabase db = this.getReadableDatabase();
        if(!isFilterApplied) {
            Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, COL_GREAT_FOR + " LIKE ? ", new String[]{"%" + greatfor + "%"}, null, null, null, null);
            return cursor;
        }else{
            String selection = COL_GREAT_FOR + " LIKE ?";
            ArrayList<String> tempArgs = new ArrayList<>();
            tempArgs.add("%"+greatfor+"%");
            String[] selectionArgs;
            if(price != null) {
                selection += " AND " + COL_PRICE + " = ? ";
                tempArgs.add(price);
            }
            if(wifi == 1){
                selection += "AND " + COL_WIFI + " = ? ";
                tempArgs.add(String.valueOf(wifi));
            }
            if(creditcard == 1){
                selection += " AND " + COL_CREDITCARD + " = ?";
                tempArgs.add(String.valueOf(creditcard));
            }
            selectionArgs = new String[tempArgs.size()];
            for (int i = 0; i < tempArgs.size(); i++){
                selectionArgs[i] = tempArgs.get(i);
            }
            Cursor cursor = db.query(TABLE_NAME,NEIGHBOR_COLUMNS, selection, selectionArgs, null, null, null, null);
            return cursor;
        }
    }


    public Cursor mainSearchFunction(String query, boolean isFilterApplied, String price, int wifi, int creditcard) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_NAME + " LIKE ? OR " + COL_NEIGHBOR + " LIKE ? OR " + COL_ADDRESS + " LIKE ? OR " + COL_KEYWORD + " LIKE ? OR " + COL_TYPE + " LIKE ? ";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%", "%" + query + "%", "%" + query + "%"};

        if (!isFilterApplied) {
            Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, selection, selectionArgs, null, null, null, null);
            return cursor;
        } else {
            //Filter is applied
            selection = " ( " + selection + " ) ";
            ArrayList<String> tempArgs = new ArrayList<>();
            tempArgs.add(selectionArgs[0]);
            tempArgs.add(selectionArgs[1]);
            tempArgs.add(selectionArgs[2]);
            tempArgs.add(selectionArgs[3]);
            tempArgs.add(selectionArgs[4]);

            if (price != null) {
                selection += " AND " + COL_PRICE + " = ? ";
                tempArgs.add(price);
            }
            if (wifi == 1) {
                selection += "AND " + COL_WIFI + " = ? ";
                tempArgs.add(String.valueOf(wifi));
            }
            if (creditcard == 1) {
                selection += " AND " + COL_CREDITCARD + " = ?";
                tempArgs.add(String.valueOf(creditcard));
            }
            selectionArgs = new String[tempArgs.size()];
            for (int i = 0; i < tempArgs.size(); i++) {
                selectionArgs[i] = tempArgs.get(i);
            }
            Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, selection, selectionArgs, null, null, null, null);
            return cursor;
        }
    }

    public Cursor getDetailsById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, COL_ID+" = ? ", new String[]{String.valueOf(id)},null,null,null,null);
        return cursor;
    }

    public void updateFavorite(int changedFavorite, int rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_FAVORITE+" = "+changedFavorite+" WHERE "+COL_ID+" = "+rowId);
    }

    public void updateRating(int rating, int rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getDetailsById(rowId);
        cursor.moveToFirst();
        int votes = cursor.getInt(cursor.getColumnIndex(COL_VOTES));
        int currentRating = cursor.getInt(cursor.getColumnIndex(COL_RATING));
        int updatedRating = ((currentRating*votes)+rating)/(votes+1);

        db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_RATING+" = "+updatedRating+" WHERE "+COL_ID+" = "+rowId);
        db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_VOTES+" = "+ (votes+1) +" WHERE "+COL_ID+" = "+rowId);
    }

    public void updateComment(String comment, int rowId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getDetailsById(rowId);
        cursor.moveToFirst();
        String currentComment = cursor.getString(cursor.getColumnIndex(COL_COMMENT));
        if(currentComment == null){
            db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_COMMENT+" = '"+comment+"' WHERE "+COL_ID+" = "+rowId);
        }else{
            String updatedComment = currentComment + "~" + comment;
            db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_COMMENT+" = '"+updatedComment+"' WHERE "+COL_ID+" = "+rowId);
        }
    }

    public Cursor getFilteredList(Cursor oldCursor, String price){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection;
        ArrayList<String> tempArgs = new ArrayList<>();
        String[] selectionArgs;
        selection = COL_PRICE + " = ? ";
        tempArgs.add(price);
        selectionArgs = new String[tempArgs.size()];
        for (int i = 0; i < tempArgs.size(); i++){
            selectionArgs[i] = tempArgs.get(i);
        }
        Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, selection, selectionArgs, null, null, null, null);
        return cursor;
    }

    public Cursor getRandomVenue(int limit){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, null, null, null, null, "RANDOM()", String.valueOf(limit));
        return cursor;
    }

    public Cursor getFavoriteList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, COL_FAVORITE + " = ?", new String[]{String.valueOf(1)}, null, null, null, null);
        return cursor;
    }

    public Cursor getNeighborListByNeighbor(String neighbor, boolean isFilterApplied, String price, int wifi, int creditcard){
        SQLiteDatabase db = this.getReadableDatabase();
        if(!isFilterApplied) {
            Cursor cursor = db.query(TABLE_NAME, NEIGHBOR_COLUMNS, COL_NEIGHBOR + " LIKE ?", new String[]{"%"+neighbor+"%"}, null, null, null, null);
            return cursor;
        }else{
            String selection = COL_NEIGHBOR + " LIKE ?";
            ArrayList<String> tempArgs = new ArrayList<>();
            tempArgs.add("%"+neighbor+"%");
            String[] selectionArgs;
            if(price != null) {
                selection += " AND " + COL_PRICE + " = ? ";
                tempArgs.add(price);
            }
            if(wifi == 1){
                selection += "AND " + COL_WIFI + " = ? ";
                tempArgs.add(String.valueOf(wifi));
            }
            if(creditcard == 1){
                selection += " AND " + COL_CREDITCARD + " = ?";
                tempArgs.add(String.valueOf(creditcard));
            }
            selectionArgs = new String[tempArgs.size()];
            for (int i = 0; i < tempArgs.size(); i++){
                selectionArgs[i] = tempArgs.get(i);
            }
            Cursor cursor = db.query(TABLE_NAME,NEIGHBOR_COLUMNS, selection, selectionArgs, null, null, null, null);
            return cursor;
        }
    }

}
