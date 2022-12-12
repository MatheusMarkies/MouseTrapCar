package com.matheusmarkies.objects;

import java.io.Serializable;

public class Car implements Serializable {

    double wheelDiameter = 12;//(cm)
    int encoderTooth = 30;
    int axleTooth = 40;
    int encoderCPR = 500;

    public Car(){}

    public Car(double wheelDiameter){
        this.wheelDiameter = wheelDiameter;
    }

    public Car(double wheelDiameter, int encoderTooth, int axleTooth, int encoderCPR) {
        this.wheelDiameter = wheelDiameter;
        this.encoderTooth = encoderTooth;
        this.axleTooth = axleTooth;
        this.encoderCPR = encoderCPR;
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
        return 0.75;//(encoderTooth/axleTooth);
    }
    public double getEncoderTransmissionRatio(){
        return (1/getAxleTransmissionRatio());
    }

    public int getEncoderCPR() {
        return 500;//encoderCPR;
    }

    public void setEncoderCPR(int encoderCPR) {
        this.encoderCPR = encoderCPR;
    }
}
