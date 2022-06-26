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
import org.jzy3d.plot3d.transform.TranslateDrawable;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2019
 */
public class Line extends Composite implements Pickable, PickableObjects{	
    
    protected Cylinder cylinder;
    private int pickingId;
    private Vector3d vec;
    private Point3d dif;
    private float length;
    private float radius;
    private float slices;
    private float rings;

    public void setData(Point3d p1, Point3d p2, float radius, int slices, int rings, Color color){
        
        this.radius = radius;
        this.slices = slices;
        this.rings = rings;
        this.color = color;
        
        if(dif == null){
           dif = new Point3d(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z); 
        }
        
        this.vec =  new Vector3d(new Coord3d(p1.x,p1.y,p1.z), new Coord3d(p2.x, p2.y, p2.z));
        
        Coord3d position = vec.getCenter();
        length = vec.norm();  
        
        cylinder = new Cylinder();
        cylinder.setData(new Coord3d(0, 0, -length/2f),
                         length, radius, slices, rings, color);
        cylinder.setWireframeColor(Color.GRAY);
        add(cylinder);
        
        Transform trans = new Transform(); 
        Rotate rot = createRotateTo(new Coord3d(0d,0d,1d), vec.vector());
        trans.add(rot);
        Translate translate = new Translate(position);
        trans.add(translate);
        applyGeometryTransform(trans);
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
        return DrawableTypes.LINE;
    }
    
    @Override
    public void setNewPosition(Coord3d position) {
        this.clear();
        this.setData(new Point3d(position.x,position.y,position.z), new Point3d(position.x+dif.x,position.y+dif.y,position.z+dif.z), radius, pickingId, pickingId, color);
    }
    
    @Override
    public Coord3d getPosition() {
        return this.getBounds().getCenter();
    }
}