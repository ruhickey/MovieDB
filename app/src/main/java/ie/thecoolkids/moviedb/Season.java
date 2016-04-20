package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;

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

    Season(){}

    public Season(String a, Episode[] x, String n, String o, int i, String pp, int sesnum){
        air_date = a;
        episodes = x;
        name = n;
        overview = o;
        id = i;
        poster_path = pp;
        season_number = sesnum;
    }


    public String getAirDate(){
        return air_date;
    }

    public ArrayList<Episode> getEpisodes(){
        ArrayList<Episode> eps = new ArrayList<Episode>();
        for(int i=0; i< episodes.length; i++){
            eps.add(episodes[i]);
        }
        return eps;
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
