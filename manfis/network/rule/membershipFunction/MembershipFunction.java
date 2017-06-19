package com.ar.nas.manfis.network.rule.membershipFunction;

import java.util.Random;

public abstract class MembershipFunction {
    
    protected double a;
    protected double b;
    protected double μ;

    public MembershipFunction() {
        Random rand = new Random();
        a = rand.nextDouble();
        b = rand.nextDouble();
    }
    
    public abstract double compute(double x);
	
    public abstract void correctError(double error, double x, double η, double m);
        
    public void setParameters(double a, double b) {
        this.a = a;
        this.b = b;
    }
    
    public double a() {
        return a;
    }
    
    public double b() {
        return b;
    }
    
    public double μ() {
        return μ;
    }
	
}
