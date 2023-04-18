package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2022
 */
public class EuclidPlane extends Composite implements Pickable, PickableObjects {	
   
    private Point3d[] corners;
    private Point3d location;
    private String label;
    private int pickingID = 0;
    private PickablePolygon plane;
    
    /**
     * @param location
     * @param corners polygon points laying all in the same plane
     * @param color
     * @param label 
     */
    public void setData(Point3d location, Point3d[] corners, Color color, String label){
        this.corners = corners;
        this.location = location;
        this.color = color;
        this.label = label;
        
        plane = new PickablePolygon();
        
        for (int i=0;i<corners.length;i++){
            Coord3d coord3d = new Coord3d();
            coord3d.set((float) corners[i].x, (float) corners[i].y, (float) corners[i].z);
            plane.add(new Point(coord3d, color));
        }
        plane.setColor(color);
        add(plane);
        
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
        Point3d pos = new Point3d(position.x,position.y,position.z);
        for (int i=0;i<corners.length;i++){
            corners[i].sub(location);
            corners[i].add(pos);
        }
        this.setData(pos, corners, color, label);
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
     * Returns the coordinates of the plane as an array.
     * 
     * @return the coordinates inside an array
     */
    public Coord3d[] getCoordArray(){
        return plane.getCoordArray();
    }
}