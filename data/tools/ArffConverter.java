package com.ar.nas.data.tools;

/**
 *
 * @author ARNAS_
 */

import com.ar.nas.manfis.data.AnfisInput;
import com.ar.nas.data.handler.MetaData;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
 
 /**
  * Generates a little ARFF file with different attribute types.
  *
  * @author FracPete
  */
 public class ArffConverter {
   
    public Instances toArff(AnfisInput data[], MetaData metadata) {
        ArrayList<Attribute> atts = new ArrayList();
        ArrayList<String> classVal = new ArrayList();
        for (int i = 1; i <= metadata.nClass(); i++) {
            classVal.add("C"+Integer.toString(i));
        }
        for (int i = 0; i < metadata.nAttributes(); i++) {
            if (metadata.getDataType()[i].equals("linear")) {
                atts.add(new Attribute(Integer.toString(i)));
            } else {
                ArrayList nominalValues = new ArrayList(2);
                nominalValues.add("yes");
                nominalValues.add("no");
                atts.add(new Attribute(Integer.toString(i), nominalValues));
            }
        }
        atts.add(new Attribute("@@class@@", classVal)); 

        Instances dataArff = new Instances("ArrytmiasInstances", atts, 0);
        dataArff.setClassIndex(dataArff.numAttributes() - 1);
        
        for (int i = 0; i < metadata.nDataTraining(); i++) {
            double[] instanceValue = new double[metadata.nAttributes() + 1];
            for (int j = 0; j < metadata.nAttributes(); j++) {
                if (metadata.getDataType()[j].equals("linear")) {
                    instanceValue[j] = data[i].X(j);
                } else {
                    if (data[i].X(j) == -1) {
                        instanceValue[j] = 0;
                    } else {
                        instanceValue[j] = 1;
                    }
                }
            }
            instanceValue[metadata.nAttributes()] = data[i].YNum()[0];
            dataArff.add(new DenseInstance(1.0, instanceValue));
        }
        
        /* code for print data
        for (int i = 0; i < dataArff.numAttributes(); i++)
        {
            // Print the current attribute.
            System.out.print(dataArff.attribute(i).name() + ": ");

            // Print the values associated with the current attribute.
            double[] values = dataArff.attributeToDoubleArray(i);
            System.out.println(Arrays.toString(values));
        }
        */
                
        return dataArff;
   }
 }
