package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.algorithm.HealthAlgorithm;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MonitorActivity extends AppCompatActivity {

    TextView name;
    private Button safetyPromptButton;
    TextView heartRateValue;
    Button heartRateButton;
    RatingBar healthRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("facebook_id");
        final String firebaseId = intent.getStringExtra("firebase_id");

        safetyPromptButton = (Button) findViewById(R.id.safety);
        safetyPromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagingService.sendSafetyPrompt(
                        firebaseId,
                        FirebaseInstanceId.getInstance().getToken(),
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                displayMessage("Failed to send safety prompt");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    displayMessage("Prompt has been sent!");
                                } else {
                                    displayMessage("panic: I don't know how to handle this!");
                                }
                            }
                        });
            }
        });

        name = (TextView) findViewById(R.id.depname);
        heartRateValue = (TextView) findViewById(R.id.heartRateValue);
        heartRateButton = (Button) findViewById(R.id.heartRateButton);
        healthRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        FirebaseRef.dependentRer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Dependent dependent = FirebaseQuery.getDependent(uid, dataSnapshot);

                name.setText(dependent.name);
                heartRateValue.setText(String.valueOf(dependent.heartRates.get(0)));
                int num_star = HealthAlgorithm.healthEvaluate(dependent);
                healthRatingBar.setNumStars(5);
                healthRatingBar.setRating(num_star);
                healthRatingBar.setIsIndicator(true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        heartRateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                heartRateGraph.putExtra("key", uid);
                startActivity(heartRateGraph);
            }
        });
    }

    public void prompt(View view) {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

    /**
     * Helper function to display a Toast message without cluttering the codebase.
     *
     * @param message - The message to print on the Toast message.
     */
    private void displayMessage(@NonNull final String message) {
        // Launch the Toast on a UI thread to prevent it from crashing.
        MonitorActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MonitorActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
