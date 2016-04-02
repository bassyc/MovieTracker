package com.androidessence.movietracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidessence.movietracker.adapters.MovieAdapter;
import com.androidessence.movietracker.data.OMDBAPI;
import com.androidessence.movietracker.pojos.Movie;
import com.androidessence.movietracker.pojos.MovieList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String API = "http://omdbapi.com";

    private RecyclerView mMovieRecyclerView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        getUIElements(view);
        readFromAPI();

        return view;
    }

    private void getUIElements(View view) {
        mMovieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
    }

    /**
     * Test method that reads all of the Harry Potter movies from the OMDB API.
     */
    private void readFromAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();

        OMDBAPI omdbapi = restAdapter.create(OMDBAPI.class);

        Log.v(LOG_TAG, "About to read.");

        omdbapi.getMovieByTitle("Cloverfield", new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                Log.v(LOG_TAG, "SUCCESS ONE!");
                Log.v(LOG_TAG, movie.getTitle());
                Log.v(LOG_TAG, movie.getPlot());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(LOG_TAG, "Failed one.");
            }
        });

        omdbapi.getMoviesWithTitle("Cloverfield", new Callback<MovieList>() {
            @Override
            public void success(MovieList omdbModels, Response response) {
                Log.v(LOG_TAG, "SUCCESS!");
                Log.v(LOG_TAG, "Num movies: " + omdbModels.getSearch().size());
                Log.v(LOG_TAG, omdbModels.getSearch().get(0).getClass().toString());

                for(Movie movie : omdbModels.getSearch()) {
                    Log.v(LOG_TAG, movie.getTitle());
                    Log.v(LOG_TAG, String.valueOf(movie.getYear()));
                }

                setupRecyclerView(omdbModels.getSearch());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v(LOG_TAG, "FAILURE!");
                Log.v(LOG_TAG, error.getMessage());
            }
        });
    }

    private void setupRecyclerView(List<Movie> movies) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMovieRecyclerView.setLayoutManager(linearLayoutManager);

        // Sort movies first
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getYear() - rhs.getYear();
            }
        });

        MovieAdapter adapter = new MovieAdapter(getActivity(), movies);
        mMovieRecyclerView.setAdapter(adapter);

        Log.v(LOG_TAG, "Setup recyclerview.");
    }
}
