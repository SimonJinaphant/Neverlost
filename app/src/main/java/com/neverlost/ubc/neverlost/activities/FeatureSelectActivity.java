package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.neverlost.ubc.neverlost.R;

public class FeatureSelectActivity extends AppCompatActivity {

    Button dependent;
    Button userlistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_select);

        dependent = (Button) findViewById(R.id.dependent);
        userlistButton = (Button) findViewById(R.id.userlist_button);

        dependent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent depInt = new Intent(view.getContext(),BluetoothActivity.class);
                startActivity(depInt);
            }
        });

        userlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureSelectActivity.this, CloudUserActivity.class);
                startActivity(intent);
            }
        });

    }
}
