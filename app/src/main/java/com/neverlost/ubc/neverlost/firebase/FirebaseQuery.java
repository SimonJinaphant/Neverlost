package com.neverlost.ubc.neverlost.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;
import com.neverlost.ubc.neverlost.objects.Caretaker;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.List;

/**
 * Created by clarence on 2017-03-24.
 */

public class FirebaseQuery {


    public FirebaseQuery(){};

    public static Dependent getDependent(String name, DataSnapshot dataSnapshot){

        DataSnapshot user =  dataSnapshot.child(name);

        long                 age        = (long)     user.child("age").getValue();
        long              weight        = (long)  user.child("weight").getValue();
        long              height        = (long)  user.child("height").getValue();
        List<Long> heartRates        = (List<Long>) user.child("heartRates").getValue();
        List<DistanceData>  distances   = (List<DistanceData>)  user.child("distances").getValue();

        return new Dependent(name, age, weight, height, heartRates, distances);

    };

    public static Caretaker getCaretaker(String name, DataSnapshot dataSnapshot){

        DataSnapshot user =  dataSnapshot.child(name);

        long                 age         = (long)     user.child("age").getValue();
        long              weight         = (long)  user.child("weight").getValue();
        long              height         = (long)  user.child("height").getValue();

        return new Caretaker(name, age, weight, height);

    };

    public static void updateDependent(Dependent dependent){

        DatabaseReference userRef = FirebaseRef.dependentRer.child(dependent.name);

        userRef.child("age").setValue(dependent.age);
        userRef.child("weight").setValue(dependent.weight);
        userRef.child("height").setValue(dependent.height);
        userRef.child("heartRates").setValue(dependent.heartRates);
        userRef.child("distances").setValue(dependent.distances);
    }

    public static void updateCaretaker(Caretaker caretaker){

        DatabaseReference userRef = FirebaseRef.dependentRer.child(caretaker.name);

        userRef.child("age").setValue(caretaker.age);
        userRef.child("weight").setValue(caretaker.weight);
        userRef.child("height").setValue(caretaker.height);

    }


}
