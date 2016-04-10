package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    private boolean adult;
    private String[] also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private int id;
    private String name;
    private String place_of_birth;
    private String profile_path;
    private Movie[] known_for;



    public boolean isAdult(){
        return adult;
    }

    public List<String> getOtherNames() {
        List<String> names_list = new ArrayList<>();
        for(int i=0; i< also_known_as.length ; i++){
            names_list.add(also_known_as[i]);
        }
        return names_list;
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


    public List<Movie> getKnownFor() {
        List<Movie> movies_list = new ArrayList<>();
        for(int i=0; i< known_for.length ; i++){
            movies_list.add(known_for[i]);
        }
        return movies_list;
    }
}
