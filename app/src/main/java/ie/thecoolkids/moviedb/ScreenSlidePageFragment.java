package ie.thecoolkids.moviedb;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends android.support.v4.app.Fragment {

    private String textDate, textName;


    public void setText(Season season){
        this.textDate=season.getAirDate();
        this.textName = season.getSeasonName();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide, container, false);
        ((TextView)rootView.findViewById(R.id.textArea)).setText(textDate);
        ((TextView)rootView.findViewById(R.id.textArea2)).setText(textName);
        return rootView;
    }
}
