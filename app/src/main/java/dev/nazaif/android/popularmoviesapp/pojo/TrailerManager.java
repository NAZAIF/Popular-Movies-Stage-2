package dev.nazaif.android.popularmoviesapp.pojo;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by nazaif on 3/2/18.
 */
@Parcel
public class TrailerManager {
    @SerializedName("key")
    String key;

    @SerializedName("name")
    String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
