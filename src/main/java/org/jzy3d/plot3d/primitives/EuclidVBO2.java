/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Translate;

/**
 *
 * @author Nutzer
 */
public class EuclidVBO2 extends DrawableVBO2 {

    public EuclidVBO2(float[] verticesFloat, int i) {
        super(verticesFloat, i);
    }
    
    /**
     * Translate the indices of the vbo
     * @param translate the translation
     * @return the translated indices to create a new VBO object
     */
    public float[] translate(Translate translate){
        float[] newVertices = new float[this.getVertices().capacity()];
        for(int i = 0; i < this.getVertices().capacity()-2; i = i+3){
            Coord3d newCoord = new Coord3d(getVertices().get(i),getVertices().get(i+1),getVertices().get(i+2));
            newCoord = translate.compute(newCoord);
            newVertices[i] = newCoord.x;
            newVertices[i+1] = newCoord.y;
            newVertices[i+2] = newCoord.z;
        }
        return newVertices;
    }
    
    /**
     * Rotate the indices of the vbo
     * @param rotate the rotation
     * @return the rotated indices to create a new VBO object
     */
    public float[] rotate(Rotate rotate){
        float[] newVertices = new float[this.getVertices().capacity()];
        for(int i = 0; i < this.getVertices().capacity()-2; i = i+3){
            Coord3d newCoord = new Coord3d(getVertices().get(i),getVertices().get(i+1),getVertices().get(i+2));
            newCoord = rotate.compute(newCoord);
            newVertices[i] = newCoord.x;
            newVertices[i+1] = newCoord.y;
            newVertices[i+2] = newCoord.z;
        }
        return newVertices;
    }
    
}
