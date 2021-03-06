package com.neverlost.ubc.neverlost.objects;

import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;

import java.util.List;

/**
 * Created by clarence on 2017-03-11.
 */

public class Dependent extends User {

    public List<Long> heartRates;
    public List<Long> distances;

    public Dependent(String name, long age, long weight, long height,
                     List<Long> heartRates, List<Long> distances, String firebaseID) {

        super(name, age, weight, height,firebaseID);
        this.heartRates = heartRates;
        this.distances = distances;
    }


}
