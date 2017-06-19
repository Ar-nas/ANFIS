package com.ar.nas.manfis.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * To ANFIS training log handler
 * @author ARNAS_
 */
public class AnfisLog {
    
    String mfType;
    int nRule;
    double learningRate;
    double momentum;
    int epoch;
    int duration;
    
    ArrayList<Double> rmseTr;
    ArrayList<Double> rmseTs;
    ArrayList<Double> accTr;
    ArrayList<Double> accTs;
    
    public AnfisLog(String mfType, int nRule, double learningRate, double momentum) {
        this.mfType = mfType;
        this.nRule = nRule;
        this.learningRate = learningRate;
        this.momentum = momentum;
        epoch = 0;
        duration = 0;
        rmseTr = new ArrayList();
        rmseTs = new ArrayList();
        accTr = new ArrayList();
        accTs = new ArrayList();
    }
    
    public void updateLog(int epoch, int duration, double rmseTr, double accTr, double rmseTs, double accTs) {
        this.epoch = epoch;
        this.duration = duration;
        this.rmseTr.add(rmseTr);
        this.accTr.add(accTr);
        this.rmseTs.add(rmseTs);
        this.accTs.add(accTs);
    }
    
    public void saveToFile(File file, String duration) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("mf_type "+mfType);writer.newLine();
            writer.write("number_of_rule "+String.valueOf(nRule));writer.newLine();
            writer.write("learning_rate "+String.valueOf(learningRate));writer.newLine();
            writer.write("momentum "+String.valueOf(momentum));writer.newLine();
            writer.write("epoch "+String.valueOf(epoch));writer.newLine();
            writer.write("duration "+duration);writer.newLine();
            writer.write("rmseTr");writer.newLine();
            for (double d : rmseTr) {
                writer.write(String.valueOf(d));writer.newLine();
            }
            writer.write("rmseV");writer.newLine();
            for (double d : rmseTs) {
                writer.write(String.valueOf(d));writer.newLine();
            }
            writer.write("accTr");writer.newLine();
            for (double d : accTr) {
                writer.write(String.valueOf(d));writer.newLine();
            }
            writer.write("accV");writer.newLine();
            for (double d : accTs) {
                writer.write(String.valueOf(d));writer.newLine();
            }
            writer.flush();
        }
    }

    public void loadFromFile(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] part = reader.readLine().split(" ");
        if (part[0].equals("mf_type")) {
            mfType = part[1];
        } else {
            throw new Exception("wrong file");
        }
        part = reader.readLine().split(" ");
        if (part[0].equals("number_of_rule")) {
            nRule = Integer.parseInt(part[1]);
        } else {
            throw new Exception("wrong file");
        }
        part = reader.readLine().split(" ");
        if (part[0].equals("learning_rate")) {
            learningRate = Double.parseDouble(part[1]);
        } else {
            throw new Exception("wrong file");
        }
        part = reader.readLine().split(" ");
        if (part[0].equals("momentum")) {
            momentum = Double.parseDouble(part[1]);
        } else {
            throw new Exception("wrong file");
        }
        part = reader.readLine().split(" ");
        if (part[0].equals("epoch")) {
            epoch = Integer.parseInt(part[1]);
        } else {
            throw new Exception("wrong file");
        }
        part = reader.readLine().split(" ");
        if (part[0].equals("duration")) {
            duration = Integer.parseInt(part[1]);
        } else {
            throw new Exception("wrong file");
        }
        if (reader.readLine().equals("rmseTr")) {
            rmseTr.clear();
            String line;
            while (!(line = reader.readLine()).equals("rmseV")) {
                rmseTr.add(Double.valueOf(line));
            }
            rmseTs.clear();
            while (!(line = reader.readLine()).equals("accTr")) {
                rmseTs.add(Double.valueOf(line));
            }
            accTr.clear();
            while (!(line = reader.readLine()).equals("accV")) {
                accTr.add(Double.valueOf(line));
            }
            accTs.clear();
            while ((line = reader.readLine()) != null) {
                accTs.add(Double.valueOf(line));
            }
        } else {
            throw new Exception("wrong file");
        }
    }

    public String getMembershipFunctionType() {
        return mfType;
    }

    public int getNumberOfRule() {
        return nRule;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public int getEpoch() {
        return epoch;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<Double> getTrainingRMSE() {
        return rmseTr;
    }

    public double getLastTrainingRMSE() {
        return rmseTr.get(epoch);
    }
    
    public ArrayList<Double> getTestingRMSE() {
        return rmseTs;
    }

    public double getLastTestingRMSE() {
        return rmseTs.get(epoch);
    }
    
    public ArrayList<Double> getTrainingAccuracy() {
        return accTr;
    }

    public double getLastTrainingAccuracy() {
        return accTr.get(epoch);
    }
    
    public ArrayList<Double> getTestingAccuracy() {
        return accTs;
    }
    
    public double getLastTestingAccuracy() {
        return accTs.get(epoch);
    }
    
}
