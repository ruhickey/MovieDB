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

    /*
     * This method gets called for every item in movies.
     * It gathers the information from Movie and creates a ListItem out of it.
     * It then returns the List item.
     * We don't call this function, it automatically gets called.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.movie_list, null);

        /*
         * TODO: I took this whole Holder stuff from an online tutorial,
         * but it seems a bit redundant, so I might remove it soon.
         * It does, show us exactly what we can use in the UI though,
         * so I'm not sure yet.
         * The following connects the Holder to the UI controls.
         */
        holder.imgPoster = (ImageView) rowView.findViewById(R.id.imgPoster);
        holder.tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        holder.ratBar = (RatingBar) rowView.findViewById(R.id.ratBar);

        /*
         * This is where we give information to the UI.
         */
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.ratBar.setRating((movies.get(position).getRating() / 2));

        /*
         * This is where we load in the image.
         * Use Picasso's Placeholder to put a Stock image in the Movie Poster first.
         */
        Picasso.with(main).load(movies.get(position).getPoster()).placeholder(R.drawable.movies).fit().into(holder.imgPoster);

        /*
         * This sets up the List Items OnClickListener Event.
         * When a List Item gets clicked, it brings us to the Movie's Info Activity.
         */
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewMovie.class);
                intent.putExtra("passedMovieID", movies.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });

        /* Return the List Item */
        return rowView;
    }
}
