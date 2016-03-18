package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Season implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    //TODO : add array of epiosde info including crew, guest stars etc.
    private String name;
    private String overview;
    private int id;
    private String poster_path;
    private int season_number;



    public String getSeasonName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }


    public String getPosterImage(){
        return String.format("%s%s", BASE_URL, poster_path);
    }

    public int getSeasonNumber() {
        return season_number;
    }
}
