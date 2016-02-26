package ie.thecoolkids.moviedb;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText etQuery;
    List<Movie> movies;

    private final String DEBUG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        etQuery = (EditText) findViewById(R.id.etQuery);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG, "Button Clicked");
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String query = etQuery.getText().toString();

                    builder.scheme("https")
                            .authority("yts.ag")
                            .appendPath("api")
                            .appendPath("v2")
                            .appendPath("list_movies.json")
                            .appendQueryParameter("query_term", query);

                    Log.d(DEBUG, builder.build().toString());
                    new ApiHelper().execute(builder.build().toString());
                } catch (Exception ex) {
                    Log.d(DEBUG, ex.getMessage());
                }
            }
        });
    }

    public void SetMovieList(List<Movie> _movies){
        ListView lvMovies = (ListView) findViewById(R.id.lvMovies);
        lvMovies.setAdapter(new MovieListAdapter(this, _movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
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
                movies = new ArrayList<>();

                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray movieArray = obj.getJSONObject("data").getJSONArray("movies");
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

            for(int i = 0; i < movies.size(); i++) {
                Log.d(DEBUG, movies.get(i).getTitle());
            }
        }
    }
}
