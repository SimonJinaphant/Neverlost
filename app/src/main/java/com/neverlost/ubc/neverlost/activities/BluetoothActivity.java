package com.neverlost.ubc.neverlost.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.models.MyCustomArrayAdaptor;
import com.neverlost.ubc.neverlost.models.readData;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private String my_macs[] = new String[]{"00:06:66:72:7A:18", "00:06:66:6C:A5:F8"};
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    private MyCustomArrayAdaptor myPairedArrayAdapter;
    private MyCustomArrayAdaptor myDiscoveredArrayAdapter;
    private BroadcastReceiver mReceiver;
    private ArrayList<BluetoothDevice> Discovereddevices = new ArrayList<BluetoothDevice>();
    private ArrayList<String> myDiscoveredDevicesStringArray = new ArrayList<String>();
    private ArrayList<Boolean> myDiscoveredValid = new ArrayList<>();
    private ArrayList<BluetoothDevice> Paireddevices = new ArrayList<BluetoothDevice>();
    private ArrayList<String> myPairedDevicesStringArray = new ArrayList<String>();
    private ArrayList<Boolean> myPairedValid = new ArrayList<>();
    private BluetoothSocket mmSocket = null;
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    private boolean Connected = false;

    private AdapterView.OnItemClickListener mPairedClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            String text = "Paired Device: " +
                    myPairedDevicesStringArray.get(position);
            if (myPairedValid.get(position)) {
                if (Connected == true) {
                    closeConnection();
                    myPairedArrayAdapter.clearValidity();
                    myDiscoveredArrayAdapter.clearValidity();
                }
                CreateSerialBluetoothDeviceSocket(Paireddevices.get(position));
                ConnectToSerialBlueToothDevice();
                if (Connected == true)
                    myPairedArrayAdapter.setConnected(position);
                myDiscoveredArrayAdapter.notifyDataSetChanged();
                myPairedArrayAdapter.notifyDataSetChanged();
            }
        }
    };
    private AdapterView.OnItemClickListener mDiscoveredClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            String text = "Discovered Device: " +
                    myDiscoveredDevicesStringArray.get(position);
            if (myDiscoveredValid.get(position)) {
                if (Connected == true) {
                    closeConnection();
                    myPairedArrayAdapter.clearValidity();
                    myDiscoveredArrayAdapter.clearValidity();
                }
                CreateSerialBluetoothDeviceSocket(Discovereddevices.get(position));
                ConnectToSerialBlueToothDevice();
                if (Connected == true)
                    myDiscoveredArrayAdapter.setConnected(position);
                myDiscoveredArrayAdapter.notifyDataSetChanged();
                myPairedArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        context = getApplicationContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //update firebase id for dependent
        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(Profile.getCurrentProfile().getId(), dataSnapshot);
                dependent.firebaseID = FirebaseInstanceId.getInstance().getToken();
                FirebaseQuery.updateDependent(dependent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        if (mBluetoothAdapter == null) {
            Toast toast = Toast.makeText(context, "No Bluetooth", Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }
        myPairedArrayAdapter = new MyCustomArrayAdaptor(this,
                android.R.layout.simple_list_item_1, myPairedDevicesStringArray);
        myDiscoveredArrayAdapter = new MyCustomArrayAdaptor(this,
                android.R.layout.simple_list_item_1, myDiscoveredDevicesStringArray);

        ListView PairedlistView = (ListView) findViewById(R.id.listView2);
        ListView DiscoveredlistView = (ListView) findViewById(R.id.listView3);
        PairedlistView.setOnItemClickListener(mPairedClickedHandler);
        DiscoveredlistView.setOnItemClickListener(mDiscoveredClickedHandler);
        PairedlistView.setAdapter(myPairedArrayAdapter);
        DiscoveredlistView.setAdapter(myDiscoveredArrayAdapter);

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice newDevice;
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String theDevice = new String(newDevice.getName() +
                            "\nMAC Address = " + newDevice.getAddress());
                    Discovereddevices.add(newDevice);
                    myDiscoveredDevicesStringArray.add(theDevice);
                    if (validMAC(newDevice.getAddress())) {
                        myDiscoveredArrayAdapter.setValid(myDiscoveredDevicesStringArray.size() - 1);
                        myDiscoveredValid.add(true);
                    }
                    else
                        myDiscoveredValid.add(false);
                    myDiscoveredArrayAdapter.notifyDataSetChanged();
                }
            }
        };
        IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter filterStop = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filterFound);
        registerReceiver(mReceiver, filterStart);
        registerReceiver(mReceiver, filterStop);
        Set<BluetoothDevice> thePairedDevices = mBluetoothAdapter.getBondedDevices();
        if (thePairedDevices.size() > 0) {
            Iterator<BluetoothDevice> iter = thePairedDevices.iterator();
            BluetoothDevice aNewdevice;
            while (iter.hasNext()) {
                aNewdevice = iter.next();
                String PairedDevice = new String(aNewdevice.getName()
                        + "\nMAC Address = " + aNewdevice.getAddress());
                Paireddevices.add(aNewdevice);
                myPairedDevicesStringArray.add(PairedDevice);
                if (validMAC(aNewdevice.getAddress())) {
                    myPairedArrayAdapter.setValid(myPairedDevicesStringArray.size() - 1);
                    myPairedValid.add(true);
                }
                else
                    myPairedValid.add(false);
                myPairedArrayAdapter.notifyDataSetChanged();
            }
        }
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
        Button button=(Button)findViewById(R.id.confirmChoice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connected) {
                    readData.sendName();
                    Intent healthAct = new Intent(v.getContext(), HealthActivity.class);
                    healthAct.putExtra("key", Profile.getCurrentProfile().getId());
                    startActivity(healthAct);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                Toast toast = Toast.makeText(context, "BlueTooth Failed to Start ",
                        Toast.LENGTH_SHORT);
                toast.show();
                finish();
                return;
            }
            else {
                Intent restart = new Intent(this, BluetoothActivity.class);
                startActivity(restart);
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void closeConnection() {
        try {
            mmInStream.close();
            mmInStream = null;
        } catch (IOException e) {
        }
        try {
            mmOutStream.close();
            mmOutStream = null;
        } catch (IOException e) {
        }
        try {
            mmSocket.close();
            mmSocket = null;
        } catch (IOException e) {
        }

        Connected = false;
    }

    public void CreateSerialBluetoothDeviceSocket(BluetoothDevice device) {
        mmSocket = null;
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Toast.makeText(context, "Socket Creation Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void ConnectToSerialBlueToothDevice() {
        mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
            Toast.makeText(context, "Connection Made", Toast.LENGTH_SHORT).show();
        } catch (IOException connectException) {
            Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        GetInputOutputStreamsForSocket();
        Connected = true;
    }

    public void GetInputOutputStreamsForSocket() {
        try {
            mmInStream = mmSocket.getInputStream();
            mmOutStream = mmSocket.getOutputStream();
        } catch (IOException e) {
        }
    }

    public boolean validMAC(String s){
        for (int i = 0; i < my_macs.length; i++){
            if (my_macs[i].equals(s))
                return true;
        }
        return false;
    }
}
