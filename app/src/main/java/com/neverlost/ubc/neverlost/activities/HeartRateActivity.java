package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.neverlost.ubc.neverlost.R;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;

public class HeartRateActivity extends AppCompatActivity {

    LineChart heartRateGraph;
    TextView trendValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        Intent intent = getIntent();
        final String uname = intent.getStringExtra("key");

        //get layout elements
        heartRateGraph = (LineChart) findViewById(R.id.heartRateGraph);
        trendValue = (TextView) findViewById(R.id.trendValue);

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(uname, dataSnapshot);

                ArrayList<Entry> graphEntries = new ArrayList<>();

                for(int i=0; i<5; i++){
                    graphEntries.add(new BarEntry(i,dependent.heartRates.get(i).heartRate));
                }

                LineDataSet lineDataSet = new LineDataSet(graphEntries, "Heart Rate");
                LineData theData = new LineData(lineDataSet);
                heartRateGraph.setData(theData);

                //todo: need a function to detect the trend
                trendValue.setText("Increasing");


                //todo: add a trend line to the graph
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
