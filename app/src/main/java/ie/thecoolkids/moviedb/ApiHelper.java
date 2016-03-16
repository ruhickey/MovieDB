package ie.thecoolkids.moviedb;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiHelper extends AsyncTask<String, Void, String> {

    public static final String API_KEY = "216feddaff308181c8dbc34ef3658b57";
    private Context context = null;
    private String query = "";
    private OkHttpClient client;

    public ApiHelper(MainActivity main) {
        context = main;
        client = new OkHttpClient();
    }

    public ApiHelper SetPopularQuery(int page) {
        this.query = "http://api.themoviedb.org/3/movie/popular?api_key=216feddaff308181c8dbc34ef3658b57&page=" + Integer.toString(page);
        return this;
    }

    public ApiHelper SetMovieQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL(page)
                .appendPath("movie")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.build().toString();

        Log.d("QUERY", query);

        return this;
    }

    public ApiHelper SetTvQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL(page)
                .appendPath("tv")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        query = builder.build().toString();

        return this;
    }

    public Uri.Builder GetBaseURL(int page) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("search")
                .appendPath("")
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        return builder;
    }

    protected String doInBackground(String... urls){
        String retval = null;

        try {
            retval = run(query);
        } catch (IOException ex){
            Log.d("EXCEPTION", ex.getMessage());
        }

        return retval;
    }

    String run (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected void onPostExecute(String json) {
        if(context != null) {
            ((MainActivity) context).ParseJson(json);
        }
    }
}
