package ie.thecoolkids.moviedb;

import android.util.Log;

/*
 * I'm gonna use this class to make debugging easier.
 * It will help me look for the different info I want by searching the log
 * for keywords. For example DEBUG, EXCEPTION, WARNING.
 */
public class Logger {
    public static void Debug(String msg) {
        Log.d("DEBUG", msg);
    }

    public static void Exception(String msg) {
        Log.d("EXCEPTION", msg);
    }

    public static void Warning(String msg) {
        Log.d("WARNING", msg);
    }
}
