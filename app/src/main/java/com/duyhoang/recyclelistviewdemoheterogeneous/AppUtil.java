package com.duyhoang.recyclelistviewdemoheterogeneous;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rogerh on 7/26/2018.
 */

public class AppUtil {

    String[] name;
    String[] lastName;
    String[] nationality;
    String[] gender;
    List<Person> personList;
    List<Advertisement> adList;
    List<Object> catalogue;
    Context context;

    String[] nationalities;
    String[] adMessage;

    public AppUtil(Context context){
        this.context = context;
        name = context.getResources().getStringArray(R.array.names);
        lastName = context.getResources().getStringArray(R.array.lastnames);
        nationality = context.getResources().getStringArray(R.array.nationality);
        gender = context.getResources().getStringArray(R.array.gender);
        adMessage = context.getResources().getStringArray(R.array.advertisementMessages);


        personList = new ArrayList<Person>();
        Person p;
        Person.GENDER g;
        for(int i = 0; i < name.length; i++){
            g = gender[i].equals("male") ? Person.GENDER.male: Person.GENDER.female;
            p = new Person(name[i], lastName[i], g, nationality[i]);
            personList.add(p);
        }

        adList = new ArrayList<Advertisement>();
        for(String msg : adMessage){
            adList.add(new Advertisement(msg));
        }

        createCataglog();
    }

    public List<Person> getPersonList(){
        return personList;
    }


    public static boolean isStringEmpty(String string){
        if(string == null)
            return true;
        else if(string.equals(" ") || string.length() == 0)
            return true;

        return false;
    }


    public int getUniqueNationlityBySelectedIndex(String value){
        nationalities = context.getResources().getStringArray(R.array.nationalities);
        for(int i = 0; i < nationalities.length; i++){
            if(nationalities[i].contains(value)){
                return i;
            }
        }
        return -1;
    }

    public void createCataglog(){
        catalogue = new ArrayList<>();
        catalogue.addAll(adList);
        catalogue.addAll(personList);
        Collections.shuffle(catalogue);
    }



    public List<Object> getCatalogue(){
        return catalogue;
    }


}
