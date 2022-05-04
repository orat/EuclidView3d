package de.orat.math.cga.spi;

import de.orat.math.cga.api.CGAMultivector;
import static de.orat.math.ga.basis.InnerProductTypes.LEFT_CONTRACTION;
import org.jogamp.vecmath.Tuple3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public interface iCGAMultivector {
    
    // base methods to create specific cga multivectors
    
    public iCGAMultivector createOrigin(double scale);
    public iCGAMultivector createEx(double scale);
    public iCGAMultivector createEy(double scale);
    public iCGAMultivector createEz(double scale);
    public iCGAMultivector createInf(double scale);
    
    public iCGAMultivector createE(Tuple3d p);
    public iCGAMultivector createScalar(double d);
    
    public boolean isScalar();
    
    
    // dual operators
    
    public iCGAMultivector add (iCGAMultivector b);
    default iCGAMultivector add (double b){
        return add(createScalar(b));
    }
    public iCGAMultivector sub (iCGAMultivector b);
    default iCGAMultivector sub (double b){
        return sub(createScalar(b));
    }
    
    
    // products
   
    //TODO
    // testen ob diese default impl wirklich das gleiche liefert als die impl
    // in CGA1Multivector1a 
    default iCGAMultivector gp(double a){
        return gp(createScalar(a));
    }
    
    public iCGAMultivector gp(iCGAMultivector a);
    
    //TODO
    // implement default implementations
    public iCGAMultivector ip(iCGAMultivector b, int type);
    public iCGAMultivector op(iCGAMultivector b);
    
    
    // Scalarproduct
    default double scp(iCGAMultivector x) {
	return ip(x, LEFT_CONTRACTION).scalarPart();
    }
             
    default iCGAMultivector vee(iCGAMultivector x){
        return dual().op(x.dual()).undual();
    }
    
    // monadic operators
    
    public iCGAMultivector generalInverse();
    public iCGAMultivector dual();
    //TODO
    // implement default implementation
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
    default iCGAMultivector normalize(){
        double s = scp(reverse());
        if (s == 0.0) throw new java.lang.ArithmeticException("null multivector");
        else return this.gp(1d / Math.sqrt(Math.abs(s)));
    }
    
    /**
     * @return 
     */
    default double abs(){
        double result = scp(reverse());
        if (result != 0d){
            result = Math.sqrt(Math.abs(result));
        }
        return result;
    }
    default double squaredNorm(){
        return gp(conjugate()).scalarPart();
    }
    
    public iCGAMultivector extractGrade(int grade);
    
    //TODO
    // die folgenden Indize-Methoden taugen so nicht, besser statt dessen create/get-
    // Methoden für origin, euclid3d und inf einführen, aber ich brauche ja noch 
    // weitere für Teile von Bivektoren oder um z.B. Quaternionen zu extrahieren etc.
    // Wie bekomme ich das allgemeingültig hin?
    // irgendwie sollten alle diese Methode in die Metric-Klasse ausgelagert werden
    
    /**
     * Get the index of the basevector in the conformal vector which represents
     * the euclid x base vector.
     * 
     * @return index in the conformal vector representing the euclid x base vector
     */
    public int getEStartIndex();
    public int getEinfIndex();
    public int getOriginIndex();
    
    public double[] extractCoordinates(int grade);
}
