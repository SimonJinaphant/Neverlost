package com.neverlost.ubc.neverlost.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by clarence on 2017-03-24.
 */

public class FirebaseRef {

    final static public FirebaseDatabase db =  FirebaseDatabase.getInstance();
    final static public DatabaseReference userRef = db.getReference("user");
    final static public DatabaseReference dependentRer = userRef.child("dependent");
    final static public DatabaseReference caretakerRer = userRef.child("caretaker");

}
