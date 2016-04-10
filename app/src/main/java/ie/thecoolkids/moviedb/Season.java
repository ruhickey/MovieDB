package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Season implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    private String air_date;
    private Episode[] episodes;
    private String name;
    private String overview;
    private int id;
    private String poster_path;
    private int season_number;


    public String getAirDate(){
        return air_date;
    }

    public Episode[] getEpisodes(){
        return episodes;
    }

    public String getSeasonName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public String getPoster(){
        return String.format("%s%s", BASE_URL, poster_path);
    }

    public int getSeasonNumber() {
        return season_number;
    }
}
