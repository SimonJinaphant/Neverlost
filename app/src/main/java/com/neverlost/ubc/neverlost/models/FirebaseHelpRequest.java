package com.neverlost.ubc.neverlost.models;


public class FirebaseHelpRequest {
    private final String dependant;
    private final double lat;
    private final double lng;

    public FirebaseHelpRequest(String dependant, double lat, double lng) {
        this.dependant = dependant;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDependant() {
        return dependant;
    }

}
