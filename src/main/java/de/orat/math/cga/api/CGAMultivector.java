package de.orat.math.cga.api;

import de.orat.math.cga.spi.iCGAMultivector;

/**
 * Da die blades unterschiedlich definiert sein können je nach Implementierung, darf
 * es keine Methoden geben um coordinaten rauszuholen?
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGAMultivector {
    
    private iCGAMultivector impl;
    
    public CGAMultivector(iCGAMultivector impl){
        this.impl = impl;
    }
    
    public double[] extractCoordinates(int grade){
        return impl.extractCoordinates(grade);
    }
    
    /*public CGAMultivector add (CGAMultivector b){
        return new CGA
    }
    public CGAMultivector add (double b);
    public CGAMultivector sub (CGAMultivector b);
    public CGAMultivector sub (double b);
    public CGAMultivector mul(CGAMultivector b);
    public CGAMultivector mul(double s);*/
}
