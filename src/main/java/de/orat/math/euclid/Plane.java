/**
 * Plane.java
 *
 * (C) 1998 Oliver Rettig, ORAT Software-Entwicklung
 */
package de.orat.math.euclid;

import static de.orat.math.euclid.iNumericConstants.EPSILON;
import org.jogamp.vecmath.*;


/**
 * @author Oliver Rettig
 */
public class Plane  {
    
    public static double minAngle = Math.PI/180d;
    
    protected Vector3d o = null;
    // n ist immer normalisiert!
    protected Vector3d n = null;
    
    // ax+by+cz=d
    public double determineD(){
        return n.dot(o);
    }
    public final void set(Circle circle){
        o = circle.getOrigin();
        n = circle.getNormalVector();
    }
    public Plane(Circle circle){
        set(circle);
    }
    public Plane(Plane plane) {
        set(plane);
    }

    public Plane(Vector3d o, Vector3d n){
        set(o,n);
    }

    public final void set(Plane plane) {
        o = plane.getOrigin();
        n = plane.getNormalVector();
    }
    public final void set(Vector3d o, Vector3d n) {
        this.o = o;
        this.n = new Vector3d(n);
        (this.n).normalize();
    }
    public final void set(Point3d p1, Point3d p, Point3d p3){
         o = new Vector3d(p);
         Vector3d a = new Vector3d(p1);
         a.sub(p);
         Vector3d b = new Vector3d(p3);
         b.sub(p);
         n = new Vector3d();
         n.cross(a,b);
         n.normalize();
    }
    /*
     * Eine Ebene durch die Punkte p1 und p2, die senkrecht zur normalPlane ist.
     *
     * Eigentlich muss ich doch hier noch eine Exception werfen, wenn der Normalenvektor
     * der Ebene parallel zu dem Vektor p2-p1 ist, bzw. der Winkel zwischen beiden zu
     * klein wird sodass die Ebene nicht mehr sauber definiert ist!
     *FIXME
     *
     */
    public final void set(Vector3d p1, Vector3d p2, Plane normalPlane){
        // n sei der gesuchte Normalenvektor
        // l sei der Normalenvektor der normalPlane
        // m sei der Vektor in der Verbindung der beiden angegebenen Punkte

        Vector3d m = new Vector3d(p2);
        m.sub(p1);
        Vector3d l = normalPlane.getNormalVector();
        n = new Vector3d();
        n.x = 1d;
        n.y = (m.z*l.x/l.z-m.x)/(m.y-m.z*l.y/l.z);
        n.z = (n.x*l.x+n.y*l.y)/l.z;
        n.normalize();
        o = new Vector3d(p1);
    }
    public Plane(Point3d o, Vector3d v1, Vector3d v2){
        this.o = new Vector3d(o);
        n = new Vector3d();
        n.cross(v1,v2);
        n.normalize();
    }
    public Plane(Point3d p1, Point3d p2, Point3d p3){
        set(p1,p2,p3);
    }
    
    public Plane(Vector3d p1, Vector3d p2, Plane normalPlane){
        set(p1,p2,normalPlane);
    }
    
    
    public Line3d cut(Plane plane) throws CutFailedException {
        return cut(this, plane);
    }
    public Vector3d[] cut(Circle circle) throws CutFailedException {
        return Circle.cut(this,circle);
    }
    public Vector3d[] cut(Line3d line) throws CutFailedException {
        return cut(this,line);
    }
    public static Vector3d[] cut(Plane plane, Line3d line) throws CutFailedException {
       // besser wäre es statt auf 0 auf einen sehr kleinen Wert zu testen damit
       // der cut bereits ungültig wird, wenn die line nahezu parallel zur Ebene ist
       //TODO
       if (plane.getNormalVector().dot(line.getDirectionVector()) == 0d) {
            throw new CutFailedException("The line is parallel to the plane. so there is not cut!");
       }
       
       // E: n1 dot x = d
       // g: x = P + t n2
       // t = -((n1 dot P) + d)/(n1 dot n2)
       // result = P + t n2
       double t = -(plane.getNormalVector().dot(line.getOrigin()) - // muss da nicht ein plus hin?
               plane.getNormalVector().dot(plane.getOrigin()))/
               (plane.getNormalVector().dot(line.getDirectionVector()));
       Vector3d result = new Vector3d(line.getDirectionVector());
       result.scale(t);
       result.add(line.getOrigin());
       return new Vector3d[]{result};
    }
    /*
     * @result null, wenn der Winkel zwischen den beiden ebenen kleiner 1 Grad
     *
     * hier sollte ich doch einen minimalen Winkel zwischen den Normalenvektoren
     * als Argument aufnehmen.
     *
     *FIXME
     *schlägt fehl, wenn die z-Komponente des Normalenvektors einer Ebene = 0 ist!!!!!
     *
     * ausserdem klappt es nicht, wenn die Schnittgerade x=y=0 ergeben soll!
     */
    public static Line3d cut(Plane plane1, Plane plane2) throws CutFailedException{
       
        Line3d line = null;
       
        // Ebene 1
        Vector3d n1 = plane1.getNormalVector(); // da bekomme ich bereits einen normalisierten Vektor
        Vector3d e1 = plane1.getOrigin();
        
        // Ebene 2
        Vector3d n2 = plane2.getNormalVector();
        Vector3d e2 = plane2.getOrigin();
        
          
        // Winkel zwischen den beiden Ebenen
        double angle = n1.angle(n2); // 0-pi
        
        // Wenn der Winkel zwischen den beiden Ebenen kleiner als 1 Grad ist,
        // dann wird dies als Fehler gemeldet
        // Konstante nach iNumericConstants auslagern oder besser als Argument in
        // die Funktione übernehmen
        if (angle > minAngle && (Math.PI-angle) > minAngle) {
            
            // Hier muss ich doch noch irgendwie den Fall abfangen, dass n1.z=0 ist!
            // d.h. Ebene 1 und die Schnittgerade liegen nicht waagrecht im Raum
            // d.h. die Schnittgerade hat auf jeden Fall auch Lösungen für x=0 
            // und/oder y=0 ...
            if (Math.abs(n1.z) > EPSILON){
                // Ebenengleichung I
                // a1*x + b1*y + z = d1

                //double d1 =(n1.x*e1.x + n1.y*e1.y + n1.z*e1.z)/n1.z;
                double d1 = n1.dot(e1)/n1.z;
                double a1 = n1.x/n1.z;
                double b1 = n1.y/n1.z;

                // Ebenengleichung II
                // a2*x + b2*y + c2*z = d2

                double d2 = n2.dot(e2);

                // Geradengleichung
                // z = 0 willkürlich
                // a*x + b*y = c

                double a = n2.x - n2.z*a1;
                double b = n2.y - n2.z*b1;
                double c = d2-d1*n2.z;

                // Richtungvektor der Geraden
                Vector3d f = new Vector3d();
                f.cross(n1,n2); // wenn n1=n2 ist das Kreuzprodukt (0,0,0)
                // Der Fall, dass die beiden Ebenen identisch sind wird schon durch
                // das if vorher mit angefangen

                Vector3d o;
                // kann ich die Fallunterscheidung irgendwie intelligent vermeiden?
                // habe ich hier sicher alle Fälle abgehandelt
                if (Math.abs(b) > EPSILON){
                    // x=0 in die 1. Ebenengleichung eingesetzt!
                    o = new Vector3d(0d, c/b,d1-b1*(c/b));
                // ware es nicht besser hier y=0 einzusetzen?
                // FIXME
                } else {
                    o = new Vector3d(c/a,(d1-a1*(c/a))/b1,0d);
                    // x=1 in die Geradengleichung eingesetzt um o.y zu bestimmen
                    // x=1 und o.y in die 1. Ebenengleichung eingesetzt!
                    //FIXME
                    // das ist noch nicht ausreichend getestet!!!
                    //System.out.println("ungetestet!");
                    //double oy = (c-a)/b;
                    //o = new Vector3d(1d,oy,(d1-a1)/(b1*(c-a)/b));
                }
                line = new Line3d(o,f);
            } else {
                //FIXME
                System.out.println("Die zweite Ebene liegt wagrecht im Raum. "+
                                   "Für diesen Fall fehlt noch die Implementierung!");
                throw new CutFailedException("Die zweite Ebene liegt wagrecht im Raum. "+
                                   "Für diesen Fall fehlt noch die Implementierung!");
            }
        // vermutlich ist der Fall n.z zu klein weiter aufzusplitten in
        // n.x zu klein und n.y zu klein
        } else {
            throw new CutFailedException("Der Winkel zwischen den beiden Ebenen E1: P0="+e1+
                    ", n="+n1+"; E2: P0="+e2+", n="+n2+" ist kleiner als 1 Grad!");
        } 
        return line;
    }
    
    public void project(Vector3d p){
        project(p,this);
    }
    public static void project(Vector3d p, Plane plane){
       project(p,plane.getOrigin(), plane.getNormalVector());
    }
    
    /**
     * Project a point onto a plane.
     * 
     * FIXME
     * stimmt vermutlich nicht!!!!
     * 
     * @param p point to project (overwritten by its projection)
     * @param o point defining the plane
     * @param n normal (normalized!) vector of the plane
     * 
     * The result point is saved in the first argument. If a component of n is NaN
     * this is the point defining the plane as the projected point.
     */
    public static void project(Vector3d p, Vector3d o, Vector3d n){
       if (Double.isNaN(n.x) || Double.isNaN(n.y) || Double.isNaN(n.z)){
            p.x = o.x;
            p.y = o.y;
            p.z = o.z;
       } else {
           // E(o,n) in Normalenform
           // g(p,n)
           // E mit g schneiden, Skalarprodukte, t = o*n - p*n
           // Projektion = p + t*n
           double t = n.dot(o) - p.dot(n);
           p.x += t*n.x;
           p.y += t*n.y;
           p.z += t*n.z;
       }
    }
    /**
     * true, wenn p auf der Seite der Ebene liegt in dessen Richtung ausgehend
     * von der Ebene der Normalenvektor der Ebene zeigt.
     * @param p
     * @param o
     * @param n
     * @return 
     */
    public static boolean side(Vector3d p, Vector3d o, Vector3d n){
        boolean result = false;
        double t = n.dot(o) - p.dot(n);
        if (t < 0) {
            result = true;
        }
        return result;
    }
    public boolean side(Vector3d p){
        return side(p, getOrigin(),getNormalVector());
    }
    public static boolean alignment(Vector3d v1, Vector3d v2){
        boolean result = true;
        if (v1.dot(v2) <0) {
            result = false;
        }
        return result;
    }
    // Abstand eines Punktes p von der Ebene(n,p0)
    public static double dist(Vector3d n, Vector3d p0, Vector3d p){
        Vector3d p1 = new Vector3d(p);
        double t = n.dot(p0)-p.dot(n);
        p1.x +=t*n.x;
        p1.y +=t*n.y;
        p1.z +=t*n.z;
        p1.negate();
        p1.add(p);
        return p1.length();
    }
    public static double dist(Plane plane, Vector3d p){
        return dist(plane.getNormalVector(), plane.getOrigin(),p);
    }
    
    public double dist(Vector3d p){
        return dist(this,p);
    }
    
    /**
     * (X-A)*(X-A) = l1*l1
     * (X-B)*(X-B) = l2*l2
     * X = A + t(D-A)+s(B-A)
     *
     * Der gesuchter Punkte liegt in der Ebene aus A,B,D und hat von A den Abstand 
     * l1 und von B den Abstand l2.Da es unter diesen Bedingungen zwei Lösungen gibt, enthält l1 mit dem Vorzeichen, 
 den Hinweis welche Lösung gemeint ist.Statt ein Vorzeichzeichen in l1,l2 zu speichern, könnte ich mir auch überlegen, wie
 der gesuchte Punkt bezüglich D zu liegen kommen soll, oder in Bezug zum Normalenvektor
 der Ebene ...Idee: Konstruiere eine Ebene, die senkrecht zur Ebene durch ABD ist und die
      beiden Punkte A und B enthält.Jetzt kann die Lösung definiert werden als
      unterhalb oder oberhalb dieser Ebene ...
     * @param A
     * @param B
     * @param D
     * @param l1
     * @param l2
     * @return 
     */
    public static Vector3d doubleDistPlanePoint(Vector3d A, Vector3d B, Vector3d D, 
                                          double l1, double l2){
        
        Vector3d ds = new Vector3d(D); ds.sub(A);
        Vector3d bs = new Vector3d(B); bs.sub(A);
        
        boolean sign = false;
        if (l1<0){
            sign = true;
        }
        l1 = Math.abs(l1);
        l2 = Math.abs(l2);
        
        double l = bs.length();
        double lr = l1+l2;
        if (l > lr){
            double d = l-lr;
            l1 += (l1*d)/lr;
            l1 += 1.0;
            l2 +=(l2*d)/lr;
            l2 += 1.0;
            //System.out.println("PointDoubleDistPlaneCalcElement - Warning: Abstand der Referenzmarker ist kleiner als die Summer Referezl�ngen! l1="+l1+" l2="+l2+" l="+l);
        }
        
        double l1q = l1*l1;
        double l2q = l2*l2;
        
        double u = ds.x*ds.x + ds.y*ds.y + ds.z*ds.z;
        double v = bs.x*bs.x + bs.y*bs.y + bs.z*bs.z;
        double w = ds.x*bs.x + ds.y*bs.y + ds.z*bs.z;
          
        double m = (l1q-l2q+v)/(2*w);
        double n = -v/w;
        
        double a = n*n*u+2*w*n+v;
        double b = 2*m*(w+n*u);
        double c = m*m*u-l1q;
        
        Vector3d result = new Vector3d(A);
        if (!sign){
            double s1 = (-b+Math.sqrt(b*b-4*a*c))/(2*a);
            double t1 = m+n*s1;
            
            ds.x *=t1; ds.y *=t1; ds.z *=t1;
            result.add(ds);
            
            bs.x *=s1; bs.y *=s1; bs.z *=s1;
            result.add(bs);
        } else { 
            double s2 = (-b-Math.sqrt(b*b-4*a*c))/(2*a);
            double t2 = m+n*s2;
            
            ds.x *=t2; ds.y *=t2; ds.z *=t2;
            result.add(ds);
            
            bs.x *=s2; bs.y *=s2; bs.z *=s2;
            result.add(bs);
        }
        return result;
    }
    public Vector3d doubleDistPlanePoint(Vector3d A, Vector3d B, 
                                          double l1, double l2){
         return doubleDistPlanePoint(A, B, getOrigin(), l1, l2);
        
    }
    

    //FIXME
    // ungeklärt: Die Komponenten von n können NaN sein, wenn der Circle zu einem Punkt
    // geschrumpft ist
    // unklar ist auch wie es passieren kann dass die Komponenten von n alle 0 sein können
    // Wer kann da alle auf 0 setzen?
    // soll ich ein invalid überhaupt zulässen? Ich könnte ja dafür sorgen, dass der Konstruktor
    // eine Exception wirft, wenn die Argumente zu keinem Kreis oder Punkte führen.
    // dann wäre aber eine Methode gut, mit der ich erfragen kann, ob es sich bei dem Kreis um einen
    // Punkt handelt! Aber vielleicht wäre ein invalid Kreis doch gut, wenn sich dieser z.B. durch den
    // Schnitt von zwei Kugeln ergibt. Ein ungültiger Kreis als Alternative zu einer CutFailedException
    public boolean isValid(){
        if (o != null && n != null && !(n.x == 0.0d && n.x == 0.0d && n.z == 0.0d)) {
            return true;
        }
        return false;
    }
    public Vector3d getNormalVector(){
        return n;
    }
    public Vector3d getNormalVector(int positionIndex){
        throw new RuntimeException("not implemented!");
    }
    public Vector3d getOrigin(){
        return o;
    }
    public Vector3d getOrigin(int positionIndex){
        throw new RuntimeException("not implemented!");
    }
}
