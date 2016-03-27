package com.androidessence.movietracker.pojos;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class represents a list of movies returned by the OMDB API.
 */
@Generated("org.jsonschema2pojo")
public class MovieList {

    @SerializedName("Search")
    @Expose
    private List<Movie> Search = new ArrayList<Movie>();

    /**
     *
     * @return
     * The Search
     */
    public List<Movie> getSearch() {
        return Search;
    }

    /**
     *
     * @param Search
     * The Search
     */
    public void setSearch(List<Movie> Search) {
        this.Search = Search;
    }

}
