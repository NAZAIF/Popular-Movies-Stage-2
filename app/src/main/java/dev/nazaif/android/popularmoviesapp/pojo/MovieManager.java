package dev.nazaif.android.popularmoviesapp.pojo;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nazaif on 21/12/17.
 */
@Parcel
public class MovieManager {

    @SerializedName("runtime")
    String runtime;

    @SerializedName("images")
    Images images;

    @SerializedName("reviews")
    ReviewResults reviewResults;

    @SerializedName("videos")
    TrailerResults trailerResults;

    boolean inFavorite;

    @SerializedName("id")
    long id;

    @SerializedName("original_title")
    String title;

    @SerializedName("poster_path")
    String poster;

    @SerializedName("backdrop_path")
    String backdrop;

    @SerializedName("overview")
    String overview;

    @SerializedName("vote_average")
    String rating;

    @SerializedName("release_date")
    String releaseDate;


    public MovieManager() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isInFavorite() {
        return inFavorite;
    }

    public void setInFavorite(boolean inFavorite) {
        this.inFavorite = inFavorite;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public ReviewResults getReviewResults() {
        return reviewResults;
    }

    public void setReviewResults(ReviewResults reviewResults) {
        this.reviewResults = reviewResults;
    }

    public TrailerResults getTrailerResults() {
        return trailerResults;
    }

    public void setTrailerResults(TrailerResults trailerResults) {
        this.trailerResults = trailerResults;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", backdrop='" + backdrop + '\'' +
                ", synopsis='" + overview + '\'' +
                ", rating='" + rating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime='" + runtime + '\'' +
                ", trailersResults=" + trailerResults +
                ", reviewsResults=" + reviewResults +
                ", images=" + images +
                ", isFavourite=" + inFavorite +
                '}';
    }

    @Parcel
    public static class Images {
        @SerializedName("backdrops")
        List<Backdrops> backdropsList;

        public List<Backdrops> getBackdropsList() {
            return backdropsList;
        }


    }

    @Parcel
    public static class Backdrops {

        @SerializedName("file_path")
        String path;

        public String getPath() {
            return path;
        }

    }
}
