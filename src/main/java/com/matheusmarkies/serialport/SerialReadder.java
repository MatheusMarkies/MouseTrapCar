package com.matheusmarkies.serialport;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import java.io.*;

public class SerialReadder implements SerialPortPacketListener {

    private SerialPort serialPort;
    private String serialPortName;

    static int PORT_RATE = 9600;
    public static int PACKET_SIZE_IN_BYTES = 8;

    public SerialReadder(String serialPortName) {
        this.serialPortName = serialPortName;
    }
    public SerialReadder(){
        super();
    }

    public boolean connect(){
        serialPort = SerialPort.getCommPort(this.serialPortName);

        if (serialPort.isOpen())
            return false;
        else {
                serialPort.openPort();
            serialPort.addDataListener(this);
                return true;
        }
    }

    public synchronized void close(){
        if(serialPort.isOpen()){
            serialPort.closePort();
        }
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }


    @Override
    public int getPacketSize() {
        return this.getPacketSize();
    }//

    @Override
    public int getListeningEvents() {
        return this.getListeningEvents();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        String str = new String(newData).split("\n", 2)[0].replaceAll("\\s+", "");
        int byteSize = 0;
        try {
            byteSize = str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException ex) {

        }
        if (byteSize == this.PACKET_SIZE_IN_BYTES) {
            //System.out.println("(Received data of size: " + byteSize + ")" + str);
            System.out.println("Received data: " + str);
        }
    }

}

