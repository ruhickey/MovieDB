package ie.thecoolkids.moviedb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie implements Serializable {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    static Map<Integer, String> genres = new HashMap<>();
    static
    {
        genres = new HashMap<Integer, String>();
        genres.put(28, "Action");
        genres.put(12, "Adventure");
        genres.put(16, "Animation");
        genres.put(35, "Comedy");
        genres.put(80, "Crime");
        genres.put(99, "Documentary");
        genres.put(18, "Drama");
        genres.put(10751, "Family");
        genres.put(14, "Fantasy");
        genres.put(10769, "Foreign");
        genres.put(36, "History");
        genres.put(27, "Horror");
        genres.put(10402, "Music");
        genres.put(9648, "Mystery");
        genres.put(10749, "Romance");
        genres.put(878, "Sci-Fi");
        genres.put(10770, "TV Movie");
        genres.put(53, "Thriller");
        genres.put(10752, "War");
        genres.put(37, "Western");
    }

    private String poster_path;
    private String overview;
    private String release_date;
    private int[] genre_ids;
    private int id;
    private String title;
    private String backdrop_path;
    private float vote_average;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return Integer.parseInt(release_date.substring(0, 4));
    }

    public float getRating() {
        return vote_average;
    }

    public List<String> getGenres() {
        List<String> genre_list = new ArrayList<>();
        for(int i = 0; i < genre_ids.length; i++) {
            genre_list.add(genres.get(genre_ids[i]));
        }
        return genre_list;
    }

    public String getSynopsis() {
        return overview;
    }

    public String getMediumCoverImage(){
        return String.format("%s%s", BASE_URL, poster_path);
    }
}