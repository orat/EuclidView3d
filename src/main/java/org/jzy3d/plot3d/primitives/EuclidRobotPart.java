/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

/**One part of a Robot created from multiple DrawbaleVBO2 objects
 *
 * @author Dominik Scharnagl
 */
public class EuclidRobotPart {
    
    private List<DrawableVBO2> parts = null;
    
    /**
     * Creates a new RobotPart
     * @param parts the parts of the Robot as DrawableVBO2
     */
    public EuclidRobotPart(List<DrawableVBO2> parts){
        this.parts = parts;
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
    
}
