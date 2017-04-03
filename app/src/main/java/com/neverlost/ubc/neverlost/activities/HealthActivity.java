package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.models.readData;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    TextView bmrValue;

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
        bmrValue = (TextView) findViewById(R.id.bmrValue);

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(uname, dataSnapshot);


                name.setText(dependent.name);

                int bmr = (int) HealthAlgorithm.computeBMR_male(dependent);

                readData dataReader = new readData();
                int sum=0;
                for(int i=0; i<10; i++){
                    sum += dataReader.getHRData();
                }

                //todo: get the GPS data and compute distance traveled

                Location curLoc = new Location();

                int newHeartrateReading = sum/10;
                int distanceTraveled = 0;

                boolean isHeartrateNormal = HealthAlgorithm.IsHeartRateAbnormal(dependent, newHeartrateReading);

                bmrValue.setText(Integer.toString(bmr));
                hearRateValue.setText(Integer.toString(newHeartrateReading));
                distanceValue.setText(Integer.toString(distanceTraveled));

                //todo:need an evaluation function
                int num_star  = HealthAlgorithm.healthEvaluate(dependent);
                healthRatingBar.setNumStars(5);
                healthRatingBar.setRating(num_star);
                healthRatingBar.setIsIndicator(true);

                //scale
                if(num_star< 3){
                    healthEvaluation.setText("NEED IMPROVEMENT");
                }else {
                    healthEvaluation.setText("GOOD");
                }

                if(!isHeartrateNormal){
                    MessagingService.broadcastForHelp(currentLocation, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            displayMessage("Neverlost failed to send help over the network; good luck...");

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                displayMessage("Help is on the way!");
                            } else {
                                displayMessage("panic: I don't know how to handle this!");
                            }
                        }
                    });

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
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                heartRateGraph.putExtra("key", uname);
                startActivity(heartRateGraph);
            }
        });

    }

    /**
     * Helper function to display a Toast message without cluttering the codebase.
     *
     * @param message - The message to print on the Toast message.
     */
    private void displayMessage(@NonNull final String message) {
        // Launch the Toast on a UI thread to prevent it from crashing.
        HealthActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HealthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
