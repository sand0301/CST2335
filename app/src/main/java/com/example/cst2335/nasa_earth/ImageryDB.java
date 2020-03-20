package com.example.cst2335.nasa_earth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class ImageryDB extends SQLiteOpenHelper {

    //Constants for using as database strings
    String databaseName = "imageryDatabase";
    String tableName = "imageryData";
    String colID = "id";
    String colDate = "date";
    String colUrl = "url";
    String colLat = "lat";
    String colLon = "lon";

    /**
     * @param context of an activity or fragment
     * */
    public ImageryDB(Context context) {
        super(context, "imageryDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating table
        String sql = "create table " + tableName
                + "(" + colID + " text primary key, " +
                colDate + " text," +
                colUrl + " text," +
                colLat + " text," +
                colLon + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop the existing table
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    /**
     * @param data To be stored
     * */
    void saveImageryData(EarthImage data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(colID, data.getId());
        contentValues.put(colDate, data.getDate());
        contentValues.put(colUrl, data.getUrl());
        contentValues.put(colLat, data.getLat());
        contentValues.put(colLon, data.getLon());
        SQLiteDatabase database = this.getWritableDatabase();
        //inserting the record into the database
        database.insert(tableName, null, contentValues);
    }

    /**
     * Deletes the record matching with the id
     * @param id of a record
     * */
    void deleteImageryData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,
                colID + " = ? ",
                new String[]{id});
    }


    /**
     * Checks if the data exists or not in the database
     * */
    int checkData(String id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(tableName, new String[]{
                        colID, colDate, colUrl, colLat, colLon
                }, colID + " = ?", new String[]{id},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * returns the list of records to show into the listview
     * @return List of EarthImage record.
     * */
    ArrayList<EarthImage> getAllImageryData() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(tableName, new String[]{
                        colID, colDate, colUrl, colLat, colLon
                }, null,
                null, null, null, null);
        ArrayList<EarthImage> earthImages = new ArrayList<>();
        //reading the data through cursor
        if(cursor.moveToFirst()){
            do {
                //Processing each record value
                EarthImage earthImage = new EarthImage();
                earthImage.setId(cursor.getString(0));
                earthImage.setDate(cursor.getString(1));
                earthImage.setUrl(cursor.getString(2));
                earthImage.setLat(cursor.getString(3));
                earthImage.setLon(cursor.getString(4));
                //adding each record to the list
                earthImages.add(earthImage);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return earthImages;
    }
}