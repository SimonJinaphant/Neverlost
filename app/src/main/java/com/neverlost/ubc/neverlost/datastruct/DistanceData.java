package com.neverlost.ubc.neverlost.datastruct;

import java.util.Date;

/**
 * Created by clarence on 2017-03-15.
 */

public class DistanceData {
    public int distance;
    public Date date;

    public DistanceData(int heartRate, Date date) {
        this.distance = heartRate;
        this.date = date;
    }
}
