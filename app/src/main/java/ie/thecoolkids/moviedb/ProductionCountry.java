package ie.thecoolkids.moviedb;


import java.io.Serializable;

public class ProductionCountry implements Serializable{

    private String iso_3166_1;
    private String name;

    ProductionCountry(String name){
        this.name = name;
    }


    public String getCode() {
        return iso_3166_1;
    }

    public String getName() {
        return name;
    }

}

