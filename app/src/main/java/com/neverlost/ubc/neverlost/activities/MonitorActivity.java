package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    TextView heartRateValue;
    Button   heartRateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("key");

        name = (TextView) findViewById(R.id.depname);
        heartRateValue = (TextView) findViewById(R.id.heartRateValue);
        heartRateButton = (Button) findViewById(R.id.heartRateButton);

        FirebaseRef.dependentRer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Dependent dependent = FirebaseQuery.getDependent(uid, dataSnapshot);

                name.setText(dependent.name);
                heartRateValue.setText(String.valueOf(dependent.heartRates.get(0)));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        heartRateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                heartRateGraph.putExtra("key", uid);
                startActivity(heartRateGraph);
            }
        });
    }

    public void prompt (View view){
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

}
