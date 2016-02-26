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

    public String getTitle()
    {
        return title;
    }

    public String getPosterURL()
    {
        return medium_cover_image;
    }
}
