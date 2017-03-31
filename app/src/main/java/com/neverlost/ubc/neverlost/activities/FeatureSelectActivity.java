package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.neverlost.ubc.neverlost.R;

public class FeatureSelectActivity extends AppCompatActivity {

    Button dependent;
    Button caretaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        dependent = (Button) findViewById(R.id.dependent);
        caretaker = (Button) findViewById(R.id.caretaker);

        dependent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent depInt = new Intent(view.getContext(), HealthActivity.class);
                startActivity(depInt);
            }
        });

        caretaker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent careInt = new Intent(view.getContext(), MonitorActivity.class);
                startActivity(careInt);
            }
        });
    }
}
