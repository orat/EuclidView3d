package de.orat.math.cga.api;

import de.orat.math.cga.impl1.CGA1Multivector1a;
import de.orat.math.cga.spi.iCGAMultivector;
import de.orat.math.cga.util.Decomposition3d;
import static de.orat.math.ga.basis.InnerProductTypes.LEFT_CONTRACTION;
import static de.orat.math.ga.basis.InnerProductTypes.RIGHT_CONTRACTION;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Quat4d;
import org.jogamp.vecmath.Tuple3d;
import org.jogamp.vecmath.Vector3d;

/**
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class CGAMultivector {
    
    private static CGAMultivector defaultInstance = create();
    private static int default_ip_type = LEFT_CONTRACTION;
    
    private iCGAMultivector impl;
    
    static CGAMultivector create(){
        return new CGAMultivector(new CGA1Multivector1a());
    }
    
    CGAMultivector(iCGAMultivector impl){
        this.impl = impl;
    }
    
    
    public static CGAMultivector createBasisVectorOrigin(double scale){
        return new CGAMultivector(defaultInstance.impl.createBasisVectorOrigin(scale));
    }
    public static CGAMultivector createBasisVectorEinf(double scale){
        return new CGAMultivector(defaultInstance.impl.createBasisVectorEinf(scale));
    }
    public static CGAMultivector createBasisVectorEx(double scale){
        return new CGAMultivector(defaultInstance.impl.createBasisVectorEx(scale));
    }
    public static CGAMultivector createBasisVectorEy(double scale){
        return new CGAMultivector(defaultInstance.impl.createBasisVectorEy(scale));
    }
    public static CGAMultivector createBasisVectorEz(double scale){
        return new CGAMultivector(defaultInstance.impl.createBasisVectorEz(scale));
    }
    public static CGAMultivector createBasisVectorE3(){
        return createBasisVectorEx(1d).add(createBasisVectorEy(1d)).add(createBasisVectorEz(1d));
    }
    public static CGAMultivector createVector(Vector3d v){
        return createBasisVectorEx(v.x).add(createBasisVectorEy(v.y)).add(createBasisVectorEz(v.z));
    }
    
    
    public double[] extractCoordinates(int grade){
        return impl.extractCoordinates(grade);
    }
   
      
    // Create conformal algebra primitives
    
    
    /**
     * Create sphere in inner product null space representation (grade 1 multivector).
     * 
     * Be careful: This is a dual real sphere corresponding to Dorst2007 but a 
     * real sphere in Hildenbrand2013.
     * 
     * @param o origin of the shphere
     * @param r radius of the sphere (A negative radius does not create an imagninary 
     *        sphere. Use the method createImaginarySphere() instead.)
     * @return multivector representation of a sphere (e0, e1, e2, e3, einfM)
     */
    public static CGAMultivector createSphere(Point3d o, double r){
        return createSphere(createPoint(o), r);
    }
    /**
     * Create sphere in inner product null space representation (grade 1 multivector).
     * 
     * Multiplication of the resulting multivector by double alpha is possible.
     * 
     * Dorst2007 page 363 == Hildenbrand1998 page 29
     * 
     * @param location multivector representing a result
     * @param r radius of the sphere to create
     * @return multivector representing a sphere
     */
    public static CGAMultivector createSphere(CGAMultivector location, double r){
        return location.sub(createBasisVectorEinf(0.5*r*r));
    }
    public static CGAMultivector createImaginarySphere(CGAMultivector o, double r){
        return o.add(createBasisVectorEinf(0.5*r*r));
    }
    /**
     * Create dual sphere.
     * 
     * Multiplication of the resulting multivector by double alpha is possible.
     * 
     * @param o origin of the sphere
     * @param p result on the sphere
     * @return dual sphere (inner product null space representation) as a multivector of grade 4.
     */
    public static CGAMultivector createDualSphere(Point3d o, Point3d p){
        return createPoint(p).ip(createBasisVectorEinf(1d).op(createPoint(o)));
    }
    /**
     * Create dual sphere in outer product null space representation (grade 4 multivector).
     * 
     * @param p1 multivector representing a result on the sphere
     * @param p2 multivector representing a result on the sphere
     * @param p3 multivector representing a result on the sphere
     * @param p4 multivector representing a result on the sphere
     * @return multivector representing a dual sphere.
     */
    public static CGAMultivector createDualSphere(CGAMultivector p1, CGAMultivector p2, 
                                                CGAMultivector p3, CGAMultivector p4){
        return p1.op(p2).op(p3).op(p4);
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
    public static CGAMultivector createDualSphere(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        return createPoint(p1).op(createPoint(p2)).op(createPoint(p3)).op(createPoint(p4));
    }
    /**
     * Create a conformal point (grade 1 multivector).
     * 
     * Inner and outer product null space representation is identical.<p>
     * 
     * Multiplication of the multivector by double alpha possible
     * 
     * @param p result
     * @return conformal result
     */
    public static CGAMultivector createPoint(Tuple3d p){
        return createBasisVectorOrigin(1d)
                .add(createBasisVectorEx(p.x))
                .add(createBasisVectorEy(p.y))
                .add(createBasisVectorEz(p.z))
                .add(createBasisVectorEinf(0.5*(p.x*p.x+p.y*p.y+p.z*p.z)));
    }
    /**
     * Create a conformal result in inner product null space representation (grade 4 multivector).
     * 
     * @param sphere1 first sphere in inner product null space representation
     * @param sphere2 seconds sphere in inner product null space represenation
     * @param sphere3 third sphere in inner product null space representation
     * @param sphere4 forth sphere in inner product null space represenation
     * @return conformal result in inner product null space representation
     */
    public static CGAMultivector createDualPoint(CGAMultivector sphere1, CGAMultivector sphere2, 
                                              CGAMultivector sphere3, CGAMultivector sphere4){
        return sphere1.op(sphere2).op(sphere3).op(sphere4);
    }
    
    /**
     * Create plane in inner product null space representation (grade 1 multivector).
     * 
     * Be careful: This corresponds to dual plane in Dorst2007.
     * 
     * @param n normal vector of the plane
     * @param d distance of the plane to the origin
     * @return conformal plane in inner product null space representation (e1, e2, e3, einfM).
     */
    public static CGAMultivector createPlane(Vector3d n, double d){
        return createBasisVectorEx(n.x)
            .add(createBasisVectorEy(n.y))
            .add(createBasisVectorEz(n.z))
            .add(createBasisVectorEinf(d));
    }
    /**
     * Create plane in outer product null space representation (grade 4 multivector).
     * 
     * @param p1 first result in inner product null space representation
     * @param p2 second result in inner product null space representation
     * @param p3 third result in inner product null space representation
     * @return conformal plane in outer product null space representation.
     */
    public static CGAMultivector createDualPlane(CGAMultivector p1, CGAMultivector p2, CGAMultivector p3){
        return p1.op(p2).op(p3).op(createBasisVectorEinf(1d));
    }
    /**
     * Create a dual plane as a mid plane between two given result (in outer product
     * null space representation).
     * 
     * @param p1
     * @param p2
     * @return 
     */
    public static CGAMultivector createDualPlane(CGAMultivector p1, CGAMultivector p2){
        return createBasisVectorEinf(1d).op((p1.op(p2)).dual());
    }
    /**
     * Create dual plane from a result on the plane an its normal vector (in outer product
     * null space representation).
     * 
     * @param p result on the plane.
     * @param n normal vector.
     * @return 
     */
    public static CGAMultivector createDualPlane(Point3d p, Vector3d n){
        CGAMultivector cp = createPoint(p);
        CGAMultivector cn = createPoint(n);
        return cp.ip(cn.op(createBasisVectorEinf(1d)));
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
    public static CGAMultivector createLine(CGAMultivector plane1, CGAMultivector plane2){
        return plane1.op(plane2);
    }
    /**
     * Create line in outer product null space representation (grade 3 multivector).
     * 
     * Be careful: This corresponds to a line in Dorst2007 but to a dual line in
     * Hildenbrand2013.
     * 
     * @param p1 first point on the line
     * @param p2 second point on the line or direction of the line
     * @return conformal line in outer product null space representation (tri-vector: 
     * (e12inf, e13inf, e23inf, e10inf, e20inf, e30inf = tri-vector))
     */
    public static CGAMultivector createDualLine(Point3d p1, Tuple3d p2){
        return createDualLine(createPoint(p1), createPoint(p2));
    }
    /**
     * Create line in outer product null space representation (grade 3 multivector).
     * 
     * @param p1 first result in inner product null space representation
     * @param p2 seconds result in inner product null space representation
     * @return conformal line in outer product null space representation (tree-vector)
     * 
     * Be careful: The representation is called dual in Hildenbrand213 but not
     * in Dorst2007.
     */
    public static CGAMultivector createDualLine(CGAMultivector p1, CGAMultivector p2){
        return p1.op(p2).op(createBasisVectorEinf(1d));
    }
    
    /**
     * Create circle in inner product null space represenation (grade 2 multivector).
     * 
     * @param sphere1
     * @param sphere2
     * @return conformal circle
     */
    public static CGAMultivector createCircle(CGAMultivector sphere1, CGAMultivector sphere2){
        return sphere1.op(sphere2);
    }
    /**
     * Create dual circle in outer product null space representation (grade 3 multivector).
     * 
     * @param point1
     * @param point2
     * @param point3
     * @return 
     */
    public static CGAMultivector createDualCircle(CGAMultivector point1, CGAMultivector point2, CGAMultivector point3){
        return point1.op(point2).op(point3);
    }
    /**
     * Create dual circle in outer product null space representation (grade 3 multivector).
     * 
     * @param point1
     * @param point2
     * @param point3
     * @return 
     */
    public static CGAMultivector createDualCircle(Point3d point1, Point3d point2, Point3d point3){
        return createPoint(point1).op(createPoint(point2)).op(createPoint(point3));
    }
    
    /**
     * Create result pair in inner product null space representation (grade 3 multivector).
     * 
     * @param sphere1
     * @param sphere2
     * @param sphere3
     * @return 
     */
    public static CGAMultivector createPointPair(CGAMultivector sphere1, CGAMultivector sphere2, CGAMultivector sphere3){
        return sphere1.op(sphere2).op(sphere3);
    }
    /**
     * Create dual result pair in outer product null space representation (grade 2 multivector).
     * 
     * @param point1
     * @param point2
     * @return 
     */
    public static CGAMultivector createDualPointPair(CGAMultivector point1, CGAMultivector point2){
        return point1.op(point2);
    }
    public static CGAMultivector createDualPointPair(Point3d point1, Point3d point2){
        return createPoint(point1).op(createPoint(point2));
    }
    
    /**
     * Create the pseudoscalar - The canonical rotor for the R41 of the conformal 
     * space vector base.
     * 
     * @return the multivector representing the pseudoscalar
     */
    public static CGAMultivector createPseudoscalar(){
        return createBasisVectorOrigin(1d).op(createBasisVectorEx(1d))
                .op(createBasisVectorEy(1d)).op(createBasisVectorEz(1d))
                .op(createBasisVectorEinf(1d));
    }
    
    /**
     * Create tangent vector which includes a result and a direction in inner product null space 
     * representation.
     * 
     * @param p result 
     * @param u direction of the tangent
     * @return bivector representing a tangend vector
     */
    public static CGAMultivector createTangentVector(Point3d p, Vector3d u){
        CGAMultivector cp = createPoint(p);
        return cp.ip(cp.op(createPoint(u)).op(createBasisVectorEinf(1d)));
    }
    
    /**
     * Create a parallelogram (area formed by two anchored vectors).
     * 
     * FIXME 
     * Dual?
     * 
     * @param v1
     * @param v2
     * @return multivector representing a parallelogram
     */
    public static CGAMultivector createDualParallelogram(Vector3d v1, Vector3d v2){
        return createVector(v1).op(createVector(v2));
    }
    /**
     * Create parallelepiped (volumen formed by three anchored vectors).
     * 
     * @param v1
     * @param v2
     * @param v3
     * @return multivector representing a parallelepiped
     */
    public static CGAMultivector createDualParallelepiped(Vector3d v1, Vector3d v2, Vector3d v3){
        return createVector(v1).op(createVector(v2)).op(createVector(v3));
    }
    
    
    
    // decompose
    
    /**
     * Extract the direction and location of a line/plane.
     * 
     * @param probePoint normalized probe result (e0=1, e1,e2,e3, einfM). If not specified use e0.
     * @return direction of the given flat
     */
    public Decomposition3d.FlatAndDirectionParameters decomposeFlat(CGAMultivector probePoint){
        // Kleppe2016
        //Multivector attitude = flat.ip(Multivector.createBasisVector(0), RIGHT_CONTRACTION)
        //        .ip(Multivector.createBasisVector(4), RIGHT_CONTRACTION);
        
        // use dualFlat in Dorst2007
        // damit bekomme ich die attitude in der Form E.op(einfM)
        // für attitude ist ein Vorzeichen nach Dorst2007 zu erwarten, scheint aber nicht zu stimmen
        CGAMultivector attitude = createBasisVectorEinf(1d).op(this).undual();
        // attitude=-5.551115123125783E-17*no^e1^e2 + 0.9999999999999996*e1^e2^ni
        System.out.println("attitude="+String.valueOf(attitude.toString()));
                
        // Dorst2007 - Formel für dual-flat verwenden
        // locations are determined as dual spheres
        CGAMultivector loc = probePoint.op(this).gp(generalInverse());
        //CGAMultivector location = probePoint.ip(this, LEFT_CONTRACTION).gp(generalInverse());
        double[] locationCoord = loc.extractCoordinates(1);
       
        return new Decomposition3d.FlatAndDirectionParameters(attitude.extractDirectionFromEeinfRepresentation(), 
               new Point3d(locationCoord[1], locationCoord[2], locationCoord[3]));
    }
    
    /**
     * Determine the euclid decomposition parameters corresponding to the given dual Flat.
     * 
     * A Dual flat is a tri-vector.
     * 
     * Be careful: This corresponds to non-dual in Dorst2007.
     * 
     * @param probePoint normalized probe result (e0=1, e1,e2,e3, einfM) to define the location dualFlat parameter.. If not specified use e0.
     * @return euclid parameters. The location is determined as a result of the dualFlat
     * with the smallest distance to the given probe result.
     */
    public Decomposition3d.FlatAndDirectionParameters decomposeDualFlat(CGAMultivector probePoint){
        
        // Dorst2007
        //TODO funktioniert nicht - alle components sind 0
        // Ich brauchen undualize into the full space, macht das dual()?
        //CGAMultivector vector = new CGA1Multivector(Multivector.createBasisVector(4).op(this).dual(CGA1Utils.CGA_METRIC));
        //System.out.println("dirvec="+vector.toString(CGA1Utils.baseVectorNames)); // ==0
        
        // Bestimmung von E einf M
        // stimmt nicht
        //CGA1Multivector dir = CGA1Multivector.createBasisVector(4,-1d).ip(this, LEFT_CONTRACTION);
        // Vector3d attitude = dir.extractDirectionFromEeinfRepresentation();
        
        // Nach Kleppe2016
        CGAMultivector dir = rc(createBasisVectorOrigin(1d)).rc(createBasisVectorEinf(1d));
        // attitude=-0.9799999999999993*e1 statt (0.98,0.0,0.0) mit right contraction
        //FIXME Warum stimmt das Vorzeichen nicht?
        System.out.println("attitude Kleppe="+dir.toString()); 
        Vector3d attitude = dir.extractDirection();
        System.out.println("attitude extraction=("+String.valueOf(attitude.x)+","+String.valueOf(attitude.y)+","+String.valueOf(attitude.z)+")");
        // Kleppe2016 adaptiert
        // oder left contraction?
        // left contraction ist null wenn k > l
        //dir = dualFlat.op(Multivector.createBasisVector(4)).ip(Multivector.createBasisVector(0), HESTENES_INNER_PRODUCT);
        //System.out.println("dirvec2="+vector.toString(CGA1Utils.baseVectorNames)); // ==0
        
        // Dorst2007
        // das sieht richtig aus! ist aber die Formel von dualflat statt flat
        CGAMultivector location = probePoint.op(this).gp(generalInverse());
        // Formel von flat - funktioniert nicht
        //CGAMultivector location = probePoint.ip(this, LEFT_CONTRACTION).gp(generalInverse());
         
        // grade 1 ist drin und sieht sinnvoll aus, grade-3 ist mit sehr kleinen Werten aber auch dabei
        // und zusätzlich auch e1einf und e0e1
        System.out.println("location="+location.toString());
        
        // locations are determined as duals-spheres (e0, e1, e2, e3, einfM)
        double[] locationCoord = location.extractCoordinates(1);
        //System.out.println("locationCoord=("+String.valueOf(locationCoord[1])+", "+String.valueOf(locationCoord[2])+" ,"+
        //        String.valueOf(locationCoord[3])+")");
        
        return new Decomposition3d.FlatAndDirectionParameters(attitude, 
               new Point3d(locationCoord[1], locationCoord[2], locationCoord[3]));
    }
    
    public CGAMultivector decomposeTangentAndRoundDirectionAsMultivector(){
        // ungetestet
        CGAMultivector einfM = CGAMultivector.createBasisVectorEinf(-1d);
        CGAMultivector einf = CGAMultivector.createBasisVectorEinf(1d);
        return einfM.ip(undual()).op(einf);
    }
    /**
     * Decompose direction.
     * 
     * Dorst2007
     * 
     * @return direction
     */
    private Vector3d decomposeTangentAndRoundDirection(){
        CGAMultivector attitude = decomposeTangentAndRoundDirectionAsMultivector();
        //System.out.println("tangent(Eeinf)= "+attitude.toString(CGA1Utils.baseVectorNames));
        return attitude.extractDirectionFromEeinfRepresentation();
    }
    /**
     * Decompose dual tangent and round direction.
     * 
     * ungetestet
     * 
     * @return direction
     */
    private Vector3d decomposeDualTangentAndRoundDirection(){
        CGAMultivector einf = CGAMultivector.createBasisVectorEinf(1d);
        CGAMultivector attitude = einf.ip(this).op(einf);
        Vector3d result = attitude.extractDirectionFromEeinfRepresentation();
        //FIXME
        // unklar ob das negate überhaupt an den schluss verschoben werden darf
        result.negate();
        return result;
    }
    /**
     * Decompose tangent.
     * 
     * Dorst2007
     * 
     * Keep in mind: Corresponding to Dorst2007 dual and not-dual ist switched.
     * 
     * @return direction and location, radius=0
     */
    public Decomposition3d.RoundAndTangentParameters decomposeTangent(){
        return new Decomposition3d.RoundAndTangentParameters(decomposeTangentAndRoundDirection(), decomposeTangentAndRoundLocation(), 0d);
    }
    
    /**
     * Decompose dual tangend.
     * 
     * ungetestet
     * 
     * @return direction and location, radius=0
     */
    public Decomposition3d.RoundAndTangentParameters decomposeDualTangent(){
        return new Decomposition3d.RoundAndTangentParameters(decomposeDualTangentAndRoundDirection(), 
                decomposeTangentAndRoundLocation(), 0d);
    }
    
    /**
     * Determines the location from rounds, dual-round, tangent and dual-tangent.
     * 
     * Dorst2007
     * 
     * @return location
     */
    private Point3d decomposeTangentAndRoundLocation(){
        
        // vermutlich stimmt das so nicht?
        
        // decompose location as a sphere (dual sphere in Dorst2007)
        // Vorzeichen wird unten gedreht
        // das sollte aber ein normalized dual sphere ergeben
        CGAMultivector location = 
                gp(createBasisVectorEinf(1d).ip(this).generalInverse());
        // das ergibt einen reinen vector (1-blade)
        System.out.println("location1="+location.toString());
        /*double[] vector = location.extractCoordinates(1);
        Point3d result = new Point3d(vector[1], vector[2], vector[3]);
        result.negate();*/
        
        // Hildenbrand2004 (Tutorial)
        location = gp(CGAMultivector.createBasisVectorEinf(1d)).gp(this).div((createBasisVectorEinf(1d).ip(this)).sqr()).gp(-0.5);
        System.out.println("location="+location.toString());
        double[] vector = location.extractCoordinates(1);
        Point3d result = new Point3d(vector[1], vector[2], vector[3]);
        return result;
    }
    
    /**
     * Decompose round object.
     * 
     * Dorst2007
     * 
     * @return attitude, location and squared size for multivectors corresponding to rounds in
     * inner product null space representaton
     */
    public Decomposition3d.RoundAndTangentParameters decomposeRound(){
        // (-) because the radius for dual round corresponding to Dorst2007 is needed to
        // get the value corresponding to inner product null space representation
        return new Decomposition3d.RoundAndTangentParameters(decomposeTangentAndRoundDirection(), 
                decomposeTangentAndRoundLocation(), -roundSquaredSize());
    }
    
    /**
     * Decompose a sphere. 
     * 
     * Only for testing. 
     * 
     * @Deprecated use decomposeRound instead.
     * @return location and squared-radius, direction=(0,0,0)
     */
    public Decomposition3d.RoundAndTangentParameters decomposeSphere(){
        double[] result = extractCoordinates(1);
        return new Decomposition3d.RoundAndTangentParameters(new Vector3d(), 
                new Point3d(result[1], result[2], result[3]), -2d*result[4]);
    }
    
    /**
     * Decompose rotation around origin.
     * 
     * @return quaternion representing a rotation around the origin
     */
    public Quat4d decomposeRotation(){
        Quat4d result = new Quat4d();
        result.w = extractCoordinates(0)[0];
        double[] vector = extractCoordinates(2);
        result.x = -vector[4]; // i
        result.y = vector[5];  // j
        result.z = -vector[8]; // k
        return result;
    }
    
    /**
     * Determine squared radius for a round.
     * 
     * Dorst2007
     * 
     * FIXME
     * Da das Ergebnis nicht stimmt befürchte ich, dass die Formel nur für Kugeln
     * im Ursprung gilt.
     * 
     * @return squared size/radius for a round corresponding of Dorst2007 and (-) 
     * squared size/radius for dual round
     */
    private double roundSquaredSize(){
        CGAMultivector mvNumerator = gp(gradeInversion());
        CGAMultivector mvDenominator = createBasisVectorEinf(1d).ip(this);
        
        // (-) d.h. das ist die Formel für dual-round nach Dorst2007. Das sieht also
        // richtig aus.
        // aber der Radius im Test stimmt nur ungefährt mit dem ursprünglichen überein
        // vermutlich Probleme mit der norm-Berechnung? radius = 2.061455835083547 statt 2.0
        //FIXME
        //double squaredSize = mvNumerator.gp(-1d/mvDenominator.norm_e2()).scalarPart();
        return mvNumerator.gp(-1d/mvDenominator.squaredNorm()).scalarPart(); 
        //return mvNumerator.gp(-1d/(2d*mvDenominator.squaredNorm())).scalarPart(); // *2.0 aus Hildenbrand, wird noch falscher
    }
    
    /**
     * Decompose dual round.
     * 
     * Dorst2007
     * 
     * @return attitude, location and radius
     */
    public Decomposition3d.RoundAndTangentParameters decomposeDualRound(){
        return new Decomposition3d.RoundAndTangentParameters(decomposeDualTangentAndRoundDirection(), 
                decomposeTangentAndRoundLocation(), -roundSquaredSize());
    }
    
    /**
     * Decompose the geometric product of two lines.
     *
     * @return dij and P if l1 and l2 are not coincident and not parallel else an empty array
     */
    public Decomposition3d.LinePairParameters decomposeLinePair(){
        
        // X soll eine sum aus 1- und 2-blade sein
        // falsch da sind auch zwei 4-blades mit drin
        CGAMultivector n0 = CGAMultivector.createBasisVectorOrigin(1d);
        CGAMultivector ni = CGAMultivector.createBasisVectorEinf(1d);
        CGAMultivector X = sub(n0.ip(this).op(createBasisVectorEinf(1d)));
        System.out.println("X="+X.toString());
        
        // scheint korrekt sum aus 3- und 1-blade
        CGAMultivector Y = n0.ip(this);
        System.out.println("Y="+Y.toString());
        CGAMultivector Y3 = Y.extractGrade(3);
        System.out.println("Y3="+Y3.toString());
        
        CGAMultivector X2 = X.extractGrade(2);
        System.out.println("X2="+X2.toString());
         // quatrieren und test auf !=0
        CGAMultivector X22 = X2.gp(X2);
        
        
        // identisch
        if (this.isScalar()){
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
            CGAMultivector d = Y3.gp(X2.reverse().gp(1d/X2.squaredNorm()));
            System.out.println("d="+d.toString());

            double[] dValues = d.extractCoordinates(2);
         
            double alpha = 0d;
            Vector3d p = new Vector3d(); 
            return new Decomposition3d.LinePairParameters(alpha, new Point3d(p.x,p.y,p.z), 
                    new Vector3d(dValues[0], dValues[1], dValues[2]));
        }
    }
    
    // Decompose weight
    // eventuell in die vector records einbauen
    // Problem: Die Methoden zur Bestimmung der attitude lieferen direkt Vector3d und nicht result
    // das könnte ich aber ändern
    //TODO
    /**
     * Determine the squared weight of an CGA object.
     * 
     * @param attitude direction specific for the object form the multivector is representing
     * @param probePoint If not specified use e0.
     * @return squared weight
     */
    public static double decomposeSquaredWeight(CGAMultivector attitude, CGAMultivector probePoint){
        CGAMultivector A = probePoint.ip(attitude);
        return A.reverse().gp(A).scalarPart();
    }
    /**
     * Determine the weight of an CGA object.
     * 
     * @param attitude direction specific for the object form the multivector is representing
     * @param probePoint If not specified use e0.
     * @return 
     */
    public static double decomposeWeight(CGAMultivector attitude, CGAMultivector probePoint){
        return Math.sqrt(Math.abs(decomposeSquaredWeight(attitude, probePoint)));
    }
    
    /**
     * Intersection of lines, planes and spheres.
     * 
     * FIXME
     * ist das überhaupt so sinnvoll - besser meet oder vee verweden?
     * 
     * @param mv2 Line, Plane, Sphere
     * @return 
     */
    public CGAMultivector intersect(CGAMultivector mv2){
        return createPseudoscalar().gp(this).ip(mv2);
    }
    
    
    
    
    
    /**
     * Extract direction from E einf multivector representation.
     * 
     * @return direction
     */
    private Vector3d extractDirectionFromEeinfRepresentation(){
        double[] coordinates = extractCoordinates(3);
        return new Vector3d(coordinates[9], coordinates[8], coordinates[7]);
    }
    private Vector3d extractDirection(){
        double[] coordinates = extractCoordinates(1);
        return new Vector3d(coordinates[1], coordinates[2], coordinates[3]);
    }
    
    
    // monadic operators
    
    public CGAMultivector reverse(){
        return new CGAMultivector(impl.reverse());
    }
    public CGAMultivector sqr(){
        return gp(this);
    }
    public CGAMultivector dual(){
        return new CGAMultivector(impl.dual());
    }
    public CGAMultivector undual(){
        return new CGAMultivector(impl.undual());
    }
    public CGAMultivector generalInverse(){
        return new CGAMultivector(impl.generalInverse());
    }
    public double squaredNorm(){
        return impl.squaredNorm();
    }
    public double norm(){
        return Math.sqrt(Math.abs(squaredNorm()));
    }
    public boolean isNull(){
        return impl.isNull();
    }
    public double scalarPart(){
        return impl.scalarPart();
    }
    
    
    // dual operators
    
    public double scp(CGAMultivector x) {
        return impl.scp(x.impl);
    }
    /**
     * Inner product.
     * 
     * @param x right side argument of the inner product
     * @return inner product of this with a 'x' using metric 'M'
     */
    public CGAMultivector ip(CGAMultivector x){
        return new CGAMultivector(impl.ip(x.impl, default_ip_type));
    }
    public CGAMultivector rc(CGAMultivector x){
         return new CGAMultivector(impl.ip(x.impl, RIGHT_CONTRACTION));
    }
    public CGAMultivector lc(CGAMultivector x){
         return new CGAMultivector(impl.ip(x.impl, LEFT_CONTRACTION));
    }
    public CGAMultivector gp(CGAMultivector x){
        return new CGAMultivector(impl.gp(x.impl));
    }
    public CGAMultivector gp(double x){
        return new CGAMultivector(impl.gp(x));
    }
    public CGAMultivector div(CGAMultivector x){
        return gp(x.generalInverse());
    }
    public CGAMultivector op(CGAMultivector x){
         return new CGAMultivector(impl.op(x.impl));
    }
    public CGAMultivector vee(CGAMultivector x){
        return dual().op(x.dual()).undual();
    }
    
    public CGAMultivector add(CGAMultivector b){
        return new CGAMultivector(impl.add(b.impl));
    }
    public CGAMultivector sub(CGAMultivector b){
        return new CGAMultivector(impl.sub(b.impl));
    }
    
    public boolean isScalar(){
        return impl.isScalar();
    }
    
    public CGAMultivector extractGrade(int grade){
        return new CGAMultivector(impl.extractGrade(grade));
    }
    
    public CGAMultivector normalize(){
        return new CGAMultivector(impl.normalize());
    }
    
    public CGAMultivector gradeInversion(){
        return new CGAMultivector(impl.gradeInversion());
    }

    @Override
    public String toString(){
        return impl.toString();
    }
}
