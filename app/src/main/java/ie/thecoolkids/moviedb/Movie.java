package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private boolean adult;
    private String backdrop_path;
    private Collection belongs_to_collection;
    private int budget;
    private Genres[] genres;
    private String homepage;
    private int id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private float popularity;
    private String poster_path;
    private ProductionCompany[] production_companies;
    private ProductionCountry[] production_countries;
    private String release_date;
    private int revenue;
    private int runtime;
    private SpokenLanguage[] spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float vote_average;
    private float vote_count;





    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public boolean getBelongsToCollection() {
        boolean inCollection;
        if(belongs_to_collection == null)   inCollection = false;
        else                                inCollection = true;
        return inCollection;
    }

    public Collection getCollection() {
        return belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public List<String> getGenres(){
        List<String> genre_list = new ArrayList<>();
        for(int i=0; i< genres.length ; i++){
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

    public String getImdbId() {
        return imdb_id;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getSynopsis() {
        return overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPoster(){
        if(poster_path != null) return String.format("%s%s", BASE_URL, poster_path);
        else                    return null;
    }

    public ProductionCompany[] getProductionCompanies() {
        return production_companies;
    }

    public List<String> getProductionCompanyNames(){
        List<String> production_company_list = new ArrayList<>();
        for(int i=0; i< production_companies.length ; i++){
            production_company_list.add(production_companies[i].getName());
        }
        return production_company_list;
    }

    public int getYear() {
        return Integer.parseInt(release_date.substring(0, 4));
    }

    public String getReleaseDate() {
        return release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<String> getSpokenLanguages(){
        List<String> languages_list = new ArrayList<>();
        for(int i=0; i< spoken_languages.length ; i++){
            languages_list.add(spoken_languages[i].getName());
        }
        return languages_list;
    }

    public List<String> getProductionCountries(){
        List<String> countries_list = new ArrayList<>();
        for(int i=0; i< production_countries.length ; i++){
            countries_list.add(production_countries[i].getName());
        }
        return countries_list;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public boolean getVideo() {
        return video;
    }

    public float getRating() {
        return vote_average;
    }

    public float getVoteCount() {
        return vote_count;
    }



}