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
    private TextView title, year, genres, rating, synopsis, genreHeading;
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

        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        genres = (TextView) findViewById(R.id.genres);
        genreHeading = (TextView) findViewById(R.id.genreHeading);
        rating = (TextView) findViewById(R.id.rating);
        synopsis = (TextView) findViewById(R.id.synopsis);
        poster = (ImageView) findViewById(R.id.poster);
        mainPage = (RelativeLayout) findViewById(R.id.mainpage);

        title.setText(movie.getTitle());
        year.setText("" + movie.getYear());
        rating.setText("" + movie.getRating());
        synopsis.setText(movie.getSynopsis());

        if(movie.getGenres().size()>0) {
            genreHeading.setText("Genre(s):");
            genres.setText(createGenreString(movie.getGenres()));
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

    private String createGenreString(List<String> genres) {
        String genreString="";
        genreString = genres.get(0);
        for (int i = 1; i < genres.size(); i++) {
            genreString += " / " +  genres.get(i);
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
