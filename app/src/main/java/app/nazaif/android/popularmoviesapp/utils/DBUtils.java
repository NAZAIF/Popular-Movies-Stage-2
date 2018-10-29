package app.nazaif.android.popularmoviesapp.utils;

/**
 * Created by nazaif on 6/2/18.
 */

import android.content.Context;
import android.content.SharedPreferences;

import app.nazaif.android.popularmoviesapp.pojo.MovieManager;

import static app.nazaif.android.popularmoviesapp.api.MDBServiceAPI.SORTBY_DEFAULT;
import static app.nazaif.android.popularmoviesapp.activity.MovieDetailsActivity.SHARED_PREF_FILE;

public class DBUtils {
    public static final String SELECTED_ITEM_MENU = "selected_item_menu";


    public static void storeSelectedItem(Context context, String sort) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_ITEM_MENU, sort);
        editor.commit();
    }

    public static boolean getState(Context context, MovieManager movie) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        boolean isChecked = preferences.getBoolean(movie.getTitle(), false);
        return isChecked;
    }

    public static String loadSelectedItem(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        String item = sharedPreferences.getString(SELECTED_ITEM_MENU, SORTBY_DEFAULT);
        return item;
    }

}
