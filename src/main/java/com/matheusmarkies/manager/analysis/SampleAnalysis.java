package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.manager.RotationManager;
import com.matheusmarkies.manager.utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class SampleAnalysis {

    public static List<RotationManager.Rotations> averageSampleFilter(List<RotationManager.Rotations> rotations, int filterRange) {
        List<RotationManager.Rotations> filteredRotations = new ArrayList<>();

        filteredRotations.add(new RotationManager.Rotations(
                rotations.get(0).speed,
                rotations.get(0).deltaTime,
                rotations.get(0).rpm)
        );

        for(int i = 0; i < rotations.size();) {
            int sampleHighIndex = i + filterRange;

            if(sampleHighIndex >= rotations.size()) {
                sampleHighIndex = (rotations.size() - 1);

                filteredRotations.add(new RotationManager.Rotations(
                        rotations.get((rotations.size() - 1)).speed,
                        rotations.get((rotations.size() - 1)).deltaTime,
                        rotations.get((rotations.size() - 1)).rpm)
                );

                break;
            }

            double rotationAvarage = 0;
            double rpmAvarage = 0;
            double deltaTimeAvarage = 0;
            try {
                for (int u = i; u < (sampleHighIndex); u++) {
                    rotationAvarage += rotations.get(u).speed / filterRange;
                    rpmAvarage += rotations.get(u).rpm / filterRange;
                    deltaTimeAvarage += rotations.get(u).deltaTime / filterRange;
                }
            }catch (Exception exception){System.err.println(exception);}

            filteredRotations.add(
                    new RotationManager.Rotations(
                    (double) Math.round(rotationAvarage*1000)/1000
                    ,(double) Math.round(deltaTimeAvarage*1000)/1000,
                    rpmAvarage)
            );

            if(sampleHighIndex < rotations.size())
                i = sampleHighIndex;
            else
                break;
        }

        return filteredRotations;
    }

    public static List<RotationManager.Rotations> getSmoothChart(List<RotationManager.Rotations> rotations){
        List<RotationManager.Rotations> smoothedRotations = new ArrayList<>();

        for(int i = 0; i < rotations.size();) {
            int sampleHighIndex = i + 6;

            if (sampleHighIndex > rotations.size())
                break;

            Vector2D A = new Vector2D(rotations.get(i).deltaTime,rotations.get(i).speed);
            Vector2D B = new Vector2D(rotations.get(i+5).deltaTime,rotations.get(i+5).speed);
            Vector2D C = new Vector2D(rotations.get(i+3).deltaTime,rotations.get(i+3).speed);

            for(float k =0;k<1;k+=0.2f){
                Vector2D curve = Vector2D.bezierCurve(A,C,B,k);
                smoothedRotations.add(new RotationManager.Rotations(
                        (double) Math.round(curve.y()*1000)/1000,
                        (double) Math.round(curve.x()*1000)/1000
                        ,0)
                );
            }

            if(sampleHighIndex < rotations.size())
                i = sampleHighIndex;
            else
                break;
        }
        return smoothedRotations;
    }

    public static List<RotationManager.Rotations> getFrequencyCurve(List<RotationManager.Rotations> rotations){
        List<RotationManager.Rotations> smoothedRotations = new ArrayList<>();

        RotationManager.Rotations maxAmplitude = null;
        for(int i = 0; i < rotations.size();i++) {
            if(i == 0)
                maxAmplitude = rotations.get(0);
            else
            if(maxAmplitude.speed <= rotations.get(i).speed)
                maxAmplitude = rotations.get(i);
        }

        Vector2D A = new Vector2D(rotations.get(0).deltaTime,rotations.get(0).speed);
        Vector2D B = new Vector2D(rotations.get(rotations.size()-1).deltaTime,rotations.get(rotations.size()-1).speed);
        Vector2D C = new Vector2D(maxAmplitude.deltaTime,maxAmplitude.speed);

        for(double k =0;k < 1;k+=0.1f){
           Vector2D curve = Vector2D.bezierCurve(A,C,B,k);
           smoothedRotations.add(new RotationManager.Rotations(
                   (double) Math.round(curve.y()*1000)/1000,
                   (double) Math.round(curve.x()*1000)/1000,
                   (curve.y()/0.1f)*60)
           );
        }

        smoothedRotations.add(new RotationManager.Rotations(A.y(),A.x(),(A.y()/0.1f)*60));
        smoothedRotations.add(new RotationManager.Rotations(B.y(),B.x(),(A.y()/0.1f)*60));
        //smoothedRotations.add(new RotationManager.Rotations(C.y(),C.x(),(A.y()/0.1f)*60));

        return smoothedRotations;
    }

}
