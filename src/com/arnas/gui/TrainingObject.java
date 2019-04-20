/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arnas.gui;

import com.arnas.manfis.data.AnfisLog;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ARNAS_
 */
public class TrainingObject {
    
    private final JTextField ruleField;
    private final JComboBox mfBox;
    private final JTextField momentumField;
    private final JTextField learningRateField;
    
    private final JTextField maxEpochField;
    private final JTextField toleranceField;
    
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton continueButton;
    
    private final JTextField timeField;
    private final JTextField epochField;
    
    private final JTextField rmseTrField;
    private final JTextField rmseTsField;
    private final JPanel rmsePanel;
    private GraphicPanel rmseGraph;
    private final ArrayList<float[]> rmsePoint[];

    private final JTextField accTrField;
    private final JTextField accTsField;
    private final JPanel accPanel;
    private GraphicPanel accGraph;
    private final ArrayList<float[]> accPoint[];
    
    private final JButton loadModelButton;
    private final JButton loadResultButton;
    
    private final JTable resultListTable;
    
    /**
     * 
     * @param ruleField
     * @param mfBox
     * @param momentumField
     * @param learningRateField
     * @param maxEpochField
     * @param toleranceField
     * @param startButton
     * @param stopButton
     * @param continueButton
     * @param timeField
     * @param epochField
     * @param rmseTrField
     * @param rmseTsField
     * @param accTrField
     * @param accTsField
     * @param rmsePanel 
     * @param accPanel 
     * @param loadModelButton 
     * @param loadResultButton 
     * @param resultListTable 
     */
    public TrainingObject (JTextField ruleField, JComboBox mfBox, 
            JTextField momentumField, JTextField learningRateField,
            JTextField maxEpochField, JTextField toleranceField,
            JButton startButton, JButton stopButton, JButton continueButton,
            JTextField timeField, JTextField epochField, 
            JTextField rmseTrField, JTextField rmseTsField, JPanel rmsePanel, 
            JTextField accTrField, JTextField accTsField, JPanel accPanel,
            JButton loadModelButton, JButton loadResultButton, 
            JTable resultListTable) {
        
        this.ruleField = ruleField;
        this.mfBox = mfBox;
        this.momentumField = momentumField;
        this.learningRateField = learningRateField;
        
        this.maxEpochField = maxEpochField;
        this.toleranceField = toleranceField;
        
        this.startButton = startButton;
        this.stopButton = stopButton;
        this.continueButton = continueButton;
        
        this.timeField = timeField;
        this.epochField = epochField;
        
        this.rmseTrField = rmseTrField;
        this.rmseTsField = rmseTsField;
        this.rmsePanel = rmsePanel;
        rmsePoint = new ArrayList[2];
        for (int i = 0; i < rmsePoint.length; i++) {
            rmsePoint[i] = new ArrayList();
        }
        
        this.accTrField = accTrField;
        this.accTsField = accTsField;
        this.accPanel = accPanel;
        accPoint = new ArrayList[2];
        for (int i = 0; i < rmsePoint.length; i++) {
            accPoint[i] = new ArrayList();
        }
        
        this.loadModelButton = loadModelButton;
        this.loadResultButton = loadResultButton;
        
        this.resultListTable = resultListTable;
    }

    public void setView(AnfisLog log) {
        rmseTrField.setText(String.format("%.5f", log.getLastTrainingRMSE()));
        rmseTsField.setText(String.format("%.5f", log.getLastTestingRMSE()));
        
        rmseGraph = new GraphicPanel(360);
        rmsePanel.removeAll();
        rmsePanel.add(rmseGraph, "Center");
        rmseGraph.setTitle("RMSE Graph", "epoch", "RMSE");
        rmseGraph.setBound(null, null, new Float(0.0), null);
        rmsePoint[0].clear();
        int i = 0;
        for (double d : log.getTrainingRMSE()) {
            rmsePoint[0].add(new float[] {(float)i, (float)d});
            i++;
        }
        rmsePoint[1].clear();
        i = 0;
        for (double d : log.getTestingRMSE()) {
            rmsePoint[1].add(new float[] {(float)i, (float)d});
            i++;
        }
        rmseGraph.setData(rmsePoint);
        rmseGraph.displayPlot(true);
        
        accTrField.setText(String.format("%.2f", log.getLastTrainingAccuracy()));
        accTsField.setText(String.format("%.2f", log.getLastTestingAccuracy()));
        
        accGraph = new GraphicPanel(360);
        accPanel.removeAll();
        accPanel.add(accGraph, "Center");
        accGraph.setTitle("Accuracy Graph", "epoch", "Accuracy");
        accGraph.setBound(null, null, new Float(0.0), null);
        accPoint[0].clear();
        i = 0;
        for (double d : log.getTrainingRMSE()) {
            accPoint[0].add(new float[] {(float)i, (float)d});
            i++;
        }
        accPoint[1].clear();
        i = 0;
        for (double d : log.getTestingRMSE()) {
            accPoint[1].add(new float[] {(float)i, (float)d});
            i++;
        }
        accGraph.setData(accPoint);
        accGraph.displayPlot(true);
        
        
        ruleField.setText(String.valueOf(log.getNumberOfRule()));
        mfBox.setSelectedItem(log.getMembershipFunctionType());
        momentumField.setText(String.valueOf(log.getMomentum()));
        learningRateField.setText(String.valueOf(log.getLearningRate()));
        
        timeField.setText(String.valueOf(log.getDuration()));
        epochField.setText(String.valueOf(log.getEpoch()));
        //maxEpochField.setText(String.valueOf(log.getEpoch()));
    }
    
    public void updateView(AnfisLog log) {
        timeField.setText(String.valueOf(log.getDuration()));
        epochField.setText(String.valueOf(log.getEpoch()));
        
        rmseTrField.setText(String.format("%.5f", log.getLastTrainingRMSE()));
        rmseTsField.setText(String.format("%.5f", log.getLastTestingRMSE()));
        
        rmsePoint[0].add(new float[] {(float)log.getEpoch(), (float)log.getLastTrainingRMSE()});
        rmsePoint[1].add(new float[] {(float)log.getEpoch(), (float)log.getLastTestingRMSE()});
        if (rmsePoint[0].get(rmsePoint[0].size()-1)[1] != Float.NaN) {
            rmseGraph.setData(rmsePoint);
            rmseGraph.displayPlot(true);
        }
        
        accTrField.setText(String.format("%.2f", log.getLastTrainingAccuracy()));
        accTsField.setText(String.format("%.2f", log.getLastTestingAccuracy()));
        
        accPoint[0].add(new float[] {(float)log.getEpoch(), (float)log.getLastTrainingAccuracy()});
        accPoint[1].add(new float[] {(float)log.getEpoch(), (float)log.getLastTestingAccuracy()});
        if (accPoint[0].get(accPoint[0].size()-1)[1] != Float.NaN) {
            accGraph.setData(accPoint);
            accGraph.displayPlot(true);
        }
    }
   
    public void startTrain() {
        rmseGraph = new GraphicPanel(360);
        rmsePanel.removeAll();
        rmsePanel.add(rmseGraph, "Center");
        rmseGraph.setTitle("RMSE Graph", "epoch", "RMSE");
        rmseGraph.setBound(null, null, (float)0.0, null);
        for (int i = 0; i < rmsePoint.length; i++) {
            rmsePoint[i] = new ArrayList();
        }
        
        accGraph = new GraphicPanel(360);
        accPanel.removeAll();
        accPanel.add(accGraph, "Center");
        accGraph.setTitle("Accuracy Graph", "epoch", "Accuracy");
        accGraph.setBound(null, null, (float)0.0, null);
        for (int i = 0; i < accPoint.length; i++) {
            accPoint[i] = new ArrayList();
        }
    }
    
    public void stopTrain() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        continueButton.setEnabled(true);
        loadModelButton.setEnabled(true);
        loadResultButton.setEnabled(true);
        ruleField.setEditable(true);
        mfBox.setEnabled(true);
        momentumField.setEditable(true);
        learningRateField.setEditable(true);
        maxEpochField.setEditable(true);
        toleranceField.setEditable(true);
    }
    
    public void continueTrain() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        continueButton.setEnabled(false);
        loadModelButton.setEnabled(false);
        loadResultButton.setEnabled(false);
        ruleField.setEditable(false);
        mfBox.setEnabled(false);
        momentumField.setEditable(false);
        learningRateField.setEditable(false);
        maxEpochField.setEditable(false);
        toleranceField.setEditable(false);
    }
    
    public void loadModelStart() {
        loadModelButton.setEnabled(false);
    }

    public void loadModelFinish(String fileName) {
        loadModelButton.setEnabled(true);
    }
    
    public void loadResultStart() {
        loadResultButton.setEnabled(false);
    }

    public void loadResultFinish(String fileName) {
        loadResultButton.setEnabled(true);
    }
    
    public void addToResultTable(int index, AnfisLog log) {
        ((DefaultTableModel)resultListTable.getModel()).addRow(
            new Object[]{index+1,
                String.format("%.5f", log.getLastTrainingRMSE()), 
                String.format("%.5f", log.getLastTestingRMSE()),
                String.format("%.2f", log.getLastTrainingAccuracy()), 
                String.format("%.2f", log.getLastTestingAccuracy()),
                log.getEpoch(), log.getDuration(), "view", "save"
            }
        );
    }
    
    public void clearTrainingList() {
        ((DefaultTableModel)resultListTable.getModel()).setRowCount(0);
    }
    
    private String timeConverter(long miliSecond) {
        String time = new String();
        int second = (int)(miliSecond / 1000);
        int minute = second / 60;
        int hour = minute / 60;
        if (hour > 0) {
            time = time.concat(String.valueOf(hour)).concat(":");
            minute = minute % 60;
            if (minute < 10) {
                time = time.concat("0"+minute);
            } else {
                time = time.concat(String.valueOf(minute));
            }
            time = time.concat(":");
            second = second % 60;
            if (second < 10) {
                time = time.concat("0"+second);
            } else {
                time = time.concat(String.valueOf(second));
            }
        } else {
            if (minute < 10) {
                time = time.concat("0"+minute);
            } else {
                time = time.concat(String.valueOf(minute));
            }
            time = time.concat(":");
            second = second % 60;
            if (second < 10) {
                time = time.concat("0"+second);
            } else {
                time = time.concat(String.valueOf(second));
            }
            time = time.concat(":");
            miliSecond = miliSecond % 1000;
            if (miliSecond < 10) {
                time = time.concat("00"+miliSecond);
            } else if (miliSecond < 100) {
                time = time.concat("0"+miliSecond);
            } else {
                time = time.concat(String.valueOf(miliSecond));
            }
        }
        return time;
    }
}
