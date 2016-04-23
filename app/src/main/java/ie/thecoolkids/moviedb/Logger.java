package ie.thecoolkids.moviedb;

import android.util.Log;

/*
 * I'm gonna use this class to make debugging easier.
 * It will help me look for the different info I want by searching the log
 * for keywords. For example DEBUG, EXCEPTION, WARNING.
 */
public class Logger {
    public static void Debug(String msg) {
        if(msg != null) {
            Log.d("DEBUG", msg);
        } else {
            Log.d("DEBUG", "null");
        }
    }

    public static void Exception(String msg) {
        if(msg != null) {
            Log.e("EXCEPTION", msg);
        } else {
            Log.e("EXCEPTION", "null");
        }
    }

    public static void Warning(String msg) {
        if(msg != null) {
            Log.d("WARNING", msg);
        } else {
            Log.w("WARNING", "null");
        }
    }
}
