package com.ar.nas.data.tools;

import com.ar.nas.data.handler.MetaData;
import com.ar.nas.manfis.data.AnfisInput;

/**
 *
 * @author ARNAS_
 */
public class AnfisDataBuilder {
    
    
    /**
     * anfis data maker method for training data
     * @param D
     * @param T
     * @return 
     */
    public AnfisInput[] makeAnfisData(double[][] D, int[][] T) {
        
        AnfisInput[] data = new AnfisInput[D.length];
        
        int nOutput = getNOutput(T);
        for (int i = 0; i < D.length; i++) {
            double[] X = D[i];
            int[] YNum = T[i];
            int[] YBin = getBinary(nOutput, YNum);
            double[] Y = getAnfisTarget(YBin);
            data[i] = new AnfisInput(i, X);
            data[i].setTarget(YNum, YBin, Y);
        }
        return data;
    }
    
    public AnfisInput[] reClassCoding(AnfisInput[] data, MetaData metadata, int[][] newClassCode) {
        int nOutput = getNOutput(newClassCode);
        for (int i = 0; i < data.length; i++) {
            int[] YNum = newClassCode[metadata.getClassIndexFromCode(data[i].YNum()[0])];
            int[] YBin = getBinary(nOutput, YNum);
            double[] Y = getAnfisTarget(YBin);
            data[i].setTarget(YNum, YBin, Y);
        }
        String[] newClassLabel = new String[metadata.nClassCode()];
        for (int i = 0; i < newClassCode.length; i++) {
            for (int j = 0; j < newClassCode[i].length; j++) {
                newClassLabel[newClassCode[i][j]] = metadata.getClassLabelFromIndex(i);
            }
        }
        metadata.setClassLabel(newClassLabel);
        return data;
    }
    
    private int getNOutput(int[][] classes) {
        int max = Integer.MIN_VALUE;
        for (int[] c : classes) {
            for (int c1 : c) {
                if (c1 > max) {
                    max = c1;
                }
            }
        }
        return 1+(int)(Math.log(max)/Math.log(2));
        //return max;
    }
    
    private int[] getBinary(int n, int[] value) {
        int[] v = new int[value.length];
        System.arraycopy(value, 0, v, 0, value.length);
        int[][] temp = new int[v.length][n];
        for (int i = 0; i < v.length; i++) {
            int j = 0;
            while (v[i] > 0) {
                temp[i][j++] = v[i] % 2;
                v[i] = v[i] / 2;
            }
        }
        int bin[] = new int[n];
        for (int i = 0; i < n; i++) {
            bin[i] = temp[0][i];
            boolean vrt = false;
            int j = 1;
            while ((!vrt)&&(j < temp.length)) {
                if (bin[i] != temp[j][i]) {
                    bin[i] = 2;
                    vrt = true;
                } else {
                    j++;
                }
            }
        }
        return bin;
    }
    
    private int[] getBinary(int n, int value) {
        int[] v = new int[n];
        v[value] = 1;
        return v;
    }
    
    private double[] getAnfisTarget(int[] bin) {
        double target[] = new double[bin.length];
        for (int i = 0; i < target.length; i++) {
            if (bin[i] == 0) {
                target[i] = -1;
            } else if (bin[i] == 1) {
                target[i] = 1;
            }
        }
        return target;
    }
    
    /**
     * anfis data maker method for testing data
     * @param index
     * @param D
     * @return 
     */   
    public AnfisInput makeAnfisData(int index, double[] D) {
        AnfisInput data = new AnfisInput(index, D);
        return data;
    }
    
}
