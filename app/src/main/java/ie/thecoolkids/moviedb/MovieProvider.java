package ie.thecoolkids.moviedb;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

public class MovieProvider extends ContentProvider{
    private static final String PROVIDER = "ie.thecoolkids.moviedb.MovieProvider";
    private static final String URL = "content://" + PROVIDER + "/movies";
    private static final Uri URI = Uri.parse(URL);
    private SQLiteDatabase db;
    private static final String TABLE_MOVIES = "movies";

    private static HashMap<String, String> MOVIES_PROJECTION_MAP;

    static final int MOVIES = 1;
    static final int MOVIES_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "movies", MOVIES);
        uriMatcher.addURI(PROVIDER, "movies/#", MOVIES_ID);
    }


    @Override
    public boolean onCreate() {
        Log.d("STATE", "ContentProvider onCreate");
        db = new DBHelper(getContext()).getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_MOVIES);
        qb.setProjectionMap(MOVIES_PROJECTION_MAP);
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;//db.query(TABLE_MOVIES, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
