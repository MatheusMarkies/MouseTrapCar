package com.matheusmarkies.objects;

import java.io.Serializable;

public class Car implements Serializable {

    double wheelDiameter = 12;//(cm)

    public Car(){}

    public Car(double wheelDiameter){
        this.wheelDiameter = wheelDiameter;
    }

    public double getWheelDiameter() {
        return wheelDiameter;
    }

    public void setWheelDiameter(double wheelDiameter) {
        this.wheelDiameter = wheelDiameter;
    }
}
