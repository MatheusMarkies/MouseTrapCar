package com.matheusmarkies.objects;

import java.io.Serializable;
import java.util.Date;

public class Rotations implements Serializable {
    public double rotationValue;
    public double speed;
    public Date addedTime = new Date();
    public double deltaTime = 0;

    public double rpm;
    public double elapsedTime;
    public double elapsedDistance;

    public enum MovementType{
        ACCELERATED, RETARDED, CONSTANT
    }

    public MovementType movementType;

    public double changeRate;

    public Rotations(){ }

    public Rotations(double rotationValue, double deltaTime,double rpm){
        this.deltaTime = deltaTime;
        this.rpm = rpm;
        this.rotationValue = rotationValue;
    }

    @Override
    public String toString() {
        return "Rotations{" +
                "rotationValue=" + rotationValue +
                ", speed=" + speed +
                ", addedTime=" + addedTime +
                ", deltaTime=" + deltaTime +
                ", rpm=" + rpm +
                ", movementType=" + movementType +
                ", changeRate=" + changeRate +
                '}';
    }

    public double getRotationValue() {
        return rotationValue;
    }

    public void setRotationValue(double rotationValue) {
        this.rotationValue = rotationValue;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getElapsedDistance() {
        return elapsedDistance;
    }

    public void setElapsedDistance(double elapsedDistance) {
        this.elapsedDistance = elapsedDistance;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }
}