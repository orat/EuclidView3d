/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.text.drawable.DrawableText;

/**
 * A factory to create a label
 * @author Dominik Scharnagl
 */
public class LabelFactory {
    
    private static LabelFactory singelton;
    private final float labelOffset = 0.5f;
    
    /**
     * Private constructor for singeltons
     */
    private LabelFactory(){
        
    }
    
    /**
     * Returns the singelton instance of the label factory
     * @return the label factory singelton
     */
    public static LabelFactory getInstance(){
        if(singelton == null){
            singelton = new LabelFactory();
        }
        return singelton;
    }
    
    /**
     * Creates a new label
     * @param location the location of the label
     * @param text the text of the label
     * @param color the color of the text
     * @return the label as a text instance
     */
    public DrawableText addLabel(Point3d location, String text, Color color){
        Coord3d coord3d = new Coord3d();
        coord3d.set((float) location.x, (float) location.y, (float) location.z);
        DrawableText label = new DrawableText(text, coord3d, color);
        return label;
    }
    
    /**
     * Returns the current offset
     * @return the offset. 
     */
    public float getOffset(){
        return this.labelOffset;
    }
}
