package ru.gurps.generator.models;

import ru.gurps.generator.config.Model;

public class User extends Model {
    public Integer id;
    public String name;
    public String currentPoints;
    public String maxPoints;
    public Integer st = 10;
    public Integer dx = 10;
    public Integer iq = 10;
    public Integer ht = 10;
    public Integer hp = 10;
    public Integer will = 10;
    public Integer per = 10;
    public Integer fp = 10;
    public Double bs = 5.0;
    public Integer move = 5;
    public Integer sm = 0;
    public Boolean noFineManipulators = false;
    
    public User(){
    }

    public User(String name, String maxPoints) {
        this.name = name;
        this.maxPoints = maxPoints;
    }

    public String getName() {
        return name;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getMaxPoints() {
        return maxPoints;
    }
}