package com.example.newsbuzz.Api;

import com.example.newsbuzz.NewsItem;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<JsonObject> getTopHeadlines(@Query("apiKey") String apiKey, @Query("sortBy") String sortParameter, @Query("page") int page, @Query("country") String country);

    @GET("everything")
    Call<JsonObject> getOtherNews(@Query("apiKey") String apiKey, @Query("sortBy") String sortParameter, @Query("page") int page, @Query("q") String category);

}
