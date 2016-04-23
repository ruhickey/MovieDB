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

public class ItemListAdapter extends BaseAdapter {

    private List<IListItem> items = null;
    private static LayoutInflater inflater = null;
    private MainActivity context;

    public ItemListAdapter(MainActivity _main)
    {
        context = _main;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMovies(List<IListItem> _items)
    {
        items = _items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * This method gets called for every item in movies.
     * It gathers the information from Movie and creates a ListItem out of it.
     * It then returns the List item.
     * We don't call this function, it automatically gets called.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return items.get(position).getRowView(context, inflater);
    }
}
