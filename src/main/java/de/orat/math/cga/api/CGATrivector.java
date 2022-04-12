package de.orat.math.cga.api;

import de.orat.math.cga.spi.iCGAMultivector;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGATrivector extends CGAMultivector {
    public CGATrivector(iCGAMultivector impl){
        super(impl);
    }
    
    /**
     * Is this 3-vector representing a line.
     * 
     * Lines Classification in the Conformal Space R{n+1,1}
     * Lilian Aveneau and Ángel F. Tenorio
     * 
     * @return true if this object represents a line.
     */
    public boolean isLine(){
        //TODO
        // 1. is 3-blade ist ja bereits klar
        // 2. op(einf) = 0;
        // 3. Richtung ungleich 0, d.h. einf leftcontraction this != 0
        return true;
    }
}
