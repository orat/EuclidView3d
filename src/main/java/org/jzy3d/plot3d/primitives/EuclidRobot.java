/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import de.orat.math.view.euclidview3d.ColladaLoader;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;

/**
 *
 * @author Nutzer
 */
public class EuclidRobot extends Composite{
    
    private ArrayList<Composite> parts;
    
    public EuclidRobot(){
        super();
    }
    
    public void setData(List<String> componentsPaths){
        parts = new ArrayList<Composite>();
        ColladaLoader loader = new ColladaLoader();
        System.out.println(componentsPaths.size());
        parts.add(loader.getCOLLADA(componentsPaths.get(0)));
        for(String path: componentsPaths){
            parts.add(loader.getCOLLADA(path));
        }
    } 
    
    public void addToChart(Chart chart){
        for(Composite c: parts){
            chart.add(c);
        }
    }
    
}
