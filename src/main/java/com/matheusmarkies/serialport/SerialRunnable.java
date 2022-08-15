package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.matheusmarkies.manager.MouseTrapCarManager;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SerialRunnable implements SerialPortPacketListener {

    private final SerialPort port;
    private final MouseTrapCarManager mouseTrapCarManager;

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
        byte[] newData = new byte[port.bytesAvailable()];

        String inputString = new String(newData, StandardCharsets.UTF_8);
        System.out.println("Input data: "+newData.length);

        switch (inputString){
            case "RPM:":
                readType = ReadType.RPM;
                getReadType = false;
                break;

            default:
                if(getReadType)
                    readType = null;

                if(readType != null)
                    switch (readType)
                    {
                        case RPM:
                            double RPM = Double.parseDouble(inputString);
                            mouseTrapCarManager.addEntityRpmList(RPM);
                            System.out.println("RPM: "+RPM);
                            getReadType = true;
                            break;
                    }
                break;
        }

    }
    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
