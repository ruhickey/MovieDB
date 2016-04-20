package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout baseLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedOption;
    private DBHelper db;
    private Handler handler;


    @Override
    public void setContentView(@LayoutRes int layoutResID){
        baseLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout childView = (FrameLayout)baseLayout.findViewById(R.id.baseContent);
        getLayoutInflater().inflate(layoutResID, childView, true);
        super.setContentView(baseLayout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navView = (NavigationView)findViewById(R.id.navView);
        toolbar.setVisibility(View.GONE);
        setupNavDrawer();
        db = new DBHelper(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setupNavDrawer(){
        navView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                baseLayout,
                toolbar,
                R.string.drawerOpened,
                R.string.drawerClosed
        );
        baseLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        baseLayout.closeDrawer(GravityCompat.START);
        selectedOption = item.getItemId();
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                return true;
            case R.id.nav_movies:
                return true;
            case R.id.nav_tvshows:
                return true;
            case R.id.nav_movies_local:
                new Thread(new GetLocalMovies()).start();
                return true;
            case R.id.nav_tvshows_local:
                new Thread(new GetLocalTvShows()).start();
                return true;
            case R.id.nav_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class GetLocalMovies implements Runnable{
        @Override
        public void run(){
            List<TheMovieDB> list = new ArrayList<TheMovieDB>();
            Cursor c = db.getAllLocalMovies();
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    Movie movie = new Movie();
                    movie.setId(c.getInt(0));
                    movie.setTitle(c.getString(1));
                    movie.setRating(c.getFloat(3));
                    list.add((TheMovieDB)movie);
                }
                Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("instanceOf", "Movie");
                startActivity(intent);
            }
            else{
                Message msg = new Message();
                msg.obj = "Favourite Movies Is Empty";
                handler.sendMessage(msg);
            }
        }
    }

    class GetLocalTvShows implements Runnable{
        @Override
        public void run(){
            List<TheMovieDB> list = new ArrayList<TheMovieDB>();
            Cursor c = db.getAllLocalMovies();
            if(c.getCount() > 0){
                while(c.moveToNext()){
                    TvShow tvShow = new TvShow();
                    tvShow.setId(c.getInt(0));
                    tvShow.setTitle(c.getString(1));
                    tvShow.setRating(c.getFloat(4));
                    list.add((TheMovieDB)tvShow);
                }
                Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("instanceOf", "TvShow");
                startActivity(intent);
            }
            else{
                Message msg = new Message();
                msg.obj = "Favourite Tv Shows Is Empty";
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DBHelper(this);
    }
}
