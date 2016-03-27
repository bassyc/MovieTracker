package com.androidessence.movietracker.data;

import com.androidessence.movietracker.pojos.Movie;
import com.androidessence.movietracker.pojos.MovieList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * This class is used to connect the MovieTracker application to the OMDB API.
 *
 * Created by adammcneilly on 11/9/15.
 */
public interface OMDBAPI {
    @GET("/?type=movie")
    // String title is for passing values
    // Response is the response from the server which is now in the POJO
    void getMovieByTitle(@Query("t") String title, Callback<Movie> response);


    @GET("/?type=movie")
    void getMoviesWithTitle(@Query("S") String title, Callback<MovieList> responses);
}
