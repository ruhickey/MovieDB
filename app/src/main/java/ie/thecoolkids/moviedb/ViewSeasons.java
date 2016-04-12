package ie.thecoolkids.moviedb;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewSeasons extends FragmentActivity implements IParser{



    private RelativeLayout mainPage;
    private ImageView poster;

    private int tvShowID;
    private int numSeasons;

    private List<Season> seasons;
    private Season season;
    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);


        //gets the passed tvshow id & numSeasons
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvShowID = extras.getInt("passedTVShowID");
            numSeasons = extras.getInt("passedTVShowNumSeasons");
        }

        for(int i=0; i<numSeasons; i++){
            new ApiHelper(this).SetSeasonIDQuery(tvShowID, i+1).execute();
        }
        NUM_PAGES = seasons.size();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), seasons);
        mPager.setAdapter(mPagerAdapter);
    }

    public void parseJson(String json) {
        try {
            Gson gson = new Gson();
            season = gson.fromJson(json, Season.class);
            seasons.add(season);
        }
        catch(Exception ex){
            if(ex == null) {
                Log.d("EXCEPTION", "NULL");
            } else if (ex.getMessage() == null){
                ex.printStackTrace();
            } else {
                Log.d("EXCEPTION", ex.getMessage());
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Season> seasons;
        int currentPage;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Season> seasons) {
            super(fm);
            this.seasons  = seasons;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            currentPage = position;
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            fragment.setText(seasons.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }






}
