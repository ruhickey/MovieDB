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
    TextView title, year, genres, rating, mpaaRating, runtime, synopsis;
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
        rating = (TextView) findViewById(R.id.rating);
        mpaaRating = (TextView) findViewById(R.id.mpaarating);
        runtime = (TextView) findViewById(R.id.runtime);
        synopsis = (TextView) findViewById(R.id.synopsis);
        poster = (ImageView) findViewById(R.id.poster);
        mainPage = (RelativeLayout) findViewById(R.id.mainpage);


        title.setText(movie.getTitle());
        year.setText("" + movie.getYear());
        rating.setText("" + movie.getRating());
<<<<<<< HEAD
        // TODO: Commented out to fix build.
        /*
        mpaaRating.setText("" + movie.getMpaRating());
        runtime.setText(createRuntimeString(movie.getRuntime()));
        */
=======
        //mpaaRating.setText("" + movie.getMpaRating());
        //runtime.setText(createRuntimeString(movie.getRuntime()));
>>>>>>> e74ab2f82c897523fd538e06cdd86903c74884d0
        synopsis.setText(movie.getSynopsis());
        genres.setText(createGenreString(movie.getGenres()));
        setImages();
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

        /*
         * Idea for how to make this more efficient.
         * Check that genres size > 0.
         * Set genreString to genres[0].
         * Do a for loop starting at i = 1
         *   Add ' / ' then the genre[i]
         * This makes sure there's something in genres
         * And it removes the use of Substring.
         */

        if(genres.size()>1) {
            for (int i = 0; i < genres.size(); i++) {
                genreString += genres.get(i) + " / ";
            }
            genreString=genreString.substring(0,genreString.length()-2);
        }
        else{
            genreString = genres.get(0);
        }

        return genreString;
    }

    private String createRuntimeString(int rt) {
        int hrs, mins;
        hrs = rt / 60;
        mins = rt % 60;
        return "" + hrs + "Hrs  " + mins + "Mins";
    }

}
