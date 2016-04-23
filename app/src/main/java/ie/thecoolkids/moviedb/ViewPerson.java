package ie.thecoolkids.moviedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPerson extends BaseActivity implements IParser {

    Person person;
    RelativeLayout mainPage;
    ImageView poster;
    TextView name, homepage, homepageHeading, placeOfBirth, otherNames, otherNamesHeading, birthday, deathday, deathdayHeader, biography;
    LinearLayout deathdayLine, otherNamesLine, homepageLine;
    Button rolesButton;
    int personID;
    ViewPerson context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person);
         context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personID = extras.getInt("passedPersonID");
            Log.d("Person id", "" + personID);
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
        rolesButton = (Button) findViewById(R.id.rolesButton);
        deathdayLine = (LinearLayout) findViewById(R.id.deathdayLine);
        otherNamesLine = (LinearLayout) findViewById(R.id.otherNamesLine);
        homepageLine = (LinearLayout) findViewById(R.id.homepageLine);

        name.setText(person.getName());
        placeOfBirth.setText(person.getPlaceOfBirth());
        birthday.setText("" + person.getBirthDay());
        biography.setText(person.getBiography());

        if(!person.getHomepage().equals("")){
            homepageHeading.setText("Homepage: ");
            homepage.setText(person.getHomepage());
            homepageLine.setVisibility(View.VISIBLE);
        }

        if(!person.getDeathDay().equals("")){
            deathdayHeader.setText("Death Day:");
            deathday.setText("" + person.getDeathDay());
            deathdayLine.setVisibility(View.VISIBLE);
        }
        if(person.getOtherNames().size()>0) {
            otherNamesHeading.setText("Also goes by:");
            otherNames.setText(createAlsoKnownAs(person.getOtherNames()));
            otherNamesLine.setVisibility(View.VISIBLE);
        }
        if(!person.getPersonPicture().equals(null)) {
            setImages();
        }


        rolesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewRoles.class);
                intent.putExtra("passedPersonID", person.getId());
                intent.putExtra("passedPersonName", person.getName());
                v.getContext().startActivity(intent);


            }
        });

    }

    private void setImages() {
        Picasso.with(this).load(person.getPersonPicture()).into(poster);
    }

    private String createAlsoKnownAs(List<String> otherNames) {
        String otherNamesString="";
        otherNamesString = otherNames.get(0);
        for (int i = 1; i < otherNames.size(); i++) {
            otherNamesString += " / " +  otherNames.get(i);
        }
        return otherNamesString;
    }



    public void parseJson(String json) {
        try {
            Gson gson = new Gson();
            person = gson.fromJson(json, Person.class);
            init();
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

