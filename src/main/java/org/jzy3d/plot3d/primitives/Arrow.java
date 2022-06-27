package org.jzy3d.plot3d.primitives;

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
    
    public void setData(Vector3d vec, float radius, int slices, int rings, Color color){
        Coord3d position = vec.getCenter();
        float length = vec.norm();  
        float coneHeight = radius*2.5f;
        float cylinderHeight = length-coneHeight;
        
        this.radius = radius;
        this.slices = slices;
        this.rings = rings;
        this.vec = vec;
        this.color = color;
        
        cylinder = new Cylinder();
        cylinder.setData(new Coord3d(0, 0, -length/2f),
                         cylinderHeight, radius, slices, rings, color);
        cylinder.setWireframeColor(Color.GRAY);
        cone = new Cone();
        cone.setData(new Coord3d(0, 0, length/2d-coneHeight), 
                     coneHeight, radius*1.6f, slices, rings, color);
        cone.setWireframeColor(Color.GRAY);
        add(cylinder);
        add(cone);
        
        Transform trans = new Transform(); 
        Rotate rot = createRotateTo(new Coord3d(0d,0d,1d), vec.vector());
        trans.add(rot);
        Translate translate = new Translate(position);
        trans.add(translate);
        applyGeometryTransform(trans);
        
        //TODO label anfügen, im Methodenaufruf zustäzlich argument String label einfügen ...
        //Coord3d labelP = new Coord3d(position.x + (border1.x + border2.x) / 2,
        //position.y + (border1.y + border2.y) / 2, position.z + height / 2);
        //DrawableTextBitmap txt3d = new DrawableTextBitmap("Face " + (i + 1), labelP, Color.BLACK);
        //add(txt3d);
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
        float length = vec.norm();  
        float coneHeight = radius*2.5f;
        float cylinderHeight = length-coneHeight;
        
        cylinder = new Cylinder();
        cylinder.setData(new Coord3d(0, 0, -length/2f),
                         cylinderHeight, radius, slices, rings, color);
        cylinder.setWireframeColor(Color.GRAY);
        cone = new Cone();
        cone.setData(new Coord3d(0, 0, length/2d-coneHeight), 
                     coneHeight, radius*1.6f, slices, rings, color);
        cone.setWireframeColor(Color.GRAY);
        add(cylinder);
        add(cone);
        
        Transform trans = new Transform(); 
        Rotate rot = createRotateTo(new Coord3d(0d,0d,1d), vec.vector());
        trans.add(rot);
        Translate translate = new Translate(position);
        trans.add(translate);
        applyGeometryTransform(trans);
    }

    @Override
    public Coord3d getPosition() {
        return this.getPosition();
    }
}