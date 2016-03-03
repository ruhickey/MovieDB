package ie.thecoolkids.moviedb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends BaseAdapter {

    private List<Movie> movies = null;
    private static LayoutInflater inflater = null;
    private MainActivity main;

    public MovieListAdapter(MainActivity _main)
    {
        main = _main;
        inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMovies(List<Movie> _movies)
    {
        movies = _movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        ImageView imgPoster;
        TextView tvTitle;
        RatingBar ratBar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.movie_list, null);

        holder.imgPoster = (ImageView) rowView.findViewById(R.id.imgPoster);
        holder.tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        holder.ratBar = (RatingBar) rowView.findViewById(R.id.ratBar);

        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.ratBar.setRating((movies.get(position).getRating() / 2));

        Picasso.with(main).load(movies.get(position).getMediumCoverImage()).into(holder.imgPoster);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewMovie.class);
                intent.putExtra("passedMovie", new Gson().toJson(movies.get(position)));
                v.getContext().startActivity(intent);
            }
        });

        return rowView;
    }
}
