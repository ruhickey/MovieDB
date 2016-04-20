package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class Network implements Serializable {


    private int id;
    private String name;

    Network(){}

    Network(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}




