package ie.thecoolkids.moviedb;

import java.io.Serializable;

public class Torrent implements Serializable {
    private String url;
    private String hash;
    private String quality;
    private int seeds;
    private int peers;
    private String size;
    private long size_bytes;
    private String date_uploaded;
    private long getDate_uploaded_unix;
}
