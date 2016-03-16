package ie.thecoolkids.moviedb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    Context context = null;

    public NetworkHelper(Context _context) {
        context = _context;
    }

    /*
     * This checks if the network is up.
     * TODO: It hasn't been tested yet.
     * It should be one of the next things to implement.
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
