package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout baseLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedOption;
    private DBHelper db;
    private Handler handler;
    private ImageButton btnSearch;
    private ImageButton btnOpenNav;
    private EditText etQuery;


    @Override
    public void setContentView(@LayoutRes int layoutResID){
        baseLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout childView = (FrameLayout)baseLayout.findViewById(R.id.baseContent);
        getLayoutInflater().inflate(layoutResID, childView, true);

        super.setContentView(baseLayout);
        navView = (NavigationView)findViewById(R.id.navView);
        setupNavDrawer();
        db = new DBHelper(this);


        btnSearch = (ImageButton)findViewById(R.id.btnSearch);
        btnOpenNav = (ImageButton)findViewById(R.id.btnOpenNav);
        etQuery = (EditText)findViewById(R.id.etQuery);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    if(!etQuery.getText().toString().equals("")){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("searchTerm", etQuery.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Enter a query term", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("searchTerm", etQuery.getText().toString());
                startActivity(intent);
            }
        });

        btnOpenNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseLayout.openDrawer(GravityCompat.START);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (baseLayout.isDrawerOpen(GravityCompat.START)) {
            baseLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        switch(item.getItemId()){
            case R.id.nav_home:
                startActivity(intent);
                return true;
            case R.id.nav_movies:
                intent.putExtra("searchTerm", "TopMovies");
                startActivity(intent);
                return true;
            case R.id.nav_tvshows:
                intent.putExtra("searchTerm", "TopTvShows");
                startActivity(intent);
                return true;
            case R.id.nav_movies_local:
                new Thread(new CheckForLocalMovies()).start();
                return true;
            case R.id.nav_tvshows_local:
                new Thread(new CheckForLocalTvShows()).start();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
