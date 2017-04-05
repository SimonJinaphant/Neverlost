package com.neverlost.ubc.neverlost.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.neverlost.ubc.neverlost.ConnectionListAdapter;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.CloudMessageUser;
import com.neverlost.ubc.neverlost.firebase.MessagingService;

import java.util.ArrayList;
import java.util.Arrays;

public class ConnectionsActivity extends AppCompatActivity {

    ArrayList<CloudMessageUser> users;
    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        users = new ArrayList<>();
        userListView = (ListView) findViewById(R.id.user_listview);

        new LoadUsers().execute();
    }

    private class LoadUsers extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            ConnectionListAdapter adapter = new ConnectionListAdapter(getApplicationContext(), R.layout.connection_list_layout, users);

            userListView.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(CloudMessageUser user : MessagingService.getCaretaker()){
                users.add(user);
            }
            for(CloudMessageUser user : MessagingService.getDependents()){
                users.add(user);
            }
            return null;
        }
    }
}
