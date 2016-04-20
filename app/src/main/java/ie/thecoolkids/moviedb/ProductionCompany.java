package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class ProductionCompany implements Serializable {


    private int id;
    private String name;

    ProductionCompany(){}

    ProductionCompany(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}





