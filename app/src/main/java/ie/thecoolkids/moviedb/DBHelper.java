package ie.thecoolkids.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "favourites.db", TABLE_MOVIES = "movies", TABLE_TVSHOWS = "tvshows";
    private static final String COL_ID = "id", COL_TITLE = "title",
                                COL_GENRES = "genres", COL_SYNOPSIS = "synopsis",
                                COL_RELEASE_DATE = "releaseDate", COL_RATING = "rating";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIES + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                COL_TITLE + " TEXT " + COL_GENRES + " TEXT " +
                COL_SYNOPSIS + " TEXT " + COL_RELEASE_DATE + " TEXT " +
                COL_RATING + " REAL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_MOVIES + ";"
        );
        onCreate(db);
    }

    public void addMovie(Movie movie){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, movie.getTitle());
        values.put(COL_GENRES, movie.getGenres().toString());
        values.put(COL_SYNOPSIS, movie.getSynopsis());
        values.put(COL_RELEASE_DATE, movie.getYear());
        values.put(COL_RATING, movie.getRating());
        getWritableDatabase().insert(TABLE_MOVIES, null, values);
    }

    public void deleteMovie(Movie movie){
        getWritableDatabase().delete(
                TABLE_MOVIES,
                COL_TITLE + "=? AND " + COL_RELEASE_DATE + "=?",
                new String[]{
                        movie.getTitle(),
                        movie.getYear() + ""
                }
        );
    }

    /*public List<Movie> getLocalMovies(){
        List<Movie> movies = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_MOVIES + " WHERE 1",
                null
        );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

        }
        return null;
    }*/
}
