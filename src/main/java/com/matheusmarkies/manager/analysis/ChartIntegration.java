package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.MainFrameController;
import com.matheusmarkies.manager.RotationManager;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartIntegration implements Runnable{

    private XYChart.Series<String, Double> rotationSeries = new XYChart.Series<String, Double>();
    private XYChart.Series<String, Double> averageSeries = new XYChart.Series<String, Double>();
    private XYChart.Series<String, Double> smoothedSeries = new XYChart.Series<String, Double>();
    private XYChart.Series<String, Double> frequecySeries = new XYChart.Series<String, Double>();

    private MainFrameController mainFrameController;

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

        rotationSeries.getData().addAll(addEntityToRotationChart());
        averageSeries.getData().addAll(addEntityToAverageChart());
        smoothedSeries.getData().addAll(addEntityToSmoothedChart());
        frequecySeries.getData().addAll(addFrequencyCurve());
        System.out.println();
        System.out.println("Rotation: "+mainFrameController.getMouseTrapCarManager().getRotationsHistory().size());
        System.out.println("Average: "+addEntityToAverageChart().size());
        System.out.println("Smoothed: "+addEntityToSmoothedChart().size());
    }

    public void reset() {
        rotationSeries.getData().clear();
        averageSeries.getData().clear();
        smoothedSeries.getData().clear();
        frequecySeries.getData().clear();
    }

    public Collection<XYChart.Data<String, Double>> addEntityToRotationChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        try {
            List<RotationManager.Rotations> averageRotations = SampleAnalysis.averageSampleFilter(
                    mainFrameController.getMouseTrapCarManager().getRotationsHistory(), 4
            );

            for (RotationManager.Rotations rotations : mainFrameController.getMouseTrapCarManager().getRotationsHistory()) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double) rotations.deltaTime + "", rotations.rotationValue/0.1);
                dataList.add(data);
            }
        }catch (Exception exception){System.err.println("addEntityToRotationChart "+exception);}

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityToAverageChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        try {
            List<RotationManager.Rotations> averageRotations = SampleAnalysis.averageSampleFilter(
                    mainFrameController.getMouseTrapCarManager().getRotationsHistory(), 4
            );

            for (RotationManager.Rotations rotations : averageRotations) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double) rotations.deltaTime + "", rotations.rotationValue);
                dataList.add(data);
            }
        }catch (Exception exception){System.err.println("addEntityToAverageChart "+exception);}

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addEntityToSmoothedChart() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        try {
            List<RotationManager.Rotations> smoothedRotations = SampleAnalysis.getSmoothChart(
                    mainFrameController.getMouseTrapCarManager().getRotationsHistory()
            );

            for (RotationManager.Rotations rotations : smoothedRotations) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double) rotations.deltaTime + "", rotations.rotationValue);
                dataList.add(data);
            }
        }catch (Exception exception){System.err.println("addEntityToSmoothedChart "+exception);}

        return dataList;
    }

    public Collection<XYChart.Data<String, Double>> addFrequencyCurve() {
        List<XYChart.Data<String, Double>> dataList = new ArrayList<>();
        try {
            List<RotationManager.Rotations> smoothedRotations = SampleAnalysis.getFrequencyCurve(
                    mainFrameController.getMouseTrapCarManager().getRotationsHistory()
            );

            for (RotationManager.Rotations rotations : smoothedRotations) {
                XYChart.Data data = new XYChart.Data<Double, Integer>();
                data = new XYChart.Data<String, Double>((double) rotations.deltaTime + "", rotations.rotationValue);
                dataList.add(data);
            }
        }catch (Exception exception){System.err.println("addFrequencyCurve "+exception);}

        return dataList;
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

    public XYChart.Series<String, Double> getFrequecySeries() {
        return frequecySeries;
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

}
