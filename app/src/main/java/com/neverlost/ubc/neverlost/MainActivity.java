package com.neverlost.ubc.neverlost;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;
import com.neverlost.ubc.neverlost.firebase.Authorization;
import com.neverlost.ubc.neverlost.firebase.MessagingService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private final double UBC_VANCOUVER_LAT = 49.2606;
    private final double UBC_VANCOUVER_LNG = -123.2460;
    private final int REQUEST_FINE_LOC_CODE = 391;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // -----------------------------------------------------------------------------------------
        // If a Cloud Message notification was tapped, the data payload associated with it can be
        // accessible from the Intent's EXTRA field.
        // -----------------------------------------------------------------------------------------
        Bundle notificationDataPayload = getIntent().getExtras();
        if (notificationDataPayload != null) {
            for (String key : notificationDataPayload.keySet()) {
                Object value = notificationDataPayload.get(key);
                Log.d(TAG, "Key: " + key + " \t Value: " + value);
            }
        }

        // -----------------------------------------------------------------------------------------
        // Check to see if we can access the GPS.
        // -----------------------------------------------------------------------------------------
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_FINE_LOC_CODE);
        }

        // -----------------------------------------------------------------------------------------
        // Subscribe to Firebase for messages.
        // -----------------------------------------------------------------------------------------
        FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.FCM_TOPIC);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = MessageUtils.generateHelpMessageJSON("Simon", UBC_VANCOUVER_LAT, UBC_VANCOUVER_LNG);
                MessagingService.sendUpstreamMessage(message, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        displayMessage("Neverlost failed to send help over the network...good luck...");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            displayMessage("Help is on the way!");
                        } else {
                            displayMessage("panic: I don't know how to handle this!");
                        }
                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Callback for when the action bar (The top toolbar in the app) items are clicked.
     *
     * @param item - The action bar menu item selected
     * @return - True if everything went okay.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback invoked when a drawer (the siding menu on the left) item gets clicked on.
     *
     * @param item - The drawer menu item
     * @return - True if everything went okay.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // TODO: Set this to point to other UI screens.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Callback invoked when the Map UI is ready to be interacted with.
     *
     * @param googleMap - The map instance to interact with.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // TODO: Change this to point to user's current location.
        // Set the default location of our map to point at UBC.
        LatLng ubc_vancouver = new LatLng(UBC_VANCOUVER_LAT, UBC_VANCOUVER_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubc_vancouver, 15));
    }

    /**
     * Callback for when permissions are granted in this application/
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOC_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                displayMessage("Neverlost requires you're location to work");
            }
        }
    }

    /**
     * Helper function to display a Toast message without cluttering the codebase.
     *
     * @param message - The message to print on the Toast message.
     */
    private void displayMessage(@NonNull final String message) {
        // Launch the Toast on a UI thread to prevent it from crashing.
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Check to see if the app has the correct authorization keys
     */
    private void checkAuthorization() {
        if (!Authorization.containsRealAuthorizationKeys()) {

        }
    }
}
