/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;

/**
 *
 * @author Nutzer
 */
public class EuclidVBO2 extends DrawableVBO2 {

    public EuclidVBO2(float[] verticesFloat, int i) {
        super(verticesFloat, i);
    }
    
    public float[] rotateAroundVector(Rotate r){
        float[] newVertices = new float[this.getVertices().capacity()];
        for(int i = 0; i < this.getVertices().capacity()-2; i = i+3){
            Coord3d newCoord = new Coord3d(getVertices().get(i),getVertices().get(i+1),getVertices().get(i+2));
            newCoord = r.compute(newCoord);
            newVertices[i] = newCoord.x;
            newVertices[i+1] = newCoord.y;
            newVertices[i+2] = newCoord.z;
        }
        return newVertices;
    }
    
}
