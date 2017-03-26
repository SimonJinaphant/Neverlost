package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.algorithm.HealthAlgorithm;
import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthActivity extends AppCompatActivity {


    //declare the elements in the xmls
    ImageView prolioPic;
    ImageButton hearRateButton;
    TextView hearRateValue;
    TextView name;
    TextView distanceValue;
    ImageButton stepButton;
    RatingBar healthRatingBar;
    TextView healthEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        Intent intent = getIntent();
        final String uname = intent.getStringExtra("key");

        prolioPic = (ImageView) findViewById(R.id.prolioPic);
        hearRateButton = (ImageButton) findViewById(R.id.heartRateButton);
        hearRateValue = (TextView) findViewById(R.id.heartRateValue);
        name = (TextView) findViewById(R.id.name);
        stepButton = (ImageButton) findViewById(R.id.stepButton);
        distanceValue = (TextView) findViewById(R.id.distanceValue);
        healthRatingBar = (RatingBar) findViewById(R.id.healthRatingBar);
        healthEvaluation = (TextView) findViewById(R.id.healthEval);

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(uname, dataSnapshot);


                name.setText(dependent.name);

                //todo: start the heartrate measurement here and get the GPS data
                int newHeartrateReading = 0;
                int distanceTraveled = 0;

                boolean isHeartrateNormal = HealthAlgorithm.IsHeartRateAbnormal(dependent, newHeartrateReading);

                hearRateValue.setText(Integer.toString(newHeartrateReading));
                distanceValue.setText(Integer.toString(distanceTraveled));

                //todo:need an evaluation function
                int num_star  = 3;
                healthRatingBar.setNumStars(5);
                healthRatingBar.setRating(num_star);
                healthRatingBar.setIsIndicator(true);

                //scale
                if(num_star< 3){
                    healthEvaluation.setText("NEED IMPROVEMENT");
                }else {
                    healthEvaluation.setText("GOOD");
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attached button activities
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepBar = new Intent(view.getContext(), DistanceBarActivity.class);
                stepBar.putExtra("key", uname);
                startActivity(stepBar);
            }
        });

        hearRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "I am here");
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                heartRateGraph.putExtra("key", uname);
                startActivity(heartRateGraph);
            }
        });

    }

}
