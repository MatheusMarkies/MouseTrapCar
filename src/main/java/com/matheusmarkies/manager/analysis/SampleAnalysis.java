package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.manager.RotationManager;
import com.matheusmarkies.manager.utilities.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleAnalysis {

    public static List<Vector2D> averageSampleFilter(List<Vector2D> data, int filterRange) {
        List<Vector2D> filteredRotations = new ArrayList<>();

        filteredRotations.add(new Vector2D(
                data.get(0).x(),
                data.get(0).y()
        ));

        for(int i = 0; i < data.size();) {
            int sampleHighIndex = i + filterRange;

            if(sampleHighIndex >= data.size()) {
                sampleHighIndex = (data.size() - 1);

                filteredRotations.add(new Vector2D(
                        data.get((data.size() - 1)).x(),
                        data.get((data.size() - 1)).y()
                ));

                break;
            }

            double xAverage = 0;
            double yAverage = 0;

            try {
                for (int u = i; u < (sampleHighIndex); u++) {
                    xAverage += data.get(u).x() / filterRange;
                    yAverage += data.get(u).y() / filterRange;
                }
            }catch (Exception exception){System.err.println(exception);}

            filteredRotations.add(
                    new Vector2D(
                            (double) Math.round(xAverage*1000)/1000,
                            (double) Math.round(yAverage*1000)/1000)
            );

            if(sampleHighIndex < data.size())
                i = sampleHighIndex;
            else
                break;
        }

        return getReorderedList(filteredRotations);
    }

    public static List<Vector2D> getSmoothChart(List<Vector2D> rotations){
        List<Vector2D> smoothedRotations = new ArrayList<>();

        int cte = Math.round(rotations.size() * (3/10));

        for(int i = 0; i < rotations.size();) {
            int sampleHighIndex = i + 6;

            if (sampleHighIndex > rotations.size())
                break;

            Vector2D A = new Vector2D(rotations.get(i).x(),rotations.get(i).y());
            Vector2D B = new Vector2D(rotations.get(i+cte).x(),rotations.get(i+cte).y());
            Vector2D C = new Vector2D(rotations.get(i+Math.round(cte/2)).x(),rotations.get(i+Math.round(cte/2)).y());

            for(float k =0;k<1;k+=0.01f){
                Vector2D curve = Vector2D.bezierCurve(A,C,B,k);
                smoothedRotations.add(curve);
            }

            if(sampleHighIndex < rotations.size())
                i = sampleHighIndex;
            else
                break;
        }
        return getReorderedList(smoothedRotations);
    }

    public static List<Vector2D> getReorderedList(List<Vector2D> list){
        Vector2D[] vectorArray = new Vector2D[list.size()];
        list.toArray(vectorArray);

        Vector2D aux = new Vector2D(0,0);
        int i = 0;

        for(i = 0; i< vectorArray.length; i++){
            for(int j = 0; j<(vectorArray.length-1); j++){
                if(vectorArray[j].x() > vectorArray[j + 1].x()){
                    aux = vectorArray[j];
                    vectorArray[j] = vectorArray[j+1];
                    vectorArray[j+1] = aux;
                }
            }
        }

        return Arrays.asList(vectorArray);
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
