package com.neverlost.ubc.neverlost.firebase;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.neverlost.ubc.neverlost.objects.Caretaker;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clarence on 2017-03-24.
 */

public class FirebaseQuery {


    public FirebaseQuery(){};

    public static Dependent getDependent(String name, DataSnapshot dataSnapshot){

        DataSnapshot user =  dataSnapshot.child(name);

        String               uname      = (String)      user.child("name").getValue();
        long                 age        = (long)        user.child("age").getValue();
        long              weight        = (long)        user.child("weight").getValue();
        long              height        = (long)        user.child("height").getValue();
        List<Long>        heartRates    = (List<Long>)  user.child("heartRates").getValue();
        List<Long>        distances     = (List<Long>)  user.child("distances").getValue();
        String            firebaseID     = (String)     user.child("firebaseID").getValue();


        return new Dependent(uname, age, weight, height, heartRates, distances, firebaseID);

    };

    public static Caretaker getCaretaker(String name, DataSnapshot dataSnapshot){

        DataSnapshot user =  dataSnapshot.child(name);

        String               uname       = (String)      user.child("name").getValue();
        long                 age         = (long)     user.child("age").getValue();
        long              weight         = (long)  user.child("weight").getValue();
        long              height         = (long)  user.child("height").getValue();
        List<String>      dependents     = (List<String>)  user.child("dependents").getValue();
        String            firebaseID     = (String)     user.child("firebaseID").getValue();

        if (dependents == null)
            return new Caretaker(uname, age, weight, height, new ArrayList<String>(),firebaseID);

        return new Caretaker(uname, age, weight, height, dependents,firebaseID);

    };

    public static void updateDependent(Dependent dependent){

        DatabaseReference userRef = FirebaseRef.dependentRer.child(Profile.getCurrentProfile().getId());

        userRef.child("age").setValue(dependent.age);
        userRef.child("weight").setValue(dependent.weight);
        userRef.child("height").setValue(dependent.height);
        userRef.child("heartRates").setValue(dependent.heartRates);
        userRef.child("distances").setValue(dependent.distances);
        userRef.child("firebaseID").setValue(dependent.firebaseID);
    }

    public static void updateCaretaker(Caretaker caretaker){

        DatabaseReference userRef = FirebaseRef.caretakerRer.child(Profile.getCurrentProfile().getId());

        userRef.child("age").setValue(caretaker.age);
        userRef.child("weight").setValue(caretaker.weight);
        userRef.child("height").setValue(caretaker.height);
        userRef.child("dependents").setValue(caretaker.dependents);
        userRef.child("firebaseID").setValue(caretaker.firebaseID);

    }


}
