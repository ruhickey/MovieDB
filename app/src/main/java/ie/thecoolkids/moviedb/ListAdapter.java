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

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends BaseAdapter{

    private List<TheMovieDB> list = null;
    private static LayoutInflater inflater = null;
    private Context context;

    ListAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setContent(List<TheMovieDB> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.movie_list, null);

        holder.imageView = (ImageView) rowView.findViewById(R.id.imgPoster);
        holder.textView = (TextView) rowView.findViewById(R.id.tvTitle);
        holder.ratingBar = (RatingBar) rowView.findViewById(R.id.ratBar);

        holder.textView.setText(list.get(position).getTitle());
        holder.ratingBar.setRating((list.get(position).getRating() / 2));


        Picasso.with(context).load(R.drawable.movies).fit().into(holder.imageView);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewMovie.class);
                intent.putExtra("passedID", (int)list.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

    class Holder{
        ImageView imageView;
        TextView textView;
        RatingBar ratingBar;
    }
}
