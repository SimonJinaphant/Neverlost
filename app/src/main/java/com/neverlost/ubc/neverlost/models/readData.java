package com.neverlost.ubc.neverlost.models;

import android.os.SystemClock;
import android.util.Log;

import com.facebook.Profile;
import com.neverlost.ubc.neverlost.activities.BluetoothActivity;
import com.neverlost.ubc.neverlost.objects.Coordinate;

import java.io.IOException;

public class readData  {

    public readData(){};

    //returns null if can't get a stable connection to bluetooth
    public static Coordinate getGPSData() {
        String latitude;
        int counter = 0;
        do {
            for (int i = 0; i < 3; i++) {
                WriteToBTDevice("Latitude");
            }
            String s = ReadFromBTDevice();
            latitude = s.replaceAll("[^0-9.\\-]+" , "");
            counter++;
        } while (latitude.equals("") && counter <= 3);
        if (latitude.equals(""))
            return null;

        Log.d("Latitude", latitude);

        String longitude;
        counter = 0;
        do {
            for (int i = 0; i < 3; i++) {
                WriteToBTDevice("Longitude");
            }
            String s = ReadFromBTDevice();
            longitude = s.replaceAll("[^0-9.\\-]+", "");
            counter++;
        } while (longitude.equals("") && counter <= 3);
        if (longitude.equals(""))
            return null;

        Log.d("Longitude", longitude);

        Coordinate c = new Coordinate(Float.parseFloat(latitude), Float.parseFloat(longitude));

        return c;
    }

    //returns -1 if can't get a stable connection to bluetooth
    public static int getHRData() {
        String reading;
        int counter = 0;
        do {
            for (int i = 0; i < 3; i++) {
                WriteToBTDevice("HeartRate");
            }
            String s = ReadFromBTDevice();
            reading = s.replaceAll("[^0-9]", "");
            counter++;
        } while (reading.equals("") && counter <= 3);
        Log.d("HR data", reading);
        if (reading.equals(""))
            reading = "-1";
        return Integer.parseInt(reading);
    }

    public static boolean sendName() {
        for (int i = 0; i < 3; i++){
            WriteToBTDevice("UserName");
            WriteToBTDevice(Profile.getCurrentProfile().getName());
        }

        return true;
    }

    private static void WriteToBTDevice(String message) {
        String s = new String("\r\n");
        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        try {
            BluetoothActivity.mmOutStream.write(msgBuffer);
            BluetoothActivity.mmOutStream.write(newline);
        } catch (IOException e) {
        }
    }

    // This function reads a line of text from the Bluetooth device
    private static String ReadFromBTDevice() {
        byte c;
        String s = new String("");

        try { // Read from the InputStream using polling and timeout
            for (int i = 0; i < 200; i++) { // try to read for 2 seconds max
                SystemClock.sleep(10);
                if (BluetoothActivity.mmInStream.available() > 0) {
                    if ((c = (byte) BluetoothActivity.mmInStream.read()) != '\r') // '\r' terminator
                        s += (char) c; // build up string 1 byte by byte
                    else
                        return s;
                }
            }
        } catch (IOException e) {
            return new String("-- No Response --");
        }
        return s;
    }
}
