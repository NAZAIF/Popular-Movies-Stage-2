package com.example.android.popularmoviesapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.example.android.popularmoviesapp.provider.MovieContract.FavoriteMovieEntry.*;

/**
 * Created by nazaif on 5/2/18.
 */

public class MDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moviedetails.db";
    private static final int DATABASE_VERSION = 1;

    public MDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " LONG NOT NULL," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_POSTER + " TEXT NOT NULL," +
                COLUMN_RATING + " TEXT NOT NULL," +
                COLUMN_BACKDROP + " TEXT NOT NULL," +
                COLUMN_OVERVIEW + " TEXT NOT NULL," +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                COLUMN_RUNTIME + " TEXT NOT NULL" +
                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DROP_MOVIE_TABLE = "ALTER TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DROP_MOVIE_TABLE);
        onCreate(sqLiteDatabase);

    }
}
