/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;

/**One part of a Robot created from multiple DrawbaleVBO2 objects
 *
 * @author Dominik Scharnagl
 */
public class EuclidRobotPart {
    
    private List<EuclidVBO2> parts = null;
    private Vector3d x;
    private Vector3d y;
    private Vector3d z;
    private Chart chart;
    private boolean boundingBoxDisplayed;
    private Color boundingBoxColor;
    
    /**
     * Creates a new RobotPart
     * @param parts the parts of the Robot as DrawableVBO2
     */
    public EuclidRobotPart(List<EuclidVBO2> parts){
        this.parts = parts;
        boundingBoxDisplayed = false;
        boundingBoxColor = Color.RED;
        for(EuclidVBO2 part: parts){
            this.setBoundingBoxDisplayed(boundingBoxDisplayed);
        }
        x = new Vector3d(new Coord3d(0,0,0), new Coord3d(1,0,0));
        y = new Vector3d(new Coord3d(0,0,0), new Coord3d(0,0,1));
        z = new Vector3d(new Coord3d(0,0,0), new Coord3d(0,1,0));
    }
    
    /**
     * @param chart The chart, to which it should be added
     */
    public void drawRobotPart(Chart chart){
        this.setChart(chart);
        drawRobotPart();
    }
    
    /**
     * Draw the robotpart to a chart
     */
    public void drawRobotPart(){
        for(EuclidVBO2 part: parts){
            part.setWireframeDisplayed(false);
            chart.add(part);
        }
         setBoundingBoxDisplayed(boundingBoxDisplayed);
    }   
    
    public void translateAlongVector(float phi, Coord3d vector){
        
    }
    
    /**
     * Rotates the EuclidRobot around a vector with the angle di
     * @param di the angle di
     * @param vector the vector around which the robotpart should be rotatet
     */
    public void rotateAroundVector(float di, Coord3d vector){
        Rotate rotate = new Rotate(di, vector);
        ArrayList<float[]> newObjects = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();
        for(EuclidVBO2 object: getParts()){
            newObjects.add(object.rotateAroundVector(rotate));
            colors.add(object.getColor());
        }
        clearObjects();
        for(int i = 0; i < newObjects.size(); i++){
            EuclidVBO2 vbo = new EuclidVBO2(newObjects.get(i), 3);
            vbo.setColor(colors.get(i));
            getParts().add(vbo);
        }
        drawRobotPart(chart);
    }
    
    /**
     * clears all the Objects from the chart
     */
    private void clearObjects(){
        for(EuclidVBO2 object: getParts()){
            chart.remove(object);
        }
        getParts().clear();
    }
    
    /**
     * 
     * @param boundingBoxDisplayed 
     */
    public void setBoundingBoxDisplayed(boolean boundingBoxDisplayed){
        this.boundingBoxDisplayed = boundingBoxDisplayed;
        if(boundingBoxDisplayed){
            for(EuclidVBO2 object: getParts()){
                object.setBoundingBoxDisplayed(true);
                object.setBoundingBoxColor(boundingBoxColor);
            }
        }else {
            for(EuclidVBO2 object: getParts()){
                object.setBoundingBoxDisplayed(false);
                object.setBoundingBoxColor(boundingBoxColor);
            }
        }
    }
    
    public void setBoundingBoxColor(Color color){
        this.boundingBoxColor = color; 
    }
    
    /**
     * Return the single parts of the robotpart
     * @return The single parts of the robot as a list DrawablVBO2
     */
    public List<EuclidVBO2> getParts(){
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
    
    /**
     * Set the chart for this robotpart
     * @param chart the Chart for this robotpart
     */
    public void setChart(Chart chart){
        this.chart = chart;
    }
    
    /**
     * The Chart of the robotpart
     * @return the chart of this robotpart
     */
    public Chart getChart(){
        return this.chart;
    }
}
