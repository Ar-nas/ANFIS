package com.arnas.data.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ARNAS_
 */
public class DataLoader {
    
    public String[] loadFileToLine(File file) throws FileNotFoundException, IOException {
        ArrayList<String> T = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            T.add(sCurrentLine);
        }
        return T.toArray(new String[T.size()]);
    }
    
    public ArrayList<String[]> loadFileToWord(File file, String separator) throws FileNotFoundException, IOException {
        ArrayList<String[]> S = new ArrayList();
        String[] temp = loadFileToLine(file);
        for (String t : temp) {
            S.add(t.split(separator));
        }
        return S;
    }
    
    public ArrayList<String[]> parseLineToWord(String[] S, String separator) {
        ArrayList<String[]> T = new ArrayList();
        for (String s : S) {
            T.add(s.split(separator));
        }
        return T;
    }
    
    public String[] splitLastWord(ArrayList<String[]> S) {
        String[] T = new String[S.size()];
        for (int i = 0; i < S.size(); i++) {
            String[] temp = S.remove(i);
            String[] t = new String[temp.length-1];
            int j;
            for (j = 0; j < t.length; j++) {
                t[j] = temp[j];
            }
            T[i] = temp[j];
            S.add(i, t);
        }
        return T;
    }
    
    public double[][] convertWordToDouble(ArrayList<String[]> S) {
        double[][] data;
        if (useAd) {
            data = new double[S.size()+ad.length][];
        } else {
            data = new double[S.size()][];
        }
        for (int i = 0; i < S.size(); i++) {
            String [] S1 = S.get(i);
            if (useAd) {
                for (int j : ad) {
                    if (i == j) {
                        S.add(S.get(i));
                    }
                }
            }
            data[i] = new double[S1.length];
            for (int j = 0; j < S1.length; j++) {
                if (!"?".equals(S1[j])) {
                    data[i][j] = Double.parseDouble(S1[j]);
                } else {
                    data[i][j] = Double.NaN;
                }
            }
        }
        return data;
    }
    
    public int[][] convertWordToInteger(ArrayList<String[]> S) {
        int[][] data;
        if (useAd) {
            data = new int[S.size()+ad.length][];
        } else {
            data = new int[S.size()][];
        }
        for (int i = 0; i < S.size(); i++) {
            String[] S1 = S.get(i);
            if (useAd) {
                for (int j : ad) {
                    if (i == j) {
                        S.add(S.get(i));
                    }
                }
            }
            data[i] = new int[S1.length];
            for (int j = 0; j < S1.length; j++) {
                data[i][j] = Integer.parseInt(S1[j]);
            }
        }
        return data;
    }
    
    boolean useAd = false;
    public void setUseAd(boolean state) {
        useAd = state;
    }
    
    int[] ad = new int[] {5, 7, 9, 13, 15, 18, 19, 20, 24, 27, 33, 36, 38, 
        39, 40, 42, 44, 45, 47, 51, 53, 55, 57, 63, 66, 67, 69, 73, 78, 79, 80, 83, 87, 
        92, 94, 95, 97, 100, 102, 103, 109, 110, 111, 112, 113, 114, 115, 121, 124, 
        127, 128, 129, 133, 135, 137, 142, 145, 147, 150, 152, 158, 160, 161, 163, 
        164, 166, 168, 170, 171, 172, 174, 181, 189, 191, 194, 195, 197, 198, 203, 
        202, 205, 210, 211, 213, 216, 
        217, 218, 219, 220, 221, 223, 225, 226, 229, 231, 232, 233, 236, 237, 239, 241, 242, 
        245, 246, 248, 252, 253, 255, 257, 258, 261, 263, 264, 266, 268, 271, 272, 
        274, 276, 277, 280, 283, 285, 286, 288, 291, 293, 294, 296, 297, 299, 301, 
        303, 305, 306, 307, 311, 317, 318, 320, 322, 324, 326, 327, 329, 330, 
        332, 333, 341, 342, 345, 347, 348, 350, 352, 356, 357, 359, 362, 363, 365, 
        366, 368, 369, 371, 373, 375, 376, 379, 381, 382, 384, 385, 386, 388, 389, 
        391, 393, 394, 396, 398, 399, 400, 401, 403, 405, 407, 409, 411, 413, 415, 
        416, 418, 419, 422, 423, 424, 426, 428};
}
