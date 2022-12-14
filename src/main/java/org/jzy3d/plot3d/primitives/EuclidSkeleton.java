/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import de.orat.math.view.euclidview3d.ObjectLoader;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author Nutzer
 */
public class EuclidSkeleton {
    
    private ArrayList<EuclidPart> parts;
    private ArrayList<String> nameParts;
    private Chart chart;
    
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
    
     public void setUpSkeleton(){
        for(EuclidPart part: parts){
            if(part.getName().equals("head")){
                part.translateAlongVector(1.8f, new Coord3d(0,0,1));
                part.translateAlongVector(-1f, new Coord3d(1,0,0));
            }
            if(!part.getName().equals("head")&&!part.getName().equals("thorax")){
                part.translateAlongVector(-2, new Coord3d(0,0,1));
            }
        }
    }
}
