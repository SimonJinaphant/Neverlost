package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neverlost.ubc.neverlost.CloudUserListAdapter;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.CloudMessageUser;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.firebase.MessagingService;
import com.neverlost.ubc.neverlost.objects.Caretaker;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;

public class CloudUserActivity extends AppCompatActivity {

    private static final int ADD_CLOUDUSER_INTENT = 1234;

    private ArrayList<CloudMessageUser> users = new ArrayList<>();
    private ListView userListView;

    private FloatingActionButton addCloudUserFab;
    private FloatingActionButton showIdentityFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clouduser);

        //update firebase id for caretaker
        FirebaseRef.caretakerRer.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Caretaker caretaker = FirebaseQuery.getCaretaker(Profile.getCurrentProfile().getId(), dataSnapshot);
                caretaker.firebaseID = FirebaseInstanceId.getInstance().getToken();
                FirebaseQuery.updateCaretaker(caretaker);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userListView = (ListView) findViewById(R.id.user_listview);
        addCloudUserFab = (FloatingActionButton) findViewById(R.id.add_clouduser_fab);
        showIdentityFab = (FloatingActionButton) findViewById(R.id.show_identity_fab);

        addCloudUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CloudUserActivity.this, CloudUserScanActivity.class);
                startActivityForResult(intent, ADD_CLOUDUSER_INTENT);
            }
        });

        showIdentityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CloudUserActivity.this, CloudUserIdentityActivity.class);
                startActivity(intent);
            }
        });

        // Load users in asynchronously to prevent lag.
        new LoadUsers().execute();
    }

    /**
     * An asynchronous background task which loads the caretakers and dependents into the list view.
     */
    private class LoadUsers extends AsyncTask<Void, Void, Void> {

        /**
         * Executes on UIThread.
         * Handles the update to any UI elements before the task starts
         */
        @Override
        protected void onPreExecute() {
            // Clear out the list to prevent duplicates.
            users.clear();

            // Set the adapter for the List view
            CloudUserListAdapter adapter = new CloudUserListAdapter(getApplicationContext(),
                    R.layout.list_clouduser, users);

            userListView.setAdapter(adapter);
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final CloudMessageUser selectedUser = users.get(position);

                    displayMessage("Selected: " + selectedUser.getName());

                    Intent careInt = new Intent(view.getContext(), MonitorActivity.class);

                    careInt.putExtra("facebook_id", selectedUser.getFacebookId());
                    careInt.putExtra("firebase_id", selectedUser.getFirebaseClientToken());

                    startActivity(careInt);
                }
            });

        }

        /**
         * The main computation being done in the background.
         */
        @Override
        protected Void doInBackground(Void... params) {
            for (CloudMessageUser user : MessagingService.getCaretaker()) {
                users.add(user);
            }
            for (CloudMessageUser user : MessagingService.getDependents()) {
                users.add(user);
            }

            return null;
        }
    }

    /**
     * Handle the results we've obtained after launching an activity with an expected result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_CLOUDUSER_INTENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                new LoadUsers().execute();
            }
        }
        userListView.invalidate();
    }

    /**
     * Helper function to display a Toast message without cluttering the codebase.
     *
     * @param message - The message to print on the Toast message.
     */
    private void displayMessage(@NonNull final String message) {
        // Launch the Toast on a UI thread to prevent it from crashing.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CloudUserActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
