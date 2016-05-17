package com.androidessence.movietracker.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class that defines the schema for the MovieTracker database.
 *
 * Created by adammcneilly on 3/26/16.
 */
public class MovieContract {
    /**
     * The "Content Authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.androidessence.movietracker";

    /**
     * Use the content authority to create the base of all URIs which apps will use to contact
     * the content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_SEARCH = "search";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_RATED = "rated";
        public static final String COLUMN_RELEASED = "released";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_DIRECTOR = "director";
        public static final String COLUMN_WRITER = "writer";
        public static final String COLUMN_ACTORS = "actors";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_AWARDS = "awards";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_METASCORE = "metascore";
        public static final String COLUMN_RATING = "imdbRating";
        public static final String COLUMN_VOTES = "imdbVotes";
        public static final String COLUMN_ID = "imdbID";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_RESPONSE = "response";
        public static final String COLUMN_WATCHED = "watched";

        public static Uri buildAnnouncementUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SearchEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_SEARCH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SEARCH;

        public static final String TABLE_NAME = "searchTable";
        public static final String COLUMN_TEXT = "searchText";
        public static final String COLUMN_LAST_SEARCH = "lastSearch";

        public static Uri buildSearchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
