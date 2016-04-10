package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ViewTVShow extends AppCompatActivity implements IParser{


    private int id;
    private TvShow tvShow;
    private RelativeLayout mainPage;
    private ImageView poster;
    private Button seasonButton;
    private TextView name, firstAir, lastAir, status, genres, numEpisodes, numSeasons, rating,
            epRuntime,type, languages, createdBy, inProd, origCountry, networks, prodCompanies, synopsis,
            genreHeading, epRuntimeHeading, languagesHeading, createdByHeading, origCountryHeading,
            networksHeading, prodCompaniesHeading;
    private Bitmap bitmap1;
    private URL url1;
    private int tvshowID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvshowID = extras.getInt("passedMovieID");
        }
        new ApiHelper(this).SetTvIDQuery(tvshowID).execute();
    }

    private void init(final TvShow tvShow){

        mainPage = (RelativeLayout) findViewById(R.id.mainpage);
        poster = (ImageView) findViewById(R.id.poster);
        seasonButton = (Button)findViewById(R.id.seasons_button);

        name = (TextView) findViewById(R.id.name);
        firstAir = (TextView) findViewById(R.id.first_air_date);
        lastAir = (TextView) findViewById(R.id.last_airdate);
        status = (TextView) findViewById(R.id.status);
        genres = (TextView) findViewById(R.id.genres);
        genreHeading = (TextView) findViewById(R.id.genreHeading);
        numEpisodes = (TextView) findViewById(R.id.num_episodes);
        numSeasons = (TextView) findViewById(R.id.num_of_seasons);
        rating = (TextView) findViewById(R.id.rating);
        epRuntime = (TextView) findViewById(R.id.epRuntime);
        epRuntimeHeading = (TextView) findViewById(R.id.epRuntimeHeading);
        type = (TextView) findViewById(R.id.type);
        languages = (TextView) findViewById(R.id.languages);
        languagesHeading = (TextView) findViewById(R.id.languagesHeading);
        createdBy = (TextView) findViewById(R.id.created_by);
        createdByHeading  = (TextView) findViewById(R.id.createdbyHeading);
        inProd = (TextView) findViewById(R.id.in_production);
        origCountry = (TextView) findViewById(R.id.original_country);
        origCountryHeading = (TextView) findViewById(R.id.origCountryHeading);
        networks = (TextView) findViewById(R.id.networks);
        networksHeading  = (TextView) findViewById(R.id.networksHeading);
        prodCompanies = (TextView) findViewById(R.id.production_companies);
        prodCompaniesHeading  = (TextView) findViewById(R.id.prodcompHeading);
        synopsis = (TextView) findViewById(R.id.synopsis);


        name.setText(tvShow.getName());
        firstAir.setText(tvShow.getFirstAirDate());
        lastAir.setText(tvShow.getLastAirDate());
        status.setText(tvShow.getStatus());
        numEpisodes.setText(tvShow.getNumberOfEpisodes());
        numSeasons.setText(tvShow.getNumberOfSeasons());
        rating.setText("" + tvShow.getRating());
        type.setText(tvShow.getType());
        synopsis.setText(tvShow.getSynopsis());

        if(tvShow.getCreatedBy().size()>0) {
            createdByHeading.setText("Created by :");
            createdBy.setText(createArrayString(tvShow.getCreatedBy()));
        }

        if (tvShow.getRuntimes().size() > 0) {
            epRuntimeHeading.setText("Episode Runtime :");
            epRuntime.setText(tvShow.getRuntimes().get(0) + " minutes");
        }

        if (tvShow.getLanguages().size() >0) {
            languagesHeading.setText("Spoken Languages(s):");
            languages.setText(createArrayDropLineString(tvShow.getLanguages()));
        }

        if(tvShow.getInProduction())    inProd.setText("True");
        else                            inProd.setText("False");


        if(tvShow.getOriginCountry().size() >0) {
            origCountryHeading.setText("Origin Country :");
            origCountry.setText(createArrayDropLineString(tvShow.getOriginCountry()));
        }

        if (tvShow.getNetworks().size() >0) {
            networksHeading.setText("Networks :");
            networks.setText(createArrayDropLineString(tvShow.getNetworks()));
        }

        if(tvShow.getProductionCompanies().size() >0) {
            prodCompaniesHeading.setText("Production Companies :");
            prodCompanies.setText(createArrayDropLineString(tvShow.getProductionCompanies()));
        }

        if(!tvShow.getPoster().equals(null)) {
            setImages();
        }

        seasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewSeasons.class);
                intent.putExtra("passedTVShowID", tvShow.getId());
                intent.putExtra("passedTVShowNumSeasons", tvShow.getNumberOfSeasons());
                v.getContext().startActivity(intent);
            }
        });
    }

    private void setImages() {
        Picasso.with(this).load(tvShow.getPoster()).into(poster);
        try {
            url1 = new URL(tvShow.getPoster());
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
            tvShow = gson.fromJson(json, TvShow.class);
            init(tvShow);
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
