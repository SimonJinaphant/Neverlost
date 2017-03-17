package com.neverlost.ubc.neverlost.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;

public class HeartRateActivity extends AppCompatActivity {

    LineChart heartRateGraph;
    TextView trendValue;

    //demo user
    Dependent demouser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        heartRateGraph      = (LineChart) findViewById(R.id.heartRateGraph);
        trendValue          = (TextView)  findViewById(R.id.trendValue);

        ArrayList<Entry> graphEntries = new ArrayList<>();
        graphEntries.add(new BarEntry(1,65));
        graphEntries.add(new BarEntry(2,78));
        graphEntries.add(new BarEntry(3,80));

        LineDataSet lineDataSet = new LineDataSet(graphEntries, "Heart Rate");
        LineData theData = new LineData(lineDataSet);
        heartRateGraph.setData(theData);

        trendValue.setText("Increasing");
    }
}
