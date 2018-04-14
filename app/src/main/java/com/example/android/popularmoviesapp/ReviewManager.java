package com.example.android.popularmoviesapp;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by nazaif on 3/2/18.
 */
@Parcel
public class ReviewManager {

    @SerializedName("author")
    String author;

    @SerializedName("content")
    String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
