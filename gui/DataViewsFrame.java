/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ar.nas.gui;

import com.ar.nas.data.handler.MetaData;
import com.ar.nas.manfis.data.AnfisInput;
import com.ar.nas.manfis.data.AnfisOutput;
import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ARNAS_
 */
public class DataViewsFrame extends javax.swing.JFrame {

    DefaultTableModel tableModel1;
    DefaultTableModel tableModel2;
    DefaultTableModel tableModel3;
    JTable jTable1;
    
    public DataViewsFrame(AnfisInput[] D, MetaData M) {
        makeTableModel(D, M);
        initComponents();
        setYTableSize(1);
    }

    private void makeTableModel(AnfisInput[] D, MetaData M) {
        
        Object[][] row = new Object[D.length][1];
        for (int i = 0; i < D.length; i++) {
            row[i][0] = i+1;
        }
        
        tableModel1 = new DefaultTableModel(row, new String[] {"ID"}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Integer.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        String column[] = new String[D[0].nInput()];
        for (int i = 0; i < column.length; i++) {
            column[i] = "X"+String.valueOf(i+1);
        }
        row = new Object[D.length][column.length];
        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < column.length; j++) {
                row[i][j] = D[i].X(j);
            }
        }
        
        tableModel2 = new DefaultTableModel(row, column) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Double.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        column = new String[D[0].nOutput() + 2];
        final Class[] temp3 = new Class [column.length];
        column[0] = "Class"; column[1] = "Code";
        temp3[0] = java.lang.String.class; temp3[1] = java.lang.String.class;
        for (int i = 2; i < column.length; i++) {
            column[i] = "Y"+String.valueOf(i-1);
            temp3[i] = java.lang.Integer.class;
        }
        row = new Object[D.length][column.length];
        for (int i = 0; i < D.length; i++) {
            row[i][0] = M.getClassLabelFromCode(D[i].YNum()[0]);
            row[i][1] = String.valueOf(D[i].YNum()[0]);
            for (int j = 1; j < D[i].YNum().length; j++) {
                row[i][1] += "/"+String.valueOf(D[i].YNum()[j]);
            }
            for (int j = 2; j < column.length; j++) {
                if (D[i].YBin(j-2) == 2) {
                    row[i][j] = "*";
                } else {
                    row[i][j] = D[i].YBin(j-2);
                }
            }
        }
        
        tableModel3 = new DefaultTableModel(row, column) {
            Class[] types = temp3;
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }
    
    public DataViewsFrame(AnfisInput[] I, AnfisOutput O, MetaData M) {
        makeTableModel(I, O, M);
        initComponents();
        setYTableSize(2);
    }
    
    private void makeTableModel(AnfisInput[] I, AnfisOutput O, MetaData M) {
        
        Object[][] row = new Object[I.length][1];
        for (int i = 0; i < I.length; i++) {
            row[i][0] = i+1;
        }
        
        tableModel1 = new DefaultTableModel(row, new String[] {"ID"}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Integer.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        String column[] = new String[I[0].nInput()];
        for (int i = 0; i < column.length; i++) {
            column[i] = "X"+String.valueOf(i+1);
        }
        row = new Object[I.length][column.length];
        for (int i = 0; i < I.length; i++) {
            for (int j = 0; j < column.length; j++) {
                row[i][j] = I[i].X(j);
            }
        }
        
        tableModel2 = new DefaultTableModel(row, column) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Double.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        column = new String[O.yBin(0).length + 2];
        final Class[] temp1 = new Class [column.length];
        int i = 0;
        for (; i < column.length-2; i++) {
            column[i] = "Y"+String.valueOf(i+1);
            temp1[i] = java.lang.Integer.class;
        }
        column[i] = "code";
        temp1[i] = java.lang.String.class;
        i++;
        column[i] = "class";
        temp1[i] = java.lang.String.class;
        
        row = new Object[I.length][column.length];
        for (i = 0; i < I.length; i++) {
            int j = 0;
            for (; j < column.length - 2; j++) {
                row[i][j] = O.yBin(i)[j];
            }
            row[i][j] = O.yNum(i);
            j++;
            row[i][j] = M.getClassLabelFromCode(O.yNum(i));
        }
        
        tableModel3 = new DefaultTableModel(row, column) {
            Class[] types = temp1;
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }
    
    public DataViewsFrame(AnfisInput[][] D, MetaData M) {
        makeTableModel(D, M);
        //setRowColour(D);
        initComponents();
        setYTableSize(1);
    }
    
    private void makeTableModel(AnfisInput[][] D, MetaData M) {
        
        Object[][] row = new Object[D.length*D[0].length+D.length][1];
        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < D[i].length; j++) {
                row[i*D[i].length+i+j][0] = j+1;
            }
        }
        
        tableModel1 = new DefaultTableModel(row, new String[] {"ID"}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Integer.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        String column[] = new String[D[0][0].nInput()];
        for (int i = 0; i < column.length; i++) {
            column[i] = "X"+String.valueOf(i+1);
        }
        row = new Object[D.length*D[0].length+D.length][column.length];
        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < D[i].length; j++) {
                for (int k = 0; k < column.length; k++) {
                    row[i*D[i].length+i+j][k] = D[i][j].X(k);
                }
            }
        }
        
        tableModel2 = new DefaultTableModel(row, column) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Double.class;
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        
        column = new String[D[0][0].nOutput() + 2];
        final Class[] temp3 = new Class [column.length];
        column[0] = "Class"; column[1] = "Code";
        temp3[0] = java.lang.String.class; temp3[1] = java.lang.String.class;
        for (int i = 2; i < column.length; i++) {
            column[i] = "Y"+String.valueOf(i-1);
            temp3[i] = java.lang.Integer.class;
        }
        row = new Object[D.length*D[0].length+D.length][column.length];
        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < D[i].length; j++) {
                row[i*D[i].length+i+j][0] = M.getClassLabelFromCode(D[i][j].YNum()[0]);
                row[i*D[i].length+i+j][1] = D[i][j].YNum()[0];
                for (int k = 1; k < D[i][j].YNum().length; k++) {
                    row[i*D[i].length+i+j][1] += "/"+String.valueOf(D[i][j].YNum()[k]);
                }
                for (int k = 2; k < column.length; k++) {
                    row[i*D[i].length+i+j][k] = D[i][j].YBin(k-2);
                }
            }
        }
        
        tableModel3 = new DefaultTableModel(row, column) {
            Class[] types = temp3;
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }
    
    private void setYTableSize(int type) {
        jTable3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        switch(type) {
            case 1 :
                jTable3.getColumnModel().getColumn(0).setPreferredWidth(120);
                jTable3.getColumnModel().getColumn(1).setPreferredWidth(35);
                for (int i = 2; i < jTable3.getColumnCount(); i++) {
                    jTable3.getColumnModel().getColumn(i).setPreferredWidth(25);
                }
            break;
            case 2 :
                int i;
                for (i = 0; i < jTable3.getColumnCount()-2; i++) {
                    jTable3.getColumnModel().getColumn(i).setPreferredWidth(25);
                }
                jTable3.getColumnModel().getColumn(i).setPreferredWidth(35);
                i++;
                jTable3.getColumnModel().getColumn(i).setPreferredWidth(120);
            break;
        }
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
        jTable2 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data views");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTable2.setModel(tableModel2);
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClickedTable2(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyChangeTable2(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);
        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < jTable2.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setPreferredWidth(50);
        }

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable3.setModel(tableModel3);
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClickedTable3(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyChangeTable3(evt);
            }
        });
        jScrollPane2.setViewportView(jTable3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );

        jTable1 = new javax.swing.JTable();
        jTable1.setModel(tableModel1);
        jTable1.setBackground(jTable2.getTableHeader().getBackground());
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);

        JViewport viewport = new JViewport();
        viewport.setView(jTable1);
        viewport.setPreferredSize(jTable1.getPreferredSize());
        jScrollPane1.setRowHeaderView(viewport);
        jScrollPane1.setCorner(JScrollPane.UPPER_LEFT_CORNER, jTable1.getTableHeader());
        BoundedRangeModel model = jScrollPane1.getVerticalScrollBar().getModel();
        jScrollPane2.getVerticalScrollBar().setModel(model);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mouseClickedTable2(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClickedTable2
        // TODO add your handling code here:
        jTable1.setRowSelectionInterval(jTable2.getSelectedRow(), jTable2.getSelectedRow());
        jTable3.setRowSelectionInterval(jTable2.getSelectedRow(), jTable2.getSelectedRow());
    }//GEN-LAST:event_mouseClickedTable2

    private void keyChangeTable2(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyChangeTable2
        // TODO add your handling code here:
        jTable1.setRowSelectionInterval(jTable2.getSelectedRow(), jTable2.getSelectedRow());
        jTable3.setRowSelectionInterval(jTable2.getSelectedRow(), jTable2.getSelectedRow());
    }//GEN-LAST:event_keyChangeTable2

    private void mouseClickedTable3(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClickedTable3
        // TODO add your handling code here:
        jTable1.setRowSelectionInterval(jTable3.getSelectedRow(), jTable3.getSelectedRow());
        jTable2.setRowSelectionInterval(jTable3.getSelectedRow(), jTable3.getSelectedRow());
    }//GEN-LAST:event_mouseClickedTable3

    private void keyChangeTable3(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyChangeTable3
        // TODO add your handling code here:
        jTable1.setRowSelectionInterval(jTable3.getSelectedRow(), jTable3.getSelectedRow());
        jTable2.setRowSelectionInterval(jTable3.getSelectedRow(), jTable3.getSelectedRow());
    }//GEN-LAST:event_keyChangeTable3


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
