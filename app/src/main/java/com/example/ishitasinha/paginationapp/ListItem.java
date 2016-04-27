package com.example.ishitasinha.paginationapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class ListItem {
    @Expose
    String id;
    @SerializedName("hero-image-metadata")
    HeroImageMetadata imageMetaData;
    @SerializedName("headline")
    String headline;
    @SerializedName("author-name")
    String author;

    ListItem(String id, HeroImageMetadata imageMetaData, String headline, String author) {
        this.author = author;
        this.id = id;
        this.headline = headline;
        this.imageMetaData = imageMetaData;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getHeadline() {
        return headline;
    }

    public String getPosterUrl() {
        return imageMetaData.getOriginalUrl();
    }
}
