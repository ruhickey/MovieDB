package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
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
        //noinspection deprecation
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
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_home:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_movies:
                Toast.makeText(getApplicationContext(), "Top Movies", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_tvshows:
                Toast.makeText(getApplicationContext(), "Top Tv Shows", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_movies_local:
                new Thread(new CheckForLocalMovies()).start();
                return true;
            case R.id.nav_tvshows_local:
                new Thread(new CheckForLocalTvShows()).start();
                return true;
            case R.id.nav_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CheckForLocalMovies implements Runnable{
        @Override
        public void run(){
            Cursor c = db.getAllLocalMovies();
            if(c.getCount() > 0){
                Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                intent.putExtra("instanceOf", "Movie");
                startActivity(intent);
            }
            else{
                Message msg = new Message();
                msg.obj = "No Favourite Movies Saved";
                handler.sendMessage(msg);
            }
        }
    }

    class CheckForLocalTvShows implements Runnable{
        @Override
        public void run(){
            Cursor c = db.getAllLocalTvShows();
            if(c.getCount() > 0){
                Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                intent.putExtra("instanceOf", "TvShow");
                startActivity(intent);
            }
            else{
                Message msg = new Message();
                msg.obj = "No Favourite Tv Shows Saved";
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DBHelper(this);
        NotificationEventReceiver.cancelAlarm(getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }
}
