package de.orat.math.euclid;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class AxisAlignedBoundingBox {
    
    private Point3d center;
    private Vector3d size;
    
    public AxisAlignedBoundingBox(Point3d center, Vector3d size){
        this.center = center;
        this.size = size;
    }
   
    public boolean clip(Plane plane, Point3d[] points){
        
        Plane xyplane = new Plane(new Vector3d(0,0,center.z-size.z/2d), new Vector3d(0,0,1));
        Plane yzplane = new Plane(new Vector3d(center.x-size.x/2d,0,0), new Vector3d(1,0,0));
        Plane zxplane = new Plane(new Vector3d(0,center.y-size.y/2d,0), new Vector3d(0,1,0));
        
        Plane xyplaneMax = new Plane(new Vector3d(0,0,center.z+size.z/2d), new Vector3d(0,0,1));
        Plane yzplaneMax = new Plane(new Vector3d(center.x+size.x/2d,0,0), new Vector3d(1,0,0));
        Plane zxplaneMax = new Plane(new Vector3d(0,center.y+size.y/2d,0), new Vector3d(0,1,0));
       
        Line3d xyLineMin = null;
        Line3d yzLineMin = null;
        Line3d zxLineMin = null;
        
        Line3d xyLineMax = null;
        Line3d yzLineMax = null;
        Line3d zxLineMax = null;
        
        try {
            xyLineMin = xyplane.cut(plane);
        } catch (CutFailedException e){}
        try {
            yzLineMin = yzplane.cut(plane);
        } catch (CutFailedException e){}
        try {
            zxLineMin = zxplane.cut(plane);
        } catch (CutFailedException e){}
        try {
            xyLineMax = xyplaneMax.cut(plane);
        } catch (CutFailedException e){}
        try {
            yzLineMax = yzplaneMax.cut(plane);
        } catch (CutFailedException e){}
        try {
            zxLineMax = zxplaneMax.cut(plane);
        } catch (CutFailedException e){}
        
        // Linien mit den passenden Ebenen schneiden um die Punkte auf den 
        // Kanten der bounding-box zu bestimmen
        //TODO
        
        
        
        return false;
    }
    
    /**
     * Determine clipping point of a line with the bounding box of the current
     * visualization.
     * 
     * In principle "Liang-Barsky Line Clipping algorithm" is a possible 
     * implementation.
     * 
     * @param line to clip on the AA-bounding-box
     * @param p1 output near point
     * @param p2 output far point
     * @return true if there are intersection points
     */
    public boolean clip(Line3d line, Point3d p1, Point3d p2){
         
        Vector3d halfSize = new Vector3d(this.size);
        halfSize.scale(0.5d);
        
        Point3d min = new Point3d(center);
        min.sub(halfSize);
        min.sub(line.getOrigin());
        
        Point3d max = new Point3d(center);
        max.add(halfSize);
        max.sub(line.getOrigin());
            
        double near = Double.MIN_VALUE;
        double far = Double.MAX_VALUE;

        // X
        double t1 = min.x / line.getDirectionVector().x;
        double t2 = max.x / line.getDirectionVector().x;
        double tMin = Math.min(t1, t2);
        double tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            p1 = null;
            p2 = null;
            return false;
        }

        // Y
        t1 = min.y / line.getDirectionVector().y;
        t2 = max.y / line.getDirectionVector().y;
        tMin = Math.min(t1, t2);
        tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            p1 = null;
            p2 = null;
            return false;
        }

        // Z
        t1 = min.z / line.getDirectionVector().z;
        t2 = max.z / line.getDirectionVector().z;
        tMin = Math.min(t1, t2);
        tMax = Math.max(t1, t2);
        if (tMin > near) near = tMin;
        if (tMax < far) far = tMax;
        if (near > far || far < 0){
            p1 = null;
            p2 = null;
            return false;
        }

        p1 = new Point3d(line.getOrigin());
        Vector3d dir = new Vector3d(line.getDirectionVector());
        dir.scale(near);
        p1.add(dir);
        p2 = new Point3d(line.getOrigin());
        dir = new Vector3d(line.getDirectionVector());
        dir.scale(far);
        p2.add(dir); 
        return true;
    }
}
