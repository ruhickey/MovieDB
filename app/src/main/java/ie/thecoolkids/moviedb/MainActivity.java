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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("movies", movies.toArray());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = new ArrayList<Movie>(Arrays.asList((Movie[]) savedInstanceState.getSerializable("movies")));
        SetMovieList(movies);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        /* Set up UI Controls */
        btnSearch = (ImageButton) toolbar.findViewById(R.id.btnSearch);
        btnSort = (ImageButton) toolbar.findViewById(R.id.btnSort);
        etQuery = (EditText) findViewById(R.id.etQuery);

        /* Set up movie list shell */
        SetUpMovieListView();
        GetPopularMovies();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchForMovie();
            }
        });

        /*
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSortDialog();
            }
        });
        */
    }

    private void GetPopularMovies() {
        new ApiHelper(this).SetPopularQuery(page).execute();
    }

    private void ShowNetworkError() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG);
    }

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

    private void SearchForMovie() {
        page = 1;
        state = State.SEARCH;
        try {
            SetMovieQuery();
        } catch (Exception ex) {
            Logger.Debug(ex.getMessage());
        }
    }

    private void SetUpMovieListView() {
        /* Create the Movie List View Once */
        movies = new ArrayList<>();
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movieListAdapter = new MovieListAdapter(context);
        movieListAdapter.setMovies(movies);
        lvMovies.setAdapter(movieListAdapter);

        lvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void SetMovieQuery() {
        String query = etQuery.getText().toString();
        if(query.isEmpty()){
            new ApiHelper(context).SetPopularQuery(page).execute();
        } else {
            new ApiHelper(context).SetMovieQuery(query, page).execute();
        }
    }

    private void GetBestMovies() {
        page = 1;
        state = State.SEARCH;
        try {
            SetMovieQuery();
        } catch (Exception ex) {
            Logger.Debug(ex.getMessage());
        }
    }

    public ArrayList<Movie> search(String term) {
        ArrayList<Movie> newData = new ArrayList<>();
        for(int i = 0; i < movies.size(); i++){
            if(movies.get(i).getTitle().contains(term))
                newData.add(movies.get(i));
        }
        return newData;
    }

    public void UpdateList(String term) {
        movieListAdapter.setMovies(search(term));
        movieListAdapter.notifyDataSetChanged();
    }

    public void SetMovieList(List<Movie> _movies) {
        movieListAdapter.setMovies(_movies);
        movieListAdapter.notifyDataSetChanged();
    }

    public void ParseJson(String json) {
        if(json != null) {
            try {
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(json);

                movieCount = obj.getInt("total_results");

                JSONArray movieArray = obj.getJSONArray("results");

                if(state == State.SEARCH) {
                    movies.clear();
                }

                for (int i = 0; i < movieArray.length(); i++) {
                    Movie x = gson.fromJson(movieArray.get(i).toString(), Movie.class);
                    if(!x.getMediumCoverImage().endsWith("null")) {
                        movies.add(x);
                    }
                }

                if(movies != null) {
                    SetMovieList(movies);
                }
            } catch(Exception ex) {
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
}