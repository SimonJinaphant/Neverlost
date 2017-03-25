package com.neverlost.ubc.neverlost.models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neverlost.ubc.neverlost.R;

import java.util.ArrayList;

public class MyCustomArrayAdaptor extends ArrayAdapter<String> {
    // my new class variables, copies of constructor params, but add more if required
    private static int invalid = 1;
    private static int disconnected = 2;
    private static int connected = 3;
    private Context context ;
    private ArrayList<String> theStringArray;
    public final int numRows = 500 ;
    private int [] RowValidity = new int [numRows];

    // constructor
    public MyCustomArrayAdaptor(Context _context,
                                int textViewResourceId,
                                ArrayList<String> _theStringArray
    )
    {
        // call base class constructor
        super(_context, textViewResourceId, _theStringArray);
        // save the context and the array of strings we were given
        context = _context;
        theStringArray = _theStringArray;
        clearValidity ();
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate ( R.layout.row, parent, false );
        ImageView icon = (ImageView) row.findViewById (R.id.BTicon);
        icon.setImageResource (R.drawable.bluetooth);
        if (RowValidity[position] == connected)
            icon.setImageResource(R.drawable.greentick);
        icon.setVisibility (View.VISIBLE);
        TextView label = (TextView) row.findViewById( R.id.BTdeviceText);
        label.setText (theStringArray.get(position));
        TextView t = (TextView) row.findViewById(R.id.Selected);
        icon.setVisibility (View.VISIBLE);
        if(RowValidity [position] == invalid) {
            t.setText("invalid device");
        }
        else if (RowValidity [position] == disconnected) {
            t.setText("disconnected");
            t.setTextColor(Color.parseColor("#DC143C"));
        }
        else {
            t.setText("connected");
            t.setTextColor(Color.parseColor("#32CD32"));
        }
        return row;
    }

    public void setConnected (int position) { RowValidity [position] = connected ; }
    public void setValid (int position) { RowValidity [position] = disconnected ; }
    public void clearValidity () {
        for(int i = 0; i < numRows; i ++) {
            RowValidity[i] = invalid;
        }
    }
    public void disconnectAll(){
        for(int i = 0; i < numRows; i ++) {
            if (RowValidity[i] == connected)
                RowValidity[i] = disconnected;
        }
    }


}

