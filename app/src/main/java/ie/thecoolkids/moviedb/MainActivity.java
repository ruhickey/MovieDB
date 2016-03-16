package ie.thecoolkids.moviedb;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnSearch;
    private ImageButton btnSort;
    private EditText etQuery;
    private List<Movie> movies;
    private MovieListAdapter movieListAdapter;
    private NetworkHelper netHelper;
    private MainActivity context;
    private SortBy sortBy = SortBy.rating;
    private State state;
    private int page = 1;
    private ListView lvMovies;
    private int movieCount;

    private final String DEBUG = "DEBUG";

    private enum SortBy {
        asc,
        desc,
        rating,
        year
    }

    private enum State {
        SEARCH,
        UPDATE
    }


    /*
    * This method gets called when we click the back button and leave the app.
    * This is where we save any data we want to show when we re-open the app.
    * For example, here we save the list of movies so we don't need to re-download
    * them if we click out of the app.
    */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("movies", movies.toArray());
    }

    /*
    * This method gets called when we open back up the app after closing using the back button
    * This is where we re-use our saved data and show it on-screen.
    */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = new ArrayList<Movie>(Arrays.asList((Movie[]) savedInstanceState.getSerializable("movies")));
        SetMovieList(movies);
    }

    /*
    * This is part 1 of the Android app life-cycle.
    * This is where any initialization code goes.
    * For example, we show the Activity and
    * then populate the best movie list when the app opens.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Show the main activity layout */
        setContentView(R.layout.activity_main);

        /*
         * This sets the toolbar as an action bar.
         * Don't really understand this yet.
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* This is just so we can get the context from anywhere on the page.
         * If we use the keyword 'this' within a new OnClickListener, then
         * we don't get the context, we get the View.
         */
        context = this;

        /* Connect the Controls to the UI */
        btnSearch = (ImageButton) toolbar.findViewById(R.id.btnSearch);
        btnSort = (ImageButton) toolbar.findViewById(R.id.btnSort);
        etQuery = (EditText) findViewById(R.id.etQuery);

        /* Set up movie list shell */
        SetUpMovieListView();
        GetPopularMovies();

        /*
        * Create the new OnClickListener for the Search Button on the toolbar.
        * This will retrieve the data from the API and then display it in the MovieListAdapter.
        */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchForMovie();
            }
        });

        /*
         * This is commented out for the time being because I'm not sure if I'll use it or not.
         * It shows up a dialog on screen asking the user which way they want to show the results.
         */
        /*
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSortDialog();
            }
        });
        */
    }

    /*
     * This uses the API to get the most popular movies at the moment.
     * The ApiHelper Class will call the ParseJSON method when it has retrieved the JSON.
     * That's the reason that this is a simple call and
     * we don't need to retrieve any data from the method.
     */
    private void GetPopularMovies() {
        new ApiHelper(this).SetPopularQuery(page).execute();
    }

    /*
     * I haven't used this yet.
     * It will be used if the user doesn't have an internet connection.
     * At the moment the app will crash if we have no Internet Connection and
     * try to make a call to the API.
     */
    private void ShowNetworkError() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG);
    }

    /*
     * This is the method that gets called from the Sort Button OnClickListener.
     * It sets an enum to the desired sort method and this is then used by
     * SearchForMovie to query the API in different ways. E.g. GetMovies.json?sort=Year
     */
    private void ShowSortDialog() {
        final CharSequence[] items = {"Rating", "Year", "Title (Desc)", "Title (Asc)"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Sort By");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        sortBy = SortBy.rating;
                        break;
                    case 1:
                        sortBy = SortBy.year;
                        break;
                    case 2:
                        sortBy = SortBy.desc;
                        break;
                    case 3:
                        sortBy = SortBy.asc;
                }

                dialog.dismiss();
                SearchForMovie();
            }
        });

        AlertDialog sortByDialog = builder.create();
        sortByDialog.show();
    }

    /*
     * This method resets our Settings.
     * TODO: We should probably put our settings into it's own class.
     * Then we can call Settings.Page for example
     * and it will be easier to see which settings we have.
     * Finally it calls the SetMovieQuery method which sets the
     * query and then searches for the movie.
     */
    private void SearchForMovie() {
        page = 1;
        state = State.SEARCH;
        try {
            SetMovieQuery();
        } catch (Exception ex) {
            Logger.Debug(ex.getMessage());
        }
    }

    /* This should only get called once.
     * In the OnCreate method.
     * We do not want to create a new MovieListAdapter every time we change the Movie List.
     * This would be very inefficient.
     * Instead, we change the Movie List within the MovieListAdapter
     * and call it's notifyDataSetChanged method.
     * This will allow us to update the content on screen without re-creating the class.
     */
    private void SetUpMovieListView() {
        /*
         * TODO: Maybe take this movies initialization out of here
         * This method should only be used for initializing the MovieListAdapter.
         */
        movies = new ArrayList<>();
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movieListAdapter = new MovieListAdapter(context);
        movieListAdapter.setMovies(movies);
        lvMovies.setAdapter(movieListAdapter);

        /*
         * This code checks to see how far down the MovieListAdapter we are.
         * We need to know this so that we can get a certain amount of movies at a time.
         * For example, we show 20 movies, but when we reach the 20th,
         * we make another call to the API and ask for the next page and show them movies aswell.
         */
        lvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            /*
             * This gets called when the scroll state changes.
             * For example, if the user is scrolling and then flings the list.
             * If the user stops/starts scrolling etc.
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            /*
             * This is what happens while we are scrolling.
             * We keep track of which List Items are visible to the user.
             * We then use these when the ScrollStateChange Event occurs
             * to determine if we're at the end of the list.
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            /*
             * This is what actually checks the position.
             * We call this method when we receive the onScrollStateChanged Event.
             */
            private void isScrollCompleted() {
                if (((totalItem - currentFirstVisibleItem) == currentVisibleItemCount)
                        && (this.currentScrollState == SCROLL_STATE_IDLE)) {
                    if (movies.size() < movieCount) {
                        page++;
                        state = State.UPDATE;
                        try {
                            SetMovieQuery();
                        } catch (Exception ex) {
                            Logger.Debug(ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    /*
     * This sets the API query url and Executes a call to it.
     * If the Search Box is set to empty, then we just get the
     * most popular movies.
     * Otherwise we search for the term they entered.
     */
    private void SetMovieQuery() {
        String query = etQuery.getText().toString();
        if(query.isEmpty()){
            new ApiHelper(context).SetPopularQuery(page).execute();
        } else {
            new ApiHelper(context).SetMovieQuery(query, page).execute();
        }
    }

    /*
     * This isn't used anymore but should get the Top Rated IMDB movies.
     * This will be added back in eventually.
     */
    private void GetBestMovies() {
        page = 1;
        state = State.SEARCH;
        try {
            SetMovieQuery();
        } catch (Exception ex) {
            Logger.Debug(ex.getMessage());
        }
    }

    /*
     * This is the method that gets called anytime we want to update
     * our MovieListAdapter.
     * It sets the Adapters Movie List and then notifies it about the data change.
     */
    public void SetMovieList(List<Movie> _movies) {
        movieListAdapter.setMovies(_movies);
        movieListAdapter.notifyDataSetChanged();
    }

    /*
     * This is what the ApiHelper class calls once we receive
     * the JSON back from the API.
     * We iterate through the JSON array and use Gson to create a Movie class from it.
     * TODO: This will need to be modified to deal with Movies/Tv Shows/Actors
     * TODO: We could add another field in the settings to keep track of which it is.
     */
    public void ParseJson(String json) {
        /* Make sure we actually got something back */
        if(json != null) {
            try {
                /* Create the Gson Object */
                Gson gson = new Gson();
                /* Create the JSON Object from the JSON String */
                JSONObject obj = new JSONObject(json);

                /*
                 * This gets us the amount of Movies that we can retrieve.
                 * We can then use this to decide whether or not we want
                 * to get another page worth of Movie Information.
                 */
                movieCount = obj.getInt("total_results");

                /* This gets us the JSON movie objects */
                JSONArray movieArray = obj.getJSONArray("results");

                /*
                 * If we are searching we want a blank movie list.
                 * If we are updating, we don't want a blank list,
                 * the user should still be able to see page 1 as well as page 2.
                 */
                if(state == State.SEARCH) {
                    movies.clear();
                }

                /*
                 * Iterate through the JSON movie object array.
                 * Use Gson to convert the JSON movie object to an actual Movie Object.
                 * Then add it to our Movie List.
                 */
                for (int i = 0; i < movieArray.length(); i++) {
                    Movie x = gson.fromJson(movieArray.get(i).toString(), Movie.class);
                    /*
                     * We check to see if it has a cover and then add it if it does.
                     * TODO: This code should be moved before the Gson.fromJson call because
                     * I think it would make it more efficient.
                     */
                    if(!x.getMediumCoverImage().endsWith("null")) {
                        movies.add(x);
                    }
                }

                /* Set the movie list and update the MovieListAdapter */
                if(movies != null) {
                    SetMovieList(movies);
                }
            } catch(Exception ex) {
                if(ex == null) {
                    /*
                     * I Log like this because it's easier to find the exceptions.
                     */
                    Log.d("EXCEPTION", "NULL");
                } else if (ex.getMessage() == null){
                    ex.printStackTrace();
                } else {
                    Log.d("EXCEPTION", ex.getMessage());
                }
            }
        }
    }
}