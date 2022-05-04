package de.orat.math.cga.api;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGAQuadvector extends CGAMultivector {
    public CGAQuadvector(CGAMultivector m){
        super(m.impl);
    }
}
