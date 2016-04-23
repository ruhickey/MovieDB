package ie.thecoolkids.moviedb;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        
        if(season.getAirDate() != null){        this.textDate=season.getAirDate();}
        if(season.getSeasonName() != null){     this.textName = season.getSeasonName();}
        if(season.getOverview() != null){       this.textOverView = season.getOverview();}
        if(season.getOverview() != null){       this.imagePath = season.getPoster();}

        this.textSeasonNum = "Season # " + season.getSeasonNumber();

        this.epNums = new ArrayList<String>();
        this.epDates = new ArrayList<String>();
        this.epOverviews = new ArrayList<String>();

        if(season.getEpisodes() != null && season.getEpisodes().size() > 0){
            this.episodes = season.getEpisodes();
            for(int i=0; i<episodes.size(); i++){
                if(episodes.get(i).getEpisodeNumberString() != null && !episodes.get(i).getEpisodeNumberString().equals("")){
                    this.epNums.add(episodes.get(i).getEpisodeNumberString());
                }else{
                    this.epNums.add("Episode Number unknown");
                }
                if(episodes.get(i).getAirDate() != null && !episodes.get(i).getAirDate().equals("")){
                    this.epDates.add("Air Date: " + episodes.get(i).getAirDate());
                } else{
                    this.epDates.add("Air Date:\nNo episode air date details available for this episode");
                }
                if(episodes.get(i).getOverview() != null && !episodes.get(i).getOverview().equals("")){
                    this.epOverviews.add("Overview:\n" + episodes.get(i).getOverview() + "\n");
                }else{
                    this.epOverviews.add("Overview:\nNo episode overview details available for this episode");
                }
            }
        }



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = this;

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide, container, false);
        LinearLayout overviewBox = (LinearLayout) rootView.findViewById(R.id.overviewBox);
        LinearLayout episodeBox = (LinearLayout) rootView.findViewById(R.id.episodesBox);

        TextView tvName = (TextView) rootView.findViewById(R.id.name);
        TextView tvDate = (TextView)rootView.findViewById(R.id.airdate);
        TextView tvSeasonNum = (TextView)rootView.findViewById(R.id.seasonNum);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.overview);
        if(textName != null && !textName.equals("")){ tvName.setText(textName);}
        if(textDate != null && !textDate.equals("")){ tvDate.setText(textDate);}
        if(textSeasonNum != null && !textSeasonNum.equals("")){ tvSeasonNum.setText(textSeasonNum);}
        if(textOverView != null && !textOverView.equals("")){
            tvOverview.setText(textOverView);
            overviewBox.setVisibility(View.VISIBLE);
        }

        //ImageView img = (ImageView) rootView.findViewById(R.id.poster);
        //todo: add setting imgview

        episodeList = (LinearLayout)rootView.findViewById(R.id.epList);

        if(episodes.size() > 0) {
            episodeBox.setVisibility(View.VISIBLE);


            for (int i = 0; i < episodes.size(); i++) {
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
        }

        return rootView;
    }



}
