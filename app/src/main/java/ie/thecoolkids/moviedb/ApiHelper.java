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
    public String query = "";
    private OkHttpClient client;


    /* Constructor doesn't do much, just initiates two vars */
    public ApiHelper(Context main) {
        context = main;
        client = new OkHttpClient();
    }

    public Uri.Builder GetBaseURL() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3");

        return builder;
    }

    /*
    *  MOVIE QUERIES
    *  GO HERE
    * */
    public ApiHelper SetPopularMovieQuery(int page) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.toString();
        return this;
    }

    public ApiHelper SetMovieSearchQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetMovieIDQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetMovieVideoQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendPath("videos")
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }


    /*
    *  TV SHOW QUERIES
    *  GO HERE
    * */
    public ApiHelper SetPopularTvShowQuery(int page) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("tv")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.toString();
        return this;
    }

    public ApiHelper SetTvSearchQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("search")
                .appendPath("tv")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetTvIDQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("tv")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetSeasonIDQuery(int id, int seasonNum) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("tv")
                .appendPath(String.valueOf(id))
                .appendPath("season")
                .appendPath(String.valueOf(seasonNum))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetTvVideoQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("tv")
                .appendPath(String.valueOf(id))
                .appendPath("videos")
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    /*
    * ACTOR QUERIES
    * GO HERE
    * */
    public ApiHelper SetActorSearchQuery(String query, int page) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("search")
                .appendPath("person")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("query", query)
                .appendQueryParameter("page", Integer.toString(page));

        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetPersonIDQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("person")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    public ApiHelper SetPersonIDCreditsQuery(int id) {
        Uri.Builder builder = GetBaseURL()
                .appendPath("person")
                .appendPath(String.valueOf(id))
                .appendPath("combined_credits")
                .appendQueryParameter("api_key", API_KEY);
        this.query = builder.build().toString();

        return this;
    }

    /*
     * This is what gets ran in another Thread.
     * It gets called when we call .execute();
     */
    protected String doInBackground(String... urls){
        String retval = "";

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
        Logger.Debug(url);
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
