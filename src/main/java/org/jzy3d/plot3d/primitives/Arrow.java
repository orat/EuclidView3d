package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils2;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2019
 */
public class Arrow extends Composite implements Pickable, PickableObjects {	
    
    protected Cylinder cylinder;
    protected Cone cone;
    private int pickingId = 0;
    private float radius;
    private int slices;
    private int rings;
    private Vector3d vec;
    private String label;
    
    public void setData(Point3d p, org.jogamp.vecmath.Vector3d dir, float radius, 
                        int slices, int rings, Color color, String label){
        this.radius = radius;
        this.slices = slices;
        this.rings = rings;
        this.vec =  new org.jzy3d.maths.Vector3d(
                new Coord3d(p.x,p.y,p.z), 
                new Coord3d(p.x+dir.x, p.y+dir.y, p.z+dir.z));
        
        this.color = color;
        
        Coord3d position = vec.getCenter();
        float length = vec.norm();  
        float coneHeight = radius*2.5f;
        float cylinderHeight = length-coneHeight;
        
        cylinder = new Cylinder();
        cylinder.setData(new Coord3d(0, 0, -length/2f),
                         cylinderHeight, radius, slices, rings, color);
        cylinder.setWireframeColor(Color.GRAY);
        add(cylinder);
        
        cone = new Cone();
        cone.setData(new Coord3d(0, 0, length/2d-coneHeight), 
                     coneHeight, radius*1.6f, slices, rings, color);
        cone.setWireframeColor(Color.GRAY);
        add(cone);
        
        Transform trans = new Transform(); 
        Rotate rot = createRotateTo(new Coord3d(0d,0d,1d), vec.vector());
        trans.add(rot);
        Translate translate = new Translate(position);
        trans.add(translate);
        applyGeometryTransform(trans);
        
        // add label
        if (label != null){
            Point3d labelLocation = new Point3d(position.x, position.y - radius - LabelFactory.getInstance().getOffset(), position.z);
            this.add(LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK));
        }
    }
    
    private static Rotate createRotateTo(Coord3d from, Coord3d to){
        double fromMag =  (float) Math.sqrt(from.x * from.x + from.y * from.y + from.z * from.z);
        double toMag =  (float) Math.sqrt(to.x * to.x + to.y * to.y + to.z * to.z);
        double angle = Math.acos(from.dot(to)/(fromMag*toMag))*180f/Math.PI;
        //System.out.println(angle);
        Coord3d v = Utils2.cross(from,to);
        v.normalizeTo(1);
        return new Rotate(angle, v);
    }

    @Override
    public void setPickingId(int i) {
        this.pickingId = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingId;
    }

    @Override
    public DrawableTypes getType() {
        return DrawableTypes.ARROW;
    }

    @Override
    public void setNewPosition(Coord3d position) {
        this.clear();
        Coord3d v = vec.vector();
        setData(new Point3d(position.x,position.y,position.z), 
                new org.jogamp.vecmath.Vector3d(v.x, v.y, v.z), radius, 
                slices, rings, color, label);  
    }

    @Override
    public Coord3d getPosition() {
        return this.getBounds().getCenter();
    }
}