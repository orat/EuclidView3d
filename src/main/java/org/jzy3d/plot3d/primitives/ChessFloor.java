/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.LinkedList;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

public class ChessFloor{
    
    private static Chart chart;
    private float length;
    private boolean coldRemove = true;
    private static ChessFloor singelton;
    private LinkedList<DrawableVBO2> drawables;
    private Color lengthColor = Color.BLACK;
    private Color heightColor;
    
    /**
     * Update an existing chessfloor - creates one before, if it does not yet exist.
     * 
     * @param width of a square
     * @param chart The chart to which the chessfloor should be added
     * @param updateChart true if the cart should be updated directly
     * @return The chess floor singleton
     */
    public static ChessFloor update(float width, Chart chart, boolean updateChart){
        configure(chart, width);
        singelton.update();
        if(updateChart){
            chart.getCanvas().getView().shoot();
        }
        return singelton;
    }
    
    /**
     * Creates a new Chess Floor as a Singelton if needed, else only set
     * the width of the existing chess floor.
     * 
     * @param chart The chart to which the chess Floor should be added
     * @param width The lenght of a square of the chess floor
     * @return The chessfloor
     */
    public static ChessFloor configure(Chart chart, float width){
        if(singelton == null){
            singelton = new ChessFloor(chart, width);
        } else {
            singelton.setLength(width);
            //singelton.update();
        }
        return singelton;
    }
    
    
    public static void removeSingelton(Chart chart2, boolean updateChart){
        if(singelton != null && chart2 == chart){
            singelton.removeChart();
            if(updateChart){
                chart.getCanvas().getView().shoot();
            }
        }
    }
    /**
     * Creates a new ChessFloor
     * @param chart The chart to which it should be added
     * @param length The lenght of a square
     */
    private ChessFloor(Chart newchart, float length){
        this.drawables = new LinkedList();
        chart = newchart;
        this.length = length;
        this.update();
    }
    
    /**
     * Updates the ChessFloor.
     */
    public final void update(){
        this.removeChart();
        this.drawables.clear();
        BoundingBox3d bounds = chart.getView().getAxis().getBounds();
        //BoundingBox3d bounds = chart.getScene().getGraph().getBounds();
        int numLength = (int) Math.ceil((bounds.getXmax()-bounds.getXmin())/length); 
        int numHeight = (int) Math.ceil((bounds.getYmax()-bounds.getYmin())/length);
        Point3d lengthStart = new Point3d(0,0,0);
        for(int i = 0; i < numLength; i++){
            lengthStart = new Point3d(bounds.getXmin() + i * length, bounds.getYmin(), 0);
            heightColor = lengthColor;
            for(int j = 0; j < numHeight; j++){
                Point3d heightCoord = new Point3d(lengthStart.x, lengthStart.y + j * length, 0);
                //clip points of a square
                heightCoord = clipPoint(heightCoord);
                Point3d p1 = heightCoord;
                Point3d p2 = clipPoint(new Point3d(heightCoord.x, heightCoord.y+length, 0));
                Point3d p3 = clipPoint(new Point3d(heightCoord.x+length, heightCoord.y, 0));
                Point3d p4 = clipPoint(new Point3d(heightCoord.x+length, heightCoord.y+length, 0));
                //create the square from the edge points
                float[] a = getVBOArray(p1,p2,p3,p4);
                DrawableVBO2 vbo = new DrawableVBO2(getVBOArray(p1,p2,p3,p4), 3);
                vbo.setColor(new Color(heightColor.r, heightColor.g, heightColor.b, 0.65f));
                vbo.setWireframeDisplayed(false);
                this.drawables.add(vbo);
                if(heightColor == Color.BLACK){
                    heightColor = Color.WHITE;
                } else {
                    heightColor = Color.BLACK;
                } 
            }
            //Change Color of the next row.
            if(lengthColor == Color.BLACK){
                lengthColor = Color.WHITE;
            } else {
                lengthColor = Color.BLACK;
            }
        }
        //vbo = new DrawableVBO2(comp);
        //this.add(vbo);
        //vbo = new DrawableVBO2(this);
        //this.chart.add(this);
        this.draw();
        chart.getCanvas().getView().shoot();
        lengthColor = Color.BLACK;
    }
    
    /**
     * Get the VBO Array for a square
     * @param p1 Point1 of the square
     * @param p2 Point2 of the square
     * @param p3 Point3 of the square
     * @param p4 Point4 of the square
     * @return The VBO array.
     */
    private float[] getVBOArray(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        
        float[] array = {(float)p1.x,(float)p1.y,(float)p1.z,
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         (float)p4.x,(float)p4.y,(float)p4.z};
        return array;
    }
    
    /**
     * Clips a point.
     * 
     * Projection of the point to the bounding box.
     * 
     * @param point The point which should be clipped
     * @return the clipped point
     */
    public Point3d clipPoint(Point3d point){
        BoundingBox3d bounds = chart.getView().getAxis().getBounds();
        if (point.x < bounds.getXmin()){
            point.x = bounds.getXmin();
        } else if (point.x > bounds.getXmax()){
            point.x = bounds.getXmax();
        }
        if (point.y < bounds.getYmin()){
            point.y = bounds.getYmin();
        } else if (point.y > bounds.getYmax()){
            point.y = bounds.getYmax();
        } 
        if (point.z < bounds.getZmin()){
            point.z = bounds.getZmin();
        } else if (point.z > bounds.getZmax()){
            point.z = bounds.getZmax();
        }
        return point;
    }
    
    /**
     * Remove the floor from the chart, but not update the chart
     */
    public void removeChart(){
        if(coldRemove){
           coldRemove = false; 
        }else {
            for(DrawableVBO2 vbo: drawables){
                chart.remove(vbo, false);
            }
        }
    }
    
    /**
     * Set the length of the rectangles of the floor
     * @param length the length
     */
    public void setLength(float length){
        this.length = length;
        this.update();
    }
    
    /**
     * Get the Length of the rectangles
     * @return the length of the rectangles of the floor
     */
    public float getLength(){
        return this.length;
    }

    /**
     * Add the Chess Floor to the chart but do not update the chart
     */
    private void draw() {
        for(DrawableVBO2 vbo: drawables){
            chart.add(vbo, false);
        }
    }
}
