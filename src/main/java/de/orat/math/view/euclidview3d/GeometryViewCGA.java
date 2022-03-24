package de.orat.math.view.euclidview3d;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;

/**
 *
 * TODO
 * - Größe der Punkte etc. sinnvoll kontrollieren
 * - Visualisierung-Volumen an Punkten, Kugeln, Kreisen orientieren und start-endpunkte
 *   von Linien sowie betreffende Parameter von Ebenen entsprechend automatisch anpassen
 * 
 * Default Farben:
 * spheres and planes (points) grade 1 yellow
 * dual spheres and planes (grade 4) red
 * dual lines and dual circle and point pairs (grade 3) objekte blau
 * circle, lines and dual point pairs (grade 2) color?
 * imaginäre kreise dahsed
 * 
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryViewCGA extends GeometryView3d {
    
    public static Color COLOR_GRADE_1 = Color.RED; // dual sphere, dual planes, points
    public static Color COLOR_GRADE_2 = Color.GREEN; // circle, lines
    public static Color COLOR_GRADE_3 = Color.BLUE; // point-pairs
    public static Color COLOR_GRADE_4 = Color.YELLOW; // spheres, planes, points
     
    public static float POINT_RADIUS = 0.02f;
    public static float LINE_RADIUS = 0.02f;
    
    public void addPoint(Point3d p){
        addPoint(p, COLOR_GRADE_1); // oder grade 4?
    }
    public void addLine(Vector3d attitude, Point3d location){
        //FIXME
        float length = 1;
        addLine(attitude, location, COLOR_GRADE_2, LINE_RADIUS, length); 
    }
    
    public void addSphere(Point3d location, double radius){};
    public void addPlane(Point3d location, Vector3d direction){};
    public void addPointPair(){};
    
    public void addDualLine(){};
    public void addDualSphere(){};
    public void addDualPlane(){};
    public void addDualPointPair(){};
}
