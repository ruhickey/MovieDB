package ie.thecoolkids.moviedb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable, IListItem {

    /* This is the URL from where we get the images. */
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185";



    private boolean adult;
    private String[] also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private int id;
    private String name;
    private String place_of_birth;
    private String profile_path;


    private Movie[] known_for;


    Person(){}

    public boolean isAdult(){
        return adult;
    }

    public List<String> getOtherNames() {
        List<String> names_list = new ArrayList<>();
        for(int i=0; i< also_known_as.length ; i++){
            names_list.add(also_known_as[i]);
        }
        return names_list;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthDay() {
        return birthday;
    }

    public String getDeathDay() {
        return deathday;
    }

    public String getHomepage(){
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfBirth(){
        return place_of_birth;
    }

    public String getPersonPicture(){
        if(profile_path != null)    return String.format("%s%s", BASE_URL, profile_path);
        else                        return null;
    }


    public List<Movie> getKnownFor() {
        List<Movie> movies_list = new ArrayList<>();
        for(int i=0; i< known_for.length ; i++){
            movies_list.add(known_for[i]);
        }
        return movies_list;
    }

    public View getRowView(Context context, LayoutInflater inflater) {
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
        holder.tvTitle.setText(this.getName());
        holder.ratBar.setVisibility(View.INVISIBLE);

        /*
         * This is where we load in the image.
         * Use Picasso's Placeholder to put a Stock image in the Movie Poster first.
         */
        Picasso.with(context).load(this.getPersonPicture()).placeholder(R.drawable.movies).fit().into(holder.imgPoster);

        /*
         * This sets up the List Items OnClickListener Event.
         * When a List Item gets clicked, it brings us to the Movie's Info Activity.
         */
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewPerson.class);
                intent.putExtra("passedID", getId());
                v.getContext().startActivity(intent);
            }
        });

        /* Return the List Item */
        return rowView;
    }
}
