package com.example.mymusicplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SongsDatabase.db";

    // Table constants
    private static final String TABLE_SONGS = "songstable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "songname";

    // Create table SQL statement
    private static final String CREATE_TABLE_SONGS = "CREATE TABLE " + TABLE_SONGS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT)";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the songs table
        db.execSQL(CREATE_TABLE_SONGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
