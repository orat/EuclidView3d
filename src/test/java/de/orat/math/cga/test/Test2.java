package de.orat.math.cga.test;

import de.orat.math.cga.impl1.CGA1Multivector;
import de.orat.math.cga.impl1.CGA1Utils;
import de.orat.math.cga.util.Decomposition3d;
import de.orat.math.cga.util.Decomposition3d.FlatAndDirectionParameters;
import de.orat.math.cga.util.Decomposition3d.RoundAndTangentParameters;
import de.orat.math.ga.basis.Multivector;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class Test2 {
    
    public Test2() {
    }

    public void testPlane(){
        System.out.println("---------------------- plane ----");
        Vector3d n = new Vector3d(0d,0d,1d);
        double d = 2d;
        CGA1Multivector plane = CGA1Multivector.createPlane(n, d);
        System.out.println("plane="+plane.toString(CGA1Utils.baseVectorNames));
        CGA1Multivector cp = CGA1Multivector.createPoint(new Point3d(0.5d,0.5d,0.5d));
        System.out.println("cp="+cp.toString(CGA1Utils.baseVectorNames));
        FlatAndDirectionParameters flat = plane.decomposeFlat(cp);
        System.out.println("location=("+String.valueOf(flat.location().x)+", "+
                String.valueOf(flat.location().y)+", "+String.valueOf(flat.location().z)+")");
        System.out.println("n=("+String.valueOf(flat.attitude().x)+", "+
                String.valueOf(flat.attitude().y)+", "+String.valueOf(flat.attitude().z)+")");
    }
    public void testSphere(){
        System.out.println("----------------- sphere -----");
        Point3d p = new Point3d(0.02,0.02,1);
        System.out.println("p=("+String.valueOf(p.x)+","+String.valueOf(p.y)+","+String.valueOf(p.z)+")");
        double radius = 2d;
        System.out.println("radius="+String.valueOf(radius));
        CGA1Multivector sphere = CGA1Multivector.createSphere(p, radius);
        System.out.println("sphere="+sphere.toString(CGA1Utils.baseVectorNames));
        RoundAndTangentParameters rp = sphere.decomposeRound();
        System.out.println("origin = ("+String.valueOf(rp.location().x)+", "+
                String.valueOf(rp.location().y)+", "+String.valueOf(rp.location().z)+")");
        System.out.println("radius = "+String.valueOf(Math.sqrt(Math.abs(rp.squaredSize()))));
    }
    // scheint zu funktionieren
    public void testPoint(){
        System.out.println("--------------- point -------");
        Point3d p = new Point3d(0.02,0.02,1);
        System.out.println("p=("+String.valueOf(p.x)+","+String.valueOf(p.y)+","+String.valueOf(p.z)+")");
        CGA1Multivector cp = CGA1Multivector.createPoint(p);
        System.out.println("cp="+cp.toString(CGA1Utils.baseVectorNames));
        Point3d p1 = cp.decomposeRound().location();
        System.out.println("p=("+String.valueOf(p1.x)+","+String.valueOf(p1.y)+","+String.valueOf(p1.z)+")");
    }
    
    // scheint zu funktionieren
    // TODO Vergleich mit festen ERgebniszahlen einbauen etc.
    /*public void testSquareDistanceBetweenPoints(){
        CGA1Multivector p1 = CGA1Multivector.createPoint(new Point3d(0.02,0.02,1));
        System.out.println("p1="+p1.toString(CGA1Utils.baseVectorNames));
        CGA1Multivector p2 = CGA1Multivector.createPoint(new Point3d(1,0.02,1));
        System.out.println("p2="+p2.toString(CGA1Utils.baseVectorNames));
        
        double result = CGA1Utils.squareDistanceBetweenPoints(p1, p2);
        System.out.println("dist="+result);
    }*/
    
    /**
     * @Test2
     */
    public void testLine() {
        System.out.println("-------------- line --------");
        Multivector no = Multivector.createBasisVector(0);
        Multivector e1 = Multivector.createBasisVector(1);
        Multivector e2 = Multivector.createBasisVector(2);
        Multivector e3 = Multivector.createBasisVector(3);
        Multivector ni = Multivector.createBasisVector(4);
        
        Point3d p1 = new Point3d(0.02,0.02,1);
        System.out.println("p1=("+String.valueOf(p1.x)+","+String.valueOf(p1.y)+","+String.valueOf(p1.z)+")");
        CGA1Multivector cp1 = CGA1Multivector.createPoint(p1);
        System.out.println("cp1="+cp1.toString(CGA1Utils.baseVectorNames));
        
        Point3d p2 = new Point3d(1,0.02,1);
        System.out.println("p2=("+String.valueOf(p2.x)+","+String.valueOf(p2.y)+","+String.valueOf(p2.z)+")");
        CGA1Multivector cp2 = CGA1Multivector.createPoint(p2);
        System.out.println("cp2="+cp2.toString(CGA1Utils.baseVectorNames));
        
        Vector3d n = new Vector3d(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
        System.out.println("n=("+String.valueOf(n.x)+","+String.valueOf(n.y)+","+String.valueOf(n.z)+")");
        
        CGA1Multivector l1 = CGA1Multivector.createDualLine(p1, p2);
        System.out.println("l1= "+l1.toString(CGA1Utils.baseVectorNames));
      
        FlatAndDirectionParameters flatParameters = l1.decomposeDualFlat(CGA1Multivector.createPoint(new Point3d()));
        Vector3d dir = flatParameters.attitude();
        System.out.println("dir=("+String.valueOf(dir.x)+", "+String.valueOf(dir.y)+
                ", "+String.valueOf(dir.z)+")");
        Point3d loc = flatParameters.location();
        System.out.println("loc=("+String.valueOf(loc.x)+", "+String.valueOf(loc.y)+
                ", "+String.valueOf(loc.z)+")");
    }
    
    
    
        /*
            // ipns representation
            CGA1Multivector l1dual = new CGA1Multivector(l1.dual(CGA1Utils.CGA_METRIC));
            System.out.println("l1dual= "+l1dual.toString(CGA1Utils.baseVectorNames));
            Vector3d dirdual = l1dual.decomposeFlat(CGA1Multivector.createPoint(new Point3d())).attitude();
             System.out.println("dir=("+String.valueOf(dirdual.x)+", "+String.valueOf(dirdual.y)+
                ", "+String.valueOf(dirdual.z)+")");
        
        Point3d p3 = new Point3d(0.02,0.02,2);
        Point3d p4 = new Point3d(1,1,2.2);
        System.out.println("p3="+String.valueOf(p3.x)+", "+String.valueOf(p3.y)+", "+String.valueOf(p3.z));
        System.out.println("p4="+String.valueOf(p4.x)+", "+String.valueOf(p4.y)+", "+String.valueOf(p4.z));
        CGA1Multivector l2 = CGA1Multivector.createDualLine(p3, p4);
        System.out.println("l2= "+l2.toString(CGA1Utils.baseVectorNames));
        CGA1Multivector l2l1 = new CGA1Multivector(l2.gp(l1));
        System.out.println("l2l1= "+l2l1.toString(CGA1Utils.baseVectorNames));
        
        CGA1Multivector.decomposeLinePair(l2,l1);*/
}