package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.matheusmarkies.manager.RotationManager;
import javafx.application.Platform;

public class SerialReadder{

    final RotationManager rotationManager;

    private SerialPort serialPort;
    private String serialPortName;

    static int PORT_RATE = 9600;
    public static int PACKET_SIZE_IN_BYTES = 8;

    public SerialReadder(RotationManager rotationManager, String serialPortName) {
        this.rotationManager = rotationManager;
        this.serialPortName = serialPortName;
    }

    public SerialReadder(){
        super();
        rotationManager = null;
    }

    public boolean connect(){
        SerialPort[] serialPorts = SerialManager.getSerialPortList();

        for (SerialPort port: serialPorts)
            if(port.getDescriptivePortName().equals(serialPortName)) {
                serialPort = port;
                break;
            }

        if (serialPort.isOpen())
            return false;
        else {
            serialPort.openPort();

            serialPort.setBaudRate(SerialReadder.PORT_RATE);

            Platform.runLater(
                    new SerialRunnable(serialPort, rotationManager)
                );

            return serialPort.isOpen();
        }
    }

    public synchronized void close(){
        if(serialPort.isOpen())
            serialPort.closePort();
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

}

