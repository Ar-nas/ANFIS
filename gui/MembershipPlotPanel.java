/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ar.nas.gui;

import com.ar.nas.manfis.network.rule.membershipFunction.BellMF;
import com.ar.nas.manfis.network.rule.membershipFunction.GaussianMF;
import com.ar.nas.manfis.network.rule.membershipFunction.MembershipFunction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class MembershipPlotPanel extends JPanel  {
    
    private final int width, height;
    private boolean display_plot;
    private boolean initialized;
    private String graphTitle, xTitle, yTitle;
    private Float xLower, xUpper, xInterval;
    private Float yLower, yUpper, yInterval;
    private String[] legend;
    private float[] legendPos;
    private Point2D.Float[][] points;
    private Color[] color;
    
    public MembershipPlotPanel(int width, int height) {
        this.width = width;
        this.height = height;
        display_plot = false;
        initialized = false;
        this.setBounds(this.getX(), this.getY(), width, height);
        this.setBackground(Color.WHITE);
        color = new Color[] {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.PINK, Color.ORANGE};
    }
    
    public void displayPlot(boolean bln) {
        display_plot = bln;
        this.setVisible(false);
        this.setVisible(true);
    }
    
    public void setTitle(String graphTitle, String xTitle, String yTitle) {
        this.graphTitle = graphTitle;
        this.xTitle = xTitle;
        this.yTitle = yTitle;
    }
    
    private boolean isXLowerSet = false;
    private boolean isXUpperSet = false;
    private boolean isXIntervalSet = false;
    private boolean isYLowerSet = false;
    private boolean isYUpperSet = false;
    private boolean isYIntervalSet = false;
    
    public void setXAxis(Float xLower, Float xUpper, Float xInterval) {
        if (xLower != null) {
            isXLowerSet = true;
            this.xLower = xLower;
        }
        if (xUpper != null) {
            isXUpperSet = true;
            this.xUpper = xUpper;
        }
        if (xInterval != null) {
            isXIntervalSet = true;
            this.xInterval = xInterval;
        }
    }
    
    public void setYAxis(Float yLower, Float yUpper, Float yInterval) {
        if (yLower != null) {
            isYLowerSet = true;
            this.yLower = yLower;
        }
        if (yUpper != null) {
            isYUpperSet = true;
            this.yUpper = yUpper;
        }
        if (yInterval != null) {
            isYIntervalSet = true;
            this.yInterval = yInterval;
        }
    }
    
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
        return (upper - lower) / 2;
    }
    
    public void setLegend(String[] legend, float[] pos) {
        this.legend = legend;
        legendPos = pos;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (initialized && display_plot) {
            Float dx = xUpper - xLower;
            Float dy = yUpper - yLower;
 
            int marginTop_pos = 20;
            int marginBottom_pos = height - marginTop_pos; //270
            int graphHeight = marginBottom_pos - marginTop_pos; //240
            
            int marginLeft_pos = 20;
            int marginRight_pos = width - marginLeft_pos; //450
            int graphWidth = marginRight_pos - marginLeft_pos; //400
            
            int textTopOutside_pos = marginTop_pos / 3; //15
            int textTopInside_pos = textTopOutside_pos * 2; //30
            int textMiddle_pos = height / 2; //135
            int textBottomInside_pos = height - textTopInside_pos; //270
            int textBottomOutside_pos = height - textTopOutside_pos; //285
            
            int textLeftOutside_pos = marginLeft_pos / 3; //15
            int textLeftInside_pos = textLeftOutside_pos * 2; //30
            int textCenter_pos = width / 2; //250
            //int textRightInside = width - textLeftInside_pos; //470
            //int textRightOutside_pos = width - textLeftOutside_pos; //485
            
            
            g2d.setPaint(Color.black);
            g2d.setStroke(new BasicStroke());
            g2d.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
            drawCenteredString(g2d, graphTitle, textCenter_pos, textTopOutside_pos, (float) 0.);
            drawCenteredString(g2d, xTitle, textCenter_pos, textBottomOutside_pos, (float) 0.);
            drawCenteredString(g2d, yTitle, textLeftOutside_pos, textMiddle_pos, (float) 0.);
            for (int i = 0; i < legend.length; i++) {
                drawCenteredString(g2d, legend[i], 
                (float)(graphWidth * (legendPos[i] - xLower) / dx + marginLeft_pos), textTopInside_pos, 
                (float)0.);
            }
            
            float xAxis = xLower;
            for (Float x = (float) marginLeft_pos; x <= marginRight_pos; x += graphWidth * xInterval / dx) {
                g2d.setStroke(new BasicStroke());
                drawCenteredString(g2d, String.format("%.1f", xAxis), x, textBottomInside_pos, (float) 0);
                xAxis += xInterval;
                if ((x != marginLeft_pos) && (x != marginRight_pos)) {
                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
                }
                g2d.draw(new Line2D.Float(x, marginTop_pos, x, marginBottom_pos));
            }
            
            float yAxis = yUpper;
            for (Float y = (float) marginTop_pos; y <= marginBottom_pos; y += graphHeight * yInterval / dy) {
                g2d.setStroke(new BasicStroke());
                drawCenteredString(g2d, String.format("%.1f", yAxis), (float)textLeftInside_pos, y, (float) 0);
                yAxis -= yInterval;
                if ((y != marginTop_pos) && (y != marginBottom_pos)) {
                    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
                }
                g2d.draw(new Line2D.Float(marginLeft_pos, y, marginRight_pos, y));
            }
            
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < points.length; i++) {
                g2d.setPaint(color[i]);
            
                int num_points = points[i].length;
                
                Float ex0 = graphWidth * (points[i][0].x - xLower) / dx + marginLeft_pos;
                Float ey0 = -graphHeight * (points[i][0].y - yLower) / dy + marginBottom_pos;
                
                for (int j = 1; j < num_points; j++) {
                    Float ex = graphWidth * (points[i][j].x - xLower) / dx + marginLeft_pos;
                    Float ey = -graphHeight * (points[i][j].y - yLower) / dy + marginBottom_pos;
                    g2d.draw(new Line2D.Float(ex0, ey0, ex, ey));
                    ex0 = ex;
                    ey0 = ey;
                }
            }
        }
    }
 
    private void drawCenteredString(Graphics2D g2d, String string, float x0, float y0, float angle) {
        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D bounds = g2d.getFont().getStringBounds(string, frc);
        LineMetrics metrics = g2d.getFont().getLineMetrics(string, frc);
        if (angle == 0) {
            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2, y0 + metrics.getHeight() / 2);
        } else {
            g2d.rotate(angle, x0, y0);
            g2d.drawString(string, x0 - (float) bounds.getWidth() / 2, y0 + metrics.getHeight() / 2);
            g2d.rotate(-angle, x0, y0);
        }
    }
    
    public void setMembershipFunction(MembershipFunction[] mf) {
        ArrayList<float[]>[] dat = new ArrayList[mf.length];
        for (int i = 0; i < mf.length; i++) {
            dat[i] = new ArrayList();
            for (float j = -1; j < 1; j+=0.01) {
                dat[i].add(new float[] {j, (float)mf[i].compute(j)});
            }
        }
        setData(dat);
    }
    
    public static void main (String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        MembershipPlotPanel gp = new MembershipPlotPanel(500, 300);
        ArrayList<float[]>[] dat = new ArrayList[2];
        dat[0] = new ArrayList();
        dat[1] = new ArrayList();
        BellMF mf1 = new BellMF();
        GaussianMF mf2 = new GaussianMF();
        for (float i = -10; i < 10; i+=0.1) {
            dat[0].add(new float[] {i, (float)mf1.compute(i)});
            dat[1].add(new float[] {i, (float)mf2.compute(i)});
        }
        gp.setTitle("membership function", "x", "μ");
        gp.setData(dat);
        gp.setXAxis((float)-1, (float)1, (float)0.2);
        gp.setYAxis((float)0, (float)1, (float)0.5);
        gp.displayPlot(true);
        frame.add(gp);
        frame.setSize(550, 350);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
