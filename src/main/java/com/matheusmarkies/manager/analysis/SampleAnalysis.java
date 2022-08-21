package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.manager.MouseTrapCarManager;

import java.util.ArrayList;
import java.util.List;

public class SampleAnalysis {

    public static List<MouseTrapCarManager.Rotations> averageSampleFilter(List<MouseTrapCarManager.Rotations> rotations, int filterRange) {
        List<MouseTrapCarManager.Rotations> filteredRotations = new ArrayList<>();

        for(int i = 0; i < rotations.size(); i +=filterRange) {
            int sampleHighIndex = i + filterRange;
            if (sampleHighIndex > rotations.size())
                sampleHighIndex = i + (rotations.size() - i);

            double rotationAvarage = 0;
            double rpmAvarage = 0;
            double deltaTimeAvarage = 0;
            for (int u = i; u < (i + sampleHighIndex); u++) {
                rotationAvarage += rotations.get(u).rotationValue / filterRange;
                rpmAvarage += rotations.get(u).rpm / filterRange;
                deltaTimeAvarage += rotations.get(u).deltaTime / filterRange;
            }
            filteredRotations.add(new MouseTrapCarManager.Rotations(rotationAvarage,rpmAvarage,deltaTimeAvarage));
        }

        return filteredRotations;
    }

}
