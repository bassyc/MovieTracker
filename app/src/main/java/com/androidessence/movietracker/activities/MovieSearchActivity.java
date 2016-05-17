package com.androidessence.movietracker.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.androidessence.lib.MaterialSearchView;
import com.androidessence.movietracker.R;
import com.androidessence.movietracker.adapters.SearchHistoryAdapter;
import com.androidessence.movietracker.data.MovieContract;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class MovieSearchActivity extends AppCompatActivity {
    /**
     * The API that we read movies from.
     */
    private static final String API = "http://omdbapi.com";

    /**
     * The MaterialSearchView used to lookup movies.
     */
    private MaterialSearchView mSearchView;

    /**
     * Adapter of the most recent searches to display in the SearchView.
     */
    private SearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get elements
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);

        // Setup adapter
        //TODO: count?
        mSearchHistoryAdapter = new SearchHistoryAdapter(this, getRecentSearches(10));

        // Setup SearchView
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                insertSearch(s);
                mSearchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mSearchView.setAdapter(mSearchHistoryAdapter);

        // Setup FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.movie_search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.openSearch();
            }
        });
    }

    /**
     * Inserts a search entry into the database.
     */
    private void insertSearch(String search) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.SearchEntry.COLUMN_TEXT, search);
        values.put(MovieContract.SearchEntry.COLUMN_LAST_SEARCH, DateTime.now().getMillis());

        getContentResolver().insert(MovieContract.SearchEntry.CONTENT_URI, values);
    }

    private String[] getRecentSearches(int searchCount) {
        Cursor cursor = getContentResolver().query(
                MovieContract.SearchEntry.CONTENT_URI,
                SearchHistoryAdapter.SEARCH_COLUMNS,
                null,
                null,
                MovieContract.SearchEntry.COLUMN_LAST_SEARCH + " DESC " + "LIMIT " + searchCount
        );

        String[] searches = new String[cursor.getCount()];

        for(int i = 0; i < searches.length; i++) {
            if(cursor.moveToPosition(i)) {
                searches[i] = cursor.getString(SearchHistoryAdapter.TEXT_INDEX);
            }
        }

        return searches;
    }
}
