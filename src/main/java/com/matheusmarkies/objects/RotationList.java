package com.matheusmarkies.objects;

import com.matheusmarkies.manager.RotationManager;

import java.io.Serializable;
import java.util.ArrayList;

public class RotationList implements Serializable {
    ArrayList<Rotations> rotationsArrayList = new ArrayList<>();

    public RotationList(ArrayList<Rotations> rotationsArrayList) {
        this.rotationsArrayList = rotationsArrayList;
    }

    public RotationList() {
    }

    public ArrayList<Rotations> getRotationsArrayList() {
        return rotationsArrayList;
    }

    public void setRotationsArrayList(ArrayList<Rotations> rotationsArrayList) {
        this.rotationsArrayList = rotationsArrayList;
    }
}
