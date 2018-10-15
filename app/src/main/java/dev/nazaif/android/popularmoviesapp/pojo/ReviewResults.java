package dev.nazaif.android.popularmoviesapp.pojo;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nazaif on 3/2/18.
 */
@Parcel
public class ReviewResults {
    @SerializedName("results")
    List<ReviewManager> reviews;

    @SerializedName("total_results")
    int totalReviews;

    public List<ReviewManager> getReviews() {
        return reviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }
}
