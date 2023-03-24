package de.orat.math.euclid;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class AxisAlignedBoundingBox {
    
    private Point3d center;
    private Vector3d size; // diagonal durch die ganze box
    
    public AxisAlignedBoundingBox(Point3d center, Vector3d size){
        this.center = center;
        this.size = size;
    }
   
    /**
     * implementation follows:
     * https://stackoverflow.com/questions/7168484/3d-line-segment-and-plane-intersection/47649285?noredirect=1#comment82256851_47649285
     *
     * @param lineSegment_start line segments start point
     * @param lineSegment_end line segments end point
     * @param plane_normal plane_normal vector of the plane
     * @param plane_point point on the plane
     * @return hitPoint, if the plane hits the line segment
     */
    private Point3d cutLineSegmentPlane(Point3d lineSegment_start, Point3d lineSegment_end, 
            Vector3d plane_normal, Point3d plane_point){
        Vector3d ray = new Vector3d(lineSegment_end);
        ray.sub(lineSegment_start); 

        double d = plane_normal.dot(new Vector3d(plane_point));
        
        if (plane_normal.dot(ray) == 0){
            return null; 
        }

        double t = (d - plane_normal.dot(new Vector3d(lineSegment_start))) / plane_normal.dot(ray);

        Point3d hitPoint = new Point3d();
        hitPoint.x = lineSegment_start.x + ray.x * t; 
        hitPoint.y = lineSegment_start.y + ray.y * t; 
        hitPoint.z = lineSegment_start.z + ray.z * t; 
        return hitPoint;
    }
    
    /**
     * Find the intersections of a plane with each of the 12 edges 
     * of the axis aligned bounding box. Some of the edges will intersect with the plane, 
     * some won't. The resulting list of intersection points defines a
     * polygon, which is the "clipped plane".
     * 
     * @param plane
     * @return polygon corners
     */
    public Point3d[] clip(Plane plane){
        
        Point3d origin = new Point3d(center);
        Vector3d halfSize = new Vector3d(size);
        halfSize.scale(0.5);
        origin.sub(halfSize);
        Point3d corner1 = new Point3d(origin);
        corner1.x += size.x;
        Point3d corner2 = new Point3d(origin);
        corner1.y += size.y;
        Point3d corner3 = new Point3d(origin);
        corner1.z += size.z;
        Point3d origin2 = new Point3d(center);
        origin2.add(halfSize);
        Point3d corner4 = new Point3d(origin);
        corner1.x -= size.x;
        Point3d corner5 = new Point3d(origin);
        corner1.y -= size.y;
        Point3d corner6 = new Point3d(origin);
        corner1.z -= size.z;
        List<Point3d> corners = new ArrayList<>();
        
        Point3d hitPoint = cutLineSegmentPlane(origin, corner1, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(origin, corner2, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(origin, corner3, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        
        hitPoint = cutLineSegmentPlane(origin2, corner4, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(origin2, corner5, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(origin2, corner6, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        
        hitPoint = cutLineSegmentPlane(corner1, corner5, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(corner3, corner5, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(corner2, corner4, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(corner2, corner6, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(corner1, corner6, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        hitPoint = cutLineSegmentPlane(corner3, corner4, 
                                plane.getNormalVector(),  new Point3d(plane.getOrigin()));
        if (hitPoint != null) corners.add(hitPoint);
        
        return corners.toArray(Point3d[]::new);
    }
    
    /**
     * Determine clipping point of a line with the bounding box of the current
     * visualization.
     * 
     * In principle "Liang-Barsky Line Clipping algorithm" is a possible 
     * implementation.
     * 
     * Implementation is adapted from:
     * https://stackoverflow.com/questions/3106666/intersection-of-line-segment-with-axis-aligned-box-in-c-sharp
     * 
     * TODO
     * untested, seems not to work
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
            
        double near = -Double.MAX_VALUE;
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
    
 /** 
  *  Fast Ray-Box Intersection
  *  by Andrew Woo
  *  from "Graphics Gems", Academic Press, 1990
  *
  * ungetestet
  * 
  * @param line
  * @param hitPoint
  * @return true if the rays hits the boundingBox
  */
    public boolean clipRay(Line3d line, Point3d hitPoint){

        final int RIGHT	= 0;
        final int LEFT	= 1;
        final int MIDDLE= 2;

        //double origin[NUMDIM], dir[NUMDIM];   /*ray */
        double[] origin = new double[3];
        origin[0] = line.getOrigin().x;
        origin[1] = line.getOrigin().y;
        origin[2] = line.getOrigin().z;

        double[] dir = new double[3];
        dir[0] = line.getDirectionVector().x;
        dir[1] = line.getDirectionVector().y;
        dir[2] = line.getDirectionVector().z;

        //char HitBoundingBox(minB,maxB, origin, dir,coord)
        //double minB[NUMDIM], maxB[NUMDIM];    /*box */
        double[] minB = new double[3];
        minB[0] = center.x-size.x/2;
        minB[1] = center.y-size.y/2;
        minB[2] = center.y-size.y/2;
        double[] maxB = new double[3];
        maxB[0] = center.x+size.x/2;
        maxB[1] = center.y+size.y/2;
        maxB[2] = center.y+size.y/2;

        //double coord[NUMDIM]; /* hit point */

        boolean inside = true;
        int[] quadrant = new int[3];
        /*register*/ int i;
        int whichPlane;
        double[] maxT = new double[3];
        double[] candidatePlane = new double[3];

        /* Find candidate planes; this loop can be avoided if
        rays cast all from the eye(assume perpsective view) */
        for (i=0; i<3; i++){
            if (origin[i] < minB[i]) {
                quadrant[i] = LEFT;
                candidatePlane[i] = minB[i];
                inside = false;
            } else if (origin[i] > maxB[i]) {
                quadrant[i] = RIGHT;
                candidatePlane[i] = maxB[i];
                inside = false;
            } else	{
                quadrant[i] = MIDDLE;
            }
        }

        /* Ray origin inside bounding box */
        if (inside)	{
            hitPoint.x = origin[0];
            hitPoint.y = origin[1];
            hitPoint.z = origin[2];
            return true;
        }

        /* Calculate T distances to candidate planes */
        for (i = 0; i < 3; i++){
            if (quadrant[i] != MIDDLE && dir[i] !=0.)
                maxT[i] = (candidatePlane[i]-origin[i]) / dir[i];
            else
                maxT[i] = -1.;
        }

        /* Get largest of the maxT's for final choice of intersection */
        whichPlane = 0;
        for (i = 1; i < 3; i++)
            if (maxT[whichPlane] < maxT[i])
                whichPlane = i;

        /* Check final candidate actually inside box */
        if (maxT[whichPlane] < 0.) return false;
        double[] coord = new double[3];
        for (i = 0; i < 3; i++) {
            if (whichPlane != i) {
                coord[i] = origin[i] + maxT[whichPlane] *dir[i];
                if (coord[i] < minB[i] || coord[i] > maxB[i])
                        return false;
            } else {
                coord[i] = candidatePlane[i];
            }
        }
        hitPoint.x = coord[0];
        hitPoint.y = coord[1];
        hitPoint.z = coord[2];
        return true; /* ray hits box */
    }
}
