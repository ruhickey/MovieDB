package ie.thecoolkids.moviedb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoInfo implements Serializable{

    private int id;
    private VideoInfoResult[] results;

    public int getId(){
        return id;
    }

    public ArrayList<VideoInfoResult> getResults() {
        ArrayList<VideoInfoResult> results_list = new ArrayList<VideoInfoResult>();
        for (int i = 0; i < results.length; i++) {
            results_list.add(results[i]);
        }
        return results_list;
    }

    public ArrayList<String> getResultsKeys() {
        ArrayList<String> results_keys_list = new ArrayList<String>();
        for (int i = 0; i < results.length; i++) {
            if(results[i].getSite().equals("YouTube") && !results[i].getKey().equals(""))
            results_keys_list.add(results[i].getKey());
        }
        return results_keys_list;
    }


}
