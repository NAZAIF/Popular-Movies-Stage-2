package com.example.android.popularmoviesapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmoviesapp.provider.MDBHelper;
import com.example.android.popularmoviesapp.provider.MovieContract;

import static com.example.android.popularmoviesapp.provider.MovieContract.FavoriteMovieEntry.*;

/**
 * Created by nazaif on 5/2/18.
 */

public class MovieDetailsContentProvider extends ContentProvider {

    public static final int FAVOURITES_MOVIE = 100;
    public static final int FAVOURITES_MOVIE_WITH_ID = 200;

    public static final String UNKNOWN_URI = "Unknown uri: ";
    public static final String INSERT_FAILED = "Failed to insert row to  ";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.FAVOURITES_MOVIES_PATH, FAVOURITES_MOVIE);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.FAVOURITES_MOVIES_PATH + "/#", FAVOURITES_MOVIE_WITH_ID);
    }

    private MDBHelper mdbHelper;

    @Override
    public boolean onCreate() {
        mdbHelper = new MDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mdbHelper.getReadableDatabase();
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITES_MOVIE:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mdbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITES_MOVIE:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException(INSERT_FAILED + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mdbHelper.getWritableDatabase();
        int deletedMovies;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITES_MOVIE_WITH_ID:
                String stringId = uri.getPathSegments().get(1);
                deletedMovies = db.delete(TABLE_NAME, "id=?", new String[]{stringId});
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        if (deletedMovies != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedMovies;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
