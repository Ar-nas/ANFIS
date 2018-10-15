package com.arnas.data.handler;

import com.arnas.manfis.data.AnfisInput;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author arif
 */
public class AttributeEvaluatorData {
    
    private AnfisInput[] dataTraining;
    private AnfisInput[] dataTesting;
    
    private ArrayList<Integer> selectedAttributes;
    Integer checkingAttribute;
    
    public AttributeEvaluatorData(AnfisInput[] dataTraining, AnfisInput[] dataTesting) {
        this.dataTraining = dataTraining;
        this.dataTesting = dataTesting;
        selectedAttributes = new ArrayList();
        for(int i = 0; i < dataTraining[0].nInput(); i++) {
            selectedAttributes.add(i);
        }
        checkingAttribute = -1;
    }
    
    public AnfisInput[] getNextDataTraining() {
        AnfisInput[] dataTemp = new AnfisInput[dataTraining.length];
        for (int i = 0; i < dataTraining.length; i++) {
            dataTemp[i] = new AnfisInput(i, dataTraining[i].X());
            dataTemp[i].deepCopy(dataTraining[i]);
        }
        //System.out.println(Arrays.toString(dataTraining[0].X()));
        if (selectedAttributes.remove(checkingAttribute)) {
            for (int i = 0; i < dataTemp.length; i++) {
                dataTemp[i].updateInput(getNewData(dataTemp[i].X()));
            }
        }
        System.out.println(Arrays.toString(selectedAttributes.toArray()));
        return dataTemp;
    }
    
    public AnfisInput[] getNextDataTesting() {
        AnfisInput[] dataTemp = new AnfisInput[dataTesting.length];
        for (int i = 0; i < dataTesting.length; i++) {
            dataTemp[i] = new AnfisInput(i, dataTesting[i].X());
            dataTemp[i].deepCopy(dataTesting[i]);
        }
        for (int i = 0; i < dataTemp.length; i++) {
            dataTemp[i].updateInput(getNewData(dataTemp[i].X()));
        }
        return dataTemp;
    }
    
    private double[] getNewData(double[] X) {
        double[] newData = new double[selectedAttributes.size()];
        int j = 0;
        for (int i : selectedAttributes) {
            newData[j] = X[i];
            j++;
        }
        return newData;
    }
    
    public void useCurrentAttribute(boolean use) {
        if (use) {
            int i = 0;
            while ((i < selectedAttributes.size()) && 
                    (checkingAttribute > selectedAttributes.get(i))) {
                i++;
            }
            selectedAttributes.add(i, checkingAttribute);
        }
        checkingAttribute++;
    }
    
    public boolean hasNext(int nAttribute) {
        return (checkingAttribute < nAttribute);
    }
    
    public int getCurrentIndex() {
        return checkingAttribute;
    }
    
}
