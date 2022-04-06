package de.orat.math.cga.api;

import de.orat.math.cga.spi.iCGAMultivector;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGAVector extends CGAMultivector {
    public CGAVector(iCGAMultivector impl){
        super(impl);
    }
}
