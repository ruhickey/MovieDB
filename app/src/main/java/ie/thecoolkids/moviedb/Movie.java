package ie.thecoolkids.moviedb;

import java.util.List;

public class Movie {
    private int id;
    private String url;
    private String imdb_code;
    private String title;
    private String title_english;
    private String title_long;
    private String slug;
    private int year;
    private double rating;
    private int runtime;
    private List<String> genres;
    private String summary;
    private String description_full;
    private String synopsis;
    private String yt_trailer_code;
    private String language;
    private String mpa_rating;
    private String background_image;
    private String background_image_original;
    private String small_cover_image;
    private String medium_cover_image;
    private String large_cover_image;
    private String state;
    private List<Torrent> torrents;
    private String date_uploaded;
    private int date_uploaded_unix;



    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImdb_code() {
        return imdb_code;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_english() {
        return title_english;
    }

    public String getTitle_long() {
        return title_long;
    }

    public String getSlug() {
        return slug;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription_full() {
        return description_full;
    }

    public String getSynopsis() {
        return synopsis;
    }


    public String getYt_trailer() {
        return yt_trailer_code;
    }

    public String getLanguage() {
        return language;
    }

    public String getMpa_rating() {
        return mpa_rating;
    }

    public String getBackground_image() {
        return background_image;
    }

    public String getBackground_image_original() {
        return background_image_original;
    }

    public String getSmall_cover_image() {
        return small_cover_image;
    }

    public String getPosterURL(){
        return medium_cover_image;
    }

    public String getLarge_cover_image() {
        return large_cover_image;
    }

    public String getStatus() {
        return state;
    }



    public List<Torrent> getTorrents() {
        return torrents;
    }

    public String getDateUploaded() {
        return date_uploaded;
    }

    public int getDateUploadedUnix() {
        return date_uploaded_unix;
    }

}
