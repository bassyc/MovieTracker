package com.androidessence.movietracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * ContentProvider for querying the MovieTracker database.
 *
 * Created by adammcneilly on 3/27/16.
 */
public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 0;
    private static final int MOVIE_ID = 1;
    private static final int MOVIE_WATCHED = 2;
    private static final int MOVIE_UNWATCHED = 3;
    private static final int SEARCH = 10;
    private static final int SEARCH_ID = 11;

    private MovieOpenHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        String content = MovieContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(content, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);
        matcher.addURI(content, MovieContract.PATH_SEARCH, SEARCH);
        matcher.addURI(content, MovieContract.PATH_SEARCH + "/#", SEARCH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch(sUriMatcher.match(uri)) {
            case MOVIE:
                retCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case SEARCH:
                retCursor = db.query(
                        MovieContract.SearchEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SEARCH_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.SearchEntry.TABLE_NAME,
                        projection,
                        MovieContract.SearchEntry._ID + " = ?",
                        new String[] {String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case MOVIE:
            case MOVIE_WATCHED:
            case MOVIE_UNWATCHED:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case SEARCH:
                return MovieContract.SearchEntry.CONTENT_TYPE;
            case SEARCH_ID:
                return MovieContract.SearchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        long _id;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch(sUriMatcher.match(uri)) {
            case MOVIE:
                _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildAnnouncementUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            case SEARCH:
                _id = db.insert(MovieContract.SearchEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = MovieContract.SearchEntry.buildSearchUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch(sUriMatcher.match(uri)) {
            case MOVIE:
                rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SEARCH:
                rows = db.delete(MovieContract.SearchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0 || selection == null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch(sUriMatcher.match(uri)) {
            case MOVIE:
                rows = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SEARCH:
                rows = db.update(MovieContract.SearchEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
