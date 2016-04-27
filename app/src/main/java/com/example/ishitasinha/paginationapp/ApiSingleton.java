package com.example.ishitasinha.paginationapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class ApiSingleton {
    static ApiService service;

    public static ApiService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(ApiService.class);
        }
        return service;
    }
}
