package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.Profile;
import com.neverlost.ubc.neverlost.R;

public class FeatureSelectActivity extends AppCompatActivity {

    Button dependent;
    Button caretaker;
    private Button userlistButton;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        dependent = (Button) findViewById(R.id.dependent);
        caretaker = (Button) findViewById(R.id.caretaker);

        userlistButton = (Button) findViewById(R.id.userlist_button);
        mapButton = (Button) findViewById(R.id.map_button);

        dependent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent depInt = new Intent(view.getContext(),BluetoothActivity.class);
                startActivity(depInt);

                //this button now goes to dependent health page
                //create gps button on that page if needed

                /*Intent gpsInt = new Intent(view.getContext(), MainActivity.class);
                /*Intent gpsInt = new Intent(view.getContext(), MapActivity.class);
                gpsInt.putExtra("key","Logan");
                startActivity(gpsInt);
                */
            }
        });

        caretaker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent careInt = new Intent(view.getContext(), MonitorActivity.class);


                //TODO: add dependent id to caretaker userclass
                //link to fbID of dependent
                careInt.putExtra("key", /*change this to dependent id*/ Profile.getCurrentProfile().getId());

                startActivity(careInt);

                //this button now goes to caretaker health monitor page
                //create gps button on that page if needed

                /*
                Intent healthInt = new Intent(view.getContext(), HealthActivity.class);
                healthInt.putExtra("key","Logan");
                startActivity(healthInt);
                */
            }
        });

        userlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, CloudUserActivity.class);
                startActivity(intent);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
