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
    
    public static Color COLOR_GRADE_1 = Color.RED;    // sphere, planes, dual points
    public static Color COLOR_GRADE_2 = Color.GREEN;  // circle, lines, dual point-pair
    public static Color COLOR_GRADE_3 = Color.BLUE;   // point-pairs, dual line, dual circles
    public static Color COLOR_GRADE_4 = Color.YELLOW; // dual spheres, dual planes, points
     
    // FIXME abschaffen und bei tangent durch Farbe des passendes Grads ersetzen
    public static Color COLOR_TANGENT = Color.ORANGE;
    
    //TODO
    // nur als Faktoren verwenden und skalieren auf Basis des angezeigten Volumens
    public static float POINT_RADIUS = 0.02f;
    public static float LINE_RADIUS = 0.02f;
    public static float TANGENT_LENGTH = 0.1f;
    
    /**
     * Add a point to the 3d view.
     * 
     * @param location location of the point
     * @param label
     */
    public void addPoint(Point3d location, String label){
        addPoint(location, COLOR_GRADE_1, POINT_RADIUS*2, label); // oder grade 4?
    }
    /**
     * Add a line to the 3d view.
     * 
     * @param attitude
     * @param location 
     */
    public void addLine(Vector3d attitude, Point3d location, String label){
        //FIXME
        float length = 1;
        addLine(attitude, location, COLOR_GRADE_2, LINE_RADIUS, length, label); 
    }
    
    /**
     * Add a tangent to the 3d view.
     * 
     * @param location
     * @param attitude 
     * @param label null if no label needed
     */
    public void addTangent(Point3d location, Vector3d attitude, String label){
        addArrow(location, attitude, TANGENT_LENGTH, LINE_RADIUS, COLOR_TANGENT, label);
    }
    
    public void addSphere(Point3d location, double radius){};
    public void addPlane(Point3d location, Vector3d direction){};
    public void addPointPair(){};
    
    public void addDualLine(){};
    public void addDualSphere(){};
    public void addDualPlane(){};
    public void addDualPointPair(){};
}
