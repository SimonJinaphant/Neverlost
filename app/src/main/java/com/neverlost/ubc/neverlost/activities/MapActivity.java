package com.neverlost.ubc.neverlost.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.MessagingService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, LocationListener {

    private static final String TAG = "MapActivity";
    private final int REQUEST_FINE_LOC_CODE = 391;
    private final long[] vibrationPattern = {0, 400, 100, 400, 100, 400};
    // Force the Location manager to update our GPS location when the following thresholds are met
    private final int LOCATION_UPDATE_TIME = 1000;
    private final int LOCATION_UPDATE_DISTANCE = 0;
    private GoogleMap mMap;
    private Location currentLocation;
    private LocationManager locationManager;
    private BroadcastReceiver dependantHelpBroadcastReceiver;
    // Vibration to alert the caretaker that something has happened to their dependant
    private Vibrator vibrationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // -----------------------------------------------------------------------------------------
        // Listen for broadcasts coming from our local FCM Messaging Service.
        // -----------------------------------------------------------------------------------------
        dependantHelpBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                LatLng dependant = new LatLng(
                        intent.getDoubleExtra(MessagingService.FCM_DATA_LAT, 0),
                        intent.getDoubleExtra(MessagingService.FCM_DATA_LNG, 0)
                );

                mMap.addMarker(new MarkerOptions()
                        .position(dependant)
                        .title(intent.getStringExtra(MessagingService.FCM_DATA_DEPENDANT))
                );

                mMap.animateCamera(CameraUpdateFactory.newLatLng(dependant));
                vibrationService.vibrate(vibrationPattern, -1);
            }
        };

        // -----------------------------------------------------------------------------------------
        // Obtain access to the phone's vibration services to alert the user of incoming messages
        // -----------------------------------------------------------------------------------------
        if (vibrationService == null) {
            vibrationService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        // -----------------------------------------------------------------------------------------
        // Check to see if we can access the GPS.
        // -----------------------------------------------------------------------------------------
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOC_CODE);
        }

        // -----------------------------------------------------------------------------------------
        // Setup the Location Manager so we can obtain GPS location
        // -----------------------------------------------------------------------------------------
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            currentLocation = location;
        } else {
            displayMessage("Unable to obtain your location");
        }

        // -----------------------------------------------------------------------------------------
        // Subscribe to Firebase for messages.
        // -----------------------------------------------------------------------------------------
        FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.FCM_TOPIC_NEVERLOST);

        // Setup the remaining UI elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessagingService.broadcastForHelp(currentLocation, new Callback() {
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
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_TIME,
                    LOCATION_UPDATE_DISTANCE,
                    MapActivity.this
            );
        } catch (SecurityException se) {
            Log.e(TAG, se.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException se) {
            Log.e(TAG, se.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(dependantHelpBroadcastReceiver,
                        new IntentFilter(MessagingService.NEVERLOST_FCM_RESULT)
                );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(dependantHelpBroadcastReceiver);

        super.onStop();
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

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            displayMessage("Unable to show current location on map");
        }

        // Show the current location on the map.
        if (currentLocation != null) {
            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
        } else {
            displayMessage("Unable to find you on the map");
        }

    }

    /**
     * Callback for when permissions are granted in this application/
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOC_CODE) {
            if (permissions.length == 2 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    permissions[1] == android.Manifest.permission.ACCESS_COARSE_LOCATION &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
        MapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MapActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update the current client location whenever a location change occurs.
     *
     * @param location - The new location value.
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Override method for Location Manager. Do not delete.
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Override method for Location Manager. Do not delete.
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Override method for Location Manager. Do not delete.
    }

}
