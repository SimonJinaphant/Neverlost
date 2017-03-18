package com.neverlost.ubc.neverlost.algorithm;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

/**
 * Created by clarence on 2017-03-14.
 */

public class Stat {

    public Stat() {
    }

    static public double computeMean(List<Double> list) {

        Double sum = 0.0;

        for (Double i : list) {
            sum = sum + i;
        }

        return sum / list.size();

    }

    static public double computeSqureMean(List<Double> list) {

        Double sum = 0.0;

        for (Double i : list) {
            sum = sum + pow(i, 2);
        }

        return sum / list.size();

    }

    static public double computeVar(List<Double> list) {

        Double mean = computeMean(list);
        Double squreMean = computeSqureMean(list);

        return squreMean - pow(mean, 2);

    }

    static public double computeStd(List<Double> list) {

        return pow(computeVar(list), 1 / 2);

    }

    static public double computeCov(List<Double> list1, List<Double> list2) {

        int size = list1.size();

        List<Double> xy = new ArrayList<Double>();

        for (int i = 0; i < size; i++) {
            Double tempProd = list1.get(i) * list2.get(i);
            xy.add(tempProd);
        }

        double xyMean = computeMean(xy);
        double xMean = computeMean(list1);
        double yMean = computeMean(list2);

        return xyMean - xMean * yMean;
    }

    static public double computeCor(List<Double> list1, List<Double> list2) {

        double xStd = computeStd(list1);
        double yStd = computeStd(list2);

        return computeCov(list1, list2) / (xStd * yStd);

    }
}
