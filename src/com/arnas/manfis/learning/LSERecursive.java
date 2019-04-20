package com.arnas.manfis.learning; 

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 *
 * @author ARNAS_
 */
public class LSERecursive {
    
    double[][] C;   //[nData][nRule * (nInput + 1)]
    double[] y;     //[nData]
    RealMatrix P;
    RealMatrix theta;
    
    public LSERecursive(double[][] C, double[] y) {
        System.out.println(C.length+":"+C[0].length);
        this.C = C;
        this.y = y;
    }
    
    public void compute(int n) throws SingularMatrixException {
        System.out.println(n);
        RealMatrix A = MatrixUtils.createRealMatrix(getSquareMatrix(C, n));
        //printMatrix(A);
        RealMatrix y0 = MatrixUtils.createRealMatrix(getNMatrix(y, n));
        RealMatrix At = A.transpose();
        P = new LUDecomposition(At.multiply(A)).getSolver().getInverse();
        theta = P.multiply(At).multiply(y0);
        for (int i = n; i < C.length; i++) {
            System.out.print(".");
            RealMatrix a = MatrixUtils.createRealMatrix(getNMatrix(C[i], n));
            RealMatrix at = a.transpose();
            RealMatrix temp = P.multiply(a).multiply(at).multiply(P);
            double t = 1 + at.multiply(P).multiply(a).getEntry(0, 0);
            P = P.subtract(temp.scalarMultiply(1/t));
            RealMatrix y1 = MatrixUtils.createRealMatrix(toMatrix(y[i]));
            theta = theta.add(P.multiply(a).multiply(y1.subtract(at.multiply(theta))));
        }
    }
    
    public double[][] getTheta(int n) {
        int m = theta.getRowDimension() / n;
        double[][] t = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t[i][j] = theta.getEntry(i * m + j, 0);
            }
        }
        return t;
    }
    
    private double[][] getSquareMatrix(double[][] d, int n) {
        double[][] M = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = d[i][j];
            }
        }
        return M;
    }
    
    private double[][] getNMatrix(double[] d, int n) {
        double[][] M = new double[n][1];
        for (int i = 0; i < n; i++) {
            M[i][0] = d[i]; 
        }
        return M;
    }
    
    private double[][] toMatrix(double d) {
        double[][] M = new double[1][1];
        M[0][0] = d;
        return M;
    }
    
    private void printMatrix(RealMatrix M) {
        System.out.println(this.toString());
        for (double[] d: M.getData()) {
            for (double d1: d) {
                System.out.print(d1+"\t");
            }
            System.out.println("");
        }
    }
}
