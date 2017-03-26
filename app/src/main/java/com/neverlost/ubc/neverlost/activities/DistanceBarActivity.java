package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.datastruct.DistanceData;
import com.neverlost.ubc.neverlost.datastruct.HeartRateData;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DistanceBarActivity extends AppCompatActivity {

    BarChart distBarChart;
    TextView distDate;
    TextView calValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_bar);
        Intent intent = getIntent();
        final String uname = intent.getStringExtra("key");

        //get the layout elements
        distBarChart = (BarChart) findViewById(R.id.distBarChart);
        distDate = (TextView) findViewById(R.id.distDate);
        calValue = (TextView) findViewById(R.id.calValue);

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(uname, dataSnapshot);

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                barEntries.add(new BarEntry(01f, dependent.distances.get(0).distance));
                barEntries.add(new BarEntry(02f, dependent.distances.get(1).distance));
                barEntries.add(new BarEntry(03f, dependent.distances.get(2).distance));
                barEntries.add(new BarEntry(04f, dependent.distances.get(3).distance));
                barEntries.add(new BarEntry(05f, dependent.distances.get(4).distance));

                BarDataSet barDataSet = new BarDataSet(barEntries, "Distance");
                BarData theData = new BarData(barDataSet);
                distBarChart.setData(theData);

                //todo:need a fuction to calculate calories
                calValue.setText("1000");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
