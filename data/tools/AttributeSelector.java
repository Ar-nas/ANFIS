package com.ar.nas.data.tools;

import com.ar.nas.manfis.data.AnfisInput;
import com.ar.nas.data.handler.MetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.*;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;

/**
 * performs attribute selection using CfsSubsetEval and GreedyStepwise
 * (backwards) and trains J48 with that. Needs 3.5.5 or higher to compile.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AttributeSelector {
    
    int[] selectedAttributes;
    
    public AnfisInput[] selectAttributes(AnfisInput[] dataTraining, MetaData metadata) throws Exception {
        ArffConverter conv = new ArffConverter();
        Instances dataArff = conv.toArff(dataTraining, metadata);
        if (dataArff.classIndex() == -1)
            dataArff.setClassIndex(dataArff.numAttributes() - 1);
        
        AttributeSelector fs = new AttributeSelector();
        int[] temp = fs.useLowLevel(dataArff);
        int[] att = new int[temp.length-1];
        System.arraycopy(temp, 0, att, 0, att.length);
        
        return selectAttributes(att, dataTraining, metadata);
    }
    
    private Double[][] correlation;
    private Integer[] attList;
    private boolean isScoreCalculated = false;
    
    public Double[][] calculateCorrelation(AnfisInput[] dataTraining, MetaData metadata) {
        correlation = new Double[dataTraining[0].nInput()][dataTraining[0].nOutput()];
        for (int i = 0; i < dataTraining[0].nInput(); i++) {
            for (int j = 0; j < dataTraining[0].nOutput(); j++) {
                double[] x = new double[dataTraining.length];
                double[] y = new double[dataTraining.length];
                for (int k = 0; k < dataTraining.length; k++) {
                    x[k] = dataTraining[k].X(i);
                    if (dataTraining[k].Y(j) == 2) {
                        y[k] = dataTraining[k].X(i);
                    } else {
                        y[k] = dataTraining[k].Y(j);
                    }
                }
                correlation[i][j] = getCorrelation(x,y);
            }
        }
        Map<Integer, Double> correlationAvg = new LinkedHashMap();
        for (int i = 0; i < correlation.length; i++) {
            double sum = 0;
            for (double c : correlation[i]) {
                sum += Math.abs(c);
            }
            correlationAvg.put(i, sum/correlation[i].length);
        }
        correlationAvg = sortByValue(correlationAvg);
        attList = correlationAvg.keySet().toArray(new Integer[]{});
        isScoreCalculated = true;
        return correlation;
    }
    
    /*
    public AnfisInput[] selectAttributes(int nAttribute, AnfisInput[] dataTraining, MetaData metadata) {
        if (!isScoreCalculated) calculateCorrelation(dataTraining, metadata);
        isScoreCalculated = false;
        Integer[] att = new Integer[nAttribute];
        System.arraycopy(attList, 0, att, 0, nAttribute);
        return selectAttributes(Arrays.stream(att).mapToInt(Integer::intValue).toArray(), dataTraining, metadata);
    }
    */
    Double[] fisherScore;
    
    public AnfisInput[] selectAttributes(int nAttribute, AnfisInput[] dataTraining, MetaData metadata) {
        if (!isScoreCalculated) calculateFisherScore(dataTraining, metadata);
        isScoreCalculated = false;
        Integer[] att = new Integer[nAttribute];
        System.arraycopy(attList, 0, att, 0, nAttribute);
        return selectAttributes(Arrays.stream(att).mapToInt(Integer::intValue).toArray(), dataTraining, metadata);
    }
    
    public static void main(String[] args) {
        AttributeSelector A = new AttributeSelector();
        ArrayList<Double> list = new ArrayList<Double>();
        list.add(3.0);
        list.add(2.0);
        list.add(7.0);
        list.add(2.0);
        System.out.println(A.variance(list));
    }
    
    public Double[] calculateFisherScore(AnfisInput[] dataTraining, MetaData metadata) {
        fisherScore = new Double[dataTraining[0].nInput()];
        for (int i = 0; i < metadata.nAttributes(); i++) {
            ArrayList<Double> Di = new ArrayList();
            ArrayList<Double>[] Dij = new ArrayList[metadata.nClass()];
            for (int j = 0; j < metadata.nClass(); j++) {
                Dij[j] = new ArrayList();
            }
            for (AnfisInput d : dataTraining) {
                Di.add(d.X(i));
                Dij[metadata.getClassIndexFromCode(d.YNum()[0])].add(d.X(i));
            }
            double num = 0;
            double den = 0;
            for (int j = 0; j < metadata.nClass(); j++) {
                int nj = metadata.nDataTraining(j);
                double mij = mean(Dij[j]);
                double mi = mean(Di);
                double aij = variance(Dij[j]);
                num = num + (nj * Math.pow(mij - mi, 2));
                den = den + (nj * aij);
            }
            fisherScore[i] = num / den;
        }
        
        
        Map<Integer, Double> scoreMap = new LinkedHashMap();
        for (int i = 0; i < fisherScore.length; i++) {
            scoreMap.put(i, fisherScore[i]);
        }
        scoreMap = sortByValue(scoreMap);
        attList = scoreMap.keySet().toArray(new Integer[]{});
        return fisherScore;
    }

    private double variance(ArrayList<Double> data) {
        double mean = mean(data);
        double sumSquareDifference = 0;
        for (Double d : data) {
            sumSquareDifference += Math.pow(d - mean, 2);
        }
        return sumSquareDifference / (data.size()-1);
    }
    
    private double mean(ArrayList<Double> data) {
        return sum(data) / data.size();
    }
    
    private double sum(ArrayList<Double> data) {
        double sum = 0;
        for (int i = 0; i < data.size(); i++){
           sum = sum + data.get(i);
        }

        return sum;
    }
    
    public AnfisInput[] selectAttributes(int[] selectedAttributes, AnfisInput[] dataTraining, MetaData metadata) {
        this.selectedAttributes = selectedAttributes;
        dataTraining = updateAttributes(dataTraining, metadata);
        return dataTraining;
    }
    
    /**
    * uses the meta-classifier
    */
    private void useClassifier(Instances data) throws Exception {
        System.out.println("Use : Meta-classfier");
        AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        NaiveBayes base = new NaiveBayes();
        classifier.setClassifier(base);
        classifier.setEvaluator(eval);
        classifier.setSearch(search);
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(classifier, data, 5, new Random(1));
        System.out.println(evaluation.toSummaryString());
    }

    /**
     * uses the filter
     */
    private void useFilter(Instances data) throws Exception {
        System.out.println("Use : Filter");
        weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        filter.setEvaluator(eval);
        filter.setSearch(search);
        filter.setInputFormat(data);
        Instances newData = Filter.useFilter(data, filter);
        System.out.println(newData);
    }

    /**
     * uses the low level approach
     */
    private int[] useLowLevel(Instances data) throws Exception {
        System.out.println("Use : Low-level");
        weka.attributeSelection.AttributeSelection attsel = new weka.attributeSelection.AttributeSelection();
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        attsel.setEvaluator(eval);
        attsel.setSearch(search);
        attsel.SelectAttributes(data);
        int[] indices = attsel.selectedAttributes();
        System.out.println("selected attribute indices (starting with 0):\n" + Utils.arrayToString(indices));
        return indices;
    }
   
    private double getCorrelation(double[] xs, double[] ys) {
        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        int n = xs.length;

        for(int i = 0; i < n; ++i) {
          double x = xs[i];
          double y = ys[i];

          sx += x;
          sy += y;
          sxx += x * x;
          syy += y * y;
          sxy += x * y;
        }

        // covariation
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);

        // correlation is just a normalized covariation
        return cov / sigmax / sigmay;
    }
    
    private Map<Integer, Double> sortByValue(Map<Integer, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }
    
    public AnfisInput[] updateAttributes(AnfisInput[] data, MetaData metadata) {
        metadata.setDataType(selectType(metadata.getDataType()));
        for (int i = 0; i < data.length; i++) {
            data[i] = selectData(data[i]);
        }
        metadata.setMetaData(data);
        return data;
    }
    
    private AnfisInput selectData(AnfisInput data) {
        double[] newX = new double[selectedAttributes.length];
        int j = 0;
        for (int k = 0; k < selectedAttributes.length; k++) {
            newX[j] = data.X(selectedAttributes[k]);
            j++;
        }
        data.updateInput(newX);
        return data;
    }

    private String[] selectType(String[] dataType) {
        String[] newType = new String[selectedAttributes.length];
        int j = 0;
        for (int k = 0; k < selectedAttributes.length; k++) {
            newType[j] = dataType[selectedAttributes[k]];
            j++;
        }
        return newType;
    }
    
}