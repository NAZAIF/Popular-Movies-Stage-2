package com.example.android.popularmoviesapp.activity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.utils.DBUtils;
import com.example.android.popularmoviesapp.api.MDBServiceAPI;
import com.example.android.popularmoviesapp.api.MovieAPIinterface;
import com.example.android.popularmoviesapp.adapter.MovieAdapter;
import com.example.android.popularmoviesapp.provider.MovieContract;
import com.example.android.popularmoviesapp.pojo.MovieManager;
import com.example.android.popularmoviesapp.pojo.MovieResults;
import com.example.android.popularmoviesapp.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmoviesapp.provider.MovieContract.FavoriteMovieEntry.*;
import static com.example.android.popularmoviesapp.activity.MovieDetailsActivity.EXTRA_MOVIE_PARCELABLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.ClickListener {

    private static final String SELECTED_STRING = "selected_string";
    private static final String MENU_SELECTED = "menu_selected";
    private static final int LOADER_ID = 1050;
    private static boolean favSorting;
    ProgressBar pgBar;
    TextView titleText;
    Toast toast;
    RecyclerView recyclerView;
    MovieAPIinterface mAPI;
    MovieAdapter movieAdapter;
    private int menuItemSelected = -1;
    private List<MovieManager> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rcv);
        titleText = (TextView) findViewById(R.id.mainTitle);
        pgBar = (ProgressBar) findViewById(R.id.pgBar);
        mAPI = MDBServiceAPI.createService(MovieAPIinterface.class);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        toast = Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT);


        if (savedInstanceState != null) {
            titleText.setText(savedInstanceState.getString(SELECTED_STRING, MDBServiceAPI.SORTBY_POPULAR));
            menuItemSelected = savedInstanceState.getInt(MENU_SELECTED);
            movieList = Parcels.unwrap(savedInstanceState.getParcelable("MOVIE_LIST"));
        } else if (isOnline()) {
            String sortBy = DBUtils.loadSelectedItem(this);
            pgBar.setVisibility(View.VISIBLE);
            if (sortBy.equals(MDBServiceAPI.SORTBY_FAVOURITE)) {
                titleText.setText(R.string.my_favourites);
                getFavouriteMovies();
            } else if (sortBy.equals(MDBServiceAPI.SORTBY_POPULAR)) {
                titleText.setText(R.string.most_popular_str);
                getMovieData(sortBy);
            } else if (sortBy.equals(MDBServiceAPI.SORTBY_TOP_RATED)) {
                titleText.setText(R.string.top_rated_str);
                getMovieData(sortBy);
            } else if (sortBy.equals(MDBServiceAPI.SORTBY_NOW_PLAYING)) {
                titleText.setText(R.string.now_playing_str);
                getMovieData(sortBy);
            }
        } else {
            getFavouriteMovies();
            favSorting = true;
            titleText.setText(R.string.my_favourites);
        }

        setAdapter(recyclerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (favSorting) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void setAdapter(RecyclerView rv) {
        movieAdapter = new MovieAdapter(this, movieList, this);
        rv.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        pgBar.setVisibility(View.VISIBLE);
        switch (selected) {
            case R.id.action_now_playing: {
                if (isOnline()) {
                    favSorting = false;
                    titleText.setText(R.string.now_playing_str);
                    DBUtils.storeSelectedItem(this, MDBServiceAPI.SORTBY_NOW_PLAYING);
                    getMovieData(MDBServiceAPI.SORTBY_NOW_PLAYING);
                } else toast.show();
                return true;
            }

            case R.id.action_most_popular: {
                if (isOnline()) {
                    titleText.setText(R.string.most_popular_str);
                    DBUtils.storeSelectedItem(this, MDBServiceAPI.SORTBY_POPULAR);
                    favSorting = false;
                    getMovieData(MDBServiceAPI.SORTBY_POPULAR);
                } else toast.show();
                return true;
            }

            case R.id.action_top_rated: {
                if (isOnline()) {
                    favSorting = false;
                    titleText.setText(R.string.top_rated_str);
                    DBUtils.storeSelectedItem(this, MDBServiceAPI.SORTBY_TOP_RATED);
                    getMovieData(MDBServiceAPI.SORTBY_TOP_RATED);
                } else toast.show();
                return true;
            }

            case R.id.action_favourites: {
                favSorting = true;
                titleText.setText(R.string.my_favourites);
                DBUtils.storeSelectedItem(this, MDBServiceAPI.SORTBY_FAVOURITE);
                getFavouriteMovies();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_SELECTED, menuItemSelected);
        outState.putParcelable("MOVIE_LIST", Parcels.wrap(movieList));
        outState.putString(SELECTED_STRING, titleText.getText().toString());
    }

    public void getMovieData(String sortBy) {
        Call<MovieResults> call = mAPI.getPopular(sortBy);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    pgBar.setVisibility(View.INVISIBLE);
                    movieList = response.body().getMovieResults();
                    if (movieList.size() != 0) {
                        movieAdapter.setList(movieList);

                    } else
                        Toast.makeText(MainActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onListClick(int index) {
        MovieManager movieClicked = movieList.get(index);
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_PARCELABLE, Parcels.wrap(movieClicked));
        startActivity(intent);
    }


    private void getFavouriteMovies() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        pgBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }


            @Override
            public Cursor loadInBackground() {
                try {
                    Cursor cursor = getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.FavoriteMovieEntry._ID);
                    return cursor;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        movieList = (ArrayList<MovieManager>) loadDataFromCursor(cursor);
        movieAdapter.setList(movieList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public List<MovieManager> loadDataFromCursor(Cursor cursor) {
        List<MovieManager> moviesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MovieManager movieManager = new MovieManager();
            movieManager.setInFavorite(true);
            movieManager.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            movieManager.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER)));
            movieManager.setBackdrop(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP)));
            movieManager.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            movieManager.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
            movieManager.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
            movieManager.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
            moviesList.add(movieManager);

        }
        return moviesList;
    }

}
