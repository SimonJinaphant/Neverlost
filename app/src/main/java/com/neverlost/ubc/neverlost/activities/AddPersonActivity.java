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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPersonActivity extends AppCompatActivity {

    // Activity Constants
    private static final String TAG = "ADD_PERSON";

    // Facebook URI
    private static final String FACEBOOK_URI_BASE = "https://graph.facebook.com/";
    private static final String FACEBOOK_URI_ENDPOINT_PICTURE = "/picture?height=400&width=400&migration_overrides=%7Boctober_2012%3Atrue%7D";

    // Activity UI elements
    private IntentIntegrator qrScanIntent;
    private Button addCaretakerButton;
    private Button addDependentButton;
    private ImageView profilePicture;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        addCaretakerButton = (Button) findViewById(R.id.add_caretaker_button);
        addDependentButton = (Button) findViewById(R.id.add_dependent_button);
        profileName = (TextView) findViewById(R.id.profile_name_textview);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_imageview);

        // Launch the QR Scanner task to obtain the QR values.
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

    /**
     * The callback when the QR scanner finishes
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                try {

                    // Deserialize the JSON information obtained.
                    JSONObject obj = new JSONObject(result.getContents());
                    final String username = obj.getString("name");
                    final String firebaseId = obj.getString("firebase_id");
                    final String facebookId = obj.getString("facebook_id");

                    // Update the UI with the deserialize data.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileName.setText(username);

                            Picasso.with(AddPersonActivity.this)
                                    .load(FACEBOOK_URI_BASE + facebookId + FACEBOOK_URI_ENDPOINT_PICTURE)
                                    .placeholder(R.drawable.ic_person_add_black_96dp)
                                    .into(profilePicture);
                        }
                    });

                    Log.d(TAG, username);
                    Log.d(TAG, firebaseId);
                    Log.d(TAG, facebookId);
                    Log.d(TAG, FACEBOOK_URI_BASE + facebookId + FACEBOOK_URI_ENDPOINT_PICTURE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "Unable to decode QR", Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
