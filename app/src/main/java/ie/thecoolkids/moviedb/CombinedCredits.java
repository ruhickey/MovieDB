package ie.thecoolkids.moviedb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CombinedCredits implements Serializable {

    private Role[] cast;
    private Role[] crew;
    private int id;


    public int getID() {
        return id;
    }

    public List<Role> getCast() {
        List<Role> role_list = new ArrayList<Role>();
        for(int i=0; i<cast.length; i++){
            role_list.add(cast[i]);
        }
        return role_list;
    }

    public List<Role> getCrew() {
        List<Role> role_list = new ArrayList<Role>();
        for(int i=0; i<crew.length; i++){
            role_list.add(crew[i]);
        }
        return role_list;
    }

    public List<Role> getCastAndCrew(){
        List<Role> cclist = new ArrayList<Role>();
        for(int i=0; i<crew.length; i++){
            cclist.add(crew[i]);
        }
        for(int i=0; i<cast.length; i++){
            cclist.add(cast[i]);
        }
        return cclist;
    }

}
