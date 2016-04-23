package ie.thecoolkids.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ViewRoles extends BaseActivity implements IParser {

    Person person;
    ImageView poster;
    CombinedCredits personCredits;
    int personID;
    List roles;
    ListView lvRoles;
    CreditsListAdapter creditsListAdapter;
    ViewRoles context;
    TextView rolesTitle;
    String personName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_roles);
        context = this;

        Log.d("IN VIEW ROLLS", "");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personID = extras.getInt("passedID");
            personName = extras.getString("passedPersonName");
        }

        rolesTitle = (TextView) findViewById(R.id.rolesTitle);
        rolesTitle.setText("Roles for " + personName);

        new ApiHelper(this).SetPersonIDCreditsQuery(personID).execute();

    }


    private void setImages() {
        Picasso.with(this).load(person.getPersonPicture()).into(poster);
    }


    private void initializeCredits(){
        roles = personCredits.getCastAndCrew();
        lvRoles = (ListView) findViewById(R.id.lvRoles);
        creditsListAdapter = new CreditsListAdapter(context);
        creditsListAdapter.setRoles(roles);
        lvRoles.setAdapter(creditsListAdapter);

    }

    public void parseJson(String json) {
        try {
            Gson gson = new Gson();
            personCredits = gson.fromJson(json, CombinedCredits.class);
            initializeCredits();
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

