package com.neverlost.ubc.neverlost.objects;

import com.neverlost.ubc.neverlost.datastruct.distanceData;
import com.neverlost.ubc.neverlost.datastruct.heartRateData;

import java.util.List;

/**
 * Created by clarence on 2017-03-11.
 */

public class Dependent extends User {

    List<heartRateData> heartRates;
    List<distanceData> distances;

    public Dependent(String name, int age, String gender, float weight, float height,
                     List<heartRateData> heartRates, List<distanceData> distances){
        super(name,age,gender,weight,height);
        this.heartRates = heartRates;
        this.distances  = distances;
    }



}
