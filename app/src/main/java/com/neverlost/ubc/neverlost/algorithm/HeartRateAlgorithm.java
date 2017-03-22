package com.neverlost.ubc.neverlost.algorithm;

import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Little_town on 3/21/2017.
 */

public class HeartRateAlgorithm {

    Stat stat = new Stat();

    boolean IsHeartRateAbnormal(Dependent dependent, int newReading){

        int size  = dependent.heartRates.size();

        if(size>10) size = 10;

        List<Integer> histHeartRate = new ArrayList<>();
        for(int i = 0; i < size; i++){
            histHeartRate.add(dependent.heartRates.get(i).heartRate);
        }

        int mean = stat.computeMeanI(histHeartRate);
        double sd = stat.computeStdI(histHeartRate);

        if(newReading >= mean + 2*sd || newReading <= mean - 2*sd) return false;

        return true;
    }

    int computeTrend(Dependent dependent, int newReading){

        int size  = dependent.heartRates.size();

        if(size>10) size = 10;

        List<Integer> histHeartRate = new ArrayList<>();
        for(int i = 0; i < size; i++){
            histHeartRate.add(dependent.heartRates.get(i).heartRate);
        }

        //algorithm to detect inreasing or decreasing

        return 0;
    }

}
