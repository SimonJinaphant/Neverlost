package com.neverlost.ubc.neverlost.objects;

/**
 * Created by clarence on 2017-03-11.
 */

public class User {

    public String name;
    public long age;
    public double weight;
    public double height;
    public String firebaseID;

    public User(String name, long age, long weight, long height, String firebaseID) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.firebaseID = firebaseID;
    }

}
