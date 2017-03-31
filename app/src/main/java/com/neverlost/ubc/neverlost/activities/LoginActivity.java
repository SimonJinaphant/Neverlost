package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
            Toast.makeText(this, "Please turn on location services before proceeding", Toast.LENGTH_SHORT).show();
        } else {
           // Intent i = new Intent(this, FeatureSelectActivity.class);
            Intent i = new Intent(this, BluetoothActivity.class);
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
