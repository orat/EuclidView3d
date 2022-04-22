package de.orat.math.cga.impl2;

import de.orat.math.cga.impl2.generated.CGA;
import de.orat.math.cga.util.Decomposition3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class TempUtils {
        
    // Decompose
    
    public Decomposition3d.FlatAndDirectionParameters decomposeLine(){
        double[] bivectors = extractCoordinates(2);
        
        // d zeigt von l2 nach l1
        org.jogamp.vecmath.Vector3d d = new org.jogamp.vecmath.Vector3d(bivectors[4], bivectors[1], bivectors[0]);
        System.out.println("dx="+String.valueOf(d.x)+", dy="+String.valueOf(d.y)+", dz="+String.valueOf(d.z));
        
        //TODO
        Point3d m = null;
        return new Decomposition3d.FlatAndDirectionParameters(d,m);
    }
    
    public Decomposition3d.RoundAndTangentParameters decomposeSphere(){
        Point3d center = new Point3d(_mVec[1],_mVec[2],_mVec[3]);
        center.scale(-0.5);
        double r = 0d;
        return new Decomposition3d.RoundAndTangentParameters(null, center, r);
    }
    
    public Decomposition3d.PointPairParameters decomposePair2(CGA2Multivector pointPair){
        //de.orat.Math.CGA2Multivector.impl.CGA2Multivector dual = de.orat.Math.CGA2Multivector.impl.CGA2Multivector.unop_Dual(this);
        //de.orat.Math.CGA2Multivector.impl.CGA2Multivector.binop_adds(dual, -Math.sqrt(de.orat.Math.CGA2Multivector.impl.CGA2Multivector.binop_Dot(dual,dual)));
        return null;
    }
    
    public Point3d decomposePoint(){
        return new Point3d(_mVec[1],_mVec[2],_mVec[3]);
    }
   
    /*public RoundAndTangentParameters decomposeCircle(){
        //TODO
        Point3d m = null;
        Vector3d n = null;
        double Ic = get(4)+get(5);// euclidean bivector component factor = e0
        return new Decomposition3d.RoundAndTangentParameters(n, m, Math.sqrt((CGA2Multivector) this.mul(this)).get(0)/Ic));
    }*/
    
      
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
        return (CGA2Multivector) (new CGA2Multivector(1,p.x)).add(new CGA2Multivector(2,p.y)).add(new CGA2Multivector(3,p.z)).
                add(new CGA2Multivector((createEinf())).gp(0.5*(p.x*p.x+p.y*p.y+p.z*p.z))).add(new CGA2Multivector(createE0()));
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
                CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p), createPoint(new Point3d(n))), createBasisVectorEinf())));
    }
    public static CGA2Multivector createDualCircle(Point3d p1, Point3d p2, Point3d p3){
        return new CGA2Multivector(de.orat.math.cga.impl2.generated.CGA.unop_Dual(
                CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p1), createPoint(p2)), createPoint(p3))));
    }
    public static CGA2Multivector createSphere(Point3d o, double r){
        return (CGA2Multivector) createPoint(o).sub(new CGA2Multivector(createBasisVectorEinf()).gp(0.5*r*r));
    }
    public static CGA2Multivector createDualSphere(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        return new CGA2Multivector(CGA.unop_Dual(
            CGA.binop_Wedge(CGA.binop_Wedge(CGA.binop_Wedge(createPoint(p1), 
                    createPoint(p2)),createPoint(p3)),createPoint(p4))));
    }
    public static CGA2Multivector createDualPlane(Point3d p1, Point3d p2, Point3d p3){
        return new CGA2Multivector(CGA.unop_Dual(
            CGA.binop_Wedge(CGA.binop_Wedge(CGA.binop_Wedge(
                    createPoint(p1), createPoint(p2)),createPoint(p3)),createBasisVectorEinf())));
    }
}
