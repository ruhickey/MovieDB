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

    ImageButton btnSearch;
    ImageButton btnSort;
    EditText etQuery;
    List<Movie> movies;
    MovieListAdapter movieListAdapter;

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

    private SortBy sortBy = SortBy.rating;
    private State state;
    private int page;
    ListView lvMovies;
    private int movieCount;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("movies", movies.toArray());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = new ArrayList<Movie>(Arrays.asList((Movie[])savedInstanceState.getSerializable("movies")));
        SetMovieList(movies);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSearch = (ImageButton) toolbar.findViewById(R.id.btnSearch);
        btnSort = (ImageButton) toolbar.findViewById(R.id.btnSort);

        etQuery = (EditText) findViewById(R.id.etQuery);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchForMovie();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSortDialog();
            }
        });

        SetUpMovieListView();
        GetBestMovies();
    }

    private void ShowSortDialog(){
        final CharSequence[] items = { "Rating", "Year", "Title (Desc)", "Title (Asc)" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

    private void SearchForMovie()
    {
        page = 1;
        state = State.SEARCH;
        try {
            new ApiHelper().execute(GetURL(page));
        } catch (Exception ex) {
            Log.d(DEBUG, ex.getMessage());
        }
    }

    public String GetURL(int page)
    {
        Uri.Builder builder = new Uri.Builder();
        String query = etQuery.getText().toString();

        builder.scheme("https")
                .authority("yts.ag")
                .appendPath("api")
                .appendPath("v2")
                .appendPath("list_movies.json")
                .appendQueryParameter("query_term", query)
                .appendQueryParameter("page", Integer.toString(page));

        if(sortBy == SortBy.rating || sortBy == SortBy.year) {
            builder.appendQueryParameter("sort_by", sortBy.toString());
        } else {
            builder.appendQueryParameter("sort_by", "title")
                    .appendQueryParameter("order_by", sortBy.toString());
        }

        return builder.build().toString();
    }

    private void SetUpMovieListView() {
        /* Create the Movie List View Once */
        movies = new ArrayList<>();
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movieListAdapter = new MovieListAdapter(this);
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
                            new ApiHelper().execute(GetURL(page));
                        } catch (Exception ex) {
                            Log.d(DEBUG, ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void GetBestMovies() {
        page = 1;
        state = State.SEARCH;
        try {
            new ApiHelper().execute(GetURL(page));
        } catch (Exception ex) {
            Log.d(DEBUG, ex.getMessage());
        }
    }

    public void SetMovieList(List<Movie> _movies){
        movieListAdapter.setMovies(_movies);
        movieListAdapter.notifyDataSetChanged();
    }

    private class ApiHelper extends AsyncTask<String, Void, String>{
        OkHttpClient client = new OkHttpClient();

        protected String doInBackground(String... urls){
            String retval = null;

            try {
                retval = run(urls[0]);
            } catch (IOException ex){
                Log.d(DEBUG, ex.getMessage());
            }

            Log.d(DEBUG, retval);
            return retval;
        }

        String run (String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        protected void onPostExecute(String json)
        {
            Log.d(DEBUG, "Post Execute Entered");
            if(json != null) {
                Gson gson = new Gson();

                if(state == State.SEARCH) {
                    movies = new ArrayList<>();
                }

                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray movieArray = obj.getJSONObject("data").getJSONArray("movies");
                    movieCount = obj.getJSONObject("data").getInt("movie_count");
                    Log.d(DEBUG, "Movie Count - " + Integer.toString(movieCount));

                    for(int i = 0; i < movieArray.length(); i++)
                    {
                        movies.add(gson.fromJson(movieArray.get(i).toString(), Movie.class));
                    }

                    if(movies != null) {
                        SetMovieList(movies);
                    }

                }catch(Exception ex){
                    Log.d(DEBUG, ex.getMessage());
                }
            }
        }
    }
}
