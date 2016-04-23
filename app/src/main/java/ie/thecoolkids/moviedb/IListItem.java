package ie.thecoolkids.moviedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public interface IListItem {
    class Holder
    {
        ImageView imgPoster;
        TextView tvTitle;
        RatingBar ratBar;
    }

    View getRowView(Context context, LayoutInflater inflater);
}
