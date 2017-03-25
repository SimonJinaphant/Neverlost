package com.neverlost.ubc.neverlost.models;

import android.os.SystemClock;
import android.util.Log;

import com.neverlost.ubc.neverlost.activities.BluetoothActivity;
import com.neverlost.ubc.neverlost.objects.Coordinate;

import java.io.IOException;

public class readData  {

    public Coordinate getGPSData() {
        for (int i = 0; i < 3; i++)
            WriteToBTDevice("*LAT");
        String lat = ReadFromBTDevice();
        String latitude = lat.replaceAll("[^-?0-9]","");
        for (int i = 0; i < 3; i++)
            WriteToBTDevice("*LNG");
        String lng = ReadFromBTDevice();
        String longitude = lng.replaceAll("[^-?0-9]","");
        Log.d("GPS data Lat", lat);
        Log.d("GPS data Lng", lng);
        Coordinate c = new Coordinate(Float.parseFloat(latitude), Float.parseFloat(longitude));
        return c;
    }

    public int getHRData() {
        for (int i = 0; i < 3; i++)
            WriteToBTDevice("*HR");
        String s = ReadFromBTDevice();
        String reading = s.replaceAll("[^0-9]","");
        Log.d("HR data", reading);
        return Integer.parseInt(reading);
    }

    private void WriteToBTDevice(String message) {
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
    private String ReadFromBTDevice() {
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
