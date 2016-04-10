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
    private String[] origin_countries;
    private String original_language;
    private String overview;
    private float popularity;
    private String poster_path;
    private ProductionCompany[] production_companies; //P
    private Season[] seasons; //*****************
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

    public List<Integer> getRuntimes(){
        List<Integer> runtimes_list = new ArrayList<>();
        for(int i=0; i<episode_run_times.length ; i++){
            runtimes_list.add(episode_run_times[i]);
        }
        return runtimes_list;
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

    public List<String> getLanguages(){
        List<String> lang_list = new ArrayList<>();
        for(int i=0; i<languages.length ; i++){
            lang_list.add(languages[i]);
        }
        return lang_list;
    }
    public String getLastAirDate(){
        return last_air_date;
    }

    public String getName() {
        return name;
    }

    public List<String> getNetworks(){
        List<String> net_list = new ArrayList<>();
        for(int i=0; i<networks.length ; i++){
            net_list.add(networks[i].getName());
        }
        return net_list;
    }

    public int getNumberOfEpisodes(){
        return number_of_episodes;
    }

    public int getNumberOfSeasons(){
        return number_of_seasons;
    }

    public List<String> getOriginCountry(){
        List<String> coun_list = new ArrayList<>();
        for(int i=0; i<origin_countries.length ; i++){
            coun_list.add(origin_countries[i]);
        }
        return coun_list;
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

    public String getPoster(){
        return String.format("%s%s", BASE_URL, poster_path);
    }

    public List<String> getProductionCompanies(){
        List<String> coun_list = new ArrayList<>();
        for(int i=0; i<production_companies.length ; i++){
            coun_list.add(production_companies[i].getName());
        }
        return coun_list;
    }

    public List<Season> getSeasons(){
        List<Season> seasons_list = new ArrayList<>();
        for(int i=0; i<seasons.length ; i++){
            seasons_list.add(seasons[i]);
        }
        return seasons_list;
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

