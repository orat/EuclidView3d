/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import de.orat.math.view.euclidview3d.ObjectLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author Nutzer
 */
public class EuclidSkeleton {
    
    private ArrayList<EuclidPart> parts;
    private ArrayList<String> nameParts;
    private Chart chart;
    private Coord3d x = new Coord3d(1,0,0);
    private Coord3d y = new Coord3d(0,1,0);
    private Coord3d z = new Coord3d(0,0,1);
    private HashMap<String, List<String>> attached = new HashMap<String, List<String>>();
    
    public EuclidSkeleton(String waveFrontPath, Chart chart){
        parts = new ArrayList<EuclidPart>();
        this.chart = chart;
        List<EuclidVBO2> partList = ObjectLoader.getLoader().getWavefront(waveFrontPath).getParts();
        for(int i = 0; i < partList.size(); i++){
            ArrayList<EuclidVBO2> l = new ArrayList<EuclidVBO2>();
            l.add(partList.get(i));
            parts.add(new EuclidPart(l));
        }
    }
    
    public EuclidSkeleton(String waveFrontPath, String xmlPath, Chart chart){
        Coord3d x = new Coord3d(1,0,0);
        Coord3d y = new Coord3d(0,1,0);
        Coord3d z = new Coord3d(0,0,1);
        parts = new ArrayList<EuclidPart>();
        nameParts = new ArrayList<String>();
        this.chart = chart;
        //List<EuclidVBO2> partList = ObjectLoader.getLoader().getWavefront(waveFrontPath).getParts();
        List<EuclidVBO2> partList = ObjectLoader.getLoader().getWavefront(waveFrontPath, nameParts).getParts();
        for(int i = 0; i < nameParts.size(); i++){
            nameParts.set(i, nameParts.get(i).toLowerCase());
        }
        for(int i = 0; i < partList.size(); i++){
            ArrayList<EuclidVBO2> l = new ArrayList<EuclidVBO2>();
            l.add(partList.get(i));
            parts.add(new EuclidPart(l));
            parts.get(i).setLocalVectorsystem(x, y, z);
            parts.get(i).setName(nameParts.get(i));
        }
        attached = EuclidSkeletonSetup.setUpAttached();
    }
    
    /**
     * Set the Chart of this skeleton
     * @param chart The new Chart of the skeleton
     */
    public void setChart(Chart chart){
        clearFromChart();
        this.chart = chart;
        drawOnChart();
    }
    
    /**
     * Return the chart the skeleton is currently on
     * @return The chart of the skeleton
     */
    public Chart getChart(){
        return this.chart;
    }
    
    /**
     * Clear the skeleton from the chart
     */
    public void clearFromChart(){
        for(EuclidPart part: parts){
            part.clearFromChart();
        }
    }
    
    /**
     * Draw the skeleton to the chart
     */
    public void drawOnChart(){
       for(EuclidPart part: parts){
            part.drawPart(chart);
        } 
    }
    
    /**
     * Removes a single part from the chart
     * @param i the number of the part in the list
     */
    public void remove(int i){
        chart.remove(parts.get(i).getParts().get(0));
    }
    
    /**
     * Set up the skeleton on the chart. Use the thorax as the center at (0,0,0)
     */
    public void setUpSkeleton(){
        rotateParts();
        setPartsToZero();
        translateParts();
        setUpCenters();
    }
    
    /**
     * Translate all the skeleton Parts. Use the thorax as the center of the object.
     */
    private void translateParts(){
        for(EuclidPart part: parts){
            if(part.getName().equals("head")){
                part.translateAlongVector(1.44f, new Coord3d(0,0,1));
            }
            //Clavicile
            else if(part.getName().equals("rightclavicle")||part.getName().equals("leftclavicle")){
                part.translateAlongVector(0.7f, z);
                part.translateAlongVector(0.3f, x);
                if(part.getName().equals("leftclavicle")){
                    part.translateAlongVector(0.5f, y);
                }else{
                    part.translateAlongVector(-0.5f, y);
                }
            }
            //Humerus
            else if(part.getName().equals("righthumerus")||part.getName().equals("lefthumerus")){
                part.translateAlongVector(0.13f, z);
                part.translateAlongVector(0.3f, x);
                if(part.getName().equals("lefthumerus")){
                    part.translateAlongVector(0.92f, y);
                }else{
                    part.translateAlongVector(-0.92f, y);
                }
            }
            //radius
            else if(part.getName().equals("rightradius")||part.getName().equals("leftradius")){
                part.translateAlongVector(-0.9f, z);
                part.translateAlongVector(0.28f, x);
                if(part.getName().equals("leftradius")){
                    part.translateAlongVector(0.92f, y);
                }else {
                    part.translateAlongVector(-0.92f, y);
                }
            }
            //hands
            else if(part.getName().equals("righthand")||part.getName().equals("lefthand")){
                part.translateAlongVector(-1.98f, z);
                part.translateAlongVector(0.31f, x);
                if(part.getName().equals("lefthand")){
                    part.translateAlongVector(0.9f, y);
                }else{
                    part.translateAlongVector(-0.9f, y);
                }
            }
            //pelvis
            else if(part.getName().equals("pelvis")){
                part.translateAlongVector(-1.5f, z);
            }
            //femur
            else if(part.getName().equals("rightfemur")||part.getName().equals("leftfemur")){
                part.translateAlongVector(-2.2f, z);
                part.translateAlongVector(0.15f, x);
                if(part.getName().equals("leftfemur")){
                    part.translateAlongVector(0.52f, y);
                }else {
                    part.translateAlongVector(-0.52f, y);
                }
            }
            //tibia
            else if(part.getName().equals("righttibia")||part.getName().equals("lefttibia")){
                part.translateAlongVector(-3.25f, z);
                part.translateAlongVector(0.14f, x);
                if(part.getName().equals("lefttibia")){
                    part.translateAlongVector(0.5f, y);
                }else {
                    part.translateAlongVector(-0.5f, y);
                }
            }
            else if(part.getName().equals("leftfoot")||part.getName().equals("rightfoot")){
                part.translateAlongVector(-3.93f, z);
                part.translateAlongVector(0.35f, x);
                if(part.getName().equals("leftfoot")){
                    part.translateAlongVector(0.55f, y);
                }else {
                    part.translateAlongVector(-0.55f, y);
                }
            }
        }
    }
     
    /**
     * Move every part to the center.
     */
    private void setPartsToZero(){
         for(EuclidPart part: parts){
             part.translateAlongVector(part.getParts().get(0).getBounds().getCenter().x, x.negative());
             part.translateAlongVector(part.getParts().get(0).getBounds().getCenter().y, y.negative());
             part.translateAlongVector(part.getParts().get(0).getBounds().getCenter().z, z.negative());
         }
    }
    
    /**
     * Rotate the parts for setup
     */
    private void rotateParts(){
        for(EuclidPart part: parts){
           if(part.getName().equals("thorax")){
               part.rotateAroundVector(180, y);
               part.rotateAroundVector(180, z);
           }
           if(part.getName().equals("rightclavicle")){
               part.rotateAroundVector(-90, x);
           } 
           if(part.getName().equals("leftclavicle")){
                part.rotateAroundVector(90, x);
           }
           if(part.getName().equals("rightradius")||part.getName().equals("leftradius")){
               part.rotateAroundVector(180, z);
           }
           if(part.getName().equals("righthand")){
               part.rotateAroundVector(-90, z);
           }
           if(part.getName().equals("lefthand")){
               part.rotateAroundVector(90, z);
           }
           if(part.getName().equals("leftfoot")||part.getName().equals("rightfoot")){
               part.rotateAroundVector(-90, y);
           }
        }
    }
    
    private void setUpCenters(){
        for(EuclidPart part: parts){
            BoundingBox3d bounds = part.getParts().get(0).getBounds();
            float z = bounds.getZmax();
            float y = bounds.getYmin() + (bounds.getYmax()-bounds.getYmin())/2;
            float x = bounds.getXmin() + (bounds.getXmax() - bounds.getXmin())/2;
            //head
            if(part.getName().equals("head")){
                z = bounds.getZmin() + 0.22f;
            }
            //thorax
            if(part.getName().equals("thorax")){
                z = bounds.getZmin();
            }
            //clavicle
            if(part.getName().equals("rightclavicle")||part.getName().equals("leftclavicle")){
                z = bounds.getZmin() + (bounds.getZmax() - bounds.getZmin())/2;
                if(part.getName().equals("rightclavicle")){
                    y = bounds.getYmax();
                }else {
                    y =  bounds.getYmin();
                }
            }
            //radius
            if(part.getName().equals("righthand")||part.getName().equals("lefthand")){
                float handoffset = 0.05f;
                if(part.getName().equals("righthand")){
                    y = bounds.getYmin() + handoffset;
                }else{
                    y = bounds.getYmax() - handoffset; 
                }
            }
            //foot
            if(part.getName().equals("rightfoot")||part.getName().equals("leftfoot")){
                float x_div = 3.3f;
                float y_div = 1.5f;
                if(part.getName().equals("rightfoot")){
                    x = bounds.getXmin() + (bounds.getXmax() - bounds.getXmin())/x_div;
                    y = bounds.getYmin() + (bounds.getYmax()-bounds.getYmin())/y_div;
                }else{
                    x = bounds.getXmin() + (bounds.getXmax() - bounds.getXmin())/x_div; 
                    y = bounds.getYmax() - (bounds.getYmax()-bounds.getYmin())/y_div;
                }
            }
            part.setCoordCenter(new Coord3d(x,y,z));
        }
    }
    
    public void setBoundingBoxColor(Color color){
        for(EuclidPart part: parts){
            part.setBoundingBoxColor(color);
        }
    }
    
    public void setBoundingBoxDisplayed(boolean isDisplayed){
        for(EuclidPart part: parts){
            part.setBoundingBoxDisplayed(isDisplayed);
        }
    }
    
    public EuclidPart getPart(String partString){
        for(EuclidPart part: parts){
            if(part.getName().equals(partString)){
                return part;
            }
        }
        return null;
    }
    
    public void rotate(String partString, float angle, Coord3d vector, Coord3d center){
        //get all Strings of the parts which have to be rotated
        List<String> partsString = new ArrayList<String>();
        partsString.add(partString);
        if(attached.containsKey(partString)){
            partsString.addAll(attached.get(partString));
        }
        //get all parts which have to be rotated
        List<EuclidPart> partsList = new ArrayList<EuclidPart>();
        for(String s: partsString){
            for(EuclidPart part: parts){
                if(part.getName().equals(s)){
                    partsList.add(part);
                }
            }
        }
        //rotate parts
        for(EuclidPart part: partsList){
            part.rotateAroundVector2(angle, vector, center);
            Coord3d newX = part.getLocalVectorsystemX();
            Coord3d newY = part.getLocalVectorsystemY();
            Coord3d newZ = part.getLocalVectorsystemZ();
            newX = part.getLocalVectorsystemX().rotate(angle, vector);
            newY = part.getLocalVectorsystemY().rotate(angle, vector);
            newZ = part.getLocalVectorsystemZ().rotate(angle, vector);
            part.setLocalVectorsystemX(newX);
            part.setLocalVectorsystemY(newY);
            part.setLocalVectorsystemZ(newZ);

        }
        
    }
    public List<EuclidPart> getParts(){
            return this.parts;
    }
}
