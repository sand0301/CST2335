package com.example.cst2335.bbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BBCDatabase extends SQLiteOpenHelper {

    /**
     * creates or select database with the name bbc_database.
     * @param context Context of an activity
     * */
    public BBCDatabase(Context context) {
        super(context, "bbc_database", null, 1);
    }

    /**
     * creating table for storing data
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bbc_news(link text primary key, " +
                "title text, description text, pubDate text)");
    }

    /**
     * Drop table if exists to upgrade the database data.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists bbc_news");
    }

    /**
     * To save news to the SQLiteDatabase
     * @param bbcNewsItem to read individual record and save it
     *                    to the bbc_news table.
     * */
    public void saveNews(BBCNewsItem bbcNewsItem){
        //for insert record, we need writable database
        SQLiteDatabase db = getWritableDatabase();
        //reading values from bbcNewsItem and mapping it to contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("link", bbcNewsItem.getLink());
        contentValues.put("title", bbcNewsItem.getTitle());
        contentValues.put("description", bbcNewsItem.getDescription());
        contentValues.put("pubDate", bbcNewsItem.getPubDate());

        //insert records
        db.insert("bbc_news", null, contentValues);
    }

    /**
     * In our table, the link is primary key,
     * so the record with link unique identity will be deleted
     *
     * @param link primary key of a table row.
     * */
    public Integer deleteNews(String link){
        //to delete the data we need writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bbc_news",
                "link" + " = ? ",
                new String[]{link});
    }

    /**
     * Checks the data exists or not
     * @return true if the data already exists, otherwise returns false.
     * */
    boolean exists(String link){
        //readable database is enough to perform reading data.
        SQLiteDatabase database = getReadableDatabase();
        //check if the record exists with the primary key link.
        Cursor cursor = database.query("bbc_news", new String[]{
                        "link"
                }, "link" + " = ?", new String[]{link},
                null, null, null);
        //storing true or false in temporary variable
        boolean exists = cursor.getCount() != 0;

        //closing cursor and database
        cursor.close();
        database.close();

        return exists;
    }

    /**
     * Read all the records of the bbc_news table.
     * @return List of BBCNewsItem
     * */
    List<BBCNewsItem> getAllSavedNews(){
        //readable database is enough to perform reading data.
        SQLiteDatabase database = getReadableDatabase();
        //selecting all the columns of a table.
        Cursor cursor = database.query("bbc_news", new String[]{
                        "link", "title", "description", "pubDate"
                }, null, null,
                null, null, null);
        List<BBCNewsItem> bbcNewsItems = new ArrayList<>();

        //moving cursor to first and if the data exists, reading it recursively
        if(cursor.moveToFirst()) {
            do {
                String link = cursor.getString(0);
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String pubDate = cursor.getString(3);
                BBCNewsItem item = new BBCNewsItem();
                item.setLink(link);
                item.setPubDate(pubDate);
                item.setDescription(description);
                item.setTitle(title);
                //putting record to a list
                bbcNewsItems.add(item);
            } while (cursor.moveToNext());
        }
        return bbcNewsItems;
    }
}
