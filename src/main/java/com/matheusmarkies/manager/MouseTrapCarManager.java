package com.matheusmarkies.manager;

import com.matheusmarkies.MainFrameController;
import javafx.scene.chart.XYChart;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MouseTrapCarManager{

    private MainFrameController mainFrameController;

    public class Rotations {
        public double RPMValue;
        public Date RPMTime = new Date();

        @Override
        public String toString() {
            return "Rotations{" +
                    "Rotations Value=" + RPMValue +
                    ", Rotations Delta Time=" + RPMTime +
                    '}';
        }
    }

    private List<Rotations> rpmHistory = new ArrayList<>();

    public Rotations addEntityRpmList(double entity){
        Rotations rpm = new Rotations();
        rpm.RPMValue = entity;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalTime localTimeNow = LocalTime.now();

        LocalDateTime localTime = localTimeNow.atDate(LocalDate.now());

        Instant instant = localTime.atZone(ZoneId.systemDefault()).toInstant();

        rpm.RPMTime = (Date.from(instant));
        addEntityToLineChart(rpm);

        return rpm;
    }

    public XYChart.Data<String, Double> addEntityToLineChart(Rotations r){
        try {
            if (mainFrameController != null) {
                mainFrameController.getLineChart().setTitle("RPM/Time");

                XYChart.Data data = new XYChart.Data<Double, Integer>();

                int timeDelta = 0;

                if(rpmHistory.size() > 0)
                timeDelta = (int) ((r.RPMTime.getTime() - rpmHistory.get(0).RPMTime.getTime())/1000);

                data = new XYChart.Data<String, Double>((double) timeDelta + "", r.RPMValue);

                rpmHistory.add(r);
                return data;
            }
        }catch (Exception exception){System.err.println(exception);}
    return null;
    }

    public MouseTrapCarManager() { }

    public MouseTrapCarManager(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    public List<Rotations> getRpmHistory() {
        return rpmHistory;
    }
    public MainFrameController getMainFrameController() {
        return mainFrameController;
    }
    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
