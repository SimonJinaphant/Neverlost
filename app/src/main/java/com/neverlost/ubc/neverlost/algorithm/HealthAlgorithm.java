package com.neverlost.ubc.neverlost.algorithm;

import android.util.Log;

import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Little_town on 3/21/2017.
 */

public class HealthAlgorithm {


    public static boolean IsHeartRateAbnormal(Dependent dependent, int newReading){

        int size  = dependent.heartRates.size();

        if(size>10) size = 10;

        List<Long> histHeartRate = new ArrayList<>();
        for(int i = 0; i < size; i++){
            histHeartRate.add(dependent.heartRates.get(i));
        }

        long mean = StatisticMethod.computeMeanI(histHeartRate);
        double sd = StatisticMethod.computeStdI(histHeartRate);

        if(newReading >= mean + 2*10 || newReading <= mean - 2*10) return false;

        return true;
    }

    public static int computeTrend(Dependent dependent, int newReading){

        int size  = dependent.heartRates.size();

        if(size>10) size = 10;

        List<Integer> histHeartRate = new ArrayList<>();
        for(int i = 0; i < size; i++){
            histHeartRate.add(Integer.valueOf(dependent.heartRates.get(i).intValue()));
        }

        //algorithm to detect inreasing or decreasing

        return 0;
    }

    public static double computeBMR_male(Dependent dependent){

        int         BMRCONST    = 66;
        double      weightCoe   = 13.7;
        int         heightCeo   = 5;
        double      ageCeo      = 6.8;

        //BMR = 66 + (13.7 x weight in kg) + (5 x height in cm) - (6.8 x age in years)
        return BMRCONST + weightCoe*dependent.weight + heightCeo * dependent.height +
                ageCeo*dependent.age;
    }

    public static double computeBMR_female(Dependent dependent){

        int         BMRCONST    = 655;
        double      weightCoe   = 9.6;
        double      heightCeo   = 1.8;
        double      ageCeo      = 4.7;

        //BMR = 655 + (9.6 x weight in kg) + (1.8 x height in cm) - (4.7 x age in years)
        return BMRCONST + weightCoe*dependent.weight + heightCeo * dependent.height +
                ageCeo*dependent.age;
    }

    public static int healthEvaluate(Dependent dependent){

        return dependent.heartRates.get(0)>0? 3:1;

    }




}
