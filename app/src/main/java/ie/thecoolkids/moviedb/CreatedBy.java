package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class CreatedBy implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private int id;
    private String name;
    private String profile_path;

    CreatedBy(){}

    CreatedBy(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath(){
        return String.format("%s%s", BASE_URL, profile_path);
    }



}


