package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.neverlost.ubc.neverlost.R;

public class FeatureSelectActivity extends AppCompatActivity {

    Button dependent;
    //Button caretaker;
    private Button userlistButton;
    //private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        dependent = (Button) findViewById(R.id.dependent);
        //caretaker = (Button) findViewById(R.id.caretaker);

        userlistButton = (Button) findViewById(R.id.userlist_button);
        //mapButton = (Button) findViewById(R.id.map_button);

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

        userlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, CloudUserActivity.class);
                startActivity(intent);
            }
        });

        /*
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        */
    }
}
