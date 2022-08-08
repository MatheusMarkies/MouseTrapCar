package com.matheusmarkies.manager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MouseTrapCarManager {

    public class RPM {
        List<Double> rpmList = new ArrayList<Double>();
        List<String> timeHistory = new ArrayList<String>();
    }

    private RPM rpm = new RPM();

    public void addEntityRpmList(double entity){
        this.rpm.rpmList.add(entity);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalTime localTime = LocalTime.now();

        this.rpm.timeHistory.add(dtf.format(localTime));
    }

    public void setRpmList(List<Double> rpmList) {
        this.rpm.rpmList = rpmList;
    }

    public List<Double> getRpmList() {
        return rpm.rpmList;
    }
}
