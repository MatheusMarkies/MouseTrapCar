package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.MainFrameController;
import com.matheusmarkies.manager.RotationManager;
import com.matheusmarkies.manager.utilities.Vector2D;
import com.matheusmarkies.objects.Rotations;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartIntegration implements Runnable{

    private XYChart.Series<String, Double> rotationSeries = new XYChart.Series<String, Double>();
    private XYChart.Series<String, Double> averageSeries = new XYChart.Series<String, Double>();
    private XYChart.Series<String, Double> smoothedSeries = new XYChart.Series<String, Double>();

    public static class MovementDetailsChart {
        public XYChart.Series acceleratedSeries = new XYChart.Series();
        public XYChart.Series constantSeries = new XYChart.Series();
        public XYChart.Series retardedSeries = new XYChart.Series();
    }

    private MovementDetailsChart movementDetailsChart = new MovementDetailsChart();

    private MainFrameController mainFrameController;

    enum ColumnType{ROTATION,SPEED,TIME, DISTANCE, RPM}

    public ChartIntegration(){ }

    public ChartIntegration(MainFrameController mainFrameController){
        this.mainFrameController = mainFrameController;
    }

    @Override
    public void run() {

    }

    boolean seriesAdded = false;

    public void setAnalysisToChart(int sampleIndex) {
        reset();

        rotationSeries.getData().addAll(addEntityToAverageChart(ColumnType.TIME,ColumnType.RPM));
        rotationSeries.setName("Rotacao");
        averageSeries.getData().addAll(addEntityToDistanceAverageChart());
        averageSeries.setName("Distancia");
        smoothedSeries.getData().addAll(addEntityToSmoothedChart(ColumnType.TIME,ColumnType.SPEED));
        smoothedSeries.setName("Velocidade");
        movementDetailsChart.acceleratedSeries.getData().addAll(addMovementDetailsCurve().get(0));
        movementDetailsChart.constantSeries.getData().addAll(addMovementDetailsCurve().get(1));
        movementDetailsChart.retardedSeries.getData().addAll(addMovementDetailsCurve().get(2));
    }

    public void reset() {
        rotationSeries.getData().clear();
        averageSeries.getData().clear();
        smoothedSeries.getData().clear();
        movementDetailsChart.acceleratedSeries.getData().clear();
        movementDetailsChart.constantSeries.getData().clear();
        movementDetailsChart.retardedSeries.getData().clear();
    }

    public Collection<XYChart.Data<String, Double>> addEntityToAverageChart(ColumnType X, ColumnType Y) {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();

        List<Vector2D> dataVector = getDataVector(X,Y);

        try {
            List<Vector2D> averageRotations = SampleAnalysis.movingAverageFilter(
                    dataVector, 4
            );

            for (Vector2D rotations : averageRotations) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double)Math.round(rotations.x()*1000)/1000 + "", (double)Math.round(rotations.y()*1000)/1000);
                dataList.add(data);
            }
        }catch (Exception exception){/*System.err.println("addEntityToAverageChart "+exception);*/}

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityToDistanceAverageChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();

        List<Vector2D> dataVector = getDataVector(ColumnType.TIME,ColumnType.DISTANCE);
        double distance = 0;

        try {
            List<Vector2D> averageRotations = SampleAnalysis.movingAverageFilter(
                    dataVector, 4
            );

            for (Vector2D rotations : dataVector) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();

                distance += rotations.y()/100;
                //System.out.println(distance);
                data = new XYChart.Data<String, Double>((double)Math.round(rotations.x()*1000)/1000 + "",
                        (double)Math.round(distance*1000)/1000);
                dataList.add(data);
            }
        }catch (Exception exception){/*System.err.println("addEntityToAverageChart "+exception);*/}

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityToSmoothedChart(ColumnType X, ColumnType Y) {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();

        List<Vector2D> dataVector = getDataVector(X,Y);

        try {
            List<Vector2D>  smoothedRotations = SampleAnalysis.getSmoothChart(
                    dataVector
            );

            for (Vector2D rotations : smoothedRotations) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double)Math.round(rotations.x()*1000)/1000  + "",
                        (double)Math.round(rotations.y()*1000)/1000);
                dataList.add(data);
            }
        }catch (Exception exception){/*System.err.println("addEntityToSmoothedChart "+exception);*/}

        return dataList;
    }

    public List<List<XYChart.Data>> addMovementDetailsCurve() {
        List<List<XYChart.Data>> dataList = new ArrayList<>();
        dataList.add(new ArrayList<>());
        dataList.add(new ArrayList<>());
        dataList.add(new ArrayList<>());
        try {
            List<Vector2D> dataVector = getDataVector(ColumnType.TIME,ColumnType.SPEED);
            List<Vector2D>  averageSamples = SampleAnalysis.averageSampleFilter(
                    dataVector, 2
            );

            for (int i =0;i<dataVector.size();i++) {
                XYChart.Data data = new XYChart.Data<String, Integer>();
                XYChart.Data secData = new XYChart.Data<String, Integer>();
                data = new XYChart.Data<String, Double>(""+(double)Math.round(averageSamples.get(i).x()*1000)/1000,
                        (double)Math.round(averageSamples.get(i).y()*1000)/1000);

                secData = new XYChart.Data<String, Double>(""+(double)Math.round(averageSamples.get(i).x()*1000)/1000,
                        0.0);

                int index = 0;

                if(mainFrameController
                        .getMouseTrapCarManager()
                        .getRotationsHistory().get(i)
                        .movementType == Rotations.MovementType.CONSTANT)
                    index = 1;
                else if(mainFrameController
                        .getMouseTrapCarManager()
                        .getRotationsHistory().get(i)
                        .movementType == Rotations.MovementType.RETARDED)
                    index = 2;

                switch (index){
                    case 0:
                        dataList.get(0).add(data);
                        dataList.get(1).add(secData);
                        dataList.get(2).add(secData);
                        break;
                    case 1:
                        dataList.get(0).add(secData);
                        dataList.get(1).add(data);
                        dataList.get(2).add(secData);
                        break;
                    case 2:
                        dataList.get(0).add(secData);
                        dataList.get(1).add(secData);
                        dataList.get(2).add(data);
                        break;
                }

            }
        }catch (Exception exception){/*System.err.println("addFrequencyCurve "+exception);*/}

        return dataList;
    }

    public List<Vector2D> getDataVector(ColumnType X, ColumnType Y){
        List<Vector2D> dataVector = new ArrayList<>();
        for (Rotations rotations : mainFrameController.getMouseTrapCarManager().getRotationsHistory()) {
            Vector2D a = new Vector2D(0 , 0);
            switch (X){
                case RPM:
                    a.x(rotations.getRpm());
                    break;
                case ROTATION:
                    a.x(rotations.getRotationValue());
                    break;
                case TIME:
                    a.x(rotations.getElapsedTime());
                    break;
                case SPEED:
                    a.x(rotations.getSpeed());
                    break;
                case DISTANCE:
                    a.x(rotations.getElapsedDistance());
                    break;
            }
            switch (Y){
                case RPM:
                    a.y(rotations.rpm);
                    break;
                case ROTATION:
                    a.y(rotations.rotationValue);
                    break;
                case TIME:
                    a.y(rotations.elapsedTime);
                    break;
                case SPEED:
                    a.y(rotations.speed);
                    break;
                case DISTANCE:
                    a.y(rotations.elapsedDistance);
                    break;
            }
            dataVector.add(a);
        }
        return dataVector;
    }

    public XYChart.Series<String, Double> getRotationSeries() {
        return rotationSeries;
    }

    public XYChart.Series<String, Double> getAverageSeries() {
        return averageSeries;
    }

    public XYChart.Series<String, Double> getSmoothedSeries() {
        return smoothedSeries;
    }

    public MovementDetailsChart getMovementDetailsChart() {
        return movementDetailsChart;
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

}
