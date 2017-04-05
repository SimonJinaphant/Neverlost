package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.Authorization;

public class PrecheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precheck);

        if (Authorization.containsRealAuthorizationKeys()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
    }
}
