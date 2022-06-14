package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import org.jzy3d.plot3d.primitives.textured.TranslucentQuad;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2022
 */
public class EuclidPlane extends PickablePolygon implements PickableObjects {	
    
    //protected /*Translucent*/Quad quad;
    private Vector3d dir1;
    private Vector3d dir2;
    
    public void setData(Point3d location, Vector3d dir1, Vector3d dir2, Color color){
        //quad = new /*Translucent*/Quad(); 
     
        this.dir1 = dir1;
        this.dir2 = dir2;
        this.color = color;
        
        Coord3d coord3d = new Coord3d();
        coord3d.set((float) location.x, (float) location.y, (float) location.z);
        Point firstPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.set((float) (location.x+dir1.x), (float) (location.y + dir1.y), (float) (location.z+dir1.z));
        Point secondPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.set((float) (location.x+dir2.x+dir1.x), (float) (location.y + dir2.y+dir1.y), (float) (location.z+dir2.z+dir1.z));
        Point thirdPoint = new Point(coord3d, color);

        coord3d = new Coord3d();
        coord3d.set((float) (location.x+dir2.x), (float) (location.y + dir2.y), (float) (location.z+dir2.z));
        Point forthPoint = new Point(coord3d, color);

        /*quad.*/add(firstPoint);
        /*quad.*/add(secondPoint);
        /*quad.*/add(thirdPoint);
        /*quad.*/add(forthPoint);

        /*quad.*/setColor(color);
    }

    @Override
    public DrawableTypes getType() {
        return DrawableTypes.PLANE;
    }

    @Override
    public void setNewPosition(Coord3d position) {
        //Translate to origin to translate from there to new position
        Coord3d bottom = new Coord3d(this.getBounds().getXmin(), this.getBounds().getYmin(), this.getBounds().getZmin());
        Transform trans = this.getTransform();    
        Translate translate = new Translate(new Coord3d(-bottom.x,-bottom.y,-bottom.z));
        trans.add(translate);
        this.applyGeometryTransform(trans);
        
        Transform trans2 = this.getTransform();
        Translate translate2 = new Translate(position);
        trans2.add(translate2);
        this.applyGeometryTransform(trans2);
    }
    
}