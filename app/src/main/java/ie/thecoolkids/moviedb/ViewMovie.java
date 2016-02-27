package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.URL;

public class ViewMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);
        String jsonMyObject="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("passedMovie");
        }
        Movie movie = new Gson().fromJson(jsonMyObject, Movie.class);
        init(movie);
    }

    private void init(Movie movie){
        Bitmap bitmap;

        TextView title = (TextView) findViewById(R.id.title);
        TextView year = (TextView) findViewById(R.id.year);
        TextView genres = (TextView) findViewById(R.id.genres);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView mpaaRating = (TextView) findViewById(R.id.mpaarating);
        TextView runtime = (TextView) findViewById(R.id.runtime);
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        ImageView poster = (ImageView) findViewById(R.id.poster);


        title.setText(movie.getTitle());
        year.setText("" + movie.getYear());
        rating.setText("\t\t" + movie.getRating());
        mpaaRating.setText("MMPA Rating:\t\t\t\t" + movie.getMpa_rating());
        runtime.setText("Runtime:\t\t\t\t" + movie.getRuntime());
        synopsis.setText(movie.getSynopsis());

        try {
            URL newurl = new URL(movie.getPosterURL());
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            poster.setImageBitmap(bitmap);
        }
        catch(Exception e){
        }

        String genreString="";
        if(movie.getGenres().size()>1) {
            for (int i = 0; i < movie.getGenres().size(); i++) {
                genreString += movie.getGenres().get(i) + " / ";
            }
            genreString=genreString.substring(0,genreString.length()-2);
        }
        else{
            genreString = movie.getGenres().get(0);
        }
        genres.setText(genreString);
    }
}
