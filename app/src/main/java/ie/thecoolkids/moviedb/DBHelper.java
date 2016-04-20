package ie.thecoolkids.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "favourites.db", TABLE_MOVIES = "movies", TABLE_TVSHOWS = "tvshows";
    private static final String COL0_ID = "id", COL1_TITLE = "title",
                                COL2_YEAR = "year", COL3_RATING = "rating",
                                COL4_SYNOPSIS = "synopsis", COL5_TAGLINE = "tagline",
                                COL6_RELEASE = "release", COL7_STATUS = "status",
                                COL8_RUNTIME = "runtime", COL9_REVENUE = "revenue",
                                COL10_BUDGET = "budget", COL11_ORIGINAL_TITLE = "originalTitle",
                                COL12_GENRES = "genres";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIES + " (" +
                COL0_ID + " INTEGER PRIMARY KEY," +
                COL1_TITLE + " TEXT," + COL2_YEAR + " TEXT," +
                COL3_RATING + " REAL," + COL4_SYNOPSIS + " TEXT," +
                COL5_TAGLINE + " TEXT," + COL6_RELEASE + " TEXT," +
                COL7_STATUS + " TEXT," + COL8_RUNTIME + " INTEGER," +
                COL9_REVENUE + " INTEGER," + COL10_BUDGET + " INTEGER," +
                COL11_ORIGINAL_TITLE + " TEXT," + COL12_GENRES + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "DROP TABLE IF EXISTS " + TABLE_MOVIES + ";"
        );
        onCreate(db);
    }

    public boolean addRemoveMovie(Movie movie){
        int id = movie.getId();
        if(movieExists(id)){
            getWritableDatabase().delete(TABLE_MOVIES, COL0_ID + " = " + id, null);
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COL0_ID, id);
        values.put(COL1_TITLE, movie.getTitle());
        values.put(COL2_YEAR, movie.getYear());
        values.put(COL3_RATING, movie.getRating());
        values.put(COL4_SYNOPSIS, movie.getSynopsis());
        values.put(COL5_TAGLINE, movie.getTagline());
        values.put(COL6_RELEASE, movie.getReleaseDate());
        values.put(COL7_STATUS, movie.getStatus());
        values.put(COL8_RUNTIME, movie.getRuntime());
        values.put(COL9_REVENUE, movie.getRevenue());
        values.put(COL10_BUDGET, movie.getBudget());
        values.put(COL11_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(COL12_GENRES, asString(movie.getGenres()));
        if(this.getWritableDatabase().insert(TABLE_MOVIES, null, values) == -1){return false;}
        else{return true;}
    }

    public boolean movieExists(int id){
        String sql = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + COL0_ID + " = " + id;
        if(this.getWritableDatabase().rawQuery(sql, null).getCount() == 0){
            return false;
        }
        return true;
    }

    private String asString(List list){
        String s = "";
        int n = list.size();
        for(int i = 0; i < n; i++){
            s += list.get(i) + ";";
        }
        return s;
    }

    public Cursor getLocalMovies(){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
    }
}
