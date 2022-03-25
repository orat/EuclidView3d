package de.orat.math.cga.impl1;

import de.orat.math.cga.util.Decomposition3d;
import de.orat.math.cga.util.Decomposition3d.FlatAndDirectionParameters;
import de.orat.math.cga.util.Decomposition3d.LinePairParameters;
import de.orat.math.cga.util.Decomposition3d.RoundAndTangentParameters;
import de.orat.math.cga.impl2.generated.CGA;
import static de.orat.math.ga.basis.InnerProductTypes.LEFT_CONTRACTION;
import static de.orat.math.ga.basis.InnerProductTypes.RIGHT_CONTRACTION;
import de.orat.math.ga.basis.Metric;
import de.orat.math.ga.basis.Multivector;
import de.orat.math.ga.basis.ScaledBasisBlade;
import de.orat.math.ga.basis.Util;
import de.orat.math.ga.metric.MetricException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Tuple3d;
import org.jogamp.vecmath.Vector3d;
import static de.orat.math.cga.impl1.CGA1Utils.CGA_METRIC;

/**
 * CGA Multivector reference implementation based on the reference implementation 
 * described in Dorst2007.
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGA1Multivector extends Multivector {
   
    /** 
     * Creates a new instance of CGAMultivector.
     */
    public CGA1Multivector() {
        super();
    }

    /** 
     * Creates a new instance of CGAMultivector.
     * 
     * @param s scalar value
     */
    public CGA1Multivector(double s) {
        super(s);
    }

    /** 
     * Creates a new instance of CGAMultivector.
     * 
     * Do not modify 'B' for it is not copied.
     * 
     * @param B list of scaled blades
     */
    public CGA1Multivector(List<ScaledBasisBlade> B) {
	super(B);
    }

    /** 
     * Creates a new instance of CGAMultivector.
     * 
     * Do not modify 'B' for it is not copied.
     * 
     * @param B 
     */
    public CGA1Multivector(ScaledBasisBlade B) {
        super(B);
    }
    
    /** 
     * Creates a new instance of CGAMultivector.
     * 
     * Do not modify mv or its blades for it is not copied.
     * 
     * @param mv multivector 
     */
    public CGA1Multivector(Multivector mv) {
        // throws exception if mv contains blades not corresponding to cga
        super(mv.getBlades());
    }
    
    /**
     * Extract the coordinates from all basis blades of the given grade
     * inclusive 0 values.
     * 
     * @param grade grade
     * @return all coordinates corresponding to the given grade inclusive 0 values.
     */
    public double[] extractCoordinates(int grade){
        List<ScaledBasisBlade> gblades = extractBlades(new int[]{grade});
        int n =  5;//spaceDim();
        CGA1Utils indexTable = CGA1Utils.getInstance();
        double[] result = new double[Util.binominal(n, grade)];
        for (int i=0;i<gblades.size();i++){
            ScaledBasisBlade basisBlade = gblades.get(i);
            result[indexTable.getIndex(basisBlade.bitmap, grade)] = basisBlade.scale;
        }
        return result;
    }
    
    /**
     * Create sphere in inner product null space representation (grade 1 multivector).
     * 
     * Be carefule: This is a dual sphere corresponding to Dorst2007.
     * 
     * @param o origin of the shphere
     * @param r radius of the sphere
     * @return multivector representation of a sphere (e0, e1, e2, e3, einf)
     */
    public static CGA1Multivector createSphere(Point3d o, double r){
        return createSphere(createPoint(o), r);
    }
    /**
     * Create sphere in inner product null space representation (grade 1 multivector).
     * 
     * @param o multivector representing a point
     * @param r radius of the sphere to create
     * @return multivector representing a sphere
     */
    public static CGA1Multivector createSphere(CGA1Multivector o, double r){
        return new CGA1Multivector(o.sub(Multivector.createBasisVector(4,0.5*r*r)));
    }
    /**
     * Create dual sphere.
     * 
     * @param o origin of the sphere
     * @param p point on the sphere
     * @return dual sphere (inner product null space representation) as a multivector
     */
    public static CGA1Multivector createDualSphere(Point3d o, Point3d p){
        return createPoint(p).ip(CGA1Multivector.createBasisVector(4).op(createPoint(o)), LEFT_CONTRACTION);
    }
    /**
     * Create dual sphere in outer product null space representation (grade 4 multivector).
     * 
     * @param p1 multivector representing a point on the sphere
     * @param p2 multivector representing a point on the sphere
     * @param p3 multivector representing a point on the sphere
     * @param p4 multivector representing a point on the sphere
     * @return multivector representing a dual sphere.
     */
    public static CGA1Multivector createDualSphere(CGA1Multivector p1, CGA1Multivector p2, 
                                                CGA1Multivector p3, CGA1Multivector p4){
        return new CGA1Multivector(p1.op(p2).op(p3).op(p4));
    }
    /**
     * Create dual sphere in outer product null space represenation (grade 4 multivector).
     * 
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @return dual sphere in outer product null space representation
     */
    public static CGA1Multivector createDualSphere(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        return new CGA1Multivector(createPoint(p1).op(createPoint(p2)).op(createPoint(p3)).op(createPoint(p4)));
    }
    /**
     * Create a conformal point (grade 1 multivector).
     * 
     * Inner and outer product null space representation is identical.
     * 
     * @param p point
     * @return conformal point
     */
    public static CGA1Multivector createPoint(Tuple3d p){
        return new CGA1Multivector(Multivector.createBasisVector(0)
                .add(Multivector.createBasisVector(1,p.x))
                .add(Multivector.createBasisVector(2,p.y))
                .add(Multivector.createBasisVector(3,p.z))
                .add(Multivector.createBasisVector(4,0.5*(p.x*p.x+p.y*p.y+p.z*p.z))));
    }
    /**
     * Create a conformal point in inner product null space representation (grade 4 multivector).
     * 
     * @param sphere1 first sphere in inner product null space representation
     * @param sphere2 seconds sphere in inner product null space represenation
     * @param sphere3 third sphere in inner product null space representation
     * @param sphere4 forth sphere in inner product null space represenation
     * @return conformal point in inner product null space representation
     */
    public static CGA1Multivector createDualPoint(CGA1Multivector sphere1, CGA1Multivector sphere2, 
                                              CGA1Multivector sphere3, CGA1Multivector sphere4){
        return new CGA1Multivector(sphere1.op(sphere2).op(sphere3).op(sphere4));
    }
    
    /**
     * Create plane in inner product null space representation (grade 1 multivector).
     * 
     * Be careful: This corresponds to dual plane in Dorst2007.
     * 
     * @param n normal vector of the plane
     * @param d distance of the plane to the origin
     * @return conformal plane in inner product null space representation (e1, e2, e3, einf).
     */
    public static CGA1Multivector createPlane(Vector3d n, double d){
        return new CGA1Multivector(Multivector.createBasisVector(1,n.x)
            .add(Multivector.createBasisVector(2,n.y))
            .add(Multivector.createBasisVector(3,n.z))
            .add(Multivector.createBasisVector(4,d)));
    }
    /**
     * Create plane in outer product null space representation (grade 4 multivector).
     * 
     * @param p1 first point in inner product null space representation
     * @param p2 second point in inner product null space representation
     * @param p3 third point in inner product null space representation
     * @return conformal plane in outer product null space representation.
     */
    public static CGA1Multivector createDualPlane(CGA1Multivector p1, CGA1Multivector p2, CGA1Multivector p3){
        return p1.op(p2).op(p3).op(CGA1Multivector.createBasisVector(4));
    }
    /**
     * Create a dual plane as a mid plane between two given point (in outer product
     * null space representation).
     * 
     * @param p1
     * @param p2
     * @return 
     */
    public static CGA1Multivector createDualPlane(CGA1Multivector p1, CGA1Multivector p2){
        return CGA1Multivector.createBasisVector(4).op((p1.op(p2)).dual());
    }
    /**
     * Create dual plane from a point on the plane an its normal vector (in outer product
     * null space representation).
     * 
     * @param p point on the plane.
     * @param n normal vector.
     * @return 
     */
    public static CGA1Multivector createDualPlane(Point3d p, Vector3d n){
        CGA1Multivector cp = createPoint(p);
        CGA1Multivector cn = createPoint(n);
        return cp.ip(cn.op(CGA1Multivector.createBasisVector(4)), LEFT_CONTRACTION);
    }
    /**
     * Create line in inner product null space representation (grade 2 multivector).
     * 
     * Be careful: This corresponds to a dual line in Dorst2007.
     * 
     * @param plane1 plane1 in inner product null space representation
     * @param plane2 plane2 in inner product null space representation
     * @return conformal line in inner product null space representation 
     */
    public static CGA1Multivector createLine(CGA1Multivector plane1, CGA1Multivector plane2){
        return new CGA1Multivector(plane1.op(plane2));
    }
    /**
     * Create line in outer product null space representation (grade 3 multivector).
     * 
     * Be careful: This corresponds to a line in Dorst2007 but to a dual line in
     * Hildenbrand2013
     * 
     * @param p1 first point on the line
     * @param p2 second point on the line or direction of the line
     * @return conformal line in outer product null space representation (tri-vector: 
     * (e12inf, e13inf, e23inf, e10inf, e20inf, e30inf = tree-vector))
     */
    public static CGA1Multivector createDualLine(Point3d p1, Tuple3d p2){
        return new CGA1Multivector(createDualLine(createPoint(p1), createPoint(p2)));
    }
    /**
     * Create line in outer product null space representation (grade 3 multivector).
     * 
     * @param p1 first point in inner product null space representation
     * @param p2 seconds point in inner product null space representation
     * @return conformal line in outer product null space representation (tree-vector)
     * 
     * Be careful: The representation is called dual in Hildenbrand213 but not
     * in Dorst2007.
     */
    public static CGA1Multivector createDualLine(CGA1Multivector p1, CGA1Multivector p2){
        return p1.op(p2).op(CGA1Multivector.createBasisVector(4));
    }
    
    /**
     * Create circle in inner product null space represenation (grade 2 multivector).
     * 
     * @param sphere1
     * @param sphere2
     * @return conformal circle
     */
    public static CGA1Multivector createCircle(CGA1Multivector sphere1, CGA1Multivector sphere2){
        return new CGA1Multivector(sphere1.op(sphere2));
    }
    /**
     * Create dual circle in outer product null space representation (grade 3 multivector).
     * 
     * @param point1
     * @param point2
     * @param point3
     * @return 
     */
    public static CGA1Multivector createDualCircle(CGA1Multivector point1, CGA1Multivector point2, CGA1Multivector point3){
        return new CGA1Multivector(point1.op(point2).op(point3));
    }
    /**
     * Create dual circle in outer product null space representation (grade 3 multivector).
     * 
     * @param point1
     * @param point2
     * @param point3
     * @return 
     */
    public static CGA1Multivector createDualCircle(Point3d point1, Point3d point2, Point3d point3){
        return new CGA1Multivector(createPoint(point1).op(createPoint(point2)).op(createPoint(point3)));
    }
    
    /**
     * Create point pair in inner product null space representation (grade 3 multivector).
     * 
     * @param sphere1
     * @param sphere2
     * @param sphere3
     * @return 
     */
    public static CGA1Multivector createPointPair(CGA1Multivector sphere1, CGA1Multivector sphere2, CGA1Multivector sphere3){
        return new CGA1Multivector(sphere1.op(sphere2).op(sphere3));
    }
    /**
     * Create dual point pair in outer product null space representation (grade 2 multivector).
     * 
     * @param point1
     * @param point2
     * @return 
     */
    public static CGA1Multivector createDualPointPair(CGA1Multivector point1, CGA1Multivector point2){
        return new CGA1Multivector(point1.op(point2));
    }
    public static CGA1Multivector createDualPointPair(Point3d point1, Point3d point2){
        return new CGA1Multivector(createPoint(point1).op(createPoint(point2)));
    }
    
    public static CGA1Multivector createPseudoscalar(){
        return CGA1Multivector.createBasisVector(0).op(CGA1Multivector.createBasisVector(1))
                .op(CGA1Multivector.createBasisVector(2)).op(CGA1Multivector.createBasisVector(3))
                .op(CGA1Multivector.createBasisVector(4));
    }
    /**
     * Create tangent vector which includes a point and a direction in inner product null space 
     * representation.
     * 
     * @param p point 
     * @param u direction of the tangent
     * @return bivector representing a tangend vector
     */
    public CGA1Multivector createTangentVector(Point3d p, Vector3d u){
        CGA1Multivector cp = createPoint(p);
        return cp.ip(cp.op(createPoint(u)).op(CGA1Multivector.createBasisVector(4)), LEFT_CONTRACTION);
    }
    
    // decompose
    
    /**
     * Extract the direction and location of a line/plane.
     * 
     * @param probePoint normalized probe point (e0=1, e1,e2,e3, einf)
     * @return direction of the given flat
     */
    public FlatAndDirectionParameters decomposeFlat(CGA1Multivector probePoint){
        // Kleppe2016
        //Multivector attitude = flat.ip(Multivector.createBasisVector(0), RIGHT_CONTRACTION)
        //        .ip(Multivector.createBasisVector(4), RIGHT_CONTRACTION);
        
        // Dorst2007
        CGA1Multivector attitude = CGA1Multivector.createBasisVector(4).ip(this, LEFT_CONTRACTION);
        double[] dirCoord = attitude.extractCoordinates(1);
        dirCoord[0] *=-1;
        dirCoord[1] *=-1;
        dirCoord[2] *=-1;
        // d1e23+d2e13+d3e12
                
        // Dorst2007 - Formel für dual-flat verwenden
        // locations are determined as dual spheres
        CGA1Multivector loc = probePoint.op(this).gp(generalInverse());
        //CGAMultivector loc = probePoint.ip(this, LEFT_CONTRACTION).gp(generalInverse());
        double[] locationCoord = loc.extractCoordinates(1);
        
        return new FlatAndDirectionParameters(new Vector3d(dirCoord[1], dirCoord[2],dirCoord[3]), 
               new Point3d(locationCoord[1], locationCoord[2], locationCoord[3]));
    }
    
    
    //TODO
    // statt der vielen create-Methoden einfach nur flat() und round() mit unterschiedlicher
    // Zahl von Multivektoren als Argumente einführen?
    // wie bei Python- clifford lib
    
    // Seite 408, Erläuterungen und Tabellen für die Decomposition umsetzen
    
    // Flats: planes and lines (rounds containing the einf)-> hat keine size
    // Rounds: spheres, circles and point-pairs, has a size
    // Both have a weight/density
    
    
    /**
     * Determine the euclid decomposition parameters corresponding to the given dual dualFlat.
     * 
     * Be careful: This corresponds to not-dual in Dorst2007.
     * 
     * @param probePoint normalized probe point (e0=1, e1,e2,e3, einf) to define the location dualFlat parameter.
     * @return euclid parameters. The location is determined as a point of the dualFlat
     * with the smallest distance to the given probe point.
     */
    public FlatAndDirectionParameters decomposeDualFlat(CGA1Multivector probePoint){
        
        // Dorst2007
        //TODO funktioniert nicht - alle components sind 0
        // Ich brauchen undualize into the full space, macht das dual()?
        //CGAMultivector dir = new CGA1Multivector(Multivector.createBasisVector(4).op(this).dual(CGA1Utils.CGA_METRIC));
        //System.out.println("dirvec="+dir.toString(CGA1Utils.baseVectorNames)); // ==0
        
        // Bestimmung von E einf
        CGA1Multivector dir = CGA1Multivector.createBasisVector(4).ip(this, LEFT_CONTRACTION);
        //TODO
        // wie bekomme ich da die attitude raus?
        
        System.out.println("dirvec="+dir.toString(CGA1Utils.baseVectorNames)); // bivector
        
        // Kleppe2016 adaptiert
        // oder left contraction?
        // left contraction ist null wenn k > l
        //dir = dualFlat.op(Multivector.createBasisVector(4)).ip(Multivector.createBasisVector(0), HESTENES_INNER_PRODUCT);
        //System.out.println("dirvec2="+dir.toString(CGA1Utils.baseVectorNames)); // ==0
        
        double[] dirCoord = dir.extractCoordinates(1);
        dirCoord[1] *=-1;
        dirCoord[2] *=-1;
        dirCoord[3] *=-1;
        
        // Dorst2007
        // das sieht richtig aus! ist aber die Formel von dualflat statt flat
        CGA1Multivector location = probePoint.op(this).gp(generalInverse());
        // Formel von flat - funktioniert nicht
        //CGAMultivector location = probePoint.ip(this, LEFT_CONTRACTION).gp(generalInverse());
         
        // grade 1 ist drin und sieht sinnvoll aus, grade-3 ist mit sehr kleinen Werten aber auch dabei
        // und zusätzlich auch e1einf und e0e1
        System.out.println("loc="+location.toString(CGA1Utils.baseVectorNames));
        
        // locations are determined as duals-spheres (e0, e1, e2, e3, einf)
        double[] locationCoord = location.extractCoordinates(1);
        System.out.println("locationCoord=("+String.valueOf(locationCoord[1])+", "+String.valueOf(locationCoord[2])+" ,"+
                String.valueOf(locationCoord[3])+")");
        
        return new Decomposition3d.FlatAndDirectionParameters(new Vector3d(dirCoord[1], dirCoord[2], dirCoord[3]), 
               new Point3d(locationCoord[1], locationCoord[2], locationCoord[3]));
    }
    /**
     * Decompose tangent vector.
     * 
     * @return 
     */
    public RoundAndTangentParameters decomposeTangent(){
        // ungetestet
        // das ist die formel von tangent() aber vermutlich muss ich hier die Formel
        // von dualTangend verwenden!!!
        CGA1Multivector einf = CGA1Multivector.createBasisVector(4);
        CGA1Multivector attitude = new CGA1Multivector(einf.ip(this, LEFT_CONTRACTION).op(einf));
        double[] result = attitude.extractCoordinates(1);
        result[1] *=-1;
        result[2] *=-1;
        result[3] *=-1;
        return new RoundAndTangentParameters(new Vector3d(result[1], result[2], result[3]), decomposeLocation(), 0d);
    }
    public RoundAndTangentParameters decomposeDualTangent(){
        CGA1Multivector einf = CGA1Multivector.createBasisVector(4);
        //TODO
        // das stimmt so noch nicht!
        CGA1Multivector attitude = new CGA1Multivector(einf.ip(this.generalInverse(), LEFT_CONTRACTION).op(einf));
        double[] result = attitude.extractCoordinates(1);
        result[1] *=-1;
        result[2] *=-1;
        result[3] *=-1;
        return new RoundAndTangentParameters(new Vector3d(result[1], result[2], result[3]), decomposeLocation(), 0d);
    }
    
    public RoundAndTangentParameters decomposeRound(){
        RoundAndTangentParameters temp = decomposeTangent();
        
        //FIXME ist gradeInversion wirklich gradeInvolution()?
        CGA1Multivector mvNumerator = gp(gradeInversion());
        CGA1Multivector mvDenominator = CGA1Multivector.createBasisVector(4).ip(this, LEFT_CONTRACTION);
        
        //double squaredSize = mvNumerator.gp(mvDenominator.gp(mvDenominator).generalInverse()).scalarPart();
        
        // (-) d.h. das ist die Formel für dual-round nach Dorst2007. Das sieht also
        // richtig aus.
        // aber der Radius im Test stimmt nur ungefährt mit dem ursprünglichen überein
        // irgendwie scheint mir die location nicht richtig berücksichtigt zu sein. 
        // Vielleicht klappt das nur für Kugeln im Ursprung?
        //FIXME
        double squaredSize = mvNumerator.gp(-1d/mvDenominator.norm_e2()).scalarPart();
        return new RoundAndTangentParameters(temp.attitude(), temp.location(), squaredSize);
    }
    
    /**
     * Determines the location from rounds, dualround, tangent and dual tangent.
     * 
     * @return location
     */
    public Point3d decomposeLocation(){
        // Dorst2007
        CGA1Multivector loc = 
                gp(CGA1Multivector.createBasisVector(4).ip(this, LEFT_CONTRACTION).generalInverse());
        
        //System.out.println("loc="+loc.toString(CGA1Utils.baseVectorNames));
        double[] result = loc.extractCoordinates(1);
        result[1] *=-1;
        result[2] *=-1;
        result[3] *=-1;
        return new Point3d(result[1], result[2], result[3]);
    }
    
    public Decomposition3d.RoundAndTangentParameters decomposeDualRound(){
         
        Vector3d attitude = null;
        //TODO
        
        //TODO
        double radius = 0d;
        
        return new RoundAndTangentParameters(null, decomposeLocation(), radius);
    }
    
    
    /**
     * Intersection of lines, planes and spheres.
     * 
     * @param mv2 Line, Plane, Sphere
     * @return 
     */
    public CGA1Multivector intersect(CGA1Multivector mv2){
        return createPseudoscalar().gp(this).ip(mv2, LEFT_CONTRACTION);
    }
    
    public static Decomposition3d.PointPairParameters decomposePointPair2(){
        //(-1+ pointPair/denominator)* pointPair.ip(einf, IPNS);
        //TODO
        return null;
    }
    
    /**
     * Decompose the geometric product of two lines.
     * 
     * @param l2
     * @return dij and P if l1 and l2 are not coincident and not parallel else an empty array
     */
    public LinePairParameters decomposeLinePair(CGA1Multivector l2){
        
        CGA1Multivector l1l2 = gp(l2);
        
        
        
        // X soll eine sum aus 1- und 2-blade sein
        // falsch da sind auch zwei 4-blades mit drin
        CGA1Multivector n0 = CGA1Multivector.createBasisVector(0);
        CGA1Multivector ni = CGA1Multivector.createBasisVector(4);
        CGA1Multivector X = l1l2.sub(n0.ip(
                l1l2, LEFT_CONTRACTION).op(CGA1Multivector.createBasisVector(4)));
        System.out.println("X="+X.toString(CGA1Utils.baseVectorNames));
        
        // scheint korret sum aus 3- und 1-blade
        CGA1Multivector Y = n0.ip(l1l2, LEFT_CONTRACTION);
        System.out.println("Y="+Y.toString(CGA1Utils.baseVectorNames));
        CGA1Multivector Y3 = Y.extractGrade(3);
        System.out.println("Y3="+Y3.toString(CGA1Utils.baseVectorNames));
        
        CGA1Multivector X2 = X.extractGrade(2);
        System.out.println("X2="+X2.toString(CGA1Utils.baseVectorNames));
         // quatrieren und test auf !=0
        CGA1Multivector X22 = X2.gp(X2);
        
        
        // identisch
        if (l1l2.isScalar()){
            return new Decomposition3d.LinePairParameters(0d, null, null);
        // coplanar
        } else if (X2.isNull()){
            // parallel
            if (Y3.op(ni).isNull()){
                Vector3d dir = new Vector3d();
                //TODO
                return new Decomposition3d.LinePairParameters(0d, null, dir);
            // coplanar mit Schnittpunkt    
            } else {
                //TODO
                double alpha=0;
                Point3d p = new Point3d();
                //TODO
                return new Decomposition3d.LinePairParameters(alpha, p, null);
            }
        // skewed
        } else {
            CGA1Multivector d = new CGA1Multivector(Y3.gp(X2.reverse().gp(1d/X2.norm_e2())));
            System.out.println("d="+d.toString(CGA1Utils.baseVectorNames));

            double[] dValues = d.extractCoordinates(2);
         
            double alpha = 0d;
            Vector3d p = new Vector3d(); 
            return new Decomposition3d.LinePairParameters(alpha, new Point3d(p.x,p.y,p.z), 
                    new Vector3d(dValues[0], dValues[1], dValues[2]));
        }
    }
    
    
    // Operatoren
    
    public double scp(CGA1Multivector x) {
        return super.scp(x, CGA_METRIC);
    }
    public CGA1Multivector op(CGA1Multivector x) {
       return new CGA1Multivector(super.op(x));
    }
    public CGA1Multivector ip(CGA1Multivector x, int type){
        return new CGA1Multivector(super.ip(x, CGA_METRIC, type));
    }
    public CGA1Multivector gp(CGA1Multivector x){
        return new CGA1Multivector(super.gp(x));
    }
    @Override
    public CGA1Multivector gp(double a) {
        return new CGA1Multivector(super.gp(a));
    }
    public static CGA1Multivector createBasisVector(int idx) throws IllegalArgumentException {
        if (idx >= CGA1Utils.baseVectorNames.length) throw new IllegalArgumentException("Idx must be smaller than 5!");
        return new CGA1Multivector(Multivector.createBasisVector(idx));
    }
    @Override
    public CGA1Multivector reverse() {
        return new CGA1Multivector(super.reverse());
    }
    @Override
    public CGA1Multivector extractGrade(int g){
        return new CGA1Multivector(super.extractGrade(g));
    }
    public CGA1Multivector sub(CGA1Multivector x) {
        return new CGA1Multivector(super.sub(x));
    }
    @Override
    public CGA1Multivector sub(double x){
        return new CGA1Multivector(super.sub(x));
    }
    @Override
    public CGA1Multivector exp() {
        return new CGA1Multivector(super.exp(CGA_METRIC));
    }
    @Override
    public CGA1Multivector gradeInversion() {
        return new CGA1Multivector(super.gradeInversion());
    }
    public CGA1Multivector generalInverse() {
        return new CGA1Multivector(super.generalInverse(CGA_METRIC));
    }
    public CGA1Multivector dual() {
        return new CGA1Multivector(super.dual(CGA_METRIC));
    }
}