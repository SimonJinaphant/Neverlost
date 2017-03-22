package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthActivity extends AppCompatActivity {

    //demo hardcode
    Dependent demoUser;

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

        prolioPic = (ImageView) findViewById(R.id.prolioPic);
        hearRateButton = (ImageButton) findViewById(R.id.heartRateButton);
        hearRateValue = (TextView) findViewById(R.id.heartRateValue);
        name = (TextView) findViewById(R.id.name);
        stepButton = (ImageButton) findViewById(R.id.stepButton);
        distanceValue = (TextView) findViewById(R.id.distanceValue);
        healthRatingBar = (RatingBar) findViewById(R.id.healthRatingBar);
        healthEvaluation = (TextView) findViewById(R.id.healthEval);

        //demo hardcode part
        String demoName = "Logan";
        int demoAge = 22;
        double demoWeight = 68.5;
        double demoHeight = 175.0;
        List<HeartRateData> demoHeartRates = new ArrayList<>();
        List<DistanceData> demoDistanceData = new ArrayList<>();
        Date curDate = new Date();
        demoHeartRates.add(new HeartRateData(65, curDate));
        demoHeartRates.add(new HeartRateData(70, curDate));
        demoHeartRates.add(new HeartRateData(80, curDate));

        demoDistanceData.add(new DistanceData(10000, curDate));
        demoDistanceData.add(new DistanceData(10500, curDate));
        demoDistanceData.add(new DistanceData(98100, curDate));

        demoUser = new Dependent(demoName, demoAge, demoWeight, demoHeight, demoHeartRates, demoDistanceData);
        //end of demo hardcode part

        name.setText(demoUser.name);
        hearRateValue.setText(Integer.toString(demoUser.heartRates.get(0).heartRate));
        distanceValue.setText(Integer.toString(demoUser.distances.get(0).distance));

        //need an evaluation function
        healthRatingBar.setNumStars(5);
        healthRatingBar.setRating(3);
        healthRatingBar.setIsIndicator(true);

        healthEvaluation.setText("Good");

        //attached button activities
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepBar = new Intent(view.getContext(), DistanceBarActivity.class);
                startActivity(stepBar);
            }
        });

        hearRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                startActivity(heartRateGraph);
            }
        });

    }

}
