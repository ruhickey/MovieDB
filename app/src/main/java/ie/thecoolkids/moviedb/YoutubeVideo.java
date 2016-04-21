package ie.thecoolkids.moviedb;

public class YoutubeVideo{
    public String id;
    public String title;

    public YoutubeVideo(String id, String content) {
        this.id = id;
        this.title = content;
    }

    @Override
    public String toString() {
        return title;
    }
}
