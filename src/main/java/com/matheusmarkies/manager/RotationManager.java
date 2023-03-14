package com.matheusmarkies.manager;

import com.matheusmarkies.MainFrameController;
import com.matheusmarkies.manager.utilities.KalmanFilter;
import com.matheusmarkies.objects.RotationList;
import com.matheusmarkies.objects.Rotations;
import javafx.scene.chart.XYChart;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RotationManager {

    private MainFrameController mainFrameController;
    private RotationList rotationList = new RotationList();
    KalmanFilter kalmanFilter = new KalmanFilter(0, 1, 0.01, 0.1);
    public Rotations addEntityRotationsList(double entity){
        try {
            double deltaTime = 0.01f;
            Rotations rpm = new Rotations();

            double measurement = entity;
            double filteredValue = kalmanFilter.update(measurement);

            rpm.rotationValue = (filteredValue / mainFrameController.getCar().getEncoderCPR())
                    * mainFrameController.getCar().getAxleTransmissionRatio();

            rpm.elapsedTime = 0;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalTime localTimeNow = LocalTime.now();

            LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

            Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

            rpm.addedTime = (Date.from(instant));

            double deltaTimeComparedToPrevious = 0;
            double changeRate = 0;

            if (rotationList.getRotationsArrayList().size() > 0) {
                deltaTime = (double)(rpm.addedTime.getTime() - rotationList.getRotationsArrayList().get(rotationList.getRotationsArrayList().size()-1).addedTime.getTime())/1000;
                deltaTime = (double) Math.round(deltaTime * 10000)/10000;

                rpm.elapsedTime =(rpm.addedTime.getTime() -  rotationList.getRotationsArrayList().get(0).getAddedTime().getTime())/1000;
            }

            rpm.deltaTime = deltaTime;
            rpm.rpm = (rpm.rotationValue/deltaTime) * 60;
//
            float arc = (float)(rpm.rotationValue * 360);
            rpm.elapsedDistance = Math.toRadians(arc) * (mainFrameController.getCar().getWheelDiameter()/2);

            rpm.speed = (rpm.rpm * ((2*Math.PI/60) *(mainFrameController.getCar().getWheelDiameter()/200)));
            rpm.speed = (double) Math.round(rpm.speed * 100)/100;

            if (rotationList.getRotationsArrayList().size() > 0) {
                changeRate = (double)(rpm.speed - rotationList.getRotationsArrayList().get(rotationList.getRotationsArrayList().size()-1).speed) /
                        ((double)(rpm.addedTime.getTime() - rotationList.getRotationsArrayList().get(rotationList.getRotationsArrayList().size()-1).addedTime.getTime())/1000);
            }

            if(changeRate > 0)
                rpm.movementType = Rotations.MovementType.ACCELERATED;
            else if(changeRate == 0)
                rpm.movementType = Rotations.MovementType.CONSTANT;
            else
                rpm.movementType = Rotations.MovementType.RETARDED;

            rpm.changeRate = changeRate;
            rpm.changeRate = (double) Math.round(rpm.changeRate * 100)/100;

            return rpm;
        }catch (Exception e){ System.err.println("addEntityRotationsList "+e); }
        return null;
    }

    public XYChart.Data<String, Double> addEntityToRotationsChart(Rotations r){
        try {
            if (mainFrameController != null) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();

                data = new XYChart.Data<String, Double>((double) r.deltaTime + "", r.rotationValue);

                rotationList.getRotationsArrayList().add(r);

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
        return rotationList.getRotationsArrayList();
    }

    public RotationList getRotationList() {
        return rotationList;
    }

    public void setRotationList(RotationList rotationList) {
        this.rotationList = rotationList;
    }

    public MainFrameController getMainFrameController() {
        return mainFrameController;
    }
    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
