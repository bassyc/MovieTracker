package com.androidessence.movietracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Controls the connection between the app and the MovieTracker database.
 *
 * Created by adammcneilly on 3/27/16.
 */
public class MovieOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movietracker.db";

    public MovieOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createMovieTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createMovieTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID +              " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_TITLE +     " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_YEAR +      " INTEGER DEFAULT 0, " +
                        MovieContract.MovieEntry.COLUMN_RATED +     " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_RELEASED +  " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_RUNTIME +   " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_GENRE +     " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_DIRECTOR +  " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_WRITER +    " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_ACTORS +    " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_PLOT +      " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_LANGUAGE +  " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_COUNTRY +   " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_AWARDS +    " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_POSTER +    " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_METASCORE + " INTEGER DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_RATING +    " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_VOTES +     " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_ID +        " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_TYPE +      " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_RESPONSE +  " TEXT DEFAULT '', " +
                        MovieContract.MovieEntry.COLUMN_WATCHED +   " INTEGER DEFAULT 0);"
        );
    }
}
