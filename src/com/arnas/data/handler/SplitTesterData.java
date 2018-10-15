package com.arnas.data.handler;

import com.arnas.data.tools.DataSplitter;
import com.arnas.manfis.data.AnfisInput;

/**
 *
 * @author arif
 */
public class SplitTesterData {
    
    private final AnfisInput[] dataSource;
    private AnfisInput[] dataTraining;
    private AnfisInput[] dataTesting;
    private MetaData metadata;
    private int[] m;
    private int p;
    
    public SplitTesterData(AnfisInput[] dataSource, MetaData metadata) {
        this.dataSource = dataSource;
        this.metadata = metadata;
        m = new int[dataSource.length];
        split();
    }
    
    public void nextSplit(boolean isTest) {
        if (!isTest) {
            m[p-1] = 0;
        }
        split();
    }
    
    private void split() {
        m[p] = 1;
        DataSplitter splitter = new DataSplitter();
        splitter.definedSplitter(dataSource, m);
        dataTraining = splitter.getNewDataTraining();
        dataTesting = splitter.getNewDataValidation();
        metadata.setMetaData(dataTraining, dataTesting);
        p++;
    }
    
    public boolean hasNext() {
        return (p < dataSource.length);
    }
    
    public int splitIndex() {
        return p-1;
    }
    
    public AnfisInput[] getDataTraining() {
        return dataTraining;
    }
    
    public AnfisInput[] getDataTesting() {
        return dataTesting;
    }
    
    public void printIndex() {
        for (int i : m) {
            System.out.print(i+"\t");
        }
        System.out.println();
    }
    
}
