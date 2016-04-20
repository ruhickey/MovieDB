package ie.thecoolkids.moviedb;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TvShow implements Serializable, TheMovieDB {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";


    private String backdrop_path;
    private CreatedBy[] created_by;
    private int[] episode_run_time;
    private String first_air_date;
    private Genre[] genres;
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
    private ProductionCompany[] production_companies; //P
    private Season[] seasons;
    private String status;
    private String type;
    private float vote_average;
    private float vote_count;

    TvShow(){}

    TvShow(Context context, int id){
        DBHelper db = new DBHelper(context);
        Cursor c = db.getMovie(id);
        c.moveToFirst();
        this.id = c.getInt(0);
        this.name = c.getString(1);
        this.first_air_date = c.getString(2);
        this.last_air_date = c.getString(3);
        this.vote_average = c.getFloat(4);
        this.overview = c.getString(5);
        this.status = c.getString(6);
        setRuntimes(c.getString(7));
        setGenres(c.getString(8));
        this.poster_path = c.getString(9);
        languages = c.getString(10).split(";");
        setNetworks(c.getString(11));
        setProductionCompanies(c.getString(12));
        origin_country = c.getString(13).split(";");
        number_of_seasons = c.getInt(14);
        number_of_episodes = c.getInt(15);
    }


    public String getBackdropPath() {
        return backdrop_path;
    }

    public List<String> getCreatedBy(){
        List<String> created_list = new ArrayList<>();
        for(int i=0; i<created_by.length ; i++){
            created_list.add(created_by[i].getName());
        }
        return created_list;
    }

    public void setCreatedBy(String s){
        String array[] = s.split(";");
        int n = array.length;
        created_by = new CreatedBy[n];
        for(int i = 0; i < n; i++){
            created_by[i] = new CreatedBy(array[i]);
        }
    }

    public List<Integer> getRuntimes(){
        List<Integer> runtimes_list = new ArrayList<>();
        for(int i=0; i<episode_run_time.length ; i++){
            runtimes_list.add(episode_run_time[i]);
        }
        return runtimes_list;
    }

    public void setRuntimes(String s){
        String array[] = s.split(";");
        int n = array.length;
        episode_run_time = new int[n];
        for(int i = 0; i < n; i++){
            episode_run_time[i] = Integer.parseInt(array[i]);
        }
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

    public void setGenres(String s){
        String array[] = s.split(";");
        int n = array.length;
        genres = new Genre[n];
        for(int i = 0; i < n; i++){
            genres[i] = new Genre(array[i]);
        }
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
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

    public String getTitle() {
        return name;
    }

    public void setTitle(String title){
        this.name = title;
    }

    public List<String> getNetworks(){
        List<String> net_list = new ArrayList<>();
        for(int i=0; i<networks.length ; i++){
            net_list.add(networks[i].getName());
        }
        return net_list;
    }

    public void setNetworks(String s){
        String array[] = s.split(";");
        int n = array.length;
        networks = new Network[n];
        for(int i = 0; i < n; i++){
            networks[i] = new Network(array[i]);
        }
    }

    public int getNumberOfEpisodes(){
        return number_of_episodes;
    }

    public int getNumberOfSeasons(){
        return number_of_seasons;
    }

    public List<String> getOriginCountry(){
        List<String> coun_list = new ArrayList<>();
        for(int i=0; i<origin_country.length ; i++){
            coun_list.add(origin_country[i]);
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

    public void setPoster(String poster_path){
        this.poster_path = poster_path;
    }

    public List<String> getProductionCompanies(){
        List<String> coun_list = new ArrayList<>();
        for(int i=0; i<production_companies.length ; i++){
            coun_list.add(production_companies[i].getName());
        }
        return coun_list;
    }

    public void setProductionCompanies(String s){
        String array[] = s.split(";");
        int n = array.length;
        production_companies = new ProductionCompany[n];
        for(int i = 0; i < n; i++){
            production_companies[i] = new ProductionCompany(array[i]);
        }
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

    public void setRating(float rating){
        this.vote_average = rating;
    }

    public float getVoteCount() {
        return vote_count;
    }







}

