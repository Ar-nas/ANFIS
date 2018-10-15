/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arnas.gui;

import com.arnas.data.handler.MetaData;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author ARNAS_
 */
public class PreprocessingObject {
    
    public class LoaderObject {
        final JButton loadingButton;
        final JTextField loadingStatusText;
        
        public LoaderObject(JButton B1, JTextField TF1) {
            loadingButton = B1;
            loadingStatusText = TF1;
        }
        
        public void disable() {
            loadingButton.setEnabled(false);
        }
        
        public void enable() {
            loadingButton.setEnabled(true);
        }
        
        public void startProcess() {
            disable();
            selectorObject.disable();
            splitterObject.disable();
            viewerObject.disable();
            startProcessingText(loadingStatusText, "loading & processing data", "data has been loaded");
        }
        
        public void endProcess(MetaData metadata) {
            enable();
            selectorObject.enable();
            splitterObject.enable();
            viewerObject.enable();
            viewerObject.setNInstanceTr(metadata.nDataTraining());
            viewerObject.setNInstanceTs(0);
            viewerObject.setNInput(metadata.nAttributes());
            viewerObject.setNOutput(metadata.nOutput());
            stopProcessingText();
        }
    }
    
    public LoaderObject loaderObject;
    
    public void setLoaderObject(JButton B1, JTextField TF1) {
        loaderObject = new LoaderObject(B1, TF1);
    }
    
    public class SelectorObject {
        final JTextField nAttributeField;
        final JButton correlationButton;
        final JButton selectingButton;
        final JTextField selectingStatusText;

        public SelectorObject(JTextField TF1, JButton B1, JButton B2, JTextField TF2) {
            nAttributeField = TF1;
            correlationButton = B1;
            selectingButton = B2;
            selectingStatusText = TF2;
        }
        
        public void disable() {
            nAttributeField.setEditable(false);
            correlationButton.setEnabled(false);
            selectingButton.setEnabled(false);
        }
        
        public void enable() {
            nAttributeField.setEditable(true);
            correlationButton.setEnabled(true);
            selectingButton.setEnabled(true);
        }
        
        public void startProcess() {
            loaderObject.disable();
            disable();
            splitterObject.disable();
            viewerObject.disable();
            startProcessingText(selectingStatusText, "selecting attributes", nAttributeField.getText()+" attributes has been selected");
        }
        
        public void endProcess(MetaData metadata) {
            loaderObject.enable();
            enable();
            splitterObject.enable();
            viewerObject.enable();
            viewerObject.setNInput(metadata.nAttributes());
            stopProcessingText();
        }
    }
    
    public SelectorObject selectorObject;
    
    public void setSelectorObject(JTextField TF1, JButton B1, JButton B2, JTextField TF2) {
        selectorObject = new SelectorObject(TF1, B1, B2, TF2);
    }
    
    public class SplitterObject {
        final JCheckBox holdoutCheck;
        final JTextField trRatioField;
        final JTextField tsRatioField;
        final JCheckBox kFoldCheck;
        final JTextField kField;
        final JButton splittingButton;
        final JTextField splittingStatusText;
        
        public SplitterObject(JCheckBox CB1, JTextField TF1, JTextField TF2,
                JCheckBox CB2, JTextField TF3, JButton B1, JTextField TF4) {
            
            holdoutCheck = CB1;
            trRatioField = TF1;
            tsRatioField = TF2;
            kFoldCheck = CB2;
            kField = TF3;
            splittingButton = B1;
            splittingStatusText = TF4;
        }
        
        public void disable() {
            holdoutCheck.setEnabled(false);
            trRatioField.setEditable(false);
            tsRatioField.setEditable(false);
            kFoldCheck.setEnabled(false);
            kField.setEditable(false);
            splittingButton.setEnabled(false);
        }
        
        public void enable() {
            holdoutCheck.setEnabled(true);
            if (holdoutCheck.isSelected()) {
                trRatioField.setEditable(true);
                tsRatioField.setEditable(true);
            }
            kFoldCheck.setEnabled(true);
            if (kFoldCheck.isSelected()) {
                kField.setEditable(true);
            }
            splittingButton.setEnabled(true);
        }
        
        public void startProcess() {
            loaderObject.disable();
            selectorObject.disable();
            disable();
            viewerObject.disable();
            startProcessingText(splittingStatusText, "splitting data", "data has been splitted");
        }
        
        public void endProcess(MetaData metadata) {
            loaderObject.enable();
            selectorObject.enable();
            enable();
            viewerObject.enable();
            if (holdoutCheck.isSelected()) {
                viewerObject.setNInstanceTr(metadata.nDataTraining());
                viewerObject.setNInstanceTs(metadata.nDataTesting());
            } else if (kFoldCheck.isSelected()) {
                viewerObject.setNInstanceTr(metadata.nDataTesting() + "/fold");
            }
            stopProcessingText();
        }
    }
    
    public SplitterObject splitterObject;
    
    public void setSplitterObject(JCheckBox CB1, JTextField TF1, JTextField TF2, 
            JCheckBox CB2, JTextField TF3, JButton B1, JTextField TF4) {
        splitterObject = new SplitterObject(CB1, TF1, TF2, CB2, TF3, B1, TF4);
    }
    
    public class ViewerObject {
        final JLabel nInstanceTrLabel;
        final JLabel nInstanceTsLabel;
        final JLabel nInputLabel;
        final JLabel nOutputLabel;
        
        final JButton trViewButton;
        final JButton tsViewButton;
        final JButton nameViewButton;
        final JButton labelViewButton;
        
        public ViewerObject(JLabel L1, JLabel L2, JLabel L3, JLabel L4,
                JButton B1, JButton B2, JButton B3, JButton B4) {
            
            nInstanceTrLabel = L1;
            nInstanceTsLabel = L2;
            nInputLabel = L3;
            nOutputLabel = L4;
        
            trViewButton = B1;
            tsViewButton = B2;
            nameViewButton = B3;
            labelViewButton = B4;
        }
        
        public void disable() {
            trViewButton.setEnabled(false);
            tsViewButton.setEnabled(false);
            nameViewButton.setEnabled(false);
            labelViewButton.setEnabled(false);
        }
        
        public void enable() {
            trViewButton.setEnabled(true);
            tsViewButton.setEnabled(true);
            nameViewButton.setEnabled(true);
            labelViewButton.setEnabled(true);
        }
        
        public void setNInstanceTr(int n) {
            nInstanceTrLabel.setText(String.valueOf(n));
        }
        
        public void setNInstanceTr(String n) {
            nInstanceTrLabel.setText(n);
        }
        
        public void setNInstanceTs(int n) {
            nInstanceTsLabel.setText(String.valueOf(n));
        }
        
        public void setNInput(int n) {
            nInputLabel.setText(String.valueOf(n));
        }
        
        public void setNOutput(int n) {
            nOutputLabel.setText(String.valueOf(n));
        }
        
    }
    
    public ViewerObject viewerObject;
    
    public void setViewerObject(JLabel L1, JLabel L2, JLabel L3, JLabel L4, 
            JButton B1, JButton B2, JButton B3, JButton B4) {
        viewerObject = new ViewerObject(L1, L2, L3, L4, B1, B2, B3, B4);
    }
    
    private boolean processing;
    
    private void startProcessingText(final JTextField textFieldProcess, final String textProcess, final String textSuccess) {
        processing = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (processing) {
                    try {
                        Thread.sleep(500);
                        switch (i) {
                            case 0: 
                                textFieldProcess.setText(textProcess);
                                i++;
                                break;
                            case 1:
                                textFieldProcess.setText(textProcess + ".");
                                i++;
                                break;
                            case 2 :
                                textFieldProcess.setText(textProcess + "..");
                                i++;
                                break;
                            case 3 :
                                textFieldProcess.setText(textProcess + "...");
                                i++;
                                break;
                            case 4 :
                                i = 0;
                                break;
                            default :
                                i++;
                                break;
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                textFieldProcess.setText(textSuccess);
            }
        });
        t.start();
    }
    
    private void stopProcessingText() {
        processing = false;
    }
    
}
