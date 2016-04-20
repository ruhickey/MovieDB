package ie.thecoolkids.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "favourites.db", TABLE_MOVIES = "movies", TABLE_TVSHOWS = "tvshows";
    private static final String COL0_ID = "id", COL1_TITLE = "title",
                                COL2_RELEASE = "year", COL3_RATING = "rating",
                                COL4_SYNOPSIS = "synopsis", COL5_TAGLINE = "tagline",
                                COL6_STATUS = "status",COL7_RUNTIME = "runtime",
                                COL8_REVENUE = "revenue", COL9_BUDGET = "budget",
                                COL10_ORIGINAL_TITLE = "originalTitle", COL11_GENRES = "genres",
                                COL12_POSTER = "poster", COL13_COLLECTION = "collection",
                                COL14_LANGUAGES = "languages", COL15_PRODUCTION_COMPANIES = "productionCompanies",
                                COL16_PRODUCTION_COUNTRIES = "productionCountries";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + TABLE_MOVIES + " (" +
                COL0_ID + " INTEGER PRIMARY KEY," +
                COL1_TITLE + " TEXT," + COL2_RELEASE + " TEXT," +
                COL3_RATING + " REAL," + COL4_SYNOPSIS + " TEXT," +
                COL5_TAGLINE + " TEXT," + COL6_STATUS + " TEXT," +
                COL7_RUNTIME + " INTEGER," + COL8_REVENUE + " INTEGER," +
                COL9_BUDGET + " INTEGER," + COL10_ORIGINAL_TITLE + " TEXT," +
                COL11_GENRES + " TEXT," + COL12_POSTER + " TEXT," +
                COL13_COLLECTION + " TEXT," + COL14_LANGUAGES + " TEXT," +
                COL15_PRODUCTION_COMPANIES + " TEXT," + COL16_PRODUCTION_COUNTRIES + " TEXT);"
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
        values.put(COL2_RELEASE, movie.getReleaseDate());
        values.put(COL3_RATING, movie.getRating());
        values.put(COL4_SYNOPSIS, movie.getSynopsis());
        values.put(COL5_TAGLINE, movie.getTagline());
        values.put(COL6_STATUS, movie.getStatus());
        values.put(COL7_RUNTIME, movie.getRuntime());
        values.put(COL8_REVENUE, movie.getRevenue());
        values.put(COL9_BUDGET, movie.getBudget());
        values.put(COL10_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(COL11_GENRES, asString(movie.getGenres()));
        values.put(COL12_POSTER, movie.getPoster());
        values.put(COL13_COLLECTION, (movie.getBelongsToCollection()) ? movie.getCollection().getName() : "");
        values.put(COL14_LANGUAGES, asString(movie.getSpokenLanguages()));
        values.put(COL15_PRODUCTION_COMPANIES, asString(movie.getProductionCompanyNames()));
        values.put(COL16_PRODUCTION_COUNTRIES, asString(movie.getProductionCountries()));
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

    public Cursor getAllLocalMovies(){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
    }

    public Cursor getMovie(int id){
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_MOVIES + " WHERE " + COL0_ID + " = " + id, null);
    }
}
