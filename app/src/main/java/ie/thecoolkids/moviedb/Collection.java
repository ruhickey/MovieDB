package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class Collection implements Serializable {


    private int id;
    private String name;
    private String poster_path;
    private String backdrop_path;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }



}






