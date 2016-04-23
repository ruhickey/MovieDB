package ie.thecoolkids.moviedb;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePageFragment extends android.support.v4.app.Fragment {

    private String textDate, textName, textSeasonNum, textOverView, imagePath;
    LinearLayout episodeList;
    private ArrayList<Episode> episodes;
    ScreenSlidePageFragment context;

    List<String> epNums;
    List<String> epDates;
    List<String> epOverviews;


    public void setText(Season season){

        this.textDate=season.getAirDate();
        this.textName = season.getSeasonName();
        this.textSeasonNum = "Season # " + season.getSeasonNumber();
        this.textOverView = season.getOverview();
        this.imagePath = season.getPoster();

        this.epNums = new ArrayList<String>();
        this.epDates = new ArrayList<String>();
        this.epOverviews = new ArrayList<String>();

        this.episodes = season.getEpisodes();
        for(int i=0; i<episodes.size();i++){
            this.epNums.add(episodes.get(i).getEpisodeNumberString());
            this.epDates.add("Air Date: " + episodes.get(i).getAirDate());
            this.epOverviews.add("Overview:\n" +episodes.get(i).getOverview() + "\n");
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = this;

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide, container, false);
        ((TextView)rootView.findViewById(R.id.name)).setText(textName);
        ((TextView)rootView.findViewById(R.id.airdate)).setText(textDate);
        ((TextView)rootView.findViewById(R.id.seasonNum)).setText(textSeasonNum);
        ((TextView)rootView.findViewById(R.id.overview)).setText(textOverView);

        Logger.Debug(this.imagePath);
        ImageView img = (ImageView) rootView.findViewById(R.id.poster);
        Picasso.with(this.getContext()).load(this.imagePath).placeholder(R.drawable.movies).fit().into(img);
        //todo: add setting imgview
        //todo: add setting background image

        episodeList = (LinearLayout)rootView.findViewById(R.id.epList);

        for(int i=0; i<episodes.size() ; i++){
            TextView tvEpNum = new TextView(getActivity().getBaseContext());
            TextView tvEpDate = new TextView(getActivity().getBaseContext());
            TextView tvEpOverview = new TextView(getActivity().getBaseContext());
            tvEpNum.setText(epNums.get(i));
            tvEpNum.setGravity(Gravity.CENTER_HORIZONTAL);
            tvEpDate.setText(epDates.get(i));
            tvEpDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvEpOverview.setText(epOverviews.get(i));
            tvEpOverview.setTextColor(Color.parseColor("#FFFFFF"));
            episodeList.addView(tvEpNum);
            episodeList.addView(tvEpDate);
            episodeList.addView(tvEpOverview);
        }

        return rootView;
    }



}
