package com.ar.nas.manfis.data;

/**
 *
 * @author ARNAS_
 */
public class AnfisInput {
    
    //index
    private final int index;
    //input
    private double[] X;
    //target
    private int[] YNum;
    private int[] YBin;
    private double[] Y;
    
    public AnfisInput(int index, double[] X) {
        this.index = index;
        this.X = X;
    }
    
    public void updateInput(double[] X) {
        this.X = X;
    }
    
    public void setTarget(int[] YNum, int[] YBin, double[] Y) {
        this.YNum = YNum;
        this.YBin = YBin;
        this.Y = Y;
    }
    
    public int index() {
        return index;
    }

    public double[] X() {
        return X;
    }
    
    public double X(int i) {
        return X[i];
    }

    public double[] Y() {
        return Y;
    }
    
    public double Y(int i) {
        return Y[i];
    }

    public int[] YBin() {
        return YBin;
    }
    
    public int YBin(int i) {
        return YBin[i];
    }
    
    public int[] YNum() {
        return YNum;
    }
    
    public int nInput() {
        return X.length;
    }
    
    public int nOutput() {
        return Y.length;
    }
    
    public void deepCopy(AnfisInput toCopy) {
        this.X = toCopy.X;
        this.YNum = toCopy.YNum;
        this.YBin = toCopy.YBin;
        this.Y = toCopy.Y;
    }
    
}
