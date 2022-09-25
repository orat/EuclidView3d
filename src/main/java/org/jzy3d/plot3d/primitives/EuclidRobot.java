/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import de.orat.math.view.euclidview3d.ColladaLoader;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils2;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;

/**
 *
 * Exept for the Constructor, setData and addToChart the Transforming operations have to be executed after the init, 
 * because the Robot consists off multiple VBO objects, which can only be transfromed after the initialisation. 
 * 
 * @author Dominik Scharnagl
 */
public class EuclidRobot extends Composite{
    
    private ArrayList<EuclidRobotPart> parts;
    private Chart chart = null;
    
    /**
     * Creat a Robot
     * @param chart the chart to which the robot should be added.
     */
    public EuclidRobot(Chart chart){
        super();
        this.chart = chart;
    }
    
    /**
     * Set the Data for the Robot
     * @param componentsPaths the paths to the .dae Files
     */
    public void setData(List<String> componentsPaths){
        parts = new ArrayList<EuclidRobotPart>();
        ColladaLoader loader = new ColladaLoader();
        for(String path: componentsPaths){
            parts.add(loader.getCOLLADA(path));
        }
    } 
    
    /**
     * Add the Robot to the Chart
     */
    public void addToChart(){
        for(EuclidRobotPart robotPart: parts){
             robotPart.drawRobotPart(chart);
        }
    }
    
    /**
     * Rotate the Coordinate System to have the Z-Vector up top.
     */
    public void rotateCoordSystem(){
        for(EuclidRobotPart robotPart: parts){
            for(DrawableVBO2 object: robotPart.getParts()){
                Transform trans = new Transform();
                Rotate rotate = createRotateTo(new Coord3d(0,1,0), new Coord3d(0,0,1));
                trans.add(rotate);
                object.setTransform(trans);
                object.setTransformBefore(trans);
                object.applyGeometryTransform(object.getTransform());
            }
        }
        chart.getScale().setMax(2000);
        chart.getScale().setMin(-2000);
    }
    
    /**
     * Create a Rotation from one point to another
     * @param from 
     * @param to
     * @return The Rotation
     */
    private static Rotate createRotateTo(Coord3d from, Coord3d to){
        double fromMag =  (float) Math.sqrt(from.x * from.x + from.y * from.y + from.z * from.z);
        double toMag =  (float) Math.sqrt(to.x * to.x + to.y * to.y + to.z * to.z);
        double angle = Math.acos(from.dot(to)/(fromMag*toMag))*180f/Math.PI;
        //System.out.println(angle);
        Coord3d v = Utils2.cross(from,to);
        v.normalizeTo(1);
        return new Rotate(angle, v);
    }
    
    /**
     * Returns the Chart on which the Robot is located
     * @return the Chart
     */
    public Chart getChart(){
        return this.chart;
    }
    
}
