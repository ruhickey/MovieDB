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
    private int season_number;
    private String still_path;
    private float vote_average;

//    //only got if searching for season number by id
//    private CrewMember[] crew;
//    private GuestStar[] guest_stars;

    Episode(String a, int en, String n, String o, int i, int sn, String path, float v){
        air_date = a;
        episode_number = en;
        name = n;
        overview = o;
        id = i;
        season_number = sn;
        still_path = path;
        vote_average = v;
    }



    public String getAirDate(){
        return air_date;
    }

    public String getEpisodeNumberString(){
        return "Episode " + episode_number;
    }

    public int getEpisodeNumber(){
        return episode_number;
    }

    public String getEpisodeName(){
        return name;
    }

    public String getOverview(){
        return overview;
    }

    public int getId(){
        return id;
    }

    public int getSeasonNumber(){
        return season_number;
    }

    public String getPoster(){
        return String.format("%s%s", BASE_URL, still_path);
    }

    public String episodeToString(){
        return "Air Date :\t" + air_date + "\nRating :\t" + vote_average + "\nOverview :\t" + overview;
    }

    public float getVoteAverage(){
        return vote_average;
    }

//    public float getVoteCount(){
//        return vote_count;
//    }

//    public CrewMember[] getCrew(){
//        return crew;
//    }
}

