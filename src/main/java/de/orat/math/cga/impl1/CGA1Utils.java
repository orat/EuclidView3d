package de.orat.math.cga.impl1;

import static de.orat.math.cga.impl1.CGA1Multivector.createPseudoscalar;
import de.orat.math.cga.impl2.CGA2Multivector;
import de.orat.math.cga.util.Decomposition3d;
import static de.orat.math.ga.basis.InnerProductTypes.HESTENES_INNER_PRODUCT;
import static de.orat.math.ga.basis.InnerProductTypes.LEFT_CONTRACTION;
import de.orat.math.ga.basis.Metric;
import de.orat.math.ga.basis.Multivector;
import de.orat.math.ga.basis.ScaledBasisBlade;
import de.orat.math.ga.basis.Util;
import de.orat.math.ga.metric.MetricException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGA1Utils {
    
    public static final int IPNS = 0; // standard representation (inner product null space)
    public static final int OPNS = 1; // dual representation (outer product null space)
 
    //public static enum BaseVectorNames {"no", "e1", "e2", "e3", "ni"};
    
    public static final String[] baseVectorNames = {"no", "e1", "e2", "e3", "ni"};
    
    // representational space - Minkovski space
    public static Metric CGA_METRIC;
    static {
        try {
            // Metric: [-1.0, 0.9999999999999998, 1.0, 1.0, 1.0]
            CGA_METRIC = new Metric(new double[][]{
                {0.0, 0.0, 0.0, 0.0, -1.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0 ,1.0, 0.0},
                {-1.0, 0.0, 0.0 , 0.0, 0.0}});
        } catch (MetricException e){}
    }
    public static Metric CGA2_METRIC;
    static {
        try {
            CGA2_METRIC = new Metric(new double[][]{
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0 ,1.0, 0.0},
                {0.0, 0.0, 0.0 , 0.0, -1.0}});
        } catch (MetricException e){}
    }
    private static CGA1Utils instance = null;
    
    // key= bitset as int, value = index in the multivector
    private final Map<Integer, Integer> bitsetMap = new HashMap<>();
    
    // key=index in the multivector, value = index = the grade coordinates
    private final Map<Integer, Integer> gradeCoordinatesMap = new HashMap<>();
    
    // besser hier die Dimension n übergeben und dann die Tabellen automatisch passend
    // generieren statt hier die Fixkodierung. Dann kann ich das auch allg. IndexTable nennen
    //FIXME
    static CGA1Utils getInstance(){
        if (instance == null){
            instance = new CGA1Utils();
        }
        return instance;
    }
    
    int getIndex(int bitset){
        return bitsetMap.get(bitset);
    }
    int getIndex(int bitset, int grade){
        return gradeCoordinatesMap.get(bitsetMap.get(bitset));
    }
    
    /**
     * Create index table for CGA.
     * 
     * 03/22 Oliver Rettig
     * Better implement generic functionality with dimension as parameter.
     * 
     * @Deprecated 
     */
    private CGA1Utils(){
        // scalar
        bitsetMap.put(0,0);
        gradeCoordinatesMap.put(0,0);
        // vectors
        bitsetMap.put(1,1);
        gradeCoordinatesMap.put(1,0); // n0 // gaalop e1
        bitsetMap.put(2,2);
        gradeCoordinatesMap.put(2,1); // e1 // gaalop e2
        bitsetMap.put(4,3);
        gradeCoordinatesMap.put(3,2); // e2 // gaalop e3
        bitsetMap.put(8,4);
        gradeCoordinatesMap.put(4,3); // e3 // gaalop ni
        bitsetMap.put(16,5);
        gradeCoordinatesMap.put(5,4); // ni / gaalop n0
        // bivectors
        bitsetMap.put(1+2,6);
        gradeCoordinatesMap.put(6,0);
        bitsetMap.put(1+4,7);
        gradeCoordinatesMap.put(7,1);
        bitsetMap.put(1+8,8);
        gradeCoordinatesMap.put(8,2);
        bitsetMap.put(1+16,9);
        gradeCoordinatesMap.put(9,3);
        bitsetMap.put(2+4,10); // e12
        gradeCoordinatesMap.put(10,4);
        bitsetMap.put(2+8,11); // e13
        gradeCoordinatesMap.put(11,5);
        bitsetMap.put(2+16,12);
        gradeCoordinatesMap.put(12,6);
        bitsetMap.put(4+8,13); // e23
        gradeCoordinatesMap.put(13,7);
        bitsetMap.put(4+16,14);
        gradeCoordinatesMap.put(14,8);
        bitsetMap.put(8+16,15);
        gradeCoordinatesMap.put(15,9);
        //trivectors
        bitsetMap.put(1+2+4,16); //e012
        gradeCoordinatesMap.put(16,0);
        bitsetMap.put(1+2+8,17);
        gradeCoordinatesMap.put(17,1);
        bitsetMap.put(1+2+16,18);
        gradeCoordinatesMap.put(18,2);
        bitsetMap.put(1+4+8,19);
        gradeCoordinatesMap.put(19,3);
        bitsetMap.put(1+4+16,20);
        gradeCoordinatesMap.put(20,4);
        bitsetMap.put(1+8+16,21);
        gradeCoordinatesMap.put(21,5);
        bitsetMap.put(2+4+8,22); 
        gradeCoordinatesMap.put(22,6);
        bitsetMap.put(2+4+16,23); // e124
        gradeCoordinatesMap.put(23,7);
        bitsetMap.put(2+8+16,24); // e134
        gradeCoordinatesMap.put(24,8);
        bitsetMap.put(4+8+16,25); // e234
        gradeCoordinatesMap.put(25,9);
        //quatvectors
        bitsetMap.put(1+2+4+8,26);
        gradeCoordinatesMap.put(26,0);
        bitsetMap.put(1+2+4+16,27);
        gradeCoordinatesMap.put(27,1);
        bitsetMap.put(1+2+8+16,28);
        gradeCoordinatesMap.put(28,2);
        bitsetMap.put(1+4+8+16,29);
        gradeCoordinatesMap.put(29,3);
        bitsetMap.put(2+4+8+16,30);
        gradeCoordinatesMap.put(30,4);
        // pseudoscalar
        bitsetMap.put(1+2+4+8+16,31);
        gradeCoordinatesMap.put(31,0);
    }
    
    // scheint zu stimmen
    /**
     * Squared distance between two points.
     * 
     * Hildenbrand1998 page 35
     * 
     * @param p1 normalized first point
     * @param p2 normalized second point
     * @return squared distance between two normalized points
     */
    public static double squareDistanceBetweenPoints(CGA1Multivector p1, CGA1Multivector p2){
        return -2*p1.scp(p2, CGA_METRIC);
    }
    
    /**
      * Decompose l2l1 into angle, distance, direction.
      * 
      * A Covariant approach to Geometry using Geometric Algebra.
      * Technical report. Universit of Cambridge Departement of Engineering, 
      * Cambridge, UK (2004). 
      * A. Lasenby, J. Lasenby, R. Wareham
      * Formula 5.22, page 46
      * 
      * @param l2l1
      * @return parameters describing the pose of two lines to each other
      */
    public static Decomposition3d.LinePairParameters decomposeLinePair(CGA1Multivector l2l1){
        
        System.out.println("l2l1:"+l2l1.toString(CGA1Utils.baseVectorNames));
        
        // Skalar
        double cosalpha = l2l1.extractCoordinates(0)[0];
        System.out.println("cosalpha="+String.valueOf(cosalpha));
        System.out.println("alpha="+String.valueOf(Math.acos(cosalpha)*180/Math.PI));
       
        // Bivektoren 
        double[] bivectors = l2l1.extractCoordinates(2);
        double[] quadvectors = l2l1.extractCoordinates(4);
        
        // attitude zeigt von l1 nach l2?
        
        double dist = 0d;
        double sinalpha = 0;
        
        org.jogamp.vecmath.Vector3d attitude = null;
        
        // Geraden nicht senkrecht zueinander
        if (cosalpha != 0){
            System.out.println("attitude aus e01, e02 und e03 bestimmen!");
            attitude = new org.jogamp.vecmath.Vector3d(
                -bivectors[0]/cosalpha, -bivectors[1]/cosalpha, -bivectors[2]/cosalpha);
            System.out.println("d(vectors)= ("+String.valueOf(attitude.x)+", "+String.valueOf(attitude.y)+", "+String.valueOf(attitude.z)+")");
            dist = attitude.length();
            System.out.println("dist = "+dist);
            attitude.normalize();
            System.out.println("d(vectors) normiert= ("+String.valueOf(attitude.x)+", "+String.valueOf(attitude.y)+", "+String.valueOf(attitude.z)+")");
            
        } 
            
        // Geraden sind nicht parallel
        if (cosalpha*cosalpha != 1){
            System.out.println("attitude aus e23, e13, e12 und e0123 bestimmen!");
            double cos2alpha = 1d-cosalpha*cosalpha;
            attitude = new org.jogamp.vecmath.Vector3d(
                -bivectors[7]*quadvectors[0]/cos2alpha, 
                 bivectors[5]*quadvectors[0]/cos2alpha, 
                -bivectors[4]*quadvectors[0]/cos2alpha);
            System.out.println("d(vectors)= ("+String.valueOf(attitude.x)+", "+String.valueOf(attitude.y)+", "+String.valueOf(attitude.z)+")");
            dist = attitude.length();
            System.out.println("dist = "+dist);
            attitude.normalize();
            System.out.println("d(vectors) normiert= ("+String.valueOf(attitude.x)+", "+String.valueOf(attitude.y)+", "+String.valueOf(attitude.z)+")");
            
        } 
         
        // Geraden haben keinen Schnittpunkt
        if (dist != 0d){
            sinalpha = -quadvectors[0]/dist;
        } else {
            System.out.println("Geraden schneiden sich!");
            //FIXME
            // ist das so richtig?
            sinalpha = 0d;
        }
        //TODO
        Point3d location = null;
        
        return new Decomposition3d.LinePairParameters(Math.atan2(cosalpha, sinalpha), location, attitude);
    }
}
