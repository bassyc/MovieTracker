package com.androidessence.movietracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidessence.lib.MaterialSearchView;

public class MovieSearchActivity extends AppCompatActivity {
    /**
     * The API that we read movies from.
     */
    private static final String API = "http://omdbapi.com";

    /**
     * The MaterialSearchView used to lookup movies.
     */
    private MaterialSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get elements
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                mSearchView.openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
