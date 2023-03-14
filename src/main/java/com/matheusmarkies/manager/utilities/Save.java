package com.matheusmarkies.manager.utilities;

import com.matheusmarkies.objects.Car;

import java.io.*;

public class Save {

    public static String ApplicationFolder = System.getenv("APPDATA") + "\\Mouse Trap Car";
    public static String[] ImportantDirectories = new String[]{
            ApplicationFolder + "\\" + "data"
    };

    public static void write(Object o, String filename) throws IOException {
        try {
            File selectedFile = new File(filename);

            FileOutputStream fileOutput = new FileOutputStream(selectedFile);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileOutput);

            objectStream.writeObject(o);

            objectStream.close();
            fileOutput.close();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static Object read(File file){
        Object o = null;
        try {
            File selectedFile = file;

            FileInputStream fileInput = new FileInputStream(selectedFile);
            ObjectInputStream objectStream = new ObjectInputStream(fileInput);

            o = objectStream.readObject();

            objectStream.close();
            fileInput.close();

        }catch (Exception e){
            System.err.println(e);
        }
        return o;
    }


}
