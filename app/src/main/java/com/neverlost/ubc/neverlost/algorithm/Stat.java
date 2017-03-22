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

    static public int computeMeanI(List<Integer> list) {

        int sum = 0;

        for (Integer i : list) {
            sum = sum + i;
        }

        return sum / list.size();

    }

    static public double computeMeanD(List<Double> list) {

        Double sum = 0.0;

        for (Double i : list) {
            sum = sum + i;
        }

        return sum / list.size();

    }

    static public double computeSqureMeanD(List<Double> list) {

        Double sum = 0.0;

        for (Double i : list) {
            sum = sum + pow(i, 2);
        }

        return sum / list.size();

    }

    static public double computeSqureMeanI(List<Integer> list) {

        Double sum = 0.0;

        for (Integer i : list) {
            sum = sum + pow(i, 2);
        }

        return sum / list.size();

    }

    static public double computeVarD(List<Double> list) {

        Double mean = computeMeanD(list);
        Double squreMean = computeSqureMeanD(list);

        return squreMean - pow(mean, 2);

    }

    static public double computeVarI(List<Integer> list) {

        int mean = computeMeanI(list);
        Double squreMean = computeSqureMeanI(list);

        return squreMean - pow(mean, 2);

    }

    static public double computeStdD(List<Double> list) {

        return pow(computeVarD(list), 1 / 2);

    }

    static public double computeStdI(List<Integer> list) {

        return pow(computeVarI(list), 1 / 2);

    }

    static public double computeCov(List<Double> list1, List<Double> list2) {

        int size = list1.size();

        List<Double> xy = new ArrayList<Double>();

        for (int i = 0; i < size; i++) {
            Double tempProd = list1.get(i) * list2.get(i);
            xy.add(tempProd);
        }

        double xyMean = computeMeanD(xy);
        double xMean = computeMeanD(list1);
        double yMean = computeMeanD(list2);

        return xyMean - xMean * yMean;
    }

    static public double computeCor(List<Double> list1, List<Double> list2) {

        double xStd = computeStdD(list1);
        double yStd = computeStdD(list2);

        return computeCov(list1, list2) / (xStd * yStd);

    }
}
