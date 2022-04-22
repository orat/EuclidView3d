package de.orat.math.cga.spi;

import static de.orat.math.cga.impl2.generated.CGA.binop_Mul;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public interface iCGAMultivector {
    
    // base methods
    
    public iCGAMultivector createBasisVectorOrigin(double scale);
    public iCGAMultivector createBasisVectorEx(double scale);
    public iCGAMultivector createBasisVectorEy(double scale);
    public iCGAMultivector createBasisVectorEz(double scale);
    public iCGAMultivector createBasisVectorEinf(double scale);
    
    public boolean isScalar();
    
    
    // dual operators
    
    public iCGAMultivector add (iCGAMultivector b);
    public iCGAMultivector add (double b);
    public iCGAMultivector sub (iCGAMultivector b);
    public iCGAMultivector sub (double b);
    
    
    // Produkte
   
    public iCGAMultivector gp(double a);
    public iCGAMultivector gp(iCGAMultivector a);
    
    // für die folgenden Produkte könnte ich vermutlich leicht default implementations
    // ins interface mit aufnehmen
    //TODO
    
    public iCGAMultivector ip(iCGAMultivector b, int type);
    public iCGAMultivector op(iCGAMultivector b);
    
    
    // Skalarprodukt
    public double scp(iCGAMultivector b);
    
             
    // monadic operators
    
    public iCGAMultivector generalInverse();
    public iCGAMultivector dual();
    public iCGAMultivector undual();

    public double scalarPart();
    
    public boolean isNull();
    
    public iCGAMultivector conjugate();
    public iCGAMultivector reverse();
    public iCGAMultivector gradeInversion();
    
    public iCGAMultivector exp();
   
    /**
     * Unit under reverse norm.
     * 
     * nach Kleppe
     * normalize = {
     *   _P(1)/(sqrt(abs(_P(1)*_P(1)~)))
     * }
     *
     * @throws java.lang.ArithmeticException if multivector is null-vector
     * @return unit under 'reverse' norm (this / sqrt(abs(this.reverse(this))))
     */
    default iCGAMultivector unit(){
        double s = scp(reverse());
        if (s == 0.0) throw new java.lang.ArithmeticException("null multivector");
        else return this.gp(1 / Math.sqrt(Math.abs(s)));
    }
    
    default double squaredNorm(){
        return gp(conjugate()).scalarPart();
    }
    
    public double[] extractCoordinates(int grade);
    
    public iCGAMultivector extractGrade(int grade);
}
