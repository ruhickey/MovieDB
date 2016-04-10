package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.List;

public class People implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    private boolean adult;
    private List<String> also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private int id;
    private String name;
    private String place_of_birth;
    private String profile_path;



    public boolean isAdult(){
        return adult;
    }

    public List<String> getOtherNames() {
        return also_known_as;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthDay() {
        return birthday;
    }

    public String getDeathDay() {
        return deathday;
    }

    public String getHomepage(){
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfBirth(){
        return place_of_birth;
    }

    public String getPersonPicture(){
        if(profile_path != null)    return String.format("%s%s", BASE_URL, profile_path);
        else                        return null;
    }


}
