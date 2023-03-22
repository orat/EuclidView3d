/**
 * Circle.java
 *
 * (C) 1998 Oliver Rettig, ORAT Software-Entwicklung
 */
package de.orat.math.euclid;

import org.jogamp.vecmath.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wenn die beiden Kugeln sich nicht schneiden, dann wird als MidPoint/Origin etc.
 * ein Punkt auf der Verbindungslinie zwischen den beiden Kugeln an der Position 
 * entsprechend dem Verhaeltnis der Radien zurueckgegeben. Der Normalenvektor 
 * steht weiterhin genauso zur Verfuegung.
 *
 * @author Oliver Rettig
 *
 */
public class Circle extends Plane {
    
    static Log log = LogFactory.getLog(Circle.class);
    
    private double r = Double.NaN;

    public Circle(Vector3d o){
      super(o, new Vector3d(Double.NaN, Double.NaN, Double.NaN));
      r = 0d;
    }
    public Circle(Vector3d o, Vector3d n, double r){
        super(o,n);
        this.r = Math.abs(r);
    }
    
    public boolean isPoint(){
        boolean result = false;
        if (r==0d) {
            result = true;
        }
        return result;
    }
    public final Vector3d getArbitraryPoint() throws CutFailedException {
        return getArbitraryPoint(this);
    }
    // einen beliebigen Punkt des Kreises beschaffen
    public static Vector3d getArbitraryPoint(Circle circle) throws CutFailedException {
       
        Vector3d n = circle.getNormalVector();
        
        Vector3d[] points;
        if (Double.isNaN(n.x) || Double.isNaN(n.y) || Double.isNaN(n.z)){
            points = new Vector3d[1];
            points[0] = circle.getMidPoint();
        } else {
            Vector3d M = circle.getMidPoint(); 

            // Eine beliebige Gerade beschaffen, die sicher den Kreis schneidet

            Line3d line = null;

            // Einen Richtungsvektor der gewuenschten Geraden zuverlaessig bestimmen
            if (n.y != 0.0d && n.z != 0.0d){
                 line = new Line3d(M, new Vector3d(0.0d,1.0d,-n.y/n.z));
            } else if (n.x != 0.0d && n.z != 0.0d){
                 line = new Line3d(M, new Vector3d(1.0d,0.0d,-n.x/n.z));
            } else if (n.z == 0.0d && n.x != 0.0d){
                 line = new Line3d(M, new Vector3d(-n.y/n.x,1.0d,1.0d));
            } else if (n.z == 0.0d && n.y != 0.0d){
                 line = new Line3d(M, new Vector3d(1.0d,-n.x/n.y,1.0d));
            } else {
                log.error("Das Beschaffen eines Richtungsvektors einer Geraden ist fehlgeschlagen!"); 
            }
            points = cut(circle, line);
        }
        return points[0];
    }   
    
    public final Vector3d[] getArbitraryPoints(int count) throws CutFailedException {
        return getPoints(getArbitraryPoint(), getNormalVector(), getMidPoint(), count);
    }
    // beschaffe die angegebene Zahl von Punkten, die gleichmaessig auf dem
    // Kreis verteilt sind
    public static Vector3d[] getPoints(Vector3d startPoint, Vector3d normalVector,
                                      Vector3d midPoint, int count){
        
        Vector3d[] result = new Vector3d[count];
        
        result[0] = startPoint;
        
        Vector3d startVector = new Vector3d(midPoint);
        startVector.negate();
        startVector.add(startPoint);
        
        double alpha = 2.0d*Math.PI/((double) count);
        for (int i=1;i<count;i++){
            // Die Drehmatrix dreht einen Punkt um die angegebene Achse durch den Ursprung
            Matrix3d rotMatrix = new Matrix3d();
            rotMatrix.set(new AxisAngle4d(normalVector, alpha));
            rotMatrix.transform(startVector);
            result[i] = new Vector3d(startVector);
            result[i].add(midPoint);
        }
        
        return result;
    }
    
    // Es wird davon ausgegangen, dass die Gerade in der Ebene des Kreises liegt
    // falls es keinen Schnittpunkt gibt, wird der Aufpunkt des Kreismittelpunkts
    // auf die Gerade zurückgegeben
    public Vector3d[] cut2(Line3d line) throws CutFailedException{
        return cut2(this,line);
    }
    // Die methode ist kritisch, da aufgrund der Numerik es leicht passieren kann
    // dass es keine Schnittpunkt gibt
    @Override
    public Vector3d[] cut(Line3d line) throws CutFailedException {
        return cut(this,line);
    }
    /**
     * Schneidet den Kreis mit der Geraden und liefert einen bzw. zwei Schnittpunkte zurueck.
     * Wenn es keinen Schnittpunkt gibt wird eine CalculationFailedException geworfen
     *
     * Das ist riskant, wegen ungenauer Numerik könnte die Gerade haarscharf an dem Kreis vorbeigehen.
     * Leider ist es kompliziert wenn es keinen Schnittpunkt gibt einen Aufpunkt zu bestimmen, da
     * die Gerade in der Regeln nicht in der Ebene liegt in der der Kreis liegt!
     */
    public static Vector3d[] cut(Circle circle, Line3d line) throws CutFailedException {
        
         double r = circle.getRadius();
         Vector3d M = circle.getOrigin();
         
         Vector3d[] points = null;
         
         // Gerade
         Vector3d n0 = line.getDirectionVector();
         Vector3d o = line.getOrigin(); 
   
         // Kreis
         // r,M
         
         // geschnitten ergibt sich eine quadratische Gleichung
         // a*t*t + b*t + c = 0
         
         double a = n0.lengthSquared();
         
         // s=o-M
         Vector3d s = new Vector3d(M); 
         s.negate();
         s.add(o);
         
         double b = 2.0d*s.dot(n0);
         double c = s.lengthSquared() - r*r;
         
         double sqrt = b*b-4*a*c;
         if (sqrt >0){
            double t1 = (-b+Math.sqrt(sqrt))/(2.0d*a);
            double t2 = (-b-Math.sqrt(sqrt))/(2.0d*a);
            
            // x=o+t*n0
            
            Vector3d P1 = new Vector3d(n0);
            P1.scale(t1);
            P1.add(o);
            
            Vector3d P2 = new Vector3d(n0);
            P2.scale(t2);
            P2.add(o);
            
            points = new Vector3d[2];
            points[0] = P1;
            points[1] = P2;
            
         // es gibt nur einen Schnittpunkt
         } else if (sqrt == 0) {
            double t = -b/(2.0d*a);
            Vector3d P = new Vector3d(n0);
            P.scale(t);
            P.add(o);
            points = new Vector3d[1];
            points[0] = P;
         } else {
            throw new CutFailedException("Kreis (M="+M+", r="+r+") und Gerade (P0="+line.getOrigin()+",k="+line.getDirectionVector()+") haben keinen Schnittpunkt!");
         }
         return points;
    }
     
    /*
     * Diese Methode geht davon aus, dass die Gerade und der Kreis in der gleichen 
     * Ebene liegen. Das vereinfacht die Rechnung.
     *
     * Es werden ein oder zwei Punkte zurückgegeben. Echte ein oder zwei Lösungen
     * sollte eigentlich nur
     * selten vorkommen, da die Numerik in der Regel ... Besser wäre es allen
     * cut-Methoden ein double für die gewünschte Genaugigkeit mitzugeben ...
     *
     * Wenn es keine Lösung gibt, wird der Aufpunkt des Kreismittelpunkts auf die
     * Gerade bestimmt und dann auf den Kreis projeziert und zurückgegeben.
     */
    public static Vector3d[] cut2(Circle circle, Line3d line) throws CutFailedException {
        
       Vector3d[] result;
       
       Vector3d poc = line.project(circle.getOrigin());
       
       Vector3d a = new Vector3d(poc);
       a.sub(circle.getOrigin());
       double d2 = circle.getRadiusSquare()-a.lengthSquared();
       // die Gerade schneidet den Kreis in zwei Punkten
       if (d2 > 0){
            double d = Math.sqrt(d2);
            result = new Vector3d[2];
            result[0] = new Vector3d(line.getDirectionVector());
            result[0].scale(d/result[0].length());
            result[0].add(poc);
            result[1] = new Vector3d(line.getDirectionVector());
            result[1].scale(-d/result[1].length());
            result[1].add(poc);
       // Die Gerade berüht den Kreis, oder es gibt überhaupt keinen Schnittpunkt
       } else {
            result = new Vector3d[1];
            result[0] = new Vector3d(poc);
            result[0].sub(circle.getMidPoint());
            result[0].scale(circle.getRadius()/result[0].length());
            result[0].add(circle.getMidPoint());
       }
       return result;
    }
    
    public final double getRadius(){
        return r;
    }
    public double getRadiusSquare(){
        return r*r;
    }
    
    /** 
     * Wenn die beiden Kugeln sich nicht schneiden, dann wird als MidPoint/Origin etc.
     * ein Punkt auf der Verbindungslinie zwischen den beiden Kugeln an der Position 
     * entsprechend dem Verhaeltnis der Radien zurueckgegeben.
     */
    public final Vector3d getMidPoint(){
        return o;
    }
    public final boolean isValid(){
        if (!Double.isNaN(r) && !Double.isInfinite(r) && super.isValid()){
            return true;
        }
        return false;
    }
    
    /**
     * Es wird die von der Klasse Plane geerbte Methode cut() ohne Aenderung
     * aufgerufen. Die Methode wird hier nur "ueberschrieben, um diesen Kommentar
     * einfuegen zu koennen! 
     *
     * Die Methode betrachtet diesen Kreis als Ebene. Es werden also wirklich zwei
     * Ebenen geschnitten und nicht ein Kreis mit einer Ebene Deshalb kommt als
     * Ergebnis auch ein iLine Objekt herraus und nicht ein oder zwei Punkte.
     *
     */
    @Override
    public Line3d cut(Plane plane) throws CutFailedException {
        return super.cut(plane);
    }
    // das ist doch mist, bessere Namensgebung gefordert!!!
    // kann ich da nicht auch einfach nur cut() nehmen und dann das Argument immer
    // explizit nach iPlane casten, wenn es ein Circle sein sollte
     public Vector3d[] cutAsCircle(Plane plane) throws CutFailedException {
        return cut(plane,this);
    }
    /*
     * Achtung: Wenn die Ebene und der Kreis sich nicht schneiden, dann wird ein 
     * Aufpunkt auf die Ebene bestimmt, d.h. die Projektion des Kreismittelpunkts
     * auf die Schnittgerade (Ebene durch den Kreis mit der angegebene Ebene geschnitten)
     *
     * Vielleicht sollte ich einen maximalen Abstand angeben unter dem so ein Aufpunkt
     * bestimmt wird anstatt eine CutFailedException zu werfen!
     *
     */
    public static Vector3d[] cut(Plane plane, Circle circle) throws CutFailedException {
        Vector3d[] points = null;
         
        // zuerst die Ebene in der der Kreis liegt mit der zweiten Ebenen schneiden
        // das gibt in der Regel eine Gerade
        Line3d line = plane.cut((Plane) circle);
        //line = new Line3d(circle.getOrigin(),line.getDirectionVector());
        
        // jetzt die Gerade mit dem Kreis schneiden
        // ob das wohl immer gut geht? Ungenauigkeiten in der Numerik könnten
        // dazu führen, dass es keinen Schnittpunkt gibt!
        try {
            // Kreis und Gerade liegen in einer Ebene
            // wenn keine echten Schnittpunkte existieren, dann wird der Aufpunkt
            // des Kreismittelpunkts auf die Gerade zurückgeliefert
            // das ist nicht gut da ich möglicherweise sehr häufig nur die Näherungslösung
            // statt beide echten Lösungen bekomme
            //points = cut(circle,line);
            
            // wenn keine echten Schnittpunkte existieren, dann wird der Aufpunkt
            // des Kreismittelpunkts auf die Gerade auf den Kreis zurückprojeziert
            // das ist vermutlich genauso schlecht aus dem gleichen Grund!
            points = cut2(circle,line);
            
            // besser Gerade und Kugel schneiden
            //FIXME
            //iSphere sphere = new Sphere(circle);
            //sphere.cut(line); // diese Methode ist leider noch nicht implementiert
        } catch (CutFailedException e){
            // eigentlich ist zu erwarten, dass ich sehr oft hier lande!
            // wenn sich Kreis und Gerade nicht schneiden, dann den Aufpunkt
            // bestimmen
            // Kreis und Gerade liegen in einer Ebene
            Vector3d pocL = line.project(circle.getOrigin());
            points = new Vector3d[1];
            points[0] = pocL;
            log.warn("Kreis und Gerade haben keine Schnittpunkt. Daher wird der Aufpunkt in der Ebene bestimmt!");
            // in so einem Fall könnte ich für Debug-Zwecke eine Phase erzeugen!
        }
        return points;
    }
    
    public static void main(String[] args){
        Vector3d startPoint = new Vector3d(1.0d,0.0d,0.0d);
        Vector3d normalVector = new Vector3d(0.0d,0.0d,1.0d);
        Vector3d midPoint = new Vector3d();
        
        /*Vector3d[] result = getPoints(startPoint, normalVector, midPoint, 8);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);
        System.out.println(result[3]);
        System.out.println(result[4]);
        System.out.println(result[5]);
        System.out.println(result[6]);
        System.out.println(result[7]);*/
        
        Vector3d k = new Vector3d(midPoint);
        k.negate();
        k.add(startPoint);
        try {
            Vector3d[] result = cut(new Circle(midPoint,normalVector,100.0d), new Line3d(startPoint, k));
            System.out.println("P1="+result[0]);
            if (result.length == 2){
                System.out.println("P2="+result[1]);
            }
        } catch (CutFailedException e){
            System.out.println(e);
        }
    }
}
