/**
 * Sphere.java
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
package de.orat.math.euclid;

import org.jogamp.vecmath.*;

public class Sphere {
    
    private double r = Double.NaN;
    private Vector3d origin = null;
    
    public Sphere(Vector3d origin, double r) {
        this.r = Math.abs(r);
        this.origin = origin;
    }
    public Sphere(Circle circle){
        r = circle.getRadius();
        origin = circle.getOrigin();
    }
    
    public boolean isValid(){
        return !Double.isNaN(r) && !Double.isInfinite(r) && origin != null;
    }
    public Vector3d getOrigin(){
        return origin;
    }
    public Vector3d getOrigin(int positionIndex){
        throw new RuntimeException("not implemented!");
    }
    public double getRadius(){
        return r;
    }
    public double getRadiusSquared(){
        return r*r;
    }
    public final Circle cut(Sphere sphere) throws CutFailedException {
        return cut(this,sphere);
    }
    
    
    public Circle getPointBetween(Sphere sphere){
        return getPointBetween(sphere, this);
    }
    
    /*
     * Wenn sich zwei Kugeln nicht schneiden, dann bekomme ich hiermit den Punkt
     * auf der Verbindungsgeraden der beiden Mittelpunkt in der Mitte des Verbindungstücks
     *
     */
    public static Circle getPointBetween(Sphere sphere1, Sphere sphere2){
        Vector3d S1 = new Vector3d(sphere2.getOrigin());
        S1.sub(sphere1.getOrigin());
        S1.normalize();
        S1.scale(sphere1.getRadius());
        S1.add(sphere1.getOrigin());

        Vector3d S2 = new Vector3d(sphere1.getOrigin());
        S2.sub(sphere2.getOrigin());
        S2.normalize();
        S2.scale(sphere2.getRadius());
        S2.add(sphere2.getOrigin());
        S2.add(S1);
        S2.scale(0.5d);
        return new Circle(S2);
     }
    /**
     * Wenn die beiden Kugeln sich nicht schneiden, wird eine Exception gefeuert!
     *
     * Der Normalenvektor der Ebene in der der Schnittkreis liegt zeigt vom
     * Mittelpunkt der ersten Kugel zum Mittelpunkt der zweiten Kugel und ist
     * normiert.
     * 
     * @param sphere1
     * @param sphere2
     * @return 
     * @throws de.orat.math.euclid.CutFailedException 
     */
    public static Circle cut(Sphere sphere1, Sphere sphere2) 
                                           throws CutFailedException {
      
      //FIXME
      // hier ist zu ändern, wenn ich das Verhalten der Kugeln mit ungültigen Werten ändere!
      Vector3d P1 = sphere1.getOrigin();
      double R1 = sphere1.getRadius();
      if (Double.isNaN(P1.x) ||Double.isNaN(P1.y) || Double.isNaN(P1.z) || Double.isNaN(R1)){
        throw new CutFailedException("Die erste Kugel ist nicht definiert: M1="+P1+",R1="+R1);
      }
      Vector3d P2 = sphere2.getOrigin();
      double R2 = sphere2.getRadius();
      if (Double.isNaN(P2.x) ||Double.isNaN(P2.y) || Double.isNaN(P2.z) || Double.isNaN(R2)){
        throw new CutFailedException("Die zweite Kugel ist nicht definiert: M1="+P2+",R1="+R2);
      }
      
      // Abstand der beiden Kugelmittelpunkte und Normalenvektor des Kreises
      
      Vector3d k = new Vector3d(P2);
      k.sub(P1);
      double d = k.length();
      k.normalize();

      // Radius des Schnittkreises
      double a = (R1*R1-R2*R2+d*d)/(2.0d*d);
      double r = R1*R1-a*a; // Vorsicht r ist hier erst der Radius zum Quadrat

      Vector3d M = new Vector3d(k);
      // Die beiden Kugeln haben einen Schnittkreis
      if (r>0){
          r = Math.sqrt(r);
          // Mittelpunkt des Schnittkreises
          M.scale(a);
          M.add(P1);
          
      // Die beiden Kugeln schneiden sich nicht
      } else {
         throw new CutFailedException("Die beiden Kugeln (M1="+P1+",R1="+R1+",M2="+P2+",R2="+R2+") schneiden sich nicht!");
      }
      return new Circle(M,k,r);
    }
    
    public final Vector3d[] cut(Line3d line) throws CutFailedException {
        return cut(this,line);
    }
     
    public static Vector3d[] cut(Sphere sphere, Line3d line) throws CutFailedException {
        Vector3d[] result;
        Vector3d s = line.getDirectionVector();
        double e = s.lengthSquared();
        Vector3d b = new Vector3d(line.getOrigin());
        b.sub(sphere.getOrigin());
        double d = 2d*s.dot(b);
        double c = b.lengthSquared()-sphere.getRadiusSquared() ;
        double sqrt = d*d-4d*e*c;
        if (sqrt > 0){
            result = new Vector3d[2];
            sqrt = Math.sqrt(sqrt);
            // 1. Lösung
            double t = (-d+sqrt)/(2d*e);
            result[0] = new Vector3d(line.getDirectionVector());
            result[0].scale(t);
            result[0].add(line.getOrigin());
            // 2. Lösungpublic final Vector3d[] cut(iLine line) throws CutFailedException
            t = (-d-sqrt)/(2d*e);
            result[1] = new Vector3d(line.getDirectionVector());
            result[1].scale(t);
            result[1].add(line.getOrigin());
        // eine Lösung
        } else if (sqrt == 0){
            double t = -d/(2d*e);
            result = new Vector3d[1];
            result[0] = new Vector3d(line.getDirectionVector());
            result[0].scale(t);
            result[0].add(line.getOrigin());
        // keine Lösung
        } else {
            throw new CutFailedException("Die Gerade schneidet die Kugeln nicht!");
        }
        return result;
    }
     
    public static void main1(String[] args){
        Sphere sphere = new Sphere(new Vector3d(),1d);
        Line3d line = new Line3d(new Vector3d(0.5d,0d,0d),new Vector3d(0d,0d,1d));
        try {
        Vector3d[] result = sphere.cut(line);
        if (result.length == 2){
            System.out.println(result[0]+" "+result[1]);
        } else {
            System.out.println(result[0]);
        }
        } catch (CutFailedException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    /*public static void main(String[] args){
        iSphere sphere1 = new Sphere(new Vector3d(),1d);
        iSphere sphere2 = new Sphere(new Vector3d(1d,0d,0.5d),1d);
        try {
            iCircle circle = sphere1.cut(sphere2);
            System.out.println("r="+circle.getRadius()+" M="+circle.getOrigin());
        } catch (CutFailedException e){}
    }*/
    
     public static void main(String[] args){
        Sphere sphere1 = new Sphere(new Vector3d(0.5,0.5,0),1d);
        Line3d line = new Line3d(new Vector3d(),new Vector3d(0.5,0,0.5));
        
        try {
            Vector3d[] result = sphere1.cut(line);
            System.out.println(result[0]);
            //System.out.println(result.length);
            if (result.length > 1){
                System.out.println(result[1]);
            }
        } catch (CutFailedException e){}
    }
}
