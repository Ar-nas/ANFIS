package com.ar.nas.manfis.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ARNAS_
 */
public class AnfisModel {
    
    double a[][];
    double b[][];
    double p[][][];
    
    public void saveModel(double[][] A, double[][] B, double[][][] P) {
        a = A;
        b = B;
        p = P;
    }
       
    public double[][] getA() {
        return a;
    }
    
    public double[][] getB() {
        return b;
    }
    
    public double[][][] getP() {
        return p;
    }
    
    public void saveToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("A "+a.length+" "+a[0].length);
            writer.newLine();
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    writer.write(String.valueOf(i)+" "+String.valueOf(j)+" "+String.valueOf(a[i][j]));
                    writer.newLine();
                }
            }
            writer.write("B "+b.length+" "+b[0].length);
            writer.newLine();
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[i].length; j++) {
                    writer.write(String.valueOf(i)+" "+String.valueOf(j)+" "+String.valueOf(b[i][j]));
                    writer.newLine();
                }
            }
            writer.write("P "+p.length+" "+p[0].length+" "+p[0][0].length);
            writer.newLine();
            for (int i = 0; i < p.length; i++) {
                for (int j = 0; j < p[i].length; j++) {
                    for (int k = 0; k < p[i][j].length; k++) {
                        writer.write(String.valueOf(i)+" "+String.valueOf(j)+" "+String.valueOf(k)+" "+String.valueOf(p[i][j][k]));
                        writer.newLine();
                    }
                }
            }
            writer.flush();
        }
    }
    
    public void loadFromFile(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] part = reader.readLine().split(" ");
        if (part[0].equals("A")) {
            a = new double[Integer.parseInt(part[1])][Integer.parseInt(part[2])];
            part = reader.readLine().split(" ");
            do {    
                a[Integer.parseInt(part[0])][Integer.parseInt(part[1])] = Double.parseDouble(part[2]);
                part = reader.readLine().split(" ");
            } while (!part[0].equals("B"));
            b = new double[Integer.parseInt(part[1])][Integer.parseInt(part[2])];
            part = reader.readLine().split(" ");
            do {
                b[Integer.parseInt(part[0])][Integer.parseInt(part[1])] = Double.parseDouble(part[2]);
                part = reader.readLine().split(" ");
            } while (!part[0].equals("P"));
            p = new double[Integer.parseInt(part[1])][Integer.parseInt(part[2])][Integer.parseInt(part[3])];
            String line;
            while ((line = reader.readLine()) != null) {
                part = line.split(" ");
                p[Integer.parseInt(part[0])][Integer.parseInt(part[1])][Integer.parseInt(part[2])] = Double.parseDouble(part[3]);
            }
        } else {
            throw new Exception("wrong file");
        }
    }
    
}
