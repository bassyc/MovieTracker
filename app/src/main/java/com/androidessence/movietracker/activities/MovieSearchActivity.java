package com.androidessence.movietracker.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidessence.lib.MaterialSearchView;
import com.androidessence.movietracker.R;

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

        // Setup SearchView
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.v("ADAM_MCNEILLY", s);
                mSearchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Setup FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.movie_search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.openSearch();
            }
        });
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
