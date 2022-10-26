/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import org.jogamp.vecmath.Matrix3d;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point4d;
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
    
    /**
     * Rotate the indices of the vbo
     * @param rotate the rotationin degrees
     * @return the rotated indices to create a new VBO object
     */
    public float[] rotateAroundVector(float d, Coord3d u, Coord3d center){
        float w = (float) Math.toRadians(d);
        double cos = Math.cos(w);
        double cos_min = (1-Math.cos(w));
        double sin = Math.sin(w);
        //Create rotation Matrix
        Matrix4d rotate = new Matrix4d(cos+Math.pow(u.x, 2)*cos_min, u.x*u.y*cos_min-u.z*sin, u.x*u.z*cos_min+u.y*sin, 0,
                                  u.y*u.x*cos_min-u.z*sin, cos+Math.pow(u.y, 2)*cos_min, u.y*u.z*cos_min-u.x*sin, 0,
                                  u.z*u.x*cos_min-u.y*sin, u.z*u.y*cos_min+u.x*sin, cos+Math.pow(u.z, 2)*cos_min, 0,
                                  0,0,0,1);
        float[] newVertices = new float[this.getVertices().capacity()];
        for(int i = 0; i < this.getVertices().capacity()-2; i = i+3){
            Point4d point = new Point4d(getVertices().get(i),getVertices().get(i+1),getVertices().get(i+2), 1);
            //Translate back to origin
            Matrix4d translate = new Matrix4d(1,0,0,-center.x,
                                              0,1,0,-center.y,
                                              0,0,1,-center.z,
                                              0,0,0,1);
            //Rotate around origin
            Point4d p = new Point4d(translate.m00*point.x + translate.m01*point.y + translate.m02*point.z + translate.m03*point.w,
                                    translate.m10*point.x + translate.m11*point.y + translate.m12*point.z + translate.m13*point.w,
                                    translate.m20*point.x + translate.m21*point.y + translate.m22*point.z + translate.m23*point.w,
                                    1);
            //Translate back to original position
            Point4d coord = new Point4d(rotate.m00 * p.x + rotate.m01 * p.y + rotate.m02 * p.z,
                                        rotate.m10 * p.x + rotate.m11 * p.y + rotate.m12 * p.z,
                                        rotate.m20 * p.x + rotate.m21 * p.y + rotate.m22 * p.z,
                                        1);
            translate.invert();
            Point4d newCoord = new Point4d(translate.m00*coord.x + translate.m01*coord.y + translate.m02*coord.z + translate.m03*coord.w,
                                    translate.m10*coord.x + translate.m11*coord.y + translate.m12*coord.z + translate.m13*coord.w,
                                    translate.m20*coord.x + translate.m21*coord.y + translate.m22*coord.z + translate.m23*coord.w,
                                    1);  
            newVertices[i] = (float) newCoord.x;
            newVertices[i+1] = (float) newCoord.y;
            newVertices[i+2] = (float) newCoord.z;
        }
        return newVertices;
    }
    
}
