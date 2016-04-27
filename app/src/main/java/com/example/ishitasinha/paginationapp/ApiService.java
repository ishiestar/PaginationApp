package com.example.ishitasinha.paginationapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ishitasinha on 22/04/16.
 */
public interface ApiService {
    public static final String BASE_URL = "http://swarajya.staging.quintype.io/";

    @GET("api/stories")
    Call<List<ListItem>> getItem(@Query("limit") String limit, @Query("offset") String offset, @Query("fields") String fields);
}
