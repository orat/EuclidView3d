package de.orat.math.cga.spi;

import de.orat.math.cga.api.CGAMultivector;
import de.orat.math.cga.impl2.CGA2Multivector;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public interface iCGAMultivector {
    
    // dual operators
    
    public iCGAMultivector add (iCGAMultivector b);
    public iCGAMultivector add (double b);
    public iCGAMultivector sub (iCGAMultivector b);
    public iCGAMultivector sub (double b);
    public iCGAMultivector mul(iCGAMultivector b);
    public iCGAMultivector mul(double s);
    
    // Produkte
    // äußeres Produkt
    public iCGAMultivector wedge(iCGAMultivector b);
    // inneres Product
    public iCGAMultivector dot(iCGAMultivector b);
    // Skalarprodukt
    public iCGAMultivector scp(iCGAMultivector b);
    
    // monadic operators
    
    public iCGAMultivector dual();
    public iCGAMultivector conjugate();
    public iCGAMultivector reverse();
    public iCGAMultivector involute();
    
    //public CGA2Multivector[] splitPointPair()
    public double[] extractCoordinates(int grade);
}
