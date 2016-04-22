package ie.thecoolkids.moviedb;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiHelper extends AsyncTask<String, Void, String> {

    /*
     * This is our apps API Key.
     * We need this to actually be able to use the API.
     */
    public static final String API_KEY = "216feddaff308181c8dbc34ef3658b57";
    private Context context = null;
    private String query = "";
    private OkHttpClient client;


    /* Constructor doesn't do much, just initiates two vars */
    public ApiHelper(Context main) {
        context = main;
        client = new OkHttpClient();
    }





    /* This sets our API query URL to most popular. */
    public ApiHelper SetPopularQuery(int page) {
        this.query = "http://api.themoviedb.org/3/movie/popular?api_key=216feddaff308181c8dbc34ef3658b57&page=" + Integer.toString(page);
        return this;
    }

    /*
     * This sets up our movie query URL.
     * We pass it the String we want to search for
     * and the page number that we want.
     * This is called, followed by Execute.
     * Ex: new ApiHelper(this).SetMovieQuery().execute();
     * This would be called from MainActivity
     */
    public ApiHelper SetMovieQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL(page)
                .appendPath("movie")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetMovieIDQuery(int id) {
        Uri.Builder builder = GetBaseMovieIdURL()
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        Log.d("QUERY", query);
        return this;
    }

    public ApiHelper SetTvIDQuery(int id) {
        Uri.Builder builder = GetBaseMovieIdURL()
                .appendPath("tv")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        Log.d("QUERY", query);
        return this;
    }

    public ApiHelper SetPersonIDQuery(int id) {
        Uri.Builder builder = GetBaseMovieIdURL()
                .appendPath("person")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        Log.d("QUERY", query);
        return this;
    }

    public ApiHelper SetPersonIDCreditsQuery(int id) {
        Uri.Builder builder = GetBaseMovieIdURL()
                .appendPath("person")
                .appendPath(String.valueOf(id))
                .appendPath("combined_credits")
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        Log.d("QUERY", query);
        return this;
    }

    public ApiHelper SetSeasonIDQuery(int Tvshowid, int seasonNum) {
        Uri.Builder builder = GetBaseMovieIdURL()
                .appendPath("tv")
                .appendPath(String.valueOf(Tvshowid))
                .appendPath("season")
                .appendPath(String.valueOf(seasonNum))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        Log.d("QUERY", query);
        return this;
    }

    /*
     * This sets up our TV query URL.
     * We pass it the String we want to search for
     * and the page number that we want.
     * This is called, followed by Execute.
     * Ex: new ApiHelper(this).SetTvQuery().execute();
     * This would be called from MainActivity
     */
    public ApiHelper SetTvQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL(page)
                .appendPath("tv")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        query = builder.build().toString();

        return this;
    }

    /*
     * This gets us the Base url of the API.
     * TODO: I'm not sure if this is actually working properly.
     * I've to test it during the week.
     */
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

    public Uri.Builder GetBaseMovieIdURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3");
        return builder;
    }

    /*
     * This is what gets ran in another Thread.
     * It gets called when we call .execute();
     */
    protected String doInBackground(String... urls){
        String retval = null;

        try {
            retval = MakeApiRequest(query);
        } catch (IOException ex){
            Log.d("EXCEPTION", ex.getMessage());
        }

        return retval;
    }

    /*
     * This is what actually makes the call to the API.
     * It returns the JSON response as a String.
     */
    String MakeApiRequest (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /*
     * Once the background thread finishes executing.
     * This gets called. This is where we tell MainActivity to parse the Json.
     */
    protected void onPostExecute(String json) {
        if(context != null) {
            ((IParser)context).parseJson(json);
        }
    }
}
