package com.matheusmarkies.manager;

import com.matheusmarkies.MainFrameController;
import javafx.scene.chart.XYChart;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RotationManager {

    private MainFrameController mainFrameController;

    public static class Rotations {
        public double rotationValue;
        public double speed;
        public Date addedTime = new Date();
        public double deltaTime = 0;

        public double rpm;

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
    }

    private List<Rotations> rotationsHistory = new ArrayList<>();

    public Rotations addEntityRotationsList(double entity){
        try {
            Rotations rpm = new Rotations();
            rpm.rotationValue = entity;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalTime localTimeNow = LocalTime.now();

            LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

            Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

            rpm.addedTime = (Date.from(instant));

            double deltaTimeComparedToPrevious = 0;
            double deltaTime = 0;
            double changeRate = 0;

            rpm.speed = (entity/0.1) * Math.PI *(mainFrameController.getCar().getWheelDiameter()/100);
            rpm.speed = (double) Math.round(rpm.speed * 100)/100;

            if (rotationsHistory.size() > 0) {
                deltaTime = (double)(rpm.addedTime.getTime() - rotationsHistory.get(0).addedTime.getTime())/1000;
                deltaTime = (double) Math.round(deltaTime * 100)/100;

                changeRate = (double)(rpm.speed - rotationsHistory.get(rotationsHistory.size()-1).speed) /
                        ((double)(rpm.addedTime.getTime() - rotationsHistory.get(rotationsHistory.size()-1).addedTime.getTime())/1000);
            }

            if(changeRate > 0)
                rpm.movementType = Rotations.MovementType.ACCELERATED;
            else if(changeRate == 0)
                rpm.movementType = Rotations.MovementType.CONSTANT;
            else
                rpm.movementType = Rotations.MovementType.RETARDED;

            rpm.changeRate = changeRate;
            rpm.changeRate = (double) Math.round(rpm.changeRate * 100)/100;

            rpm.deltaTime = deltaTime;
            rpm.rpm = (entity/0.1) * 60;

            return rpm;
        }catch (Exception e){ System.err.println("addEntityRotationsList "+e); }
        return null;
    }

    public XYChart.Data<String, Double> addEntityToRotationsChart(Rotations r){
        try {
            if (mainFrameController != null) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();

                data = new XYChart.Data<String, Double>((double) r.deltaTime + "", r.rotationValue);

                rotationsHistory.add(r);

                //if(r.deltaTime.getSeconds() != rotationsHistory.get(rotationsHistory.size()-1).deltaTime.getSeconds())
                    return data;
                //else
                    //return null;
            }
        }catch (Exception exception){System.err.println(exception);}
    return null;
    }

    public RotationManager() { }

    public RotationManager(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    public List<Rotations> getRotationsHistory() {
        return rotationsHistory;
    }
    public MainFrameController getMainFrameController() {
        return mainFrameController;
    }
    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
