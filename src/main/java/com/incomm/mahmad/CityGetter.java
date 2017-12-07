package com.incomm.mahmad;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CityGetter {

    private static final String cityName = "Atlanta";
    public static void main(String[] args) {
        String fileName = "C:\\Users\\mahmad\\IdeaProjects\\BackendWeatherer\\src\\main\\java\\com\\incomm\\mahmad\\city_list.txt";
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cont = content.split("\n");
        for (int i = 0 ; i < cont.length; i++) {
            System.out.println(i);
        }
    }
}
