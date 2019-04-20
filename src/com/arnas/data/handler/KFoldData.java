package com.arnas.data.handler;

import com.arnas.manfis.data.AnfisInput;

/**
 *
 * @author arif
 */
public class KFoldData {
    
    private int k;
    
    private AnfisInput[][] foldData;
    
    public KFoldData(AnfisInput[][] foldData) {
        this.foldData = foldData;
        k = 0;
    }
    
    public AnfisInput[][] getFoldData() {
        return foldData;
    }
    
    public boolean hasNext() {
        return (k < foldData.length);
    }
    
    /**
     * 
     * @return true if has next, false otherwise
     */
    public boolean setNextK() {
        if (hasNext()) {
            k++;
            return true;
        } else {
            return false;
        }
    }
    
    public void resetK() {
        k = 0;
    }
    
    public AnfisInput[] getDataTraining() {
        AnfisInput[] dataTr = new AnfisInput[nDataTraining()];
        int l = 0;
        for (int i = 0; i < foldData.length; i++) {
            if (i != k) {
                for (int j = 0; j < foldData[i].length; j++) {
                    dataTr[l] = foldData[i][j];
                    l++;
                }
            }
        }
        return dataTr;
    }
    
    public AnfisInput[] getDataTesting() {
        return foldData[k];
    }
    
    public int getCurrentIndex() {
        return k;
    }
    
    public int getK() {
        return foldData.length;
    }
    
    public int nDataTraining() {
        int length = 0;
        for (int i = 0; i < foldData.length; i++) {
            if (i != k) {
                length += foldData[i].length;
            }
        }
        return length;
    }
    
    public int nDataTesting() {
        return foldData[0].length;
    }
    
    public int nAttributes() {
        return foldData[0][0].nInput();
    }
    
    public int nOutput() {
        return foldData[0][0].nOutput();
    }
    
}
