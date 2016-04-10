package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TvShow implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private String backdrop_path;
    private CreatedBy[] createdBy;
    private int[] episode_run_times;
    private String first_air_date;
    private Genres[] genres;
    private String homepage;
    private int id;
    private boolean in_production;
    private String[] languages;
    private String last_air_date;
    private String name;
    private Network[] networks;
    private int number_of_episodes;
    private int number_of_seasons;
    private String[] origin_country;
    private String original_language;
    private String overview;
    private float popularity;
    private String poster_path;
    private ProductionCompany[] production_companies;
    private Season[] seasons;
    private String status;
    private String type;
    private float vote_average;
    private float vote_count;




    public String getBackdropPath() {
        return backdrop_path;
    }

    public List<String> getCreatedBy(){
        List<String> created_list = new ArrayList<>();
        for(int i=0; i<createdBy.length ; i++){
            created_list.add(createdBy[i].getName());
        }
        return created_list;
    }

    public int[] getRuntimes(){
        return episode_run_times;
    }

    public String getFirstAirDate() {
        return first_air_date;
    }

    public List<String> getGenres(){
        List<String> genre_list = new ArrayList<>();
        for(int i=0; i<genres.length ; i++){
            genre_list.add(genres[i].getName());
        }
        return genre_list;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public boolean getInProduction(){
        return in_production;
    }
    public String[] getLanguages(){
        return languages;
    }
    public String getLastAirDate(){
        return last_air_date;
    }

    public String getName() {
        return name;
    }

    public Network[] getNetworks(){
        return networks;
    }

    public int getNumberOfEpisodes(){
        return number_of_episodes;
    }

    public int getNumberOfSeasons(){
        return number_of_seasons;
    }

    public String[] getOriginCountry(){
        return origin_country;
    }

    public String getOriginalLanguage(){
        return original_language;
    }

    public String getSynopsis() {
        return overview;
    }

    public float getPopularity(){
        return popularity;
    }

    public String getPosterPath(){
        return String.format("%s%s", BASE_URL, poster_path);
    }

    public ProductionCompany[] getProductionCompanies(){
        return production_companies;
    }

    public Season[] getSeasons(){
        return seasons;
    }

    public String getStatus(){
        return status;
    }

    public String getType(){
        return type;
    }


    public float getRating() {
        return vote_average;
    }

    public float getVoteCount() {
        return vote_count;
    }







}

