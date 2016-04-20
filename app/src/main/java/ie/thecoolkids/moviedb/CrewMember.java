package ie.thecoolkids.moviedb;


import java.io.Serializable;

public class CrewMember implements Serializable {


    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private int id;
    private String credit_id;
    private String name;
    private String department;
    private String job;
    private String profile_path;

    CrewMember(){}


    public int getId() {
        return id;
    }

    public String getCreditId() {
        return credit_id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public String getProfilePath() {
        if (profile_path != null)   return String.format("%s%s", BASE_URL, profile_path);
        else                        return null;
    }
}
