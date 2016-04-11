package ie.thecoolkids.moviedb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ViewPerson extends AppCompatActivity {

    public enum StartFin {
        STATE1, STATE2
    }

    private enum State {
        SEARCH,
        UPDATE
    }


    //todo: UNTESTED!!!!!!!!!!!!. No idea if any of this works at all.



    Person person;
    RelativeLayout mainPage;
    ImageView poster;
    TextView name, homepage, homepageHeading, placeOfBirth, otherNames, otherNamesHeading, birthday, deathday, deathdayHeader, biography;
    Bitmap bitmap1;
    CombinedCredits personCredits;
    URL url1;
    int personID;
    StartFin state;
    List roles;
    ListView lvRoles;
    CreditsListAdapter creditsListAdapter;
    ViewPerson context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person);
         context = this;
        state = StartFin.STATE1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personID = extras.getInt("passedPersonID");
        }
        new ApiHelper(this).SetPersonIDQuery(personID).execute();


    }

    private void init(){

        name = (TextView) findViewById(R.id.name);
        homepage = (TextView) findViewById(R.id.homepage);
        homepageHeading = (TextView) findViewById(R.id.homepageHeader);
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
        placeOfBirth.setText(person.getPlaceOfBirth());
        birthday.setText("" + person.getBirthDay());
        biography.setText(person.getBiography());

        if(!person.getHomepage().equals("")){
            homepageHeading.setText("Homepage: ");
            homepage.setText(person.getHomepage());
        }

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

        state = StartFin.STATE2;

        new ApiHelper(this).SetPersonIDCreditsQuery(personID).execute();

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

    private void initializeCredits(){
        roles = personCredits.getCastAndCrew();
        lvRoles = (ListView) findViewById(R.id.lvRoles); //name of id
        creditsListAdapter = new CreditsListAdapter(context);
        creditsListAdapter.setRoles(roles);
        lvRoles.setAdapter(creditsListAdapter);
        creditsListAdapter.setRoles(roles);

    }


    public void parseJson(String json) {
        try {
            if(state == StartFin.STATE1) {
                Gson gson = new Gson();
                person = gson.fromJson(json, Person.class);
                init();
            }
            else{
                Gson gson = new Gson();
                personCredits = gson.fromJson(json, CombinedCredits.class);
                initializeCredits();
            }
        }
        catch(Exception ex){
            if(ex == null) {
                Log.d("EXCEPTION", "NULL");
            } else if (ex.getMessage() == null){
                ex.printStackTrace();
            } else {
                Log.d("EXCEPTION", ex.getMessage());
            }
        }

    }


}

