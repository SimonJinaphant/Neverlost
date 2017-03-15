package com.neverlost.ubc.neverlost.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.neverlost.ubc.neverlost.R;

public class HealthMonitorActivity extends AppCompatActivity {

    //declare the elements in the xmls
    ImageView   prolioPic       = (ImageView) findViewById(R.id.prolioPic);
    ImageButton hearRate        = (ImageButton) findViewById(R.id.heartRateButton);
    TextView    hearRateValue   = (TextView) findViewById(R.id.heartRateValue);
    TextView    name            = (TextView) findViewById(R.id.name);
    TextView    age             = (TextView) findViewById(R.id.age);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_minitor_layout);


    }


}