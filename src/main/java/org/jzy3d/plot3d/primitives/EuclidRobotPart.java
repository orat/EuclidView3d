/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

/**
 *
 * @author Nutzer
 */
public class EuclidRobotPart {
    
    private List<DrawableVBO2> parts = null;
    
    public EuclidRobotPart(List<DrawableVBO2> parts){
        this.parts = parts;
    }
    
    public void drawRobotPart(Chart chart){
        for(DrawableVBO2 part: parts){
            part.setWireframeDisplayed(false);
            chart.add(part);
        }
    }
    
    public List<DrawableVBO2> getParts(){
        return this.parts;
    }
    
}
