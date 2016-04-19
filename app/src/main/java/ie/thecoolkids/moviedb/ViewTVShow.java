package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    private ImageView poster;
    private Button seasonButton;
    private TextView name, firstAir, lastAir, status, genres, numEpisodes, numSeasons, rating,
            epRuntime,type,  createdBy, inProd, origCountry, networks, prodCompanies, synopsis,
            genreHeading, epRuntimeHeading, createdByHeading, origCountryHeading,
            networksHeading, prodCompaniesHeading;
    private Bitmap bitmap1;
    private URL url1;
    private int tvshowID;
    private ArrayList<Season> seasons;
    State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tvshow);

        state = State.STATE1;
        Log.d("STATE", "" + state);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvshowID = extras.getInt("passedTVShowID");
        }
        new ApiHelper(this).SetTvIDQuery(tvshowID).execute();
    }

    private void init(final TvShow tvShow){

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



        name.setText(tvShow.getName());
        firstAir.setText(tvShow.getFirstAirDate());
        lastAir.setText(tvShow.getLastAirDate());
        status.setText(tvShow.getStatus());
        numEpisodes.setText("" + tvShow.getNumberOfEpisodes());
        numSeasons.setText("" + tvShow.getNumberOfSeasons());
        rating.setText("" + tvShow.getRating());
        type.setText(tvShow.getType());
        synopsis.setText(tvShow.getSynopsis());

        if(tvShow.getCreatedBy().size() > 0) {
            genreHeading.setText("Genre(s) :");
            genres.setText(createArrayString(tvShow.getGenres()));
        }

        if(tvShow.getCreatedBy().size() > 0) {
            createdByHeading.setText("Created by :");
            createdBy.setText(createArrayString(tvShow.getCreatedBy()));
        }

        if (tvShow.getRuntimes().size() > 0) {
            epRuntimeHeading.setText("Episode Runtime :");
            epRuntime.setText("" + tvShow.getRuntimes().get(0) + " minutes");
        }


        if(tvShow.getInProduction())    inProd.setText("True");
        else                            inProd.setText("False");


        if(tvShow.getOriginCountry().size() >0) {
            origCountryHeading.setText("Origin Country :");
            origCountry.setText(createArrayDropLineString(tvShow.getOriginCountry()));
        }

        if (tvShow.getNetworks().size() >0) {
            networksHeading.setText("Networks :");
            if(tvShow.getNetworks().size() > 1){
                networks.setText(createArrayDropLineString(tvShow.getNetworks()));
            }
            else {
                networks.setText(createArrayString(tvShow.getNetworks()));
            }
        }

        if(tvShow.getProductionCompanies().size() >0) {
            prodCompaniesHeading.setText("Production Companies :");
            prodCompanies.setText(createArrayDropLineString(tvShow.getProductionCompanies()));
        }

        if(!tvShow.getPoster().equals(null)) {
            setImages();
        }

        state = State.STATE2;
        Log.d("STATE","" +  state);
        for(int i=0; i<tvShow.getNumberOfSeasons(); i++){
            new ApiHelper(this).SetSeasonIDQuery(tvShow.getId(), i+1).execute();
        }


    }

    private void setImages() {
        Picasso.with(this).load(tvShow.getPoster()).into(poster);
        //todo: set background image here
//        try {
//            url1 = new URL(tvShow.getPoster());
//            bitmap1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
//            Drawable dr = new BitmapDrawable(getResources(), bitmap1);
//            mainPage.setBackground(dr);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            Log.w("ViewTvShow", "Exception creating image");
//        }
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
                Intent intent = new Intent(v.getContext(), ViewSeasons.class);
                intent.putExtra("numSeasons", 5);

                for (int i = 0; i < tvShow.getNumberOfSeasons(); i++) {
                    intent.putExtra("passedTVSeason" + i, new Gson().toJson(seasons.get(i)));
                }
                v.getContext().startActivity(intent);
            }
        });
    }


}
