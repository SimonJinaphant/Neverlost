package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.objects.Dependent;

public class MonitorActivity extends AppCompatActivity {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("key");

        FirebaseRef.dependentRer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = (TextView) findViewById(R.id.depname);
                Dependent dependent = FirebaseQuery.getDependent(uid, dataSnapshot);

                name.setText(dependent.name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void prompt (View view){
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

}
