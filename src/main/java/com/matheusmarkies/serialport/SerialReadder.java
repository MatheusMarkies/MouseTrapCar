package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.matheusmarkies.manager.MouseTrapCarManager;
import javafx.application.Platform;

public class SerialReadder{

    final MouseTrapCarManager mouseTrapCarManager;

    private SerialPort serialPort;
    private String serialPortName;

    static int PORT_RATE = 9600;
    public static int PACKET_SIZE_IN_BYTES = 8;

    public SerialReadder(MouseTrapCarManager mouseTrapCarManager, String serialPortName) {
        this.mouseTrapCarManager = mouseTrapCarManager;
        this.serialPortName = serialPortName;
    }

    public SerialReadder(){
        super();
        mouseTrapCarManager = null;
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
                    new SerialRunnable(serialPort, mouseTrapCarManager)
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

