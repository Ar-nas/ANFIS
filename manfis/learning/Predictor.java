package com.ar.nas.manfis.learning;

import com.ar.nas.manfis.data.AnfisInput;
import com.ar.nas.manfis.data.AnfisOutput;
import com.ar.nas.manfis.network.Network;

/**
 *
 * @author arif
 */
public class Predictor {
    
    public void predict(Network net, AnfisInput[] dataPrediction, AnfisOutput result) {
        for (AnfisInput data1 : dataPrediction) {
            net.execute(data1);
            System.out.println(data1.index());
            result.setResult(data1.index(), net.getResult());
        }
    }
    
}
