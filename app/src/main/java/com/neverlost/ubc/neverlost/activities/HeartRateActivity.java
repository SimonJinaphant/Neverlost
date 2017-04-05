package com.neverlost.ubc.neverlost.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.neverlost.ubc.neverlost.algorithm.StatisticMethod;
import com.neverlost.ubc.neverlost.firebase.FirebaseQuery;
import com.neverlost.ubc.neverlost.firebase.FirebaseRef;
import com.neverlost.ubc.neverlost.objects.Dependent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeartRateActivity extends AppCompatActivity {

    LineChart heartRateGraph;
    TextView  fluctValue;
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
        fluctValue = (TextView) findViewById(R.id.fluctValue);

        FirebaseRef.dependentRer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dependent dependent = FirebaseQuery.getDependent(uname, dataSnapshot);

                ArrayList<Entry> graphEntries = new ArrayList<>();

                int size = dependent.heartRates.size();
                if(size>=5) size=5;

                List<Long> temp = new LinkedList<Long>();

                for(int i=0; i<5; i++){
                    graphEntries.add(new BarEntry(i,dependent.heartRates.get(i)));
                    temp.add(dependent.heartRates.get(i));
                }

                double fluct = Math.round(StatisticMethod.computeStdI(temp));
                String fluctStr = String.valueOf(fluct);
                fluctValue.setText(fluctStr);

                LineDataSet lineDataSet = new LineDataSet(graphEntries, "Heart Rate");
                LineData theData = new LineData(lineDataSet);
                heartRateGraph.setData(theData);

                //compute the rend of data
                int trend = StatisticMethod.computeTrend(dependent.heartRates);
                if(trend==1){
                    trendValue.setText("Increasing");
                }else{
                    trendValue.setText("Decreasing");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
