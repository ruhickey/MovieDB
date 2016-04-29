package ie.thecoolkids.moviedb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements IParser{

    private ImageButton btnSort;
    private List<IListItem> items;
    private ItemListAdapter movieListAdapter;
    private MainActivity context;
    private QType qType;
    private int page = 1;
    private ListView lvItems;
    private int moviePages, tvPages, actorPages;
    private State state;
    private String movieJSON = null, tvJSON = null, actorJSON = null, searchTerm = "";
    private SearchBy searchBy = null;
    public boolean Kill = true;

    private final String DEBUG = "DEBUG";

    private enum QType {
        MOVIE,
        TV_SHOW,
        ACTOR
    }

    private enum State {
        IDLE,
        BUSY
    }

    private enum SearchBy {
        RATING,
        QUERY
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Kill && !searchTerm.isEmpty()){
            //finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Kill = true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("items", items.toArray());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        items = new ArrayList<IListItem>(Arrays.asList((IListItem[]) savedInstanceState.getSerializable("items")));
        SetItemList(items);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        items = new ArrayList<>();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("searchTerm")){
            searchTerm = intent.getStringExtra("searchTerm");
            if(searchTerm.equals("TopMovies")){
                Logger.Debug("Search Term - Movies");
                searchBy = SearchBy.RATING;
                qType = QType.MOVIE;
            }
            else if(searchTerm.equals("TopTvShows")) {
                Logger.Debug("Search Term - Tv Shows");
                searchBy = SearchBy.RATING;
                qType = QType.TV_SHOW;
            }
        }

        if(searchBy == null) searchBy = SearchBy.QUERY;

        /* Set up movie list shell */
        SetUpItemListView();
        ResetSearchStates();
        ResetPages();
        SearchForItems();
    }

    private void ResetPages() {
        page = 1;
        moviePages = 1;
        tvPages = 1;
        actorPages = 1;
    }

    private void ResetSearchStates() {
        if(searchBy == SearchBy.QUERY) {
            qType = QType.MOVIE;
        }
        state = State.BUSY;
    }

    private void ShowNetworkError() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG);
    }

    private void SetUpItemListView() {
        items = new ArrayList<>();
        lvItems = (ListView) findViewById(R.id.lvMovies);
        movieListAdapter = new ItemListAdapter(context);
        movieListAdapter.setMovies(items);
        lvItems.setAdapter(movieListAdapter);

        lvItems.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (((totalItem - currentFirstVisibleItem) == currentVisibleItemCount)
                        && (this.currentScrollState == SCROLL_STATE_IDLE)) {
                    /* Update the page number and search again */
                    if (state == State.IDLE) {
                        page++;
                        ResetSearchStates();
                        SearchForItems();
                    }
                }
            }
        });
    }

    private void SearchForItems() {
        if(searchBy == SearchBy.QUERY) {
            Logger.Debug("Searching For Items");
            String query = searchTerm;
            if (!query.isEmpty()) {
                switch (qType) {
                    case MOVIE:
                        SearchForMovie(query);
                        break;
                    case TV_SHOW:
                        SearchForTvShow(query);
                        break;
                    case ACTOR:
                        SearchForActor(query);
                        break;
                }
            } else {
                switch (qType) {
                    case MOVIE:
                        SearchForPopularMovie();
                        break;
                    case TV_SHOW:
                        SearchForPopularTvShow();
                        break;
                    case ACTOR:
                        ParseAllData();
                        break;
                }
            }
        } else {
            if(qType == QType.MOVIE) {
                Logger.Debug("Qtype - Movie");
                SearchForRatedMovie();
            } else {
                Logger.Debug("Qtype - Tv Show");
                SearchForRatedTvShow();
            }
        }
    }

    private void SearchForMovie(String query) {
        movieJSON = null;
        if(page <= moviePages) {
            new ApiHelper(context).SetMovieSearchQuery(query, page).execute();
        } else {
            qType = QType.TV_SHOW;
            SearchForItems();
        }
    }

    private void SearchForPopularMovie() {
        movieJSON = null;
        if(page <= moviePages) {
            new ApiHelper(context).SetPopularMovieQuery(page).execute();
        } else {
            qType = QType.TV_SHOW;
            SearchForItems();
        }
    }

    private void SearchForRatedMovie(){
        Logger.Debug("Search Movies");
        movieJSON = null;
        if(page <= moviePages) {
            new ApiHelper(context).SetRatedMovieQuery(page).execute();
        } else {
            ParseAllData();
        }
    }

    private void SearchForTvShow(String query) {
        tvJSON = null;
        if(page <= tvPages) {
            new ApiHelper(context).SetTvSearchQuery(query, page).execute();
        } else {
            qType = QType.ACTOR;
            SearchForItems();
        }
    }

    private void SearchForPopularTvShow() {
        tvJSON = null;
        if(page <= tvPages) {
            new ApiHelper(context).SetPopularTvShowQuery(page).execute();
        } else {
            actorJSON = null;
            ParseAllData();
        }
    }

    private void SearchForRatedTvShow() {
        Logger.Debug("Search Tv Shows");
        tvJSON = null;
        if (page <= tvPages) {
            new ApiHelper(context).SetRatedTvQuery(page).execute();
        } else {
            ParseAllData();
        }
    }

    private void SearchForActor(String query) {
        actorJSON = null;
        if(page <= actorPages) {
            new ApiHelper(this).SetActorSearchQuery(query, page).execute();
        } else {
            ParseAllData();
        }
    }

    private void SetItemList(List<IListItem> _items) {
        movieListAdapter.setMovies(_items);
        movieListAdapter.notifyDataSetChanged();
    }

    private void ParseMovies(Gson gson, JSONObject obj) throws JSONException {
        JSONArray movieArray = obj.getJSONArray("results");
        moviePages = obj.getInt("total_pages");
        for (int i = 0; i < movieArray.length(); i++) {
            Movie x = gson.fromJson(movieArray.get(i).toString(), Movie.class);
            if (x.getPoster() != null) {
                items.add(x);
            }
        }
        SetItemList(items);
    }

    private void ParseTvShows(Gson gson, JSONObject obj) throws JSONException {
        JSONArray tvArray = obj.getJSONArray("results");
        tvPages = obj.getInt("total_pages");
        for (int i = 0; i < tvArray.length(); i++) {
            TvShow x = gson.fromJson(tvArray.get(i).toString(), TvShow.class);
            if (x.getPoster() != null) {
                items.add(x);
            }
        }
        SetItemList(items);
    }

    private void ParseActors(Gson gson, JSONObject obj) throws JSONException {
        JSONArray tvArray = obj.getJSONArray("results");
        actorPages = obj.getInt("total_pages");
        for (int i = 0; i < tvArray.length(); i++) {
            Person x = gson.fromJson(tvArray.get(i).toString(), Person.class);
            if (x.getPersonPicture() != null) {
                items.add(x);
            }
        }
        SetItemList(items);
    }

    private void ParseAllData() {
        Gson gson = new Gson();

        try {
            if (movieJSON != null) {
                JSONObject obj = new JSONObject(movieJSON);
                ParseMovies(gson, obj);
            }
        } catch (JSONException ex) {
            Logger.Exception("Movies JsonObject - " + ex.getMessage());
        } catch (Exception ex) {
            Logger.Exception("Movies JsonObject - " + ex.getMessage());
        }

        try {
            if (tvJSON != null) {
                JSONObject obj = new JSONObject(tvJSON);
                ParseTvShows(gson, obj);
            }
        } catch (JSONException ex) {
            Logger.Exception("TV Shows JsonObject - " + ex.getMessage());
        } catch (Exception ex) {
            Logger.Exception("TV Shows JsonObject - " + ex.getMessage());
        }

        if (actorJSON != null) {
            try {
                JSONObject obj = new JSONObject(actorJSON);
                ParseActors(gson, obj);
            } catch (JSONException ex) {
                Logger.Exception("Actor JsonObject - " + ex.getMessage());
            } catch (Exception ex) {
                Logger.Exception("Actor JsonObject - " + ex.getMessage());
            }
        }

        state = State.IDLE;
    }

    public void parseJson(String json) {
        if(searchBy == SearchBy.QUERY) {
            switch (qType) {
                case MOVIE:
                    movieJSON = json;
                    qType = QType.TV_SHOW;
                    SearchForItems();
                    break;
                case TV_SHOW:
                    tvJSON = json;
                    qType = QType.ACTOR;
                    SearchForItems();
                    break;
                case ACTOR:
                    actorJSON = json;
                    ParseAllData();
                    break;
            }
        } else {
            switch(qType) {
                case MOVIE:
                    movieJSON = json;
                    break;
                case TV_SHOW:
                    tvJSON = json;
                    break;
            }

            ParseAllData();
        }
    }
}