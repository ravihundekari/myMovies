package com.ravi.mymovies.interfaces;

import com.ravi.mymovies.model.MovieResult;
import com.ravi.mymovies.model.MovieImage;
import com.ravi.mymovies.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/upcoming")
    Call<MovieResult> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Result> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/images")
    Call<MovieImage> getMovieImages(@Path("id") int id, @Query("api_key") String apiKey);
}
