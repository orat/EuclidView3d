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
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Translate;

/**
 * One part of a robot or skeleton created from multiple DrawbaleVBO2 objects
 * 
 * @author Dominik Scharnagl
 */
public class EuclidPart {
    
    private List<EuclidVBO2> parts = null;
    private Coord3d x;
    private Coord3d y;
    private Coord3d z;
    private Coord3d coordCenter;
    private Chart chart;
    private boolean boundingBoxDisplayed;
    private Color boundingBoxColor;
    private String name = "";
    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;
    private boolean isBallJoint = true;
    
    /**
     * Creates a new RobotPart
     * @param parts the parts of the Robot as DrawableVBO2
     */
    public EuclidPart(List<EuclidVBO2> parts){
        this.parts = parts;
        boundingBoxDisplayed = false;
        boundingBoxColor = Color.RED;
        this.setBoundingBoxDisplayed(boundingBoxDisplayed);
        x = new Coord3d(1,0,0);
        y = new Coord3d(0,1,0);
        z = new Coord3d(0,1,0);
        coordCenter = new Coord3d(0,0,0);
    }
    
    /**
     * Draw the RobotPart to a chart
     * @param chart The chart, to which it should be added
     */
    public void drawPart(Chart chart){
        this.setChart(chart);
        addToChart();
    }
    
    /**
     * Add the robotpart to the chart it already is part of. 
     * 
     * Does not update the chart.<p>
     * 
     * The chart has to be updated after all EuclidParts have been transformed
     */
    public void addToChart(){
        for(EuclidVBO2 part: parts){
            part.setWireframeDisplayed(false);
            chart.add(part, false);
        }
        setBoundingBoxDisplayed(boundingBoxDisplayed);
    }   
    
    /**
     * Translate the robotpart along a vector with a set distance.
     * 
     * @param distance the distance at which the robotpart should be translatet
     * @param vector the vector along the robotpart should be translatet
     */
    public void translateAlongVector(float distance, Coord3d vector){
        Coord3d newVector = new Coord3d(vector.x, vector.y, vector.z);
        newVector.normalizeTo(1);
        newVector = new Coord3d(newVector.x*distance, newVector.y*distance, newVector.z*distance);
        Translate translate = new Translate(newVector);
        ArrayList<float[]> newObjects = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();
        for(EuclidVBO2 object: getParts()){
            newObjects.add(object.translate(translate));
            colors.add(object.getColor());
        }
        clearObjects();
        for(int i = 0; i < newObjects.size(); i++){
            EuclidVBO2 vbo = new EuclidVBO2(newObjects.get(i), 3);
            vbo.setColor(colors.get(i));
            parts.add(vbo);
        }
        drawPart(chart);
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
            newObjects.add(object.rotate(rotate));
            colors.add(object.getColor());
        }
        clearObjects();
        for(int i = 0; i < newObjects.size(); i++){
            EuclidVBO2 vbo = new EuclidVBO2(newObjects.get(i), 3);
            vbo.setColor(colors.get(i));
            parts.add(vbo);
        }
        drawPart(chart);
    }
    
    /**
     * The Second rotateAround for theta
     * @param theta the theta angle how much it should be rotated in degree
     * @param vector the vector around which it should be rotated
     * @param center the center of the axis around which it will be rotated
     */
    public void rotateAroundVector2(float theta, Coord3d vector, Coord3d center){
        ArrayList<float[]> newObjects = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();
        for(EuclidVBO2 object: getParts()){
            newObjects.add(object.rotateAroundVector(theta, vector, center));
            colors.add(object.getColor());
        }
        clearObjects();
        for(int i = 0; i < newObjects.size(); i++){
            EuclidVBO2 vbo = new EuclidVBO2(newObjects.get(i), 3);
            vbo.setColor(colors.get(i));
            parts.add(vbo);
        }
        drawPart(chart); 
    }
    
    /**
     * Rotate the Center of this part
     * @param di the angle around which it should be rotated
     * @param vector the vector around which it should be rotated
     * @param centerOld the center of the part wich it will be rotated around
     */
    public void rotateCenter(float di, Coord3d vector, Coord3d centerOld){
        Coord3d newCenter = this.getParts().get(0).rotateCenter(di, vector, centerOld, this.getCenter());
        this.setCoordCenter(newCenter);
    }
    
    /**
     * clears all the Objects from the chart. 
     * Chart will not be updated. It has to be updated manually either after clearing 
     * or after readding the transformed parts.
     */
    private void clearObjects(){
        for(EuclidVBO2 object: getParts()){
            chart.remove(object, false);
        }
        parts.clear();
    }
    
    /**
     * Clear the objects from the chart
     * Chart will not be updated. It has to be updated manually either after clearing 
     * or after readding the transformed parts.
     */
    public void clearFromChart(){
        for(EuclidVBO2 object: getParts()){
            chart.remove(object, false);
        }
    }
    
    /**
     * Sets if the bounding boxes are displayed or not
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
    
    /**
     * Sets the color of the bounding box
     * @param color The color of the bounding box
     */
    public void setBoundingBoxColor(Color color){
        this.boundingBoxColor = color; 
        for(EuclidVBO2 object: getParts()){
            object.setBoundingBoxColor(color);
        }
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
    public void setLocalVectorsystem(Coord3d x, Coord3d y, Coord3d z){
        this.x = x;
        this.y = y; 
        this.z = z;
    }
    
    /**
     * Set the x vector of the Local Vectorsystem
     * @param x the new x vector
     */
    public void setLocalVectorsystemX(Coord3d x){
        this.x = x;
    }
 
    /**
     * Set the y vector of the Local Vectorsystem
     * @param y the new y vector
     */
    public void setLocalVectorsystemY(Coord3d y){
        this.y = y;
    }
    
    /**
     * Set the z vector of the Local Vectorsystem
     * @param z the new z vector
     */
    public void setLocalVectorsystemZ(Coord3d z){
        this.z = z;
    }
    
    /**
     * Set the center of this part
     * @param c The center
     */
    public void setCoordCenter(Coord3d c){
        this.coordCenter = c; 
    }
    
    /**
     * Get the x vector of the local Vectorsystem 
     * @return the x vector
     */
    public Coord3d getLocalVectorsystemX(){
        return this.x;
    }
    
    /**
     * Get the y vector of the local Vectorsystem 
     * @return the y vector
     */
    public Coord3d getLocalVectorsystemY(){
        return this.y;
    }

    /**
     * Get the z vector of the local Vectorsystem 
     * @return the z vector
     */
    public Coord3d getLocalVectorsystemZ(){
        return this.z;
    }
    
    /**
     * Returns the center of the part
     * @return the center
     */
    public Coord3d getCenter(){
        return this.coordCenter;
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
    
    /**
     * Sets the name of the object
     * @param name the new name of the object
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Returns the name of the object
     * @return the name of the part
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Sets if the object is attached to a ball joint or not. (For skeleton purposes)
     * @param isBallJoint boolean value of if it is attached to a ball joint
     */
    public void setIsBallJoint(boolean isBallJoint){
        this.isBallJoint = isBallJoint;
    }
    
    /**
     * Returns if the object is attached to a ball joint or not. (For skeleton purposes)
     * @return boolean value of if it is attached to a ball joint
     */
    public boolean getIsBallJoint(){
        return this.isBallJoint;
    }
    
    /**
     * Sets the x-angle of the object (For skeleton purposes
     * @param angle the new x-angle
     */
    public void setAngleX(float angle){
        this.angleX = angle;
    }
    
    /**
     * Returns the x-angle of the object (For skeleton purposes)
     * @return the x-angle
     */
    public float getAngleX(){
        return this.angleX;
    }
    
    /**
     * Sets the y-angle of the object (For skeleton purposes
     * @param angle the new y-angle
     */
    public void setAngleY(float angle){
        this.angleY = angle;
    }
    
    /**
     * Returns the y-angle of the object (For skeleton purposes)
     * @return the y-angle
     */
    public float getAngleY(){
        return this.angleY;
    }
    
    /**
     * Sets the z-angle of the object (For skeleton purposes
     * @param angle the new z-angle
     */
    public void setAngleZ(float angle){
        this.angleZ = angle;
    }
    
    /**
     * Returns the z-angle of the object (For skeleton purposes)
     * @return the z-angle
     */
    public float getAngleZ(){
        return this.angleZ;
    }
}
