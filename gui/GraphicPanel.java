/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ar.nas.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author ARNAS_
 */
public class GraphicPanel extends JPanel  {
    
    private int size;
    private boolean display_plot;
    private boolean initialized;
    private String title;
    private String xTitle;
    private String yTitle;
    private Float xLower, xUpper, xInterval;
    private Float yLower, yUpper, yInterval;
    private Point2D.Float[][] points;
    private Color[] color;
    
    public GraphicPanel(int size) {
        this.size = size;
        display_plot = false;
        initialized = false;
        this.setBounds(this.getX(), this.getY(), size, size);
        this.setBackground(Color.white);
        color = new Color[] {Color.red, Color.blue};
    }
    
    public void displayPlot(boolean bln) {
        display_plot = bln;
        this.setVisible(false);
        this.setVisible(true);
    }
    
    public void setTitle(String title, String xTitle, String yTitle) {
        this.title = title;
        this.xTitle = xTitle;
        this.yTitle = yTitle;
    }
    
    private boolean isXLowerSet = false;
    private boolean isXUpperSet = false;
    private boolean isYLowerSet = false;
    private boolean isYUpperSet = false;
    
    public void setBound(Float xLower, Float xUpper, Float yLower, Float yUpper) {
        if (xLower != null) {
            isXLowerSet = true;
            this.xLower = xLower;
        }
        if (xUpper != null) {
            isXUpperSet = true;
            this.xUpper = xUpper;
        }
        if (isXLowerSet && isXUpperSet) {
            this.yInterval = getInterval(xLower, xUpper);
        }
        if (yLower != null) {
            isYLowerSet = true;
            this.yLower = yLower;
        }
        if (yUpper != null) {
            isYUpperSet = true;
            this.yUpper = yUpper;
        }
        if (isYLowerSet && isYUpperSet) {
            this.yInterval = getInterval(yLower, yUpper);
        }
    }
    
    public void setData(ArrayList<float[]>[] P) {
        float[] lowerBound = getLowerBound(P);
        float[] upperBound = getUpperBound(P);
        initialized = true;
        if (!isXLowerSet)
            this.xLower = lowerBound[0];
        if (!isXUpperSet)
            this.xUpper = upperBound[0];
        if (!(isXLowerSet && isXUpperSet)) 
            this.xInterval = getInterval(xLower, xUpper);
        if (!isYLowerSet)
            this.yLower = lowerBound[1];
        if (!isYUpperSet)
            this.yUpper = upperBound[1];
        if (!(isYLowerSet && isYUpperSet)) 
            this.yInterval = getInterval(yLower, yUpper);
        points = new Point2D.Float[P.length][P[0].size()];
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < P[i].size(); j++) {
                points[i][j] = new Point2D.Float(P[i].get(j)[0], P[i].get(j)[1]);
            }
        }        
    }
    
    private float[] getLowerBound(ArrayList<float[]>[] data) {
        float min[] = new float[2];
        min[0] = Float.POSITIVE_INFINITY;
        min[1] = Float.POSITIVE_INFINITY;
        for (ArrayList<float[]> F : data) {
            for (float[] f : F) {
                if (f[0] < min[0]) {
                    min[0] = f[0];
                }
                if (f[1] < min[1]) {
                    min[1] = f[1];
                }
            }
        }
        return min;
    }
    
    private float[] getUpperBound(ArrayList<float[]>[] data) {
        float max[] = new float[2];
        max[0] = Float.NEGATIVE_INFINITY;
        max[1] = Float.NEGATIVE_INFINITY;
        for (ArrayList<float[]> F : data) {
            for (float[] f : F) {
                if (f[0] > max[0]) {
                    max[0] = f[0];
                }
                if (f[1] > max[1]) {
                    max[1] = f[1];
                }
            }
        }
        return max;
    }
    
    private float getInterval(float lower, float upper) {
        return (upper - lower) / 10;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.black);
        g2d.setStroke(new BasicStroke());
        g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
        if (initialized && display_plot) {
            Float dx = xUpper - xLower;
            Float dy = yUpper - yLower;
 
            int i1 = size / 10; //50
            int i2 = size - i1; //250
            int i3 = i2 - i1; //200
            
            int j1 = i1 / 2; //25
            int j2 = i3 / 2; //100
            int j3 = size - j1; //275
            
            drawCenteredString(g2d, title, j2, j1, (float) 0.);
            drawCenteredString(g2d, xTitle, j2, j3, (float) 0.);
            drawCenteredString(g2d, yTitle, j1, j2, (float) -Math.PI / 2);
            drawCenteredString(g2d, xLower.toString(), i1, j3, (float) 0);
            drawCenteredString(g2d, xUpper.toString(), i2, j3, (float) 0);
            drawCenteredString(g2d, String.format("%.2f", yLower), j1, i2, (float) 0);
            drawCenteredString(g2d, String.format("%.2f", yUpper), j1, i1, (float) 0);
 
            g2d.setPaint(Color.gray);
            for (Float x = (float) i1; x <= i2; x += i3 * xInterval / dx)
                g2d.draw(new Line2D.Float(x, i2, x, i1));
            for (Float y = (float) i1; y <= i2; y += i3 * yInterval / dy)
                g2d.draw(new Line2D.Float(i1, y, i2, y));
            
            for (int i = 0; i < points.length; i++) {
                g2d.setPaint(color[i]);
            
                int num_points = points[i].length;
                
                Float ex0 = i3 * (points[i][0].x - xLower) / dx + i1;
                Float ey0 = -i3 * (points[i][0].y - yLower) / dy + i2;
                
                for (int j = 1; j < num_points; j++) {
                    Float ex = i3 * (points[i][j].x - xLower) / dx + i1;
                    Float ey = -i3 * (points[i][j].y - yLower) / dy + i2;
                    g2d.draw(new Line2D.Float(ex0, ey0, ex, ey));
                    ex0 = ex;
                    ey0 = ey;
                }
            }
        }
    }
 
    private void drawCenteredString(Graphics2D g2d, String string, int x0, int y0, float angle) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D bounds = g2d.getFont().getStringBounds(string, frc);
        LineMetrics metrics = g2d.getFont().getLineMetrics(string, frc);
        if (angle == 0) {
            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
                    y0 + metrics.getHeight() / 2);
        } else {
            g2d.rotate(angle, x0, y0);
            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2,
                    y0 + metrics.getHeight() / 2);
            g2d.rotate(-angle, x0, y0);
        }
    }
    
    private void drawLineGraph(Graphics2D g2d, Paint colour, Float dx, Float dy, int idx) {
        g2d.setPaint(colour);
            
        int num_points = points[idx].length;
            
        int i1 = size / 10; //50
        int i2 = size - i1; //250
        int i3 = i2 - i1; //200
        
        Float ex0 = i3 * (points[idx][0].x - xLower) / dx + i1;
        Float ey0 = -200 * (points[idx][0].y - yLower) / dy + i2;
            
        for (int i = 1; i < num_points; i++) {
            Float ex = i3 * (points[idx][i].x - xLower) / dx + i1;
            Float ey = -i3 * (points[idx][i].y - yLower) / dy + i2;
            g2d.draw(new Line2D.Float(ex0, ey0, ex, ey));
            ex0 = ex;
            ey0 = ey;
        }
    }

}
