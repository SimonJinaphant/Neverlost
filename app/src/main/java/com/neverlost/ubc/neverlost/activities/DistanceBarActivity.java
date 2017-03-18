package com.neverlost.ubc.neverlost.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DistanceBarActivity extends AppCompatActivity {

    BarChart distBarChart;
    TextView distDate;
    TextView calValue;

    //demo
    Dependent demoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_bar);

        //demo code
        String demoName = "Logan";
        int demoAge = 22;
        double demoWeight = 68.5;
        double demoHeight = 175.0;
        List<HeartRateData> demoHeartRates = new ArrayList<>();
        List<DistanceData> demoDistanceData = new ArrayList<>();
        Date curDate = new Date();
        demoHeartRates.add(new HeartRateData(65, curDate));
        demoHeartRates.add(new HeartRateData(70, curDate));
        demoHeartRates.add(new HeartRateData(80, curDate));

        demoDistanceData.add(new DistanceData(10000, curDate));
        demoDistanceData.add(new DistanceData(10500, curDate));
        demoDistanceData.add(new DistanceData(98100, curDate));

        demoUser = new Dependent(demoName, demoAge, demoWeight, demoHeight, demoHeartRates, demoDistanceData);
        //end of demo code

        distBarChart = (BarChart) findViewById(R.id.distBarChart);
        distDate = (TextView) findViewById(R.id.distDate);
        calValue = (TextView) findViewById(R.id.calValue);

        Date currDate = new Date();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy.MM.dd");
        distDate.setText(dateFormater.format(currDate));

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(01f, 4000));
        barEntries.add(new BarEntry(02f, 2856));
        barEntries.add(new BarEntry(03f, 4102));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Distance");
        BarData theData = new BarData(barDataSet);
        distBarChart.setData(theData);

        calValue.setText("1000");


    }
}
