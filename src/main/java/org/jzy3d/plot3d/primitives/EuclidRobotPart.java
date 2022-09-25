/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

/**One part of a Robot created from multiple DrawbaleVBO2 objects
 *
 * @author Dominik Scharnagl
 */
public class EuclidRobotPart {
    
    private List<DrawableVBO2> parts = null;
    private Vector3d x;
    private Vector3d y;
    private Vector3d z;
    
    /**
     * Creates a new RobotPart
     * @param parts the parts of the Robot as DrawableVBO2
     */
    public EuclidRobotPart(List<DrawableVBO2> parts){
        this.parts = parts;
        x = new Vector3d(new Coord3d(0,0,0), new Coord3d(1,0,0));
        y = new Vector3d(new Coord3d(0,0,0), new Coord3d(0,0,1));
        z = new Vector3d(new Coord3d(0,0,0), new Coord3d(0,1,0));
    }
    
    /**
     * Draw the robotpart to a chart
     * @param chart The chart, to which it should be added
     */
    public void drawRobotPart(Chart chart){
        for(DrawableVBO2 part: parts){
            part.setWireframeDisplayed(false);
            chart.add(part);
        }
    }
    
    /**
     * Return the single parts of the robotpart
     * @return The single parts of the robot as a list DrawablVBO2
     */
    public List<DrawableVBO2> getParts(){
        return this.parts;
    }
    
    /**
     * Set the local Vectorsystem
     * @param x the x of the system
     * @param y the y of the system
     * @param z the z of the system
     */
    public void setLocalVectorsystem(Vector3d x, Vector3d y, Vector3d z){
        this.x = x;
        this.y = y; 
        this.z = z;
    }
    
    /**
     * Set the x vector of the Local Vectorsystem
     * @param x the new x vector
     */
    public void setLocalVectorsystemX(Vector3d x){
        this.x = x;
    }
    
    /**
     * Set the y vector of the Local Vectorsystem
     * @param y the new y vector
     */
    public void setLocalVectorsystemY(Vector3d y){
        this.y = y;
    }
    
    /**
     * Set the z vector of the Local Vectorsystem
     * @param z the new z vector
     */
    public void setLocalVectorsystemZ(Vector3d z){
        this.z = z;
    }
    
    /**
     * Get the x vector of the local Vectorsystem 
     * @return the x vector
     */
    public Vector3d getLocalVectorsystemX(){
        return this.x;
    }
    
    /**
     * Get the y vector of the local Vectorsystem 
     * @return the y vector
     */
    public Vector3d getLocalVectorsystemY(){
        return this.y;
    }
    
    /**
     * Get the z vector of the local Vectorsystem 
     * @return the z vector
     */
    public Vector3d getLocalVectorsystemZ(){
        return this.z;
    }
    
}
