package app.nazaif.android.popularmoviesapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nazaif on 5/2/18.
 */

public class MovieContract {
    public static final String AUTHORITY = "app.nazaif.android.app.nazaif.android.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String FAVOURITES_MOVIES_PATH = "favourite_movie";

    private MovieContract() {
    }

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVOURITES_MOVIES_PATH)
                .build();

        public static final String TABLE_NAME = "favourite_movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";

    }
}