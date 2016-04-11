package ie.thecoolkids.moviedb;


import java.io.Serializable;

public class GuestStar implements Serializable {

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private int id;
    private String name;
    private String credit_id;
    private String character;
    private int order;
    private String profile_path;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreditId() {
        return credit_id;
    }

    public String getCharacter() {
        return character;
    }

    public int getOrder() {
        return order;
    }

    public String getProfilePath() {
        if (profile_path != null)   return String.format("%s%s", BASE_URL, profile_path);
        else                        return null;
    }
}
