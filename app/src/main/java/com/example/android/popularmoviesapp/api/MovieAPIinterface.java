package com.example.android.popularmoviesapp.api;

import com.example.android.popularmoviesapp.pojo.MovieManager;
import com.example.android.popularmoviesapp.pojo.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nazaif on 21/12/17.
 */

public interface MovieAPIinterface {
    @GET("movie/{sortby}")
    Call<MovieResults> getPopular(@Path("sortby") String sortBy);

    @GET("movie/{movie_id}")
    Call<MovieManager> getMovieInfo(@Path("movie_id") long movieId,
                                    @Query("append_to_response") String movieResponse);


}
