package de.orat.math.cga.api;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGAScalar extends CGAMultivector {
    
    public CGAScalar(CGAMultivector m){
        super(m.impl);
    }
    @Override
    public boolean isScalar(){
        return true;
    }
}
