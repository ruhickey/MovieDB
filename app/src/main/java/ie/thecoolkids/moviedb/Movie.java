package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private int id;
    private String url;
    private String title;
    private int year;
    private float rating;
    private int runtime;
    private List<String> genres;
    private String synopsis;
    private String yt_trailer_code;
    private String mpa_rating;
    private String medium_cover_image;
    private String date_uploaded;
    private int date_uploaded_unix;

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public float getRating() {
        return rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getYtTrailer() {
        return yt_trailer_code;
    }

    public String getMpaRating() {
        return mpa_rating;
    }

    public String getMediumCoverImage(){
        return medium_cover_image;
    }

    public String getDateUploaded() {
        return date_uploaded;
    }

    public int getDateUploadedUnix() {
        return date_uploaded_unix;
    }

}
