package ie.thecoolkids.moviedb;

import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout baseLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedOption;

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
                return true;
            case R.id.nav_tvshows_local:
                return true;
            case R.id.nav_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
