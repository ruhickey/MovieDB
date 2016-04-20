package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class Genres implements Serializable {


    private int id;
    private String name;

    Genres(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}



