package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class VideoInfoResult implements Serializable {


    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
