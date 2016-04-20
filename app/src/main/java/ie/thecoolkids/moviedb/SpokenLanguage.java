package ie.thecoolkids.moviedb;


import java.io.Serializable;

public class SpokenLanguage implements Serializable{

    private String iso_639_1;
    private String name;

    SpokenLanguage(){}

    SpokenLanguage(String name){
        this.name = name;
    }

    public String getLanguageCode() {
        return iso_639_1;
    }

    public String getName() {
        return name;
    }

}
