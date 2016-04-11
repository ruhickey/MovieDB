package ie.thecoolkids.moviedb;


import java.io.Serializable;

public class Role implements Serializable {

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private String name;
    private String character;
    private int episode_count;
    private String first_air_date;
    private String original_name;

    private String title;
    private String department;
    private String job;
    private String release_date;
    private String original_title;


    private String credit_id;
    private String media_type;
    private int id;
    private String poster_path;






    public String getCreditId() {
        return credit_id;
    }

    public String getDepartment() {
        return department;
    }

    public String getFirstAirDate() {
        return first_air_date;
    }

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return original_name;
    }

    public String getPoster() {
        if(poster_path != null) return String.format("%s%s", BASE_URL, poster_path);
        else                    return null;
    }

    public String getMediaType() {
        return media_type;
    }

    public String  getEpisodeCount() {
        return "" + episode_count;
    }

    public String getCharacter() {
        return character;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getOriginalTitle() {
        return original_title;
    }
}
