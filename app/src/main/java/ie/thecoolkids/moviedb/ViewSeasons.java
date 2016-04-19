package ie.thecoolkids.moviedb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ViewSeasons extends FragmentActivity {


    private List<Season> seasons = new ArrayList<Season>();
    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int tvShowID, numSeasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        //gets the passed numSeasons & a number of tvseasons in json format
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numSeasons = extras.getInt("numSeasons");
            if (extras != null) {
                for(int i=0; i < numSeasons; i++) {
                    seasons.add(new Gson().fromJson(extras.getString("passedTVSeason"+i), Season.class));
                }
            }
        }

        NUM_PAGES = seasons.size();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), seasons);
        mPager.setAdapter(mPagerAdapter);
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



     //A simple pager adapter that represents  ScreenSlidePageFragment objects, in sequence.
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

