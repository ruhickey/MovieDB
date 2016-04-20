package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class Genre implements Serializable {

    private int id;
    private String name;

    Genre(){}

    Genre(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}



