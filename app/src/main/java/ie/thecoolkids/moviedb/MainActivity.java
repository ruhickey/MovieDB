package ie.thecoolkids.moviedb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements IParser{

    private ImageButton btnSearch;
    private ImageButton btnSort;
    private EditText etQuery;
    private List<IListItem> items;
    private ItemListAdapter movieListAdapter;
    private NetworkHelper netHelper;
    private MainActivity context;
    private SortBy sortBy = SortBy.rating;
    private State state;
    private QType qType;
    private int page = 1;
    private ListView lvMovies;
    private int movieCount, tvCount, moviePages, tvPages;

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

    private enum QType {
        MOVIE,
        TV_SHOW,
        ACTOR
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
        savedInstanceState.putSerializable("movies", items.toArray());
    }

    /*
    * This method gets called when we open back up the app after closing using the back button
    * This is where we re-use our saved data and show it on-screen.
    */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        items = new ArrayList<IListItem>(Arrays.asList((IListItem[]) savedInstanceState.getSerializable("movies")));
        SetItemList(items);
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
        * This will retrieve the data from the API and then display it in the ItemListAdapter.
        */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPages = 0;
                SearchForMovie();
            }
        });
    }

    /*
     * This uses the API to get the most popular movies at the moment.
     * The ApiHelper Class will call the ParseJSON method when it has retrieved the JSON.
     * That's the reason that this is a simple call and
     * we don't need to retrieve any data from the method.
     */
    private void GetPopularMovies() {
        qType = QType.MOVIE;
        try {
            new ApiHelper(this).SetPopularQuery(page).execute();
        } catch(Exception ex) {
            Logger.Exception(ex.getMessage());
        }
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
     * We do not want to create a new ItemListAdapter every time we change the Movie List.
     * This would be very inefficient.
     * Instead, we change the Movie List within the ItemListAdapter
     * and call it's notifyDataSetChanged method.
     * This will allow us to update the content on screen without re-creating the class.
     */
    private void SetUpMovieListView() {
        /*
         * TODO: Maybe take this movies initialization out of here
         * This method should only be used for initializing the ItemListAdapter.
         */
        items = new ArrayList<>();
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movieListAdapter = new ItemListAdapter(context);
        movieListAdapter.setMovies(items);
        lvMovies.setAdapter(movieListAdapter);

        /*
         * This code checks to see how far down the ItemListAdapter we are.
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
                    state = State.UPDATE;
                    Logger.Debug("Page Old - " + page);

                    Logger.Debug("MoviePages - " + moviePages);
                    Logger.Debug("TvPages - " + tvPages);

                    if(page < moviePages) {
                        Logger.Debug("Updating Page");
                        page++;
                    } else if (page < tvPages){
                        Logger.Debug("Updating Page");
                        page++;
                    }
                    Logger.Debug("Page New - " + page);
                    SetMovieQuery();
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
        if((page < moviePages) || (state == State.SEARCH)) {
            Logger.Debug("Setting Movie Query");
            qType = QType.MOVIE;
            String query = etQuery.getText().toString();
            if (query.isEmpty()) {
                new ApiHelper(context).SetPopularQuery(page).execute();
            } else {
                new ApiHelper(context).SetMovieQuery(query, page).execute();
            }
        } else {
            Logger.Debug("Setting TV Query");
            SetTvShowQuery();
        }
    }

    private void SetTvShowQuery() {
        if((tvPages == 0) || (page < tvPages)) {
            qType = QType.TV_SHOW;
            String query = etQuery.getText().toString();
            if (query.isEmpty()) {
                new ApiHelper(context).SetPopularTvShowQuery(page).execute();
            } else {
                Logger.Debug("TV URL - " + new ApiHelper(context).SetTvQuery(query, page).query);
                new ApiHelper(context).SetTvQuery(query, page).execute();
            }
        }
    }

    /*
     * This is the method that gets called anytime we want to update
     * our ItemListAdapter.
     * It sets the Adapters Movie List and then notifies it about the data change.
     */
    private void SetItemList(List<IListItem> _items) {
        movieListAdapter.setMovies(_items);
        movieListAdapter.notifyDataSetChanged();
    }


    /* Method for parsing movies */
    private void ParseMovies(Gson gson, JSONObject obj) throws Exception {
        /*
         * This gets us the amount of Movies that we can retrieve.
         * We can then use this to decide whether or not we want
         * to get another page worth of Movie Information.
         */
        moviePages = obj.getInt("total_pages");

        /* This gets us the JSON movie objects */
        JSONArray movieArray;
        try {
           movieArray = obj.getJSONArray("results");
        }catch (Exception ex) {
            Logger.Debug("No results");
            return;
        }

        /*
         * If we are searching we want a blank movie list.
         * If we are updating, we don't want a blank list,
         * the user should still be able to see page 1 as well as page 2.
         */
        if(state == State.SEARCH) {
            items.clear();
        }

        /*
         * Iterate through the JSON movie object array.
         * Use Gson to convert the JSON movie object to an actual Movie Object.
         * Then add it to our Movie List.
         */
        for (int i = 0; i < movieArray.length(); i++) {
            Movie x = gson.fromJson(movieArray.get(i).toString(), Movie.class);
            if (!x.getPoster().endsWith("null")) {
                items.add(x);
            }
        }

        /* Set the movie list and update the ItemListAdapter */
        if(items != null) {
            SetItemList(items);
        }
    }

    private void ParseTvShows(Gson gson, JSONObject obj) throws Exception {
        Logger.Debug("ParseTvShows()");

        tvPages = obj.getInt("total_pages");

        Logger.Debug("TotalPages - " + tvPages);

        JSONArray tvArray = obj.getJSONArray("results");

        for (int i = 0; i < tvArray.length(); i++) {
            TvShow x = gson.fromJson(tvArray.get(i).toString(), TvShow.class);
            if(!x.getPoster().endsWith("null")) {
                items.add(x);
            }
        }

        if(items != null) {
            SetItemList(items);
        }
    }

    private void ParseActors(Gson gson, JSONObject obj) throws Exception {

    }

    /*
     * This is what the ApiHelper class calls once we receive
     * the JSON back from the API.
     * We iterate through the JSON array and use Gson to create a Movie class from it.
     * TODO: This will need to be modified to deal with Movies/Tv Shows/Actors
     * TODO: We could add another field in the settings to keep track of which it is.
     */
    public void parseJson(String json) {
        /* Make sure we actually got something back */
        if(json != null) {
            try {
                /* Create the Gson Object */
                Gson gson = new Gson();
                /* Create the JSON Object from the JSON String */
                JSONObject obj = new JSONObject(json);

                switch (qType) {
                    case MOVIE: ParseMovies(gson, obj); break;
                    case TV_SHOW: ParseTvShows(gson, obj); break;
                    case ACTOR: ParseActors(gson, obj); break;
                }

            } catch(Exception ex) {
                Logger.Exception("parseJson() - " + ex.getMessage());
            }

            if(qType == QType.MOVIE) SetTvShowQuery();
        }
    }
}