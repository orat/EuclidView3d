/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

/**
 *
 * @author Nutzer
 */
public class ChessFloor extends Composite{
    
    private Chart chart;
    private float length;
    private boolean coldRemove = true;
    private static ChessFloor singelton;
    private Color lengthColor = Color.BLACK;
    private Color heightColor;
    
    public static ChessFloor getSingelton(Chart chart, float length){
        if(singelton == null){
            singelton = new ChessFloor(chart, length);
        } else {
            singelton.setLength(length);
            //singelton.update();
        }
        
        return singelton;
    }
    
    public static ChessFloor getSingelton(Chart chart){
        if(singelton == null){
            singelton = new ChessFloor(chart, 1.0f);
        }
        singelton.update();
        return singelton;
    }
    
    private ChessFloor(Chart chart, float length){
        this.chart = chart;
        this.length = length;
        this.update();
    }
    
    public void update(){
        this.removeChart();
        this.clear();
        BoundingBox3d bounds = chart.getView().getAxis().getBounds();
        int numLength = (int) Math.ceil((bounds.getXmax()-bounds.getXmin())/length); 
        int numHeight = (int) Math.ceil((bounds.getYmax()-bounds.getYmin())/length);
        Point3d lengthStart = new Point3d(0,0,0);
        Vector3d dir1 = new Vector3d(length, 0, 0);
        Vector3d dir2 = new Vector3d(0, length, 0);
        for(int i = 0; i < numLength; i++){
            lengthStart = new Point3d(bounds.getXmin() + i * length, bounds.getYmin(), 0);
            heightColor = lengthColor;
            for(int j = 0; j < numHeight; j++){
                Point3d heightCoord = new Point3d(lengthStart.x, lengthStart.y + j * length, 0);
                //clip plane
                heightCoord = clipPoint(heightCoord);
                Point3d p1 = new Point3d(heightCoord.x+dir1.x,heightCoord.y+dir1.y, heightCoord.z+dir1.z);
                Point3d p2 = new Point3d(heightCoord.x+dir2.x,heightCoord.y+dir2.y, heightCoord.z+dir2.z);
                p1 = clipPoint(p1);
                p2 = clipPoint(p2);
                dir1 = new Vector3d(p1.x-heightCoord.x, p1.y-heightCoord.y, p1.z-heightCoord.z);
                dir2 = new Vector3d(p2.x-heightCoord.x, p2.y-heightCoord.y, p2.z-heightCoord.z);
                //create plane
                EuclidPlane plane = new EuclidPlane();
                plane.setData(heightCoord, dir1, dir2, new Color(heightColor.r, heightColor.g, heightColor.b, 0.5), "");
                this.add(plane);
                if(heightColor == Color.BLACK){
                    heightColor = Color.WHITE;
                } else {
                    heightColor = Color.BLACK;
                } 
                dir1 = new Vector3d(length, 0, 0);
                dir2 = new Vector3d(0, length, 0);
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
        this.chart.add(this);
        lengthColor = Color.BLACK;
        //chart.updateProjectionsAndRender();
    }
    
    public void updateVBO(){
        this.removeChart();
        this.clear();
        BoundingBox3d bounds = chart.getView().getAxis().getBounds();
        int numLength = (int) Math.ceil((bounds.getXmax()-bounds.getXmin())/length); 
        int numHeight = (int) Math.ceil((bounds.getYmax()-bounds.getYmin())/length);
        Point3d lengthStart = new Point3d(0,0,0);
        for(int i = 0; i < numLength; i++){
            lengthStart = new Point3d(bounds.getXmin() + i * length, bounds.getYmin(), 0);
            heightColor = lengthColor;
            for(int j = 0; j < numHeight; j++){
                Point3d heightCoord = new Point3d(lengthStart.x, lengthStart.y + j * length, 0);
                //clip plane
                heightCoord = clipPoint(heightCoord);
                Point3d p1 = heightCoord;
                Point3d p2 = clipPoint(new Point3d(heightCoord.x, heightCoord.y+length, 0));
                Point3d p3 = clipPoint(new Point3d(heightCoord.x+length, heightCoord.y, 0));
                Point3d p4 = clipPoint(new Point3d(heightCoord.x+length, heightCoord.y+length, 0));
                //create plane
                DrawableVBO2 vbo = new DrawableVBO2(getVBOArray(p1,p2,p3,p4), 3);
                vbo.setColor(new Color(heightColor.r, heightColor.g, heightColor.b, 0.4f));
                this.add(vbo);
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
        this.chart.add(this);
        lengthColor = Color.BLACK;
    }
    
    private float[] getVBOArray(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
        
        float[] array = {(float)p1.x,(float)p1.y,(float)p1.z,
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         //triangle 2
                         (float)p1.x,(float)p1.y,(float)p1.z,
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p4.x,(float)p4.y,(float)p4.z,
                         //triangle 3
                         (float)p1.x,(float)p1.y,(float)p1.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         (float)p4.x,(float)p4.y,(float)p4.z,
                         //triangle 4
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         (float)p4.x,(float)p4.y,(float)p4.z};
        
        /*
        float[] array = {(float)p1.x,(float)p1.y,(float)p1.z,
                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,

                         (float)p2.x,(float)p2.y,(float)p2.z,
                         (float)p3.x,(float)p3.y,(float)p3.z,
                         (float)p4.x,(float)p4.y,(float)p4.z};
        */
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
    
    private void removeChart(){
        if(coldRemove){
           coldRemove = false; 
        }else {
            this.chart.remove(this);
        }
    }
    
    public void setLength(float length){
        this.length = length;
        this.update();
    }
    
    public float getLength(){
        return this.length;
    }
}
