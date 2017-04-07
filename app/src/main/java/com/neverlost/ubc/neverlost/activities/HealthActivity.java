package com.neverlost.ubc.neverlost.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.algorithm.HealthAlgorithm;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.models.readData;
import com.neverlost.ubc.neverlost.objects.Coordinate;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.io.IOException;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HealthActivity extends AppCompatActivity {


    //declare the elements in the xmls
    ImageView prolioPic;
    Button hearRateButton;
    TextView hearRateValue;
    TextView name;
    TextView distanceValue;
    Button stepButton;
    RatingBar healthRatingBar;
    TextView healthEvaluation;
    TextView bmrValue;
    private BroadcastReceiver caretakerPromptReciever;
    private final long[] vibrationPattern = {0, 400, 100, 400, 100, 400};

    // Vibration to alert the caretaker that something has happened to their dependant
    private Vibrator vibrationService;

    Dependent dependent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("key");

        prolioPic = (ImageView) findViewById(R.id.prolioPic);
        hearRateButton = (Button) findViewById(R.id.heartRateButton);
        hearRateValue = (TextView) findViewById(R.id.heartRateValue);
        name = (TextView) findViewById(R.id.name);
        stepButton = (Button) findViewById(R.id.stepButton);
        distanceValue = (TextView) findViewById(R.id.distanceValue);
        healthRatingBar = (RatingBar) findViewById(R.id.healthRatingBar);
        healthEvaluation = (TextView) findViewById(R.id.healthEval);
        bmrValue = (TextView) findViewById(R.id.bmrValue);
        // -----------------------------------------------------------------------------------------
        // Obtain access to the phone's vibration services to alert the user of incoming messages
        // -----------------------------------------------------------------------------------------
        if (vibrationService == null) {
            vibrationService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        caretakerPromptReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String caretakerName = intent.getStringExtra(MessagingService.FCM_DATA_CARETAKER_NAME);
                final String caretakerId = intent.getStringExtra(MessagingService.FCM_DATA_CARETAKER_ID);

                final AlertDialog alertDialog = new AlertDialog.Builder(HealthActivity.this).create();
                alertDialog.setTitle("Safety prompt");
                alertDialog.setMessage(caretakerName + " wants to see if you're safe. \n You have 10 seconds to reply...");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "I am Safe",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MessagingService.respondSafetyPrompt(caretakerId, true, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        displayMessage("Neverlost failed to reply to the caretaker");

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            displayMessage("Safety reply sent!");
                                        } else {
                                            displayMessage("panic: I don't know how to handle this!");
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                // Hide after some seconds
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                            displayMessage("Failed to respond in time");
                        }
                    }
                };

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 10000);

                vibrationService.vibrate(vibrationPattern, -1);
            }
        };

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dependent = FirebaseQuery.getDependent(uid, dataSnapshot);

                name.setText(dependent.name);

                int bmr = (int) HealthAlgorithm.computeBMR_male(dependent);

                long distanceTraveled = dependent.distances.get(0);

                Log.d("BMR", String.valueOf(bmr));
                bmrValue.setText(String.valueOf(bmr));
                distanceValue.setText(String.valueOf(distanceTraveled));

                int num_star = HealthAlgorithm.healthEvaluate(dependent);
                healthRatingBar.setNumStars(5);
                healthRatingBar.setRating(num_star);
                healthRatingBar.setIsIndicator(true);

                //scale
                if (num_star < 3) {
                    healthEvaluation.setText("NEED IMPROVEMENT");
                } else {
                    healthEvaluation.setText("GOOD");
                }

                //FirebaseQuery.updateDependent(dependent);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attached button activities
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stepBar = new Intent(view.getContext(), DistanceBarActivity.class);
                stepBar.putExtra("key", uid);
                startActivity(stepBar);
            }
        });

        hearRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent heartRateGraph = new Intent(view.getContext(), HeartRateActivity.class);
                heartRateGraph.putExtra("key", uid);
                startActivity(heartRateGraph);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(caretakerPromptReciever,
                        new IntentFilter(MessagingService.NCM_PROMPT_REQUEST)
                );
    }

    @Override
    protected void onStop() {

        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(caretakerPromptReciever);

        super.onStop();
    }

    /**
     * Helper function to display a Toast message without cluttering the codebase.
     *
     * @param message - The message to print on the Toast message.
     */
    private void displayMessage(@NonNull final String message) {
        // Launch the Toast on a UI thread to prevent it from crashing.
        HealthActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HealthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class BlueToothData extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPostExecute(Integer reading) {
            super.onPostExecute(reading);

            hearRateValue.setText(Integer.toString(reading));
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            readData dataReader = new readData();
            int sum = 0;
            for (int i = 0; i < 3; i++) {
                sum += dataReader.getHRData();
            }

            Coordinate curLoc = dataReader.getGPSData();
            if (curLoc == null) {
                curLoc = new Coordinate((float) 1000.0, (float) 1000.0);
            }

            int newHeartrateReading = sum / 3;

            Collections.reverse(dependent.heartRates);
            dependent.heartRates.add((long) newHeartrateReading);
            Collections.reverse(dependent.heartRates);

            boolean isHeartrateNormal = HealthAlgorithm.IsHeartRateAbnormal(dependent, newHeartrateReading);

            if (!isHeartrateNormal) {
                MessagingService.broadcastForHelpHP(curLoc, dependent.name, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        displayMessage("Neverlost failed to send help over the network; good luck...");

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            displayMessage("Help is on the way!");
                        } else {
                            displayMessage("panic: I don't know how to handle this!");
                        }
                    }
                });


            }

            return newHeartrateReading;
        }
    }

}
