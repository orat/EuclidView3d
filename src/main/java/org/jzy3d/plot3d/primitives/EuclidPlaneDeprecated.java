package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2022
 * @Deprecated
 */
public class EuclidPlaneDeprecated extends Composite implements Pickable, PickableObjects {	
    
    //protected /*Translucent*/Quad quad;
    private Vector3d dir1;
    private Vector3d dir2;
    private String label;
    private int pickingID = 0;
    private PickablePolygon plane;
    
    public void setData(Point3d location, Vector3d dir1, Vector3d dir2, Color color, String label){
        //quad = new /*Translucent*/Quad(); 
     
        this.dir1 = dir1;
        this.dir2 = dir2;
        this.color = color;
        this.label = label;
        
        plane = new PickablePolygon();
        
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

        /*quad.*/plane.add(firstPoint);
        /*quad.*/plane.add(secondPoint);
        /*quad.*/plane.add(thirdPoint);
        /*quad.*/plane.add(forthPoint);

        /*quad.*/plane.setColor(color);
        this.add(plane);
        
        Coord3d lowestPoint = plane.getCoordArray()[0];
        for(Coord3d coord: plane.getCoordArray()){
            if(coord.z < lowestPoint.z){
                lowestPoint = coord;
            }
        }
        Point3d labelLocation = new Point3d(lowestPoint.x, lowestPoint.y, lowestPoint.z - LabelFactory.getInstance().getOffset());
        this.add(LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK));
    }

    @Override
    public DrawableTypes getType() {
        return DrawableTypes.PLANE;
    }

    @Override
    public void setNewPosition(Coord3d position) {
        this.clear();
        this.setData(new Point3d(position.x,position.y,position.z), dir1, dir2, color, label);
    }

    @Override
    public Coord3d getPosition() {
        return this.getBounds().getCenter();
    }

    @Override
    public void setPickingId(int i) {
        this.pickingID = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingID;
    }
    
    /**
     * Returns the coordinates of the plane as an array
     * @return the coordinates inside an array
     */
    public Coord3d[] getCoordArray(){
        return plane.getCoordArray();
    }
}