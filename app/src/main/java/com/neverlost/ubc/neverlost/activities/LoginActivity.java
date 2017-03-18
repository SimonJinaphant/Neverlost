package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.neverlost.ubc.neverlost.R;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void goToApp(View view) {
        int location = locationOn();
        if (location == 0) {
            TextView t = (TextView) findViewById(R.id.locationOn);
            t.setText("Please turn on location services before proceeding");
            t.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            Intent i = new Intent(this, FeatureSelectActivity.class);
            startActivity(i);
        }

    }

    private int locationOn() {
        int off = 0;
        try {
            off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return off;
    }


}
