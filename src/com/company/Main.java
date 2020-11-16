package com.company;

import java.io.BufferedReader;
import java.io.FileReader;



public class Main {


    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String ls = System.getProperty("line.separator");
            while (line != null) {
                sb.append(line);
                sb.append(ls);
                line = br.readLine();
            }
            String fromFile = sb.toString();
            new DKA(fromFile);
        } catch (Exception ex) {
            System.out.println("Ошибка чтения файла!");
        }

    }


}
