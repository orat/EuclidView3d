package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.textured.TranslucentQuad;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2022
 */
public class Plane extends Quad /*TranslucentQuadComposite*/ {	
    
    //protected /*Translucent*/Quad quad;
    
    public void setData(Point3d location, Vector3d dir1, Vector3d dir2, Color color){
        //quad = new /*Translucent*/Quad(); 
     
        Coord3d coord3d = new Coord3d();
        coord3d.add((float) location.x, (float) location.y, (float) location.z);
        Point firstPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.add((float) (location.x+dir1.x), (float) (location.y + dir1.y), (float) (location.z+dir1.z));
        Point secondPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.add((float) (location.x+dir2.x+dir1.x), (float) (location.y + dir2.y+dir1.y), (float) (location.z+dir2.z+dir1.z));
        Point thirdPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.add((float) (location.x+dir2.x), (float) (location.y + dir2.y), (float) (location.z+dir2.z));
        Point forthPoint = new Point(coord3d, color);

        /*quad.*/add(firstPoint);
        /*quad.*/add(secondPoint);
        /*quad.*/add(thirdPoint);
        /*quad.*/add(forthPoint);

        /*quad.*/setColor(color);
    }
}