package com.neverlost.ubc.neverlost.algorithm;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.neverlost.ubc.neverlost.objects.Coordinate;

import java.util.List;

/**
 * Created by clarence on 2017-04-04.
 */

public class MapAlgorithm {

    public static void drawRoute(List<Coordinate> coors, GoogleMap map){

        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(coors.get(0).lat, coors.get(0).lng),
                     new LatLng(coors.get(1).lat, coors.get(1).lng),
                     new LatLng(coors.get(2).lat, coors.get(2).lng),
                     new LatLng(coors.get(3).lat, coors.get(3).lng),
                     new LatLng(coors.get(4).lat, coors.get(4).lng));

        Polyline polyline = map.addPolyline(rectOptions);
    }

    //todo: come up with a way to predict the future direction
    public static void predictRoute(List<Coordinate> coors, GoogleMap map){

    }

}
