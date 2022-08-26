/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;

/**
 *
 * @author Nutzer
 */
public class EuclidCircle extends Composite implements Pickable, PickableObjects{

    private int pickingID = 0;
    private float rings = 100.f;
    
    public void setData(Point3d origin, Vector3d direction, float radius ,Color color, String label){
        CroppableLineStrip lineStrip = new CroppableLineStrip();
        //get the orthogonal vectors to the direction to get the plane for the circle
        direction.normalize();
        Vector3d[] plane = getOrthogonalsToDirection(direction);
        Coord3d p1 = new Coord3d(origin.x+plane[1].x, origin.y+plane[1].y, origin.z+plane[1].z);
        //Calculate the first point from the circle. Scale the vector between the origin and a point p1 on the plane to the radius.
        Vector3d vec_p1_origin = new Vector3d(p1.x-origin.x, p1.y-origin.y, p1.z-origin.z);
        double length = vec_p1_origin.length();
        float ratio = (float) radius/ (float) length;
        vec_p1_origin.scale(ratio);
        //rotate the first point around the direction and the points to the strip
        Coord3d firstPoint = new Coord3d(origin.x+vec_p1_origin.x, origin.y+vec_p1_origin.y, origin.z+vec_p1_origin.z);
        Coord3d rotateAround = new Coord3d(direction.x, direction.y, direction.z);
        float rotationStep = 360.f/rings;
        float degree_now = 0.f;
        for (int i=0;i<rings;i++){
            lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
            degree_now += rotationStep;
        }
        lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
        lineStrip.setWireframeColor(color);
        this.add(lineStrip);
        Vector3d origin_firstPoint = new Vector3d(origin.x+firstPoint.x, origin.y+firstPoint.y, origin.z+firstPoint.z);
        ratio = (float) (LabelFactory.getInstance().getOffset()+origin_firstPoint.length())/(float) origin_firstPoint.length();
        vec_p1_origin.scale(ratio);
        Point3d labelLocation = new Point3d(p1.x+vec_p1_origin.x, p1.y+vec_p1_origin.y,p1.z+vec_p1_origin.z);
        this.add(LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK));
    }
    
    /**Calculates the orthogonal vectors to a normalized vector
     * 
     * @param direction the vector to which the orthogonal vectors should be calculated
     * @return the orthogonal basis with the direction and its 2 orthogonal vectors
     */
    private Vector3d[] getOrthogonalsToDirection(Vector3d direction){
        Vector3d[] orthogonals = new Vector3d[3];
        orthogonals[0] = direction;
        int smalest = 0;
        float smalest_value = (float) direction.x; 
        if (smalest_value > direction.y){
            smalest = 1; 
            smalest_value = (float) direction.y;
        }
        if (smalest_value > direction.z){
            smalest = 2; 
            smalest_value = (float) direction.z;
        }
        
        //FIXME
        // smalest_value not used
        
        Vector3d w = switch (smalest) {
            case 0 -> new Vector3d(1,0,0);
            case 1 -> new Vector3d(0,1,0);
            default -> new Vector3d(0,0,1);
        };
        Vector3d u = new Vector3d(0,0,0);
        u.cross(w, direction);
        orthogonals[1] = u;
        Vector3d v = new Vector3d(0,0,0);
        v.cross(direction, u);
        orthogonals[2] = v;
        return orthogonals;
    }
    
    @Override
    public void setPickingId(int i) {
        this.pickingID = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingID;
    }

    @Override
    public DrawableTypes getType() {
        return DrawableTypes.CIRCLE;
    }

    @Override
    public void setNewPosition(Coord3d position) {
        //TODO move circle with the mouse.
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Coord3d getPosition() {
        return this.getBounds().getCenter();
    }
    
}
