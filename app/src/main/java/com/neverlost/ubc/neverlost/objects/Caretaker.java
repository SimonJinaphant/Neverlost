package com.neverlost.ubc.neverlost.objects;

import java.util.List;

/**
 * Created by clarence on 2017-03-11.
 */

public class Caretaker extends User {

    public List<String> dependents;

    public Caretaker(String name, long age, long weight, long height, List<String> dependents) {
        super(name, age, weight, height);
        this.dependents=dependents;
    }


}
