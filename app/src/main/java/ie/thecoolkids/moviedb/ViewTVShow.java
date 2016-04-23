package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewTVShow extends BaseActivity implements IParser{


    private enum State{
        STATE1, STATE2
    }

    private int id;
    private TvShow tvShow;
    private RelativeLayout mainPage;
    private LinearLayout networksLine, genreLine, createdByLine, runtimeLine, originCountryLine, productionCompLine;
    private ImageView poster;
    private Button seasonButton;
    private TextView name, firstAir, lastAir, status, genres, numEpisodes, numSeasons, rating,
            epRuntime,type,  createdBy, inProd, origCountry, networks, prodCompanies, synopsis,
            genreHeading, epRuntimeHeading, createdByHeading, origCountryHeading,
            networksHeading, prodCompaniesHeading;
    private Bitmap bitmap1;
    private URL url1;
    private int tvshowID = 44217;
    private ArrayList<Season> seasons;
    State state;
    private ImageButton favButton;
    private DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tvshow);

        db = new DBHelper(this);

        state = State.STATE1;
        Log.d("STATE", "" + state);

        // Working for database or online... Could we maybe keep it as passedID for everything?
        /*Intent intent = getIntent();
        if(intent != null && intent.hasExtra("passedID")){
            tvshowID = intent.getIntExtra("passedID", 0);
            if(db.tvShowExists(tvshowID)){
                tvShow = new TvShow(this, tvshowID);
                init(tvShow);
            }
            else{
                new ApiHelper(this).SetTvIDQuery(tvshowID).execute();
            }
        }
        else{
            // Delete this when fully up and running
            new ApiHelper(this).SetTvIDQuery(tvshowID).execute();
        }*/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvshowID = extras.getInt("passedID");
        }
        new ApiHelper(this).SetTvIDQuery(tvshowID).execute();
    }

    private void init(final TvShow tvShow){

        Log.d("INISDE INIT", "");
        seasons = new ArrayList<Season>();

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
        createdBy = (TextView) findViewById(R.id.created_by);
        createdByHeading  = (TextView) findViewById(R.id.createdbyHeading);
        inProd = (TextView) findViewById(R.id.in_production);
        origCountry = (TextView) findViewById(R.id.original_country);
        origCountryHeading = (TextView) findViewById(R.id.origCountryHeading);
        networks = (TextView) findViewById(R.id.networks);
        networksHeading  = (TextView) findViewById(R.id.networksHeading);
        prodCompanies = (TextView) findViewById(R.id.prod_companies);
        prodCompaniesHeading  = (TextView) findViewById(R.id.prodcompHeading);
        synopsis = (TextView) findViewById(R.id.synopsis);
        networksLine = (LinearLayout) findViewById(R.id.networksLine);
        genreLine = (LinearLayout) findViewById(R.id.genresLine);
        createdByLine = (LinearLayout) findViewById(R.id.createdbyLine);
        runtimeLine  = (LinearLayout) findViewById(R.id.episoderuntimesLine);
        originCountryLine = (LinearLayout) findViewById(R.id.origcountryLine);
        productionCompLine = (LinearLayout) findViewById(R.id.prodCompaniesLine);

        favButton = (ImageButton)findViewById(R.id.addFavButton);
        if(db.tvShowExists(tvShow.getId())){
            favButton.setImageResource(R.mipmap.fav_yes);
        }
        new Thread(new AddRemoveTvShow()).start();

        name.setText(tvShow.getTitle());
        firstAir.setText(tvShow.getFirstAirDate());
        lastAir.setText(tvShow.getLastAirDate());
        status.setText(tvShow.getStatus());
        numEpisodes.setText("" + tvShow.getNumberOfEpisodes());
        numSeasons.setText("" + tvShow.getNumberOfSeasons());
        rating.setText("" + tvShow.getRating());
        type.setText(tvShow.getType());
        synopsis.setText(tvShow.getSynopsis());


        // Should this be tvShow.getGenres().size?---------------------------------------------------------------------------------------
        //Ammm yes. Should it not be?
        if(tvShow.getGenres().size() > 0) {
            genreHeading.setText("Genre(s) :");
            genres.setText(createArrayString(tvShow.getGenres()));
            genreLine.setVisibility(View.VISIBLE);
        }

        if(tvShow.getCreatedBy().size() > 0) {
            createdByHeading.setText("Created by :");
            createdBy.setText(createArrayString(tvShow.getCreatedBy()));
            createdByLine.setVisibility(View.VISIBLE);
        }

        if (tvShow.getRuntimes().size() > 0) {
            epRuntimeHeading.setText("Episode Runtime :");
            epRuntime.setText("" + tvShow.getRuntimes().get(0) + " minutes");
            runtimeLine.setVisibility(View.VISIBLE);
        }



            if(tvShow.getInProduction())    inProd.setText("True");
            else                            inProd.setText("False");



        if(tvShow.getOriginCountry().size() >0) {
            origCountryHeading.setText("Origin Country :");
            origCountry.setText(createArrayDropLineString(tvShow.getOriginCountry()));
            originCountryLine.setVisibility(View.VISIBLE);
        }

        if (tvShow.getNetworks().size() > 0) {
            networksLine.setVisibility(View.VISIBLE);
            if(tvShow.getNetworks().size() == 1){
                networksHeading.setText("Network :");
                networksLine.setOrientation(LinearLayout.HORIZONTAL);
                networks.setPadding(10, 0, 0, 0);
                networks.setText(createArrayDropLineString(tvShow.getNetworks()));
            }
            else {
                networksHeading.setText("Networks :");
                networksLine.setOrientation(LinearLayout.VERTICAL);
                networks.setPadding(30, 0, 0, 0);
                networks.setText(createArrayString(tvShow.getNetworks()));
            }
        }

        if(tvShow.getProductionCompanies().size() >0) {
            productionCompLine.setVisibility(View.VISIBLE);
            prodCompaniesHeading.setText("Production Companies :");
            prodCompanies.setText(createArrayDropLineString(tvShow.getProductionCompanies()));
        }

        if(!tvShow.getPoster().equals(null)) {
            setImages();
        }

        state = State.STATE2;
        Log.d("STATE","" +  state);

        //cap on seasons and episode numbers - may need to be added
        //if(tvShow.getNumberOfSeasons() < 10 && tvShow.getNumberOfEpisodes() < 1000){
            seasonButton.setVisibility(View.VISIBLE);
            for(int i=0; i<tvShow.getNumberOfSeasons(); i++){
                new ApiHelper(this).SetSeasonIDQuery(tvShow.getId(), i+1).execute();
            }
       // }



    }

    // Other way was causing crash.... see ViewMovie change
    private void setImages() {
        Picasso.with(this).load(tvShow.getPoster()).placeholder(R.drawable.movies).into(poster);
        Picasso.with(this).load(tvShow.getPoster()).placeholder(R.drawable.moviereel).into(
                new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mainPage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(getApplicationContext(), "Failed To Load Background Image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                }
        );

    }

    private String createArrayString(List<String> items) {
        String x = "" + items.get(0);
        for (int i = 1; i < items.size(); i++) {
            x += " / " +  items.get(i);
        }
        return x;
    }

    private String createArrayDropLineString(List<String> items) {
        String x  = "" + items.get(0);
        for (int i = 1; i < items.size(); i++) {
            x += "\n" +  items.get(i);
        }
        return x;
    }



    public void parseJson(String json) {
        try {
            if(state == State.STATE1){
                Gson gson = new Gson();
                tvShow = gson.fromJson(json, TvShow.class);
                init(tvShow);
            }
            else{
                Gson gson = new Gson();
                seasons.add(gson.fromJson(json, Season.class));

                if(seasons.size() == tvShow.getNumberOfSeasons()){
                    setSeasonsButtonListener();
                }

            }
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

    private void setSeasonsButtonListener(){

        seasonButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.swipe);
                mp.start();

                Intent intent = new Intent(v.getContext(), ViewSeasons.class);
                intent.putExtra("numSeasons", tvShow.getNumberOfSeasons());

                for (int i = 0; i < tvShow.getNumberOfSeasons(); i++) {
                    intent.putExtra("passedTVSeason" + i, new Gson().toJson(seasons.get(i)));
                }

                v.getContext().startActivity(intent);
            }
        });
    }

    class AddRemoveTvShow implements Runnable{
        @Override
        public void run() {
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(db.addRemoveTvShow(tvShow)){
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.success);
                        mp.start();
                        Toast.makeText(ViewTVShow.this, "Tv Show Added To Favourites", Toast.LENGTH_SHORT).show();
                        favButton.setImageResource(R.mipmap.fav_yes);
                    }
                    else{
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.beep1);
                        mp.start();
                        Toast.makeText(ViewTVShow.this, "Tv Show Removed From Favourites", Toast.LENGTH_SHORT).show();
                        favButton.setImageResource(R.mipmap.fav_no);
                    }
                }
            });
        }
    }

}
