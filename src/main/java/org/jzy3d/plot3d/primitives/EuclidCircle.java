package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.text.drawable.DrawableText;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

/**
 * The class of a circle.
 * 
 * @author Dominik Scharnagl
 */
public class EuclidCircle extends Composite implements Pickable, PickableObjects {

    private int pickingID = 0;
    private float rings = 100.f;
    private float radius;
    private Color ringColor;
    private boolean stippled;
    private String label;
    private Vector3d[] axes;
    
    /**
     * Returns the (normalized) direction of the circle after invokation of setData().
     * 
     * @return the (normalized) direction
     */
    public Vector3d getDirection(){
        return axes[0];
    }
    
    /**
     * Returns the radius of the circle.
     * 
     * @return the radius
     */
    public float getRadius(){
        return this.radius;
    }
    
    /**
     * Returns the color of the circle.
     * 
     * @return the color of the circle
     */
    public Color getRingColor(){
        return this.ringColor;
    }
    
    /**
     * Returns the text of the label of the circle.
     * 
     * @return the label text
     */
    public String getLabel(){
        return this.label;
    }  
    
    /**
     * Set the data of a circle.
     * 
     * @param origin the origin of the circle
     * @param n the (not normalized) direction
     * @param radius the radius of the circle
     * @param color the color of the circle
     * @param label the text for the label of the circle
     */
    public void setData(Point3d origin, Vector3d n, float r,
                        Color color, String label, boolean stippled){
        
        //get the orthogonal vectors to the direction to get the axes for the circle
        axes = getOrthogonalsToDirection(n);
        
        this.radius = r;
        System.out.println("Circle \""+label+"\" r="+String.valueOf(r)+" o=("+String.valueOf(origin.x)+","+String.valueOf(origin.y)+
                ", "+String.valueOf(origin.z)+")");
        
        this.ringColor = color;
        this.stippled = stippled;
        this.label = label;
        
        CroppableLineStrip lineStrip = new CroppableLineStrip();
        
        Coord3d p1 = new Coord3d(axes[1].x, axes[1].y, axes[1].z);
        //Calculate the first point from the circle. Scale the vector between 
        // the origin and a point p1 on the axes to the radius.
        Vector3d vec_p1_origin = new Vector3d(axes[1].x, axes[1].y, axes[1].z);
        float ratio = (float) radius;
        vec_p1_origin.scale(ratio);
        //rotate the first point around the direction and add the points to the strip
        Coord3d firstPoint = new Coord3d(vec_p1_origin.x, vec_p1_origin.y, vec_p1_origin.z);
        Coord3d rotateAround = new Coord3d(n.x, n.y, n.z);
        float rotationStep = 360.f/rings;
        float degree_now = 0.f;
        for (int i=0;i<rings;i++){   
            // normalization of the axis is not needed
            lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
            degree_now += rotationStep;
        }
        lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
        
        lineStrip.setWireframeColor(ringColor);
        lineStrip.setStipple(stippled);
        
        //Translate the lineStrip to its position
        Transform trans = new Transform(); 
        Translate translate = new Translate(new Coord3d(origin.x, origin.y, origin.z));
        trans.add(translate);
        lineStrip.applyGeometryTransform(trans);
        this.add(lineStrip);
        
        //Add label
        Vector3d origin_firstPoint = new Vector3d(origin.x+firstPoint.x, origin.y+firstPoint.y, origin.z+firstPoint.z);
        ratio = (float) (LabelFactory.getInstance().getOffset()+origin_firstPoint.length())/(float) origin_firstPoint.length();
        vec_p1_origin.scale(ratio);
        Point3d labelLocation = new Point3d(p1.x+vec_p1_origin.x, p1.y+vec_p1_origin.y,p1.z+vec_p1_origin.z);
        DrawableText labelText = LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK);
        labelText.applyGeometryTransform(trans);
        this.add(labelText);
    }
    
    /**
     * Calculates the orthogonal vectors to a normalized vector.
     * 
     * @param direction the vector to which the orthogonal vectors should be calculated
     * @return the orthogonal basis (normalized) with the direction and its 2 orthogonal vectors
     */
    private Vector3d[] getOrthogonalsToDirection(Vector3d direction){
        Vector3d[] orthogonals = new Vector3d[3];
        orthogonals[0] = new Vector3d(direction);
        orthogonals[0].normalize();
        int smalest = 0;
        float smalest_value = (float) direction.x; 
        if (smalest_value > direction.y){
            smalest = 1; 
            smalest_value = (float) direction.y;
        }
        if (smalest_value > direction.z){
            smalest = 2; 
            //smalest_value = (float) direction.z;
        }
        
        Vector3d w = switch (smalest) {
            case 0 -> new Vector3d(1,0,0);
            case 1 -> new Vector3d(0,1,0);
            default -> new Vector3d(0,0,1);
        };
        Vector3d u = new Vector3d(0,0,0);
        u.cross(w, direction);
        orthogonals[1] = u;
        orthogonals[1].normalize();
        Vector3d v = new Vector3d(0,0,0);
        v.cross(direction, u);
        orthogonals[2] = v;
        orthogonals[2].normalize();
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
       this.clear();
       this.setData(new Point3d(position.x, position.y, position.z), 
               getDirection(), radius, ringColor, label, stippled);
    }

    @Override
    public Coord3d getPosition() {
        return this.getBounds().getCenter();
    }
}
