package com.ar.nas.data.handler;

import com.ar.nas.manfis.data.AnfisInput;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ARNAS_
 */
public class MetaData {
    
    private String[] dataType;
    private String[] attributeNames;
    private ArrayList<String> classIndex;
    private Map<String, ArrayList<Integer>> classLabel;
    private String outputType;
    
    private int numberOfFoldDataInstance;
    private int[] numberOfDataTrainingInstances;
    private int[] numberOfDataTestingInstances;
    private int numberOfAttributes;
    private int numberOfOutput;
    
    public void setDataType(String[] dataType) {
        this.dataType = dataType;
    }
    
    public void setAttributeNames(String[] attributeNames) {
        this.attributeNames = attributeNames;
    }
    
    public void setClassLabel(String[] label) {
        classIndex = new ArrayList();
        classLabel = new LinkedHashMap();
        for (int i = 0; i < label.length; i++) {
            if (classLabel.containsKey(label[i])) {
                ArrayList<Integer> t = classLabel.get(label[i]);
                t.add(i);
                classLabel.put(label[i], t);
            } else {
                ArrayList<Integer> t = new ArrayList();
                t.add(i);
                classLabel.put(label[i], t);
            }
        }
        for (String c : classLabel.keySet()) {
            classIndex.add(c);
        }
    }
    
    public void setMetaData(AnfisInput[] dataTraining) {
        numberOfDataTrainingInstances = setNumberOfDataInstance(dataTraining);
        this.numberOfAttributes = dataTraining[0].nInput();
        this.numberOfOutput = dataTraining[0].nOutput();
    }
    
    public void setMetaData(AnfisInput[] dataTraining, AnfisInput[] dataTesting) {
        numberOfDataTrainingInstances = setNumberOfDataInstance(dataTraining);
        numberOfDataTestingInstances = setNumberOfDataInstance(dataTesting);
        this.numberOfAttributes = dataTraining[0].nInput();
        numberOfOutput = dataTraining[0].nOutput();        
    }
    
    public void setMetaData(KFoldData foldData) {
        numberOfDataTrainingInstances = setNumberOfDataInstance(foldData.getDataTraining());
        numberOfDataTestingInstances = setNumberOfDataInstance(foldData.getDataTesting());
        numberOfOutput = foldData.nOutput();
    }
    
    private int[] setNumberOfDataInstance(AnfisInput[] data) {
        int[] numberOfDataInstances = new int[classIndex.size()];
        for (AnfisInput dt : data) {
            numberOfDataInstances[getClassIndexFromCode(dt.YNum()[0])] += 1;
        }
        return numberOfDataInstances;
    }
    
    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }
    
    public int nDataTraining() {
        int total = 0;
        for (int c : numberOfDataTrainingInstances) total += c; 
        return total;
    }

    public int nDataTraining(int i) {
        return numberOfDataTrainingInstances[i];
    }
    
    public int nDataTesting() {
        int total = 0;
        for (int c : numberOfDataTestingInstances) total +=c; 
        return total;
    }

    public int nDataTesting(int i) {
        return numberOfDataTestingInstances[i];
    }
    
    public int nAttributes() {
        return numberOfAttributes;
    }

    public int nOutput() {
        return numberOfOutput;
    }
    
    public int nClass() {
        return classLabel.size();
    }
    
    public int nClassCode() {
        int max = Integer.MIN_VALUE;
        for (String s : classLabel.keySet()) {
            ArrayList<Integer> l = classLabel.get(s);
            for (int i : l) {
                if (i > max) {
                    max = i;
                }
            }
        }
        return max+1;
    }
    
    public String[] getDataType() {
        return dataType;
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }
    
    public String getClassLabelFromCode(int code) {
        for (String s : classLabel.keySet()) {
            ArrayList<Integer> l = classLabel.get(s);
            if (l.contains(code)) {
                return s;
            }
        }
        return null;
    }
    
    public String getClassLabelFromIndex(int index) {
        return classIndex.get(index);
    }
    
    public int getClassIndexFromCode(int code) {
        int i = classIndex.indexOf(getClassLabelFromCode(code));
        if (i == -1) {
            return classIndex.indexOf(getClassLabelFromCode(nClassCode()-1));
        } else {
            return i;
        }
    }
    
    public ArrayList<Integer> getClassCodeFromIndex(int index) {
        return classLabel.get(classIndex.get(index));
    }
    
    public String getOutputType() {
        return outputType;
    }
    
}
