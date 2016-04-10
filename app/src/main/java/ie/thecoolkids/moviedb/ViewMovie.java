package ie.thecoolkids.moviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewMovie extends AppCompatActivity implements IParser{

    private MainActivity context;
    private EditText etQuery;
    private int id;
    private Movie movie;
    private RelativeLayout mainPage;
    private ImageView poster;
    private TextView title, year, genres, rating, synopsis, genreHeading, tagline, release,
            status, runtime, languages, collection, revenue, budget, orig_title,
            prod_countries, prod_companies, collectionHeading, languagesHeading, prod_companiesHeading, prod_countriesHeading;
    private Bitmap bitmap1;
    private URL url1;
    private int page=1;
    private int movieID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        //gets the passed movie id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieID = extras.getInt("passedMovieID");
        }
        new ApiHelper(this).SetMovieIDQuery(movieID).execute();
    }

    private void init(Movie movie){

        mainPage = (RelativeLayout) findViewById(R.id.mainpage);
        poster = (ImageView) findViewById(R.id.poster);


        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        genres = (TextView) findViewById(R.id.genres);
        genreHeading = (TextView) findViewById(R.id.genreHeading);
        rating = (TextView) findViewById(R.id.rating);
        synopsis = (TextView) findViewById(R.id.synopsis);
        tagline = (TextView) findViewById(R.id.tagline);
        release = (TextView) findViewById(R.id.releasedate);
        status = (TextView) findViewById(R.id.status);
        runtime = (TextView) findViewById(R.id.runtime);
        languages = (TextView) findViewById(R.id.languages);
        collection = (TextView) findViewById(R.id.collecion);
        revenue = (TextView) findViewById(R.id.revenue);
        budget = (TextView) findViewById(R.id.budget);
        orig_title = (TextView) findViewById(R.id.original_title);
        prod_countries = (TextView) findViewById(R.id.production_countries);
        prod_companies = (TextView) findViewById(R.id.production_companies);
        collectionHeading = (TextView) findViewById(R.id.collectionHeading);
        languagesHeading = (TextView) findViewById(R.id.languagesHeading);
        prod_companiesHeading = (TextView) findViewById(R.id.companiesHeading);
        prod_countriesHeading = (TextView) findViewById(R.id.countriesHeading);

        title.setText(movie.getTitle());
        year.setText("" + movie.getYear());
        rating.setText("" + movie.getRating());
        synopsis.setText(movie.getSynopsis());
        tagline.setText(movie.getTagline());
        release.setText(movie.getReleaseDate());
        status.setText(movie.getStatus());
        runtime.setText("" + movie.getRuntime() + " minutes");
        revenue.setText("$" + movie.getRevenue());
        budget.setText("$" + movie.getBudget());
        orig_title.setText(movie.getOriginalTitle());

        if(movie.getCollection() != null) {
            collectionHeading.setText("Collection :");
            collection.setText(movie.getCollection().getName());
        }

        if(movie.getSpokenLanguages().size() >0) {
            languagesHeading.setText("Spoken Languages(s):");
            languages.setText(createArrayDropLineString(movie.getSpokenLanguages()));
        }

        if(movie.getProductionCompanyNames().size() >0) {
            prod_companiesHeading.setText("Production Companies :");
            prod_companies.setText(createArrayDropLineString(movie.getProductionCompanyNames()));
        }

        if(movie.getProductionCountries().size() >0) {
            prod_countriesHeading.setText("Production Countries :");
            prod_countries.setText(createArrayDropLineString(movie.getProductionCountries()));
        }

        if(movie.getGenres().size()>0) {
            genreHeading.setText("Genre(s):");
            genres.setText(createArrayString(movie.getGenres()));
        }
        if(!movie.getPoster().equals(null)) {
            setImages();
        }
    }

    private void setImages() {
       Picasso.with(this).load(movie.getPoster()).into(poster);
        try {
            url1 = new URL(movie.getPoster());
            bitmap1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            Drawable dr = new BitmapDrawable(getResources(), bitmap1);
            mainPage.setBackground(dr);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.w("ViewMovie", "Exception creating image");
        }
    }

    private String createArrayString(List<String> items) {
        String genreString="";
        genreString = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            genreString += " / " +  items.get(i);
        }
        return genreString;
    }

    private String createArrayDropLineString(List<String> items) {
        String genreString="";
        genreString = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            genreString += "\n" +  items.get(i);
        }
        return genreString;
    }



    public void parseJson(String json) {
       try {
           Gson gson = new Gson();
           movie = gson.fromJson(json, Movie.class);
           init(movie);
       }
       catch(Exception ex){
           if(ex == null) {
               Log.d("EXCEPTION", "NULL");
           } else if (ex.getMessage() == null){
               ex.printStackTrace();
           } else {
               Log.d("EXCEPTION", ex.getMessage());
           }
        }

    }


}
