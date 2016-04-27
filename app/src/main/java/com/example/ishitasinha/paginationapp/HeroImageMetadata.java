package com.example.ishitasinha.paginationapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class HeroImageMetadata {

    @SerializedName("original-url")
    @Expose
    private String originalUrl;

    /**
     * @return The originalUrl
     */
    public String getOriginalUrl() {
        return originalUrl;
    }
}