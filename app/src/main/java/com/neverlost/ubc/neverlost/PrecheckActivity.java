package com.neverlost.ubc.neverlost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.neverlost.ubc.neverlost.firebase.Authorization;

public class PrecheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precheck);

        if (Authorization.containsRealAuthorizationKeys()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
