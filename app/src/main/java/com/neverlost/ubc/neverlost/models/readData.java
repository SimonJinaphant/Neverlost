package com.neverlost.ubc.neverlost.models;

import android.os.SystemClock;
import android.util.Log;

import com.neverlost.ubc.neverlost.activities.BluetoothActivity;
import com.neverlost.ubc.neverlost.objects.Coordinate;

import java.io.IOException;

public class readData  {

    //returns null if can't get a stable connection to bluetooth
    public static Coordinate getGPSData() {
        int counter = 0;
        String latitude;
        String longitude;
        do {
            for (int i = 0; i < 3; i++)
                WriteToBTDevice("Latitude");
            String lat = ReadFromBTDevice();
            latitude = lat.replaceAll("[^-?0-9]", "");
            for (int i = 0; i < 3; i++)
                WriteToBTDevice("Longitude");
            String lng = ReadFromBTDevice();
            longitude = lng.replaceAll("[^-?0-9]", "");
            counter++;
        } while ((latitude.equals("") || longitude.equals("")) && counter <= 5);
        if (latitude.equals("") || longitude.equals("")) {
            return null;
        }
        Log.d("GPS data Lat", latitude);
        Log.d("GPS data Lng", longitude);
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
        } while (reading.equals("") && counter <= 5);
        Log.d("HR data", reading);
        if (reading.equals(""))
            reading = "-1";
        return Integer.parseInt(reading);
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
