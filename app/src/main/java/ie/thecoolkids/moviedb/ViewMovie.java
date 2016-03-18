package ie.thecoolkids.moviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewMovie extends AppCompatActivity {

    Movie movie;
    RelativeLayout mainPage;
    ImageView poster;
    TextView title, year, genres, rating, synopsis, genreHeading;
    Bitmap bitmap1;
    URL url1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);
        String jsonMyObject="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("passedMovie");
        }
        movie = new Gson().fromJson(jsonMyObject, Movie.class);
        init(movie);
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
        if(!movie.getMediumCoverImage().equals(null)) {
            setImages();
        }
    }

    private void setImages() {
       Picasso.with(this).load(movie.getMediumCoverImage()).into(poster);
        try {
            url1 = new URL(movie.getMediumCoverImage());
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


}
