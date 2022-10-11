/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import de.orat.math.view.euclidview3d.ColladaLoader;
import de.orat.math.view.euclidview3d.test.robot.DH;
import de.orat.math.view.euclidview3d.test.robot.TestRobot;
import static de.orat.math.view.euclidview3d.test.robot.TestRobot.determineDHAxesFromDH;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils2;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

/**
 *
 * Exept for the Constructor, setData and addToChart the Transforming operations have to be executed after the init, 
 * because the Robot consists off multiple VBO objects, which can only be transfromed after the initialisation. 
 * 
 * @author Dominik Scharnagl
 */
public class EuclidRobot{
    
    private ArrayList<EuclidRobotPart> parts;
    private Chart chart = null;
    private Color boundingBoxColor = Color.RED;
    private ArrayList<DH> dhList;
    
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
        dhList = new ArrayList<DH>();
        ColladaLoader loader = new ColladaLoader();
        for(String path: componentsPaths){
            parts.add(loader.getCOLLADA(path));
        }
    } 
    
     public void setData(List<String> componentsPaths, double[] delta_theta_rad, double[] delta_alpha_rad,double[] delta_d_m, double[] delta_r_m){
        parts = new ArrayList<EuclidRobotPart>();
        dhList = new ArrayList<DH>();
        ColladaLoader loader = new ColladaLoader();
        for(String path: componentsPaths){
            parts.add(loader.getCOLLADA(path));
        }
        double[] d_n_m_ = new double[]{0d, 162.5, 0d, 0d, 133.3, 99.7, 99.6};
        double[] a_n_m = new double[]{0d, 0d,  -425, -392.2, 0d, 0d, 0d};
        double[] alpha_n_rad = new double[]{0d, PI/2d, 0d, 0d, PI/2d, -PI/2d, 0d};
        double[] d_m = new double[7];
        double[] r_m = new double[7];
        double[] alpha_rad = new double[7];
        double[] theta_rad = new double[7];
        for (int i=0;i<7;i++){
            d_m[i] = d_n_m_[i] + delta_d_m[i];
            r_m[i] = a_n_m[i]  + delta_r_m[i];
            alpha_rad[i] = alpha_n_rad[i] + delta_alpha_rad[i];
            theta_rad[i] = delta_theta_rad[i];
        }
        /*
        TestRobot.DHAxes axes = determineDHAxesFromDH(theta_rad, alpha_rad, d_m, r_m);
        for(int i = 0; i < axes.x().length; i++){
            if( i < parts.size()){
                parts.get(i).setLocalVectorsystemX(new Coord3d(axes.x()[i].x, axes.x()[i].y, axes.x()[i].y));
                parts.get(i).setLocalVectorsystemZ(new Coord3d(axes.z()[i].x, axes.z()[i].y, axes.z()[i].y));
            }
        }
        */
        for(int i = 0; i < theta_rad.length; i++){
            dhList.add(new DH(Math.toDegrees(theta_rad[i]), Math.round(Math.toDegrees(alpha_rad[i])), d_m[i], r_m[i]));
            System.out.println(Math.toDegrees(theta_rad[i]) + " -Alpha: " + Math.round(Math.toDegrees(alpha_rad[i])) + " -D: " + d_m[i] + " -R: " + r_m[i]);
        }
    } 
     
     public void setDataDegrees(List<String> componentsPaths, double[] theta, double[] alpha,double[] d, double[] r){
        parts = new ArrayList<EuclidRobotPart>();
        dhList = new ArrayList<DH>();
        ColladaLoader loader = new ColladaLoader();
        for(String path: componentsPaths){
            parts.add(loader.getCOLLADA(path));
        }
        for(int i = 0; i < theta.length; i++){
            dhList.add(new DH(theta[i], alpha[i], d[i], r[i]));
        }
    } 
    
    /**
     * Add the Robot to the Chart
     */
    public void addToChartParts(){
        for(EuclidRobotPart robotPart: parts){
            robotPart.drawRobotPart(chart);
        }
    }
    
    public void moveDH(){
        for(int i = 1; i < 3; i++){
            for(int j = i; j < parts.size(); j++){
                 translateD(i, j);
                 rotateTheta(i, j);
                 translateR(i, j);
                 rotateAlpha(i, j);
            }
            getCoordsys();
        }
    }
    
    private void rotateTheta(int i, int j){
        if(dhList.get(i).getTheta() != 0){
            parts.get(j).rotateAroundVector((float) dhList.get(i).getTheta(), parts.get(i).getLocalVectorsystemZ());
            parts.get(j).setLocalVectorsystemX(parts.get(j).getLocalVectorsystemX().rotate((float) dhList.get(i).getTheta(), parts.get(i).getLocalVectorsystemZ()));
            parts.get(j).setLocalVectorsystemZ(parts.get(j).getLocalVectorsystemZ().rotate((float) dhList.get(i).getTheta(), parts.get(i).getLocalVectorsystemZ()));
        }
    }
    
    private void translateD(int i, int j){
        if((float) dhList.get(i).getD() != 0){
            parts.get(j).translateAlongVector((float) dhList.get(i).getD(), parts.get(i).getLocalVectorsystemZ());
            float trans_d = (float) dhList.get(i).getD();
            Coord3d along = parts.get(i).getLocalVectorsystemZ();
            Coord3d oldX = parts.get(j).getLocalVectorsystemX();
            Coord3d oldZ = parts.get(j).getLocalVectorsystemZ();
            parts.get(j).setLocalVectorsystemX(new Coord3d(oldX.x + trans_d * along.x, oldX.y + trans_d * along.y, oldX.z + trans_d * along.z));
            parts.get(j).setLocalVectorsystemZ(new Coord3d(oldZ.x + trans_d * along.x, oldZ.y + trans_d * along.y, oldZ.z + trans_d * along.z));
        }
    }
    
    private void translateR(int i, int j){
        if((float) dhList.get(i).getR() != 0){
                parts.get(j).translateAlongVector((float) dhList.get(i-1).getR(), parts.get(i-1).getLocalVectorsystemX());
                float trans_d = (float) dhList.get(i-1).getR();
            Coord3d along = parts.get(i).getLocalVectorsystemX();
            Coord3d oldX = parts.get(j).getLocalVectorsystemX();
            Coord3d oldZ = parts.get(j).getLocalVectorsystemZ();
            parts.get(j).setLocalVectorsystemX(new Coord3d(oldX.x + trans_d * along.x, oldX.y + trans_d * along.y, oldX.z + trans_d * along.z));
             parts.get(j).setLocalVectorsystemZ(new Coord3d(oldZ.x + trans_d * along.x, oldZ.y + trans_d * along.y, oldZ.z + trans_d * along.z));
        }
    }
    
    private void rotateAlpha(int i, int j){
         if((float) dhList.get(i).getAlpha() != 0){
             if(i != j){
                 parts.get(j).rotateAroundVector((float) dhList.get(i).getAlpha(), parts.get(i-1).getLocalVectorsystemX());
             }
             //parts.get(j).rotateAroundVector((float) dhList.get(i).getAlpha(), parts.get(i-1).getLocalVectorsystemX());
             parts.get(j).setLocalVectorsystemZ(parts.get(j).getLocalVectorsystemZ().rotate((float) dhList.get(i).getAlpha(), parts.get(i-1).getLocalVectorsystemX()));
             parts.get(j).setLocalVectorsystemX(parts.get(j).getLocalVectorsystemX().rotate((float) dhList.get(i).getAlpha(), parts.get(i-1).getLocalVectorsystemX()));
         }
    }
    
    /**
     * Rotate the Coordinate System to have the Z-Vector up top.
     */
    public void rotateCoordSystem(){
        for(EuclidRobotPart robotPart: parts){
            robotPart.rotateAroundVector(90, new Coord3d(1,0,0));
            robotPart.setLocalVectorsystemZ(new Coord3d(0,0,1));
        }
        chart.getScale().setMax(2000);
        chart.getScale().setMin(-2000);
    }
    
    public void getCoordsys(){
        for(int i = 0; i < parts.size(); i++){
            System.out.println("X: " + parts.get(i).getLocalVectorsystemX() + " - Z:" +  parts.get(i).getLocalVectorsystemZ());
        }
    }
 
    /**
     * Returns the Chart on which the Robot is located
     * @return the Chart
     */
    public Chart getChart(){
        return this.chart;
    }
    
    /**
     * Set the BoundingBox Color for the Robot
     * @param color The Color of the BoundingBox
     */
    public void setBoundingBoxColor(Color color){
        for(EuclidRobotPart part: parts){
            part.setBoundingBoxColor(color);
        }
    }
    
}
