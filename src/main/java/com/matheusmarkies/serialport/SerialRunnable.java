package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.matheusmarkies.manager.RotationManager;
import javafx.scene.chart.XYChart;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SerialRunnable implements SerialPortPacketListener, Runnable {

    private final SerialPort port;
    private final RotationManager rotationManager;

    //int oldSize = 0;
    //ObservableList<XYChart.Data> data = FXCollections.observableArrayList();

    public SerialRunnable(SerialPort port, RotationManager rotationManager) {
        this.port = port;
        this.rotationManager = rotationManager;
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
    float rot=0;
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
                        if (getReadType) {
                            readType = null;
                        }
                        if (readType != null)
                            switch (readType) {
                                case RPM:
                                    double CPR = Double.parseDouble(inputString);
                                    double RPM = CPR/2000;
                                    RotationManager.Rotations rotation = rotationManager.addEntityRotationsList(Math.abs(RPM));

                                    if (rotation != null)
                                        if (RPM != 0.0 && Double.isFinite(rotation.rpm)) {
                                            rotationManager.getRotationsHistory().add(rotation);
                                            XYChart.Data data = rotationManager.addEntityToRotationsChart(rotation);
                                            if (data != null)
                                                //rotationManager.getMainFrameController().getRotationSeries().getData().add(data);
                                                rotationManager.getMainFrameController().chartRefresh(true);
                                            rot += CPR;
                                            System.out.println("R: "+rot);
                                        } else
                                            rotationManager.getMainFrameController().chartRefresh(false);

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
