package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.neverlost.ubc.neverlost.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPersonActivity extends AppCompatActivity {

    private Button addCaretakerButton;
    private Button addDependentButton;
    private ImageView profilePicture;
    private TextView profileName;

    private IntentIntegrator qrScanIntent;
    private static final String TAG = "ADD_PERSON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        addCaretakerButton = (Button) findViewById(R.id.add_caretaker_button);
        addDependentButton = (Button) findViewById(R.id.add_dependent_button);
        profileName = (TextView) findViewById(R.id.profile_name_textview);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_imageview);

        qrScanIntent = new IntentIntegrator(this);
        qrScanIntent.initiateScan();

        addCaretakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addDependentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() != null) {

                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    final String username = obj.getString("name");
                    final String firebaseId = obj.getString("firebase_id");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileName.setText(username);
                        }
                    });

                    Log.d(TAG, username);
                    Log.d(TAG, firebaseId);
                    Toast.makeText(this, username + "\n\n" + firebaseId, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
