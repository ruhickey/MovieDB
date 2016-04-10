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

public class ViewSeasons extends AppCompatActivity implements IParser{

    private int id;
    private Season[] seasons;
    private RelativeLayout mainPage;
    private ImageView poster;

    private TextView addTextViewsHere;


    private Bitmap bitmap1;
    private URL url1;
    private int tvShowID;
    private int numSeasons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        //gets the passed tvshow id
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvShowID = extras.getInt("passedTVShowID");
            numSeasons = extras.getInt("passedTVShowNumSeasons");
        }

        //todo : figure out api call
//        new ApiHelper(this).SetMovieIDQuery(movieID).execute();
    }

    private void init(Season season){

        mainPage = (RelativeLayout) findViewById(R.id.mainpage);
        poster = (ImageView) findViewById(R.id.poster);

        //todo attach views
        //eg  title = (TextView) findViewById(R.id.title);


        //todo setText and images etc.
        // eg  title.setText(movie.getTitle());


        //todo: check nulls for arrays and set headings
        //eg
//        if(movie.getCollection() != null) {
//            collectionHeading.setText("Collection :");
//            collection.setText(movie.getCollection().getName());
//        }


        //todo: set images
//        if(!movie.getPoster().equals(null)) {
//            setImages();
//        }

    }

    private void setImages() {
//        Picasso.with(this).load(movie.getPoster()).into(poster);
//        try {
//            url1 = new URL(movie.getPoster());
//            bitmap1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
//            Drawable dr = new BitmapDrawable(getResources(), bitmap1);
//            mainPage.setBackground(dr);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            Log.w("ViewMovie", "Exception creating image");
//        }
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
            //todo change
//            Gson gson = new Gson();
//            movie = gson.fromJson(json, Movie.class);
//            init(movie);
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
