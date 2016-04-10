package ie.thecoolkids.moviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewPerson extends AppCompatActivity {

    Person person;
    RelativeLayout mainPage;
    ImageView poster;
    TextView name, homepage, placeOfBirth, otherNames, otherNamesHeading, birthday, deathday, deathdayHeader, biography;
    Bitmap bitmap1;
    URL url1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person);
        String jsonMyObject="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("passedPerson");
        }
        person = new Gson().fromJson(jsonMyObject, Person.class);
        init(person);
    }

    private void init(Person person){

        name = (TextView) findViewById(R.id.name);
        homepage = (TextView) findViewById(R.id.homepage);
        placeOfBirth = (TextView) findViewById(R.id.placeOfBirth);
        otherNames = (TextView) findViewById(R.id.otherNames);
        otherNamesHeading = (TextView) findViewById(R.id.otherNamesHeading);
        birthday = (TextView) findViewById(R.id.birthday);
        deathday = (TextView) findViewById(R.id.deathday);
        deathdayHeader = (TextView) findViewById(R.id.deathdayHeader);
        biography = (TextView) findViewById(R.id.biography);
        poster = (ImageView) findViewById(R.id.poster);
        mainPage = (RelativeLayout) findViewById(R.id.mainpage);

        name.setText(person.getName());
        homepage.setText(person.getHomepage());
        placeOfBirth.setText(person.getPlaceOfBirth());
        birthday.setText("" + person.getBirthDay());
        biography.setText(person.getBiography());

        if(!person.getDeathDay().equals("")){
            deathdayHeader.setText("Death Day:");
            deathday.setText("" + person.getDeathDay());
        }
        if(person.getOtherNames().size()>0) {
            otherNamesHeading.setText("Also goes by:");
            otherNames.setText(createAlsoKnownAs(person.getOtherNames()));
        }
        if(!person.getPersonPicture().equals(null)) {
            setImages();
        }
    }

    private void setImages() {
        Picasso.with(this).load(person.getPersonPicture()).into(poster);
        try {
            url1 = new URL(person.getPersonPicture());
            bitmap1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            Drawable dr = new BitmapDrawable(getResources(), bitmap1);
            mainPage.setBackground(dr);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.w("ViewPerson", "Exception creating image");
        }
    }

    private String createAlsoKnownAs(List<String> otherNames) {
        String otherNamesString="";
        otherNamesString = otherNames.get(0);
        for (int i = 1; i < otherNames.size(); i++) {
            otherNamesString += " / " +  otherNames.get(i);
        }
        return otherNamesString;
    }


}

