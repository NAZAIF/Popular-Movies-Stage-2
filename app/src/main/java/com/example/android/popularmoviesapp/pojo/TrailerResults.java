package com.example.android.popularmoviesapp.pojo;

import com.example.android.popularmoviesapp.pojo.TrailerManager;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nazaif on 3/2/18.
 */
@Parcel
public class TrailerResults {
    @SerializedName("results")
    List<TrailerManager> TrailerResults;

    public List<TrailerManager> getTrailerResults() {
        return TrailerResults;
    }
}
