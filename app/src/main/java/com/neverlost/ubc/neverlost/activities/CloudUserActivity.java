package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.neverlost.ubc.neverlost.CloudUserListAdapter;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.CloudMessageUser;
import com.neverlost.ubc.neverlost.firebase.MessagingService;

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
    }
}
