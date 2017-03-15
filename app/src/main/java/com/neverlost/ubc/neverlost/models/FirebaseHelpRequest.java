package com.neverlost.ubc.neverlost.models;


public class FirebaseHelpRequest {
    private final String to;
    private final String dependant;
    private final double lat;
    private final double lng;

    public FirebaseHelpRequest(String to, String dependant, double lat, double lng) {
        this.to = to;
        this.dependant = dependant;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTo() {
        return to;
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
