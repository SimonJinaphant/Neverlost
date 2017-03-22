package com.neverlost.ubc.neverlost.objects;

import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;

import java.util.List;

/**
 * Created by clarence on 2017-03-11.
 */

public class Dependent extends User {

    public List<HeartRateData> heartRates;
    public List<DistanceData> distances;

    public Dependent(String name, int age, double weight, double height,
                     List<HeartRateData> heartRates, List<DistanceData> distances) {
        super(name, age, weight, height);
        this.heartRates = heartRates;
        this.distances = distances;
    }


}
