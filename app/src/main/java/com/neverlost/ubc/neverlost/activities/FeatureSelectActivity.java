package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.neverlost.ubc.neverlost.ConnectionListAdapter;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.models.readData;

public class FeatureSelectActivity extends AppCompatActivity {

    Button dependent;
    Button caretaker;

    private Button addUserButton;
    private Button identityButton;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        dependent = (Button) findViewById(R.id.dependent);
        caretaker = (Button) findViewById(R.id.caretaker);
        addUserButton = (Button) findViewById(R.id.add_person);
        identityButton = (Button) findViewById(R.id.identity);
        mapButton = (Button) findViewById(R.id.map);

        dependent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent depInt = new Intent(view.getContext(), HealthActivity.class);
                startActivity(depInt);

                //this button now goes to dependent health page
                //create gps button on that page if needed

                /*Intent gpsInt = new Intent(view.getContext(), MainActivity.class);
                gpsInt.putExtra("key","Logan");
                startActivity(gpsInt);
                */
            }
        });

        caretaker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent careInt = new Intent(view.getContext(), MonitorActivity.class);
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

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });

        identityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, IdentityActivity.class);
                startActivity(intent);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, ConnectionsActivity.class);
                startActivity(intent);
            }
        });
    }
}
