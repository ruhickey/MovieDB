package ie.thecoolkids.moviedb;

import java.io.Serializable;



public class Episode implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private String air_date;
    private int episode_number;
    private String name;
    private String overview;
    private int id;
    private String production_code;
    private int season_number;
    private String still_path;
    private float vote_average;

    private String getAirDate(){
        return air_date;
    }

    private int getEpisodeNumber(){
        return episode_number;
    }

    private String getEpisodeName(){
        return name;
    }

    private String getOverview(){
        return overview;
    }

    private int getId(){
        return id;
    }

    private String getProductionCode(){
        return production_code;
    }

    private int getSeasonNumber(){
        return season_number;
    }

    public String getStillPath(){
        return String.format("%s%s", BASE_URL, still_path);
    }

    public float getVoteAverage(){
        return vote_average;
    }
}

