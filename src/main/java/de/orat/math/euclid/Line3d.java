/**
 * StraightLine.java
 *
 * (C) 1998 Oliver Rettig, ORAT Software-Entwicklung
 */
package de.orat.math.euclid;

import org.jogamp.vecmath.*;

/**
 *
 * @author Oliver Rettig
 *
 */
public class Line3d {
    
    protected Vector3d o = null;
    protected Vector3d n = null;
    
    // wird von Arrow benoetigt
    protected Line3d(){}
    
    public Line3d(Vector3d o, Vector3d n) {
        this.o = o;
        this.n = new Vector3d(n);
        n.normalize();
    }
    
    public boolean isValid(){
        if (o != null && n != null){
            return true;
        }
        return false;
    }
    public Vector3d getDirectionVector(){
        return n;
    }
    
    public Vector3d getOrigin(){
        return o;
    }
    
    public Vector3d project(Vector3d P) {
        return project(this,P);
    }
    
    // P1 = Ursrpung der Gerade, n gleich Richtungsvektor der Geraden
    // Wenn der Punkt auf der Gerade liegt, dann wird der Punkt selbst zurueckgeliefert?
    // liefert die Aufpunkt des angegebenen Punkts P auf die Gerade
    // Achtung: Der Richtungsvektor muss normalisiert sein
    // das scheint falsch zu sein!!!!!!!!!!!
    //FIXME
    /*public static final Vector3d project(Vector3d P1, Vector3d n, Vector3d P) {
        Vector3d nvec = null;
        Vector3d P2 = new Vector3d(P1);
        P2.add(n);
        double m = n.x*(P2.x-P1.x)+n.y*(P2.y-P1.y)+n.z*(P2.z-P1.z);
        // P liegt auf der Geraden
        if (m == 0.0d){
            nvec = P;
        } else {
            double t = -(n.x*(P1.x-P.x)+n.y*(P1.y-P.y)+n.z*(P1.z-P.z))/m;
            nvec = new Vector3d(n);
            nvec.scale(t);
            nvec.add(P1);
        }
        return nvec;
        
    }*/
    
    /*
     * n: Richtungsvektor der Geraden
     * Es wird davon ausgegangen, dass n bereits normiert ist
     * P1 ein beliebiger Punkt auf der Geraden
     * P der Punkt der projiziert werden soll. Dieser ist auch gleich
     * das nvec
     */
    public static final Vector3d project(final Vector3d P1, final Vector3d n, Vector3d P) {
       // t ist der (vorzeichenbehaftete)
       // Abstand des Punkte auf der Geraden zum gesuchten Aufpunkt
       double t = n.x*(P.x-P1.x)+n.y*(P.y-P1.y)+n.z*(P.z-P1.z);
       Vector3d result = new Vector3d(n);
       result.scale(t);
       result.add(P1);
       return result;
    }
    
    
    // Wenn der Punkt auf der Gerade liegt, dann wird der Punkt selbst zurueckgeliefert?
    // liefert die Aufpunkt des angegebenen Punkts P auf die Gerade
    public static final Vector3d project(Line3d line, Vector3d P) {
        return project(line.getOrigin(),line.getDirectionVector(),P);
    }
    
    
    /*
     * Wenn sich die Geraden nicht schneiden, wird der Mittelpunkt der Strecke
     * mit dem kürzesten Abstand zwischen den beiden Geraden bestimmt. Eine
     * Exception wird statt dessen geworfen, wenn der Abstand der beiden Geraden
     * grösser ist als der angegebene threshold.
     *
     */
    public Vector3d cut(Line3d line, double threshold) throws CutFailedException {
            throw new RuntimeException("not implemented!");
    }
    /*public Line3d cut(Plane plane) throws CutFailedException{
        return plane.cut(this); 
    }*/
    public Vector3d[] cut(Sphere sphere) throws CutFailedException{
        return sphere.cut(this);
    }
    
    public Point3d getPointAtDistance(double t){
        Vector3d nvec = new Vector3d(n);
        nvec.normalize();
        nvec.scale(t);
        Point3d result = new Point3d(o);
        result.add(nvec);
        return result;
    }
    public static void main(String[] args){
        Line3d line = new Line3d(new Vector3d(0,-1349,-0.7),new Vector3d(-0.1,0.95,0.2));
        Vector3d p = line.project(new Vector3d(209,295,934));
        System.out.println("P="+p);
    }
}