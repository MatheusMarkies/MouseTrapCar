package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.matheusmarkies.manager.MouseTrapCarManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class SerialRunnable implements SerialPortPacketListener, Runnable {

    private final SerialPort port;
    private final MouseTrapCarManager mouseTrapCarManager;

    //int oldSize = 0;
    //ObservableList<XYChart.Data> data = FXCollections.observableArrayList();

    public SerialRunnable(SerialPort port, MouseTrapCarManager mouseTrapCarManager) {
        this.port = port;
        this.mouseTrapCarManager = mouseTrapCarManager;
    }

    @Override
    public int getPacketSize() {
        return SerialReadder.PACKET_SIZE_IN_BYTES;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void run() {
        port.addDataListener(this);
        //mouseTrapCarManager.getMainFrameController().getRotationSeries().getData().add(new XYChart.Data<String,Double>("0",0.));
    }

    enum ReadType{
        RPM
    }

    ReadType readType = null;
    boolean getReadType = true;
    private final byte[] buffer = new byte[2048];

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        byte[] buffer = new byte[port.bytesAvailable()];

        String inputString = new String(buffer, StandardCharsets.UTF_16LE);

        Scanner scanner_stream=  new Scanner( port.getInputStream());
        while(scanner_stream.hasNextLine()) {
            String received_string = scanner_stream.nextLine();

            int received_str_len = received_string.length();
            inputString = received_string;

            //System.out.println("Input data: " + inputString);

            try {
                switch (inputString) {
                    case "R:":
                        readType = ReadType.RPM;
                        getReadType = false;
                        break;

                    default:
                        if (getReadType)
                            readType = null;

                        if (readType != null)
                            switch (readType) {
                                case RPM:
                                    double RPM = Double.parseDouble(inputString);
                                    MouseTrapCarManager.Rotations rotation = mouseTrapCarManager.addEntityRotationsList(Math.abs(RPM));

                                if(rotation != null)
                                    if(RPM != 0.0 && Double.isFinite(rotation.rpm)) {
                                        XYChart.Data data = mouseTrapCarManager.addEntityToRotationsChart(rotation);
                                        if(data!=null)
                                        mouseTrapCarManager.getMainFrameController().getRotationSeries().getData().add(data);
                                        mouseTrapCarManager.getMainFrameController().chartRefresh(true);

                                        System.out.println("R: " + rotation.toString());
                                    }else
                                        mouseTrapCarManager.getMainFrameController().chartRefresh(false);

                                    getReadType = true;
                                    break;
                            }
                        break;
                }
            }catch (Exception exception){//System.err.println(exception);
                 }
        }

    }
    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
