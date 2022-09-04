package com.matheusmarkies.objects;

import java.io.Serializable;

public class Car implements Serializable {

    double wheelDiameter = 12;//(cm)
    int encoderTooth = 30;
    int axleTooth = 40;

    public Car(){}

    public Car(double wheelDiameter){
        this.wheelDiameter = wheelDiameter;
    }

    public Car(double wheelDiameter, int encoderTooth, int axleTooth) {
        this.wheelDiameter = wheelDiameter;
        this.encoderTooth = encoderTooth;
        this.axleTooth = axleTooth;
    }

    public int getEncoderTooth() {
        return encoderTooth;
    }

    public void setEncoderTooth(int encoderTooth) {
        this.encoderTooth = encoderTooth;
    }

    public int getAxleTooth() {
        return axleTooth;
    }

    public void setAxleTooth(int axleTooth) {
        this.axleTooth = axleTooth;
    }

    public double getWheelDiameter() {
        return wheelDiameter;
    }

    public void setWheelDiameter(double wheelDiameter) {
        this.wheelDiameter = wheelDiameter;
    }

    public double getAxleTransmissionRatio(){
        return (encoderTooth/axleTooth);
    }
    public double getEncoderTransmissionRatio(){
        return (1/getAxleTransmissionRatio());
    }

}
