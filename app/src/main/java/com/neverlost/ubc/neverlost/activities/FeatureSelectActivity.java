package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.neverlost.ubc.neverlost.R;

public class FeatureSelectActivity extends AppCompatActivity {

    Button GPS;
    Button Health;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        GPS = (Button) findViewById(R.id.gps);
        Health = (Button) findViewById(R.id.health);

        GPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent gpsInt = new Intent(view.getContext(), MainActivity.class);
                startActivity(gpsInt);
            }
        });

        Health.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent healthInt = new Intent(view.getContext(), HealthActivity.class);
                startActivity(healthInt);
            }
        });
    }
}
