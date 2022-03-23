package de.orat.math.cga;

import static de.orat.math.cga.CGAMultivector.createPseudoscalar;
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
public class CGAUtils {
    
    public static final int IPNS = 0; // standard representation (inner product null space)
    public static final int OPNS = 1; // dual representation (outer product null space)
 
    public static final String[] baseVectorNames = {"no", "e1", "e2", "e3", "ni"};
    
    public static Metric METRIC;
    static {
        try {
            METRIC = new Metric(new double[][]{
                {0.0, 0.0, 0.0, 0.0, -1.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0 ,1.0, 0.0},
                {-1.0, 0.0, 0.0 , 0.0, 0.0}});
        } catch (MetricException e){}
    }
    private static CGAUtils instance = null;
    
    // key= bitset as int, value = index in the multivector
    private final Map<Integer, Integer> bitsetMap = new HashMap<>();
    
    // key=index in the multivector, value = index = the grade coordinates
    private final Map<Integer, Integer> gradeCoordinatesMap = new HashMap<>();
    
    // besser hier die Dimension n übergeben und dann die Tabellen automatisch passend
    // generieren statt hier die Fixkodierung. Dann kann ich das auch allg. IndexTable nennen
    //FIXME
    static CGAUtils getInstance(){
        if (instance == null){
            instance = new CGAUtils();
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
    private CGAUtils(){
        // scalar
        bitsetMap.put(0,0);
        gradeCoordinatesMap.put(0,0);
        // vectors
        bitsetMap.put(1,1);
        gradeCoordinatesMap.put(1,0); // n0
        bitsetMap.put(2,2);
        gradeCoordinatesMap.put(2,1); // e1
        bitsetMap.put(4,3);
        gradeCoordinatesMap.put(3,2); // e2
        bitsetMap.put(8,4);
        gradeCoordinatesMap.put(4,3); // e3
        bitsetMap.put(16,5);
        gradeCoordinatesMap.put(5,4); // ni
        // bivectors
        bitsetMap.put(1+2,6);
        gradeCoordinatesMap.put(6,0);
        bitsetMap.put(1+4,7);
        gradeCoordinatesMap.put(7,1);
        bitsetMap.put(1+8,8);
        gradeCoordinatesMap.put(8,2);
        bitsetMap.put(1+16,9);
        gradeCoordinatesMap.put(9,3);
        bitsetMap.put(2+4,10);
        gradeCoordinatesMap.put(10,4);
        bitsetMap.put(2+8,11);
        gradeCoordinatesMap.put(11,5);
        bitsetMap.put(2+16,12);
        gradeCoordinatesMap.put(12,6);
        bitsetMap.put(4+8,13);
        gradeCoordinatesMap.put(13,7);
        bitsetMap.put(4+16,14);
        gradeCoordinatesMap.put(14,8);
        bitsetMap.put(8+16,15);
        gradeCoordinatesMap.put(15,9);
        //trivectors
        bitsetMap.put(1+2+4,16);
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
        bitsetMap.put(2+4+16,23);
        gradeCoordinatesMap.put(23,7);
        bitsetMap.put(2+8+16,24);
        gradeCoordinatesMap.put(24,8);
        bitsetMap.put(4+8+16,25);
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
    public static double squareDistanceBetweenPoints(CGAMultivector p1, CGAMultivector p2){
        //Multivector mv = p1.ip(p2, METRIC, LEFT_CONTRACTION);
        //System.out.println("p1*p2="+mv.toString(CGAUtils.baseVectorNames));
        // return mv.scalarPart()*(-2d);
        return -2*p1.scp(p2, METRIC);
    }
}
