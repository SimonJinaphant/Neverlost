package com.neverlost.ubc.neverlost.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.neverlost.ubc.neverlost.R;

import java.util.ArrayList;

public class distanceBarActivity extends AppCompatActivity {

    BarChart distBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_bar);

        distBarChart = (BarChart) findViewById(R.id.distBarChart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(44f,1));
        barEntries.add(new BarEntry(44f,2));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("Aprial");
        theDates.add("May");
        theDates.add("June");

        BarData theData = new BarData(barDataSet);
        distBarChart.setData(theData);


    }
}
