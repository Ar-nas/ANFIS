/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ar.nas.gui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ARNAS_
 */
public class ResultViewsFrame extends javax.swing.JFrame {

    DefaultTableModel tableModel1;
    
    /**
     * Creates new form ResultViews_frame
     * @param targetNames
     * @param countTr
     * @param countVal
     */
    public ResultViewsFrame(String[] targetNames, int[][] countTr, int[][] countVal) {
        makeTableModel(targetNames, countTr, countVal);
        initComponents();
    }

    private void makeTableModel(String[] targetNames, int[][] countTr, int[][] countVal) {
        Object[][] row = new Object[targetNames.length][6];
        for (int i = 0; i < row.length; i++) {
            row[i][0] = i;
            row[i][1] = targetNames[i];
            row[i][2] = countTr[0][i];
            row[i][3] = countTr[1][i];
            row[i][4] = countVal[0][i];
            row[i][5] = countVal[1][i];
        }
        
        tableModel1 = new DefaultTableModel(row, new String[] {"code", "class", "trueTr", "falseTr", "trueVal", "falseVal"}) {
            Class[] types = {java.lang.Integer.class, java.lang.String.class, 
                java.lang.Integer.class, java.lang.Integer.class, 
                java.lang.Integer.class, java.lang.Integer.class};
            boolean[] canEdit = {false, false, false, false};
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(tableModel1);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(35);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
