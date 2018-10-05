package com.example.android.popularmoviesapp.pojo;

import com.example.android.popularmoviesapp.pojo.MovieManager;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nazaif on 21/12/17.
 */

public class MovieResults {

    @SerializedName("results")
    private List<MovieManager> movieResults;

    public List<MovieManager> getMovieResults() {
        return movieResults;
    }

}
