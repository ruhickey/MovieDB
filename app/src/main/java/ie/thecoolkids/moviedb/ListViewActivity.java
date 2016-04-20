package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListViewActivity extends BaseActivity {
    private List<TheMovieDB> list;
    private ListView listView;
    private ListAdapter listAdapter;
    private DBHelper db;
    private Handler handler;
    private String instanceOf = "Unknown";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("list")){
            list = (List<TheMovieDB>) intent.getSerializableExtra("list");
            if(intent.hasExtra("instanceOf")){
                instanceOf = intent.getStringExtra("instanceOf");
            }
            setupListView();
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                setupListView();
                if(msg.obj.toString() == "moviesEmpty"){
                    Toast.makeText(getApplicationContext(), "Favourite Movies Is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(msg.obj.toString() == "tvShowsEmpty"){
                    Toast.makeText(getApplicationContext(), "Favourite Tv Shows Is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setupListView(){
        listView = (ListView)findViewById(R.id.lvMovies);
        listAdapter = new ListAdapter(this);
        listAdapter.setContent(list);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("list", (Serializable)list);
        savedInstanceState.putString("instanceOf", instanceOf);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        list = (List<TheMovieDB>)savedInstanceState.getSerializable("list");
        instanceOf = savedInstanceState.getString("instanceOf");
        listAdapter.setContent(list);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DBHelper(this);
        switch(instanceOf){
            case "Movie":
                new Thread(new CheckLocalMovies()).start();
                break;
            case "TvShow":
                new Thread(new CheckLocalTvShows()).start();
                break;
        }
    }

    class CheckLocalMovies implements Runnable{
        @Override
        public void run(){
            list = new ArrayList<TheMovieDB>();
            Message msg = new Message();
            Cursor c = db.getAllLocalMovies();
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    Movie movie = new Movie();
                    movie.setId(c.getInt(0));
                    movie.setTitle(c.getString(1));
                    movie.setRating(c.getFloat(3));
                    movie.setPoster(c.getString(12));
                    list.add((TheMovieDB)movie);
                }
                msg.obj = "dbUpdate";
            }
            else{
                msg.obj = "moviesEmpty";
            }
            handler.sendMessage(msg);
        }
    }

    class CheckLocalTvShows implements Runnable{
        @Override
        public void run(){
            list = new ArrayList<TheMovieDB>();
            Message msg = new Message();
            Cursor c = db.getAllLocalMovies();
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    TvShow tvShow = new TvShow();
                    tvShow.setId(c.getInt(0));
                    tvShow.setTitle(c.getString(1));
                    tvShow.setRating(c.getFloat(4));
                    tvShow.setPoster(c.getString(9));
                    list.add((TheMovieDB)tvShow);
                }
                msg.obj = "dbUpdate";
            }
            else{
                msg.obj = "tvShowsEmpty";
            }
            handler.sendMessage(msg);
        }
    }
}
