package objects;

/**
 * Created by clarence on 2017-03-11.
 */

public class User {

    private String name;
    private int age;
    private String gender;
    private float weight;
    private float height;

    public User (String name, int age, String gender, float weight, float height){
        this.name   = name;
        this.age    = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    //set and return
    String getName()    {   return this.name;    };
    int    getAge()     {   return this.age;     };
    String getGender()  {   return this.gender;  };
    float  getWeight()  {   return this.weight;  };
    float  getHeight()  {   return this.height;  };
}
