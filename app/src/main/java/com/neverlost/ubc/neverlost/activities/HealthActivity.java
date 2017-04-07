package com.neverlost.ubc.neverlost.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    // Vibration to alert the caretaker that something has happened to their dependant
    private Vibrator vibrationService;

    private Button panicButton;
    Dependent dependent;
    readData dataReader;
    private final long[] vibrationPattern = {0, 400, 100, 400, 100, 400};
    private static final double dlat = 49.261581;
    private static final double dlng = -123.249722;
    // Force the Location manager to update our GPS location when the following thresholds are met

    private static final String TAG = "HEALTH";

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
        panicButton = (Button) findViewById(R.id.panic);


        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dependent = FirebaseQuery.getDependent(uid, dataSnapshot);

                name.setText(dependent.name);

                int bmr = (int) HealthAlgorithm.computeBMR_male(dependent);

                long distanceTraveled = dependent.distances.get(0);

                bmrValue.setText(String.valueOf(bmr));
                distanceValue.setText(String.valueOf(distanceTraveled));

//                Coordinate curLoc = dataReader.getGPSData();
//                if (curLoc == null) {
//                    curLoc = new Coordinate((float)dlat,(float)dlng);
//
//                }

//                int newHeartrateReading = dataReader.getHRData();
//
//                hearRateValue.setText(Integer.toString(newHeartrateReading));
//
//                boolean isHeartrateNormal = HealthAlgorithm.IsHeartRateAbnormal(dependent, newHeartrateReading);
//
//                if (!isHeartrateNormal) {
//                    MessagingService.broadcastForHelp(MessagingService.Reason.ABNORMAL_HEARTRATE,
//                            curLoc.lat, curLoc.lng, getCallback());
//                }else{
//                    if(newHeartrateReading>0){
//                        Collections.reverse(dependent.heartRates);
//                        dependent.heartRates.add((long) newHeartrateReading);
//                        Collections.reverse(dependent.heartRates);
//                    }
//                }
//
//                FirebaseQuery.updateDependent(dependent);

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

        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coordinate curLoc = dataReader.getGPSData();
                if (curLoc != null) {
                    displayMessage("Panic - Sent for help!");
                    MessagingService.broadcastForHelp(MessagingService.Reason.PANIC_BUTTON,
                            curLoc.lat, curLoc.lng, getCallback());
                } else {
                    displayMessage("Failed to obtain location :(");
                    MessagingService.broadcastForHelp(MessagingService.Reason.PANIC_BUTTON,
                            dlat, dlng, getCallback());
                }

            }
        });
        new BlueToothData().execute();


        // -----------------------------------------------------------------------------------------
        // Obtain access to the phone's vibration services to alert the user of incoming messages
        // -----------------------------------------------------------------------------------------
        if (vibrationService == null) {
            vibrationService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }


        caretakerPromptReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String caretakerName = intent.getStringExtra(MessagingService.FCM_DATA_CARETAKER_NAME);
                final String caretakerId = intent.getStringExtra(MessagingService.FCM_DATA_CARETAKER_ID);

                final AlertDialog alertDialog = new AlertDialog.Builder(HealthActivity.this).create();
                alertDialog.setTitle("Safety prompt");
                alertDialog.setMessage(caretakerName + " wants to see if you're safe. \n You have 10 seconds to reply...");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "I am Safe",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MessagingService.respondSafetyPrompt(caretakerId, getCallback());
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
                            Coordinate curLoc = dataReader.getGPSData();
                            if (curLoc != null) {
                                MessagingService.broadcastForHelp(MessagingService.Reason.FAILED_RESPONSE,
                                        curLoc.lat, curLoc.lng, getCallback());
                            } else {
                                displayMessage("Failed to obtain location :(");
                                MessagingService.broadcastForHelp(MessagingService.Reason.FAILED_RESPONSE,
                                        dlat, dlng, getCallback());
                            }

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

    }

    @NonNull
    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                displayMessage("Neverlost failed to message");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    displayMessage("Message sent!");
                } else {
                    displayMessage("panic: I don't know how to handle this!");
                }
            }
        };


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


            Coordinate curLoc = readData.getGPSData();
            if (curLoc == null) {
                curLoc = new Coordinate((float)dlat,(float)dlng);

            }

            int newHeartrateReading = readData.getHRData();
            if(newHeartrateReading == 511){
                newHeartrateReading = 0;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(HealthActivity.this, "No reading from Biomed Sensor", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            }

            if(newHeartrateReading != -1) {

                boolean isHeartrateNormal = HealthAlgorithm.IsHeartRateAbnormal(dependent, newHeartrateReading);

                if (!isHeartrateNormal) {
                    MessagingService.broadcastForHelp(MessagingService.Reason.ABNORMAL_HEARTRATE,
                            curLoc.lat, curLoc.lng, getCallback());
                } else {
                    if (newHeartrateReading > 0) {
                        Collections.reverse(dependent.heartRates);
                        dependent.heartRates.add((long) newHeartrateReading);
                        Collections.reverse(dependent.heartRates);
                    }
                }

                FirebaseQuery.updateDependent(dependent);
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HealthActivity.this, "Unable to read from bluetooth", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return newHeartrateReading;
        }
    }


}
