package de.orat.math.cga.impl2;

import de.orat.math.cga.util.Decomposition3d;
import de.orat.math.cga.util.Decomposition3d.FlatAndDirectionParameters;
import de.orat.math.cga.util.Decomposition3d.LinePairParameters;
import de.orat.math.cga.util.Decomposition3d.PointPairParameters;
import de.orat.math.cga.util.Decomposition3d.RoundAndTangentParameters;
import de.orat.math.cga.impl2.generated.CGA;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGA2Multivector extends de.orat.math.cga.impl2.generated.CGA {
    
    //TODO
    // "1","e1","e2","e3","e4","e5","e12","e13","e14","e15","e23","e24","e25","e34","e35","e45","e123","e124","e125","e134","e135","e145","e234","e235","e245","e345","e1234","e1235","e1245","e1345","e2345","e12345"
    // als Enumeration bauen, damit ich die Strings und Indizes automatisch robust zusammen definieren kann
    
    CGA2Multivector(de.orat.math.cga.impl2.generated.CGA cga){
        super(cga._mVec);
    }
    public CGA2Multivector(int idx, double value){
        super(idx, value);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<_mVec.length;i++){
            sb.append(_basis[i]).append("=").append(String.valueOf(_mVec[i]));
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length()-1);
        
        return sb.toString();
    }
    public CGA2Multivector add (CGA2Multivector b){
        return new CGA2Multivector (binop_Add (this, b));
    }
    public CGA2Multivector sub (CGA2Multivector b){
        return new CGA2Multivector (binop_Sub (this, b));
    }
    public CGA2Multivector mul(CGA2Multivector b){
        return new CGA2Multivector(binop_Mul(this, b));
    }
    public CGA2Multivector smul(double s){
        return new CGA2Multivector(binop_smul (s, this));
    }
    public CGA2Multivector wedge(CGA2Multivector b){
        return new CGA2Multivector(binop_Wedge(this,b));
    }
    
    /**
     * Vee product.
     * 
     * The vee product is availabel as an optimized shorthand for the dual of 
     * the wedge of the duals.
     * 
     * @param b
     * @return 
     */
    public CGA2Multivector vee(CGA2Multivector b){
        return new CGA2Multivector(binop_Vee(this,b));
        //return dual().wedge(b.dual()).dual();
    }
    /**
     * Dot procuct.
     * 
     * The dot product implemented is the left contraction - without any 
     * extensions or modifications. The geometric meaning is usually formulated
     * as the dot product between a and b gives the orthogonal complement in b of the
     * projection of x onto y.
     * 
     * @param b
     * @return 
     */
    public CGA2Multivector dot(CGA2Multivector b){
        return new CGA2Multivector(CGA.binop_Dot(this, b));
    }
    /**
     * 
     * @return poincare duality operator
     */
    public CGA2Multivector dual(){
        return new CGA2Multivector(CGA.unop_Dual(this));
    }
    /**
     * Bei Quaternionen:
     * The conjugate is useful because it has the following properties:
     *
     * qa' * qb' = (qb*qa)' In this way we can change the order of the multiplicands.
     * q * q' = a2 + b2 + c2 + d2 = real number. Multiplying a quaternion by its 
     * conjugate gives a real number. This makes the conjugate useful for finding 
     * the multiplicative inverse. For instance, if we are using a quaternion q 
     * to represent a rotation then conj(q) represents the same rotation in the reverse direction.
     * Pout = q * Pin * q' We use this to calculate a rotation transform.
     *
     * Aber was macht das in GA?
     * 
     * This reverses all directions in space
     *
     * A~ denotes the conjugate of A
     *
     * conjugate, reverse and dual are related as follows.
     *
     * A~= (A†)* = (A*)†
     *
     * identities
     *
     * (A * B)~ = B~* A~
     * 
     * @return Cifford conjugate
     */
    public CGA2Multivector conjugate(){
        return new CGA2Multivector(super.Conjugate());
    }
    
    /**
     * The reverse function of a multivector reverses the order of its factors, 
     * including the order of the base values within a component. 
     * 
     * The reverse 
     * function is denoted by †, so the reversal of A is denoted by A†.
     * 
     * @return reverse the order of the basis blades
     * 
     */
    public CGA2Multivector reverse(){
        // ist involute wirklich reverse?
        
        // vermutlich nein!!!
        // denn
        // a# = sum k=0(N (-1)k a <k> = a<+> - a<-> .
         return new CGA2Multivector(super.Involute());
    }
    /**
     * a# = sum k=0(N (-1)k a <k> = a<+> - a<-> .
     * 
     * @return main involution
     */
    public CGA2Multivector involute(){
         return new CGA2Multivector(super.Involute());
    }
   
    public CGA2Multivector[] splitPointPair(){
        CGA sqr = CGA.binop_Dot(this.dual(), this.dual());
        System.out.println("getPointPair() sqr= "+sqr.toString()); // sollte ein skalar sein
        CGA2Multivector result = new CGA2Multivector(this);
        result._mVec[0] += Math.sqrt(sqr._mVec[0]);
        //result.div
        // division durch einf*pp
        return null;
    }
    
    public static CGA2Multivector createVector(Vector3d v){
        if (v.x == 0d && v.y == 0d && v.z == 0d){
            return CGA2Multivector.createE0();
        } 
        CGA cga = new CGA();
        cga.set(1, v.x);
        cga.set(2, v.y);
        cga.set(3, v.z);
        return new CGA2Multivector(cga);
    }
    public static CGA2Multivector createPoint(Point3d p){
        return (new CGA2Multivector(1,p.x)).add(new CGA2Multivector(2,p.y)).add(new CGA2Multivector(3,p.z)).
                add((new CGA2Multivector(createEinf())).smul(0.5*(p.x*p.x+p.y*p.y+p.z*p.z))).add(new CGA2Multivector(createE0()));
    }
    
    /*public CGA createPoint(Point3d p){
        CGA e1 = new CGA(1,1d);
        CGA e2 = new CGA(2,1d);
        CGA e3 = new CGA(3,1d);
        CGA e4 = new CGA(4,1d);
        CGA e5 = new CGA(5,1d);
        CGA e0 = CGA.binop_Add(e4,e5);
        CGA einf = CGA.binop_Sub(e5, e4);
        einf = CGA.binop_smul(0.5, einf);
        //CGA p = 
        double[] mVec = new double[CGA._basisLength];
        //mVec[0] = 
        CGA point = new CGA(mVec);
        return point;
    }*/
    
    public static CGA2Multivector createDualPointPair(Point3d p1, Point3d p2){
        return (new CGA2Multivector(CGA.binop_Wedge(createPoint(p1), createPoint(p2)))).dual();
    }
    public static CGA2Multivector createPlane(Vector3d n, double d){
        CGA2Multivector result = new CGA2Multivector(CGA.binop_smul(d, createEinf()));
        result.add(new CGA2Multivector(1,n.x)).add(new CGA2Multivector(2,n.y)).add(new CGA2Multivector(3,n.z));
        return result;
    }
    public static CGA2Multivector createDualLine(Point3d p1, Point3d p2){
        return new CGA2Multivector(de.orat.math.cga.impl2.generated.CGA.unop_Dual(
                CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p1), createPoint(p2)), createEinf())));
    }
    public static CGA2Multivector createLine(Point3d p, Vector3d n){
        return new CGA2Multivector(de.orat.math.cga.impl2.generated.CGA.unop_Dual(
                CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p), createPoint(new Point3d(n))), createEinf())));
    }
    public static CGA2Multivector createDualCircle(Point3d p1, Point3d p2, Point3d p3){
        return new CGA2Multivector(de.orat.math.cga.impl2.generated.CGA.unop_Dual(
                CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p1), createPoint(p2)), createPoint(p3))));
    }
    public static CGA2Multivector createSphere(Point3d o, double r){
        return createPoint(o).sub(new CGA2Multivector(createEinf()).smul(0.5*r*r));
    }
    public static CGA2Multivector createDualSphere(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        return new CGA2Multivector(CGA.unop_Dual(
            CGA.binop_Wedge(CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p1), 
                    createPoint(p2)),createPoint(p3)),createPoint(p4))));
    }
    public static CGA2Multivector createDualPlane(Point3d p1, Point3d p2, Point3d p3){
        return new CGA2Multivector(CGA.unop_Dual(
            CGA.binop_Wedge(CGA.binop_Wedge(CGA.binop_Wedge(
                    createPoint(p1), createPoint(p2)),createPoint(p3)),createEinf())));
    }
    
    public static CGA2Multivector createEinf(){
        return new CGA2Multivector(CGA.binop_smul(0.5d, CGA.binop_Sub(e5, e4)));
    }
    public static CGA2Multivector createE0(){
        return new CGA2Multivector(CGA.binop_Add(e4,e5));
        
    }
    
    /**
     * Get the k-blade (k-vector) of the given grade k.
     * 
     * 0-blades are scalars, 1-blades are vectors, 2-blades are bivectors, 
     * 3-blades are threevectors, 4-blades are quad-vectors and 5-blades are
     * called pseudo-scalars.
     * 
     * @param grade
     * @return 
     */
    public double[] getKBlade(int grade){
        switch (grade){
            case 0 -> {
                return new double[]{_mVec[0]};
            }
            case 1 -> {
                return new double[]{_mVec[1],_mVec[2],_mVec[3],_mVec[4],_mVec[5]};
            }
            case 2 -> {
                return new double[]{_mVec[6],_mVec[7],_mVec[8],_mVec[9],_mVec[10],
                    _mVec[11],_mVec[12],_mVec[13],_mVec[14],_mVec[15]};
            }
            case 3 -> {
                return new double[]{_mVec[16],_mVec[17],_mVec[18],_mVec[19],_mVec[20],
                    _mVec[21],_mVec[22],_mVec[23],_mVec[24],_mVec[25]};
            }
            case 4 -> {
                return new double[]{_mVec[26],_mVec[27],_mVec[28],_mVec[29],_mVec[30]};
            }
            case 5 -> {
                return new double[]{_mVec[31]};
            }
        }
        throw new IllegalArgumentException("Only 0 < grade > 32 ist allowed!");
    }
    
    /**
     * Grade projection/extraction.
     * 
     * Retrives the k-grade part of the multivector.
     * 
     * @param grade
     * @return k-grade part of the multivector
     */
    public CGA2Multivector project(int grade){
        if (grade > 5 || grade < 0) 
            throw new IllegalArgumentException ("Grade "+String.valueOf(grade)+" not allowed!");
        // eigentlich sollte das doch zulässig sein und nur 0 herauskommen, oder?
        
        double[] arr = new double[32];
        switch (grade){
            case 0:
                arr[0] = _mVec[0];
                break;
            case 1:
                for (int i=1;i<=5;i++){
                    arr[i] = _mVec[i];
                }
                break;
            case 2:
                for (int i=6;i<=15;i++){
                    arr[i] = _mVec[i];
                }
                break;
            case 3:
                for (int i=16;i<=26;i++){
                    arr[i] = _mVec[i];
                }
                break;
            case 4:
                for (int i=26;i<=30;i++){
                    arr[i] = _mVec[i];
                }
                break;
            case 5:
                arr[31] = _mVec[31];
        }
        CGA result = new CGA(arr);
        return new CGA2Multivector(result);
    }
    
    
    // Decompose
    
    public FlatAndDirectionParameters decomposeLine(){
        double[] bivectors = getKBlade(2);
        
        // d zeigt von l2 nach l1
        org.jogamp.vecmath.Vector3d d = new org.jogamp.vecmath.Vector3d(bivectors[4], bivectors[1], bivectors[0]);
        System.out.println("dx="+String.valueOf(d.x)+", dy="+String.valueOf(d.y)+", dz="+String.valueOf(d.z));
        
        //TODO
        Point3d m = null;
        return new Decomposition3d.FlatAndDirectionParameters(d,m);
    }
    
    public RoundAndTangentParameters decomposeSphere(){
        Point3d center = new Point3d(_mVec[1],_mVec[2],_mVec[3]);
        center.scale(-0.5);
        double r = 0d;
        return new Decomposition3d.RoundAndTangentParameters(null, center, r);
    }
    
    public PointPairParameters decomposePair2(CGA2Multivector pointPair){
        //de.orat.Math.CGA2Multivector.impl.CGA2Multivector dual = de.orat.Math.CGA2Multivector.impl.CGA2Multivector.unop_Dual(this);
        //de.orat.Math.CGA2Multivector.impl.CGA2Multivector.binop_adds(dual, -Math.sqrt(de.orat.Math.CGA2Multivector.impl.CGA2Multivector.binop_Dot(dual,dual)));
        return null;
    }
    
    public Point3d decomposePoint(){
        return new Point3d(_mVec[1],_mVec[2],_mVec[3]);
    }
   
    public Decomposition3d.RoundAndTangentParameters decomposeCircle(){
        //TODO
        Point3d m = null;
        Vector3d n = null;
        double Ic = get(4)+get(5);// euclidean bivector component factor = e0
        return new Decomposition3d.RoundAndTangentParameters(n, m, Math.sqrt(mul(this).get(0)/Ic));
    }
}