package com.example.android.popularmoviesapp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.utils.DBUtils;
import com.example.android.popularmoviesapp.api.MDBServiceAPI;
import com.example.android.popularmoviesapp.api.MovieAPIinterface;
import com.example.android.popularmoviesapp.pojo.MovieManager;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.pojo.ReviewManager;
import com.example.android.popularmoviesapp.pojo.ReviewResults;
import com.example.android.popularmoviesapp.adapter.ReviewsAdapter;
import com.example.android.popularmoviesapp.adapter.TrailerAdapter;
import com.example.android.popularmoviesapp.pojo.TrailerManager;
import com.example.android.popularmoviesapp.pojo.TrailerResults;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmoviesapp.api.MDBServiceAPI.YOUTUBE_URL;
import static com.example.android.popularmoviesapp.provider.MovieContract.FavoriteMovieEntry.*;


public class MovieDetailsActivity extends MainActivity implements TrailerAdapter.TrailersListener, ReviewsAdapter.ReviewListener {

    public static final String SHARED_PREF_FILE = "favourites";
    public static final String EXTRA_MOVIE_PARCELABLE = "extra_movie_parcelable";
    public static final String MOVIE_DETAILS_STATE = "movie_details_state";
    private static boolean isFavChecked;
    StringBuilder stringBuilder;
    ReviewResults reviewResults;
    TrailerResults trailerResults;
    ReviewsAdapter reviewsAdapter;
    TrailerAdapter trailerAdapter;
    RecyclerView rvTrailers, rvReviews;
    FloatingActionButton fab;
    CardView cardView;
    ProgressBar progressBar;
    ImageView ivBackdrop, ivPoster;
    TextView tvRating, tvRelease, tvOverview, tvTitle;
    private MovieAPIinterface MovieDbApi;
    private MovieManager movieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        rvReviews = (RecyclerView) findViewById(R.id.rv_reviews_list);
        rvTrailers = (RecyclerView) findViewById(R.id.rv_trailers_list);
        cardView = (CardView) findViewById(R.id.overview_card);
        progressBar = (ProgressBar) findViewById(R.id.pgBar);
        tvTitle = (TextView) findViewById(R.id.title_text);
        ivBackdrop = (ImageView) findViewById(R.id.backdrop_image);
        ivPoster = (ImageView) findViewById(R.id.poster_image);
        tvOverview = (TextView) findViewById(R.id.overview_text);
        tvRelease = (TextView) findViewById(R.id.release_text);
        tvRating = (TextView) findViewById(R.id.rating_text);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (savedInstanceState != null) {
            movieManager = Parcels.unwrap(savedInstanceState.getParcelable(MOVIE_DETAILS_STATE));
            trailerResults = Parcels.unwrap(savedInstanceState.getParcelable("TRAILERS"));
            reviewResults = Parcels.unwrap(savedInstanceState.getParcelable("REVIEWS"));
            setLayoutManagers();
            setReviewRecyclerAdapter(rvReviews);
            setTrailerAdapter(rvTrailers);
        } else {
            MovieDbApi = MDBServiceAPI.createService(MovieAPIinterface.class);
            movieManager = (MovieManager) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE_PARCELABLE));
            getMovieResponse(movieManager.getId());


        }

        isFavChecked = DBUtils.getState(this, movieManager);
        getDetails(movieManager);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_DETAILS_STATE, Parcels.wrap(movieManager));
        outState.putParcelable("TRAILERS", Parcels.wrap(trailerResults));
        outState.putParcelable("REVIEWS", Parcels.wrap(reviewResults));
    }

    public void back(View view) {
        finish();
    }

    private void getDetails(final MovieManager movie) {
        cardView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        tvRating.setText(movieManager.getRating());
        tvRating.setTypeface(null, Typeface.BOLD);
        tvRelease.setText(movieManager.getReleaseDate());
        tvOverview.setText(movieManager.getOverview());
        tvTitle.setText(movieManager.getTitle());

        Picasso.with(MovieDetailsActivity.this)
                .load(MDBServiceAPI.POSTER_URL + movieManager.getPoster())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_img)
                .into(ivPoster);

        Picasso.with(MovieDetailsActivity.this)
                .load(MDBServiceAPI.BACKDROP_URL + movieManager.getBackdrop())
                .placeholder(R.drawable.placeholder_land)
                .error(R.drawable.error_img)
                .into(ivBackdrop);

        if (isFavChecked) {
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavChecked) {
                    Toast.makeText(MovieDetailsActivity.this, getString(R.string.removi_string), Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    isFavChecked = false;
                    String movieId = String.valueOf(movieManager.getId());
                    Uri uri = CONTENT_URI.buildUpon().appendPath(movieId).build();
                    getContentResolver().delete(uri, null, null);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(movieManager.getTitle(), isFavChecked);
                    editor.commit();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.added_string, Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    isFavChecked = true;
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_ID, movieManager.getId());
                    cv.put(COLUMN_TITLE, movieManager.getTitle());
                    cv.put(COLUMN_RATING, movieManager.getRating());
                    cv.put(COLUMN_POSTER, movieManager.getPoster());
                    cv.put(COLUMN_BACKDROP, movieManager.getBackdrop());
                    cv.put(COLUMN_RELEASE_DATE, movieManager.getReleaseDate());
                    cv.put(COLUMN_OVERVIEW, movieManager.getOverview());
                    String runtime;
                    if (movieManager.getRuntime() == null) {
                        runtime = " ";
                    } else {
                        runtime = movieManager.getRuntime();
                    }
                    cv.put(COLUMN_RUNTIME, runtime);
                    getContentResolver().insert(CONTENT_URI, cv);
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = preferences.edit();
                    spEditor.putBoolean(movieManager.getTitle(), isFavChecked);
                    spEditor.commit();


                }
            }
        });
    }

    private void setLayoutManagers() {
        if (movieManager.getReviewResults() != null &&
                movieManager.getTrailerResults() != null) {
            LinearLayoutManager trailersManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvTrailers.setLayoutManager(trailersManager);
            rvTrailers.setHasFixedSize(true);

            LinearLayoutManager reviewsManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvReviews.setLayoutManager(reviewsManager);
            rvReviews.setHasFixedSize(true);
        }
    }


    @Override
    public void onTrailersClick(int index) {
        TrailerManager trailerManager = movieManager.getTrailerResults().getTrailerResults().get(index);
        Intent youtIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + trailerManager.getKey()));
        if (youtIntent.resolveActivity(getPackageManager()) != null)
            startActivity(youtIntent);
    }


    private void setTrailerAdapter(RecyclerView rv) {
        trailerAdapter = new TrailerAdapter(this, movieManager, this);
        rv.setAdapter(trailerAdapter);
    }

    private void setReviewRecyclerAdapter(RecyclerView rcv) {
        reviewsAdapter = new ReviewsAdapter(this, movieManager, this);
        rcv.setAdapter(reviewsAdapter);
    }


    public void getMovieResponse(long movieId) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.api_append_videos));
        stringBuilder.append(",");
        stringBuilder.append(getString(R.string.api_append_reviews));
        stringBuilder.append(",");
        stringBuilder.append(getString(R.string.api_append_images));

        Call<MovieManager> call = MovieDbApi.getMovieInfo(movieId, stringBuilder.toString());
        call.enqueue(new Callback<MovieManager>() {
            @Override
            public void onResponse(Call<MovieManager> call, Response<MovieManager> response) {
                movieManager = response.body();
                trailerResults = movieManager.getTrailerResults();
                movieManager.setTrailerResults(trailerResults);
                setLayoutManagers();
                setTrailerAdapter(rvTrailers);
                reviewResults = movieManager.getReviewResults();
                movieManager.setReviewResults(reviewResults);
                setReviewRecyclerAdapter(rvReviews);
            }

            @Override
            public void onFailure(Call<MovieManager> call, Throwable t) {
                Log.d(MovieDetailsActivity.class.getSimpleName(), t.getMessage());

            }
        });
    }

    @Override
    public void onReviewsClick(int index) {
        Intent reviewsIntent = new Intent(this, ReviewViewer.class);
        ReviewManager manager = reviewResults.getReviews().get(index);
        reviewsIntent.putExtra("EXTRA_REVIEW_AUTHOR", manager.getAuthor());
        reviewsIntent.putExtra("EXTRA_REVIEW_CONTENT", manager.getContent());
        startActivity(reviewsIntent);
    }
}