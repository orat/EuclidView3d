package de.orat.math.cga.api;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGABivector extends CGAMultivector {
    public CGABivector(CGAMultivector m){
        super(m.impl);
    }
}
