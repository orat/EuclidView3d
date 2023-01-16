/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point4d;
import org.jzy3d.io.obj.OBJFileLoader;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO2;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Translate;

/**
 * A special VBO2 object 
 * @author Dominik Scharnagl
 */
public class EuclidVBO2 extends DrawableVBO2 {

    private Matrix4d i = new Matrix4d(1,0,0,0,
                                      0,1,0,0,
                                      0,0,1,0,
                                      0,0,0,1);
    
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
     * Rotate the indices of the VBO with the Rodrigues Rotation Formula
     * @param d the angle how much it will be rotated in degree
     * @param v the vector around it will be rotated
     * @param center the center for the translation back to origin
     * @param thisCenter the center of the object which will be rotated 
     * @return 
     */
    public float[] rotateAroundVector(float d, Coord3d v, Coord3d center){
        float w = (float) Math.toRadians(d);
        double cos = Math.cos(w);
        double cos_min = (1-Math.cos(w));
        double sin = Math.sin(w);
        double l = Math.sqrt(Math.pow(v.x, 2)+Math.pow(v.y, 2) + Math.pow(v.z, 2));
        Coord3d u = new Coord3d(v.x/l, v.y/l, v.z/l);
        //Create rotation Matrix
        Matrix4d rotate = new Matrix4d(cos+Math.pow(u.x, 2)*cos_min, u.x*u.y*cos_min-u.z*sin, u.x*u.z*cos_min+u.y*sin, 0,
                                  u.y*u.x*cos_min+u.z*sin, cos+Math.pow(u.y, 2)*cos_min, u.y*u.z*cos_min-u.x*sin, 0,
                                  u.z*u.x*cos_min-u.y*sin, u.z*u.y*cos_min+u.x*sin, cos+Math.pow(u.z, 2)*cos_min, 0,
                                  0,0,0,1);
        float[] newVertices = new float[this.getVertices().capacity()];
        Matrix4d translate = new Matrix4d(1,0,0,-center.x,
                                              0,1,0,-center.y,
                                              0,0,1,-center.z,
                                              0,0,0,1);
        Matrix4d translate_inv = i;
        translate_inv.invert(translate);
        for(int i = 0; i < this.getVertices().capacity()-2; i = i+3){
            Point4d point = new Point4d(getVertices().get(i),getVertices().get(i+1),getVertices().get(i+2), 1);
            //Translate back to origin
            //Rotate around origin
            Point4d p = new Point4d(translate.m00*point.x + translate.m01*point.y + translate.m02*point.z + translate.m03*point.w,
                                    translate.m10*point.x + translate.m11*point.y + translate.m12*point.z + translate.m13*point.w,
                                    translate.m20*point.x + translate.m21*point.y + translate.m22*point.z + translate.m23*point.w,
                                    1);
            //Rotate
            Point4d coord = new Point4d(rotate.m00 * p.x + rotate.m01 * p.y + rotate.m02 * p.z,
                                        rotate.m10 * p.x + rotate.m11 * p.y + rotate.m12 * p.z,
                                        rotate.m20 * p.x + rotate.m21 * p.y + rotate.m22 * p.z,
                                        1);
            //Translate back to original position
            Point4d newCoord = new Point4d(translate_inv.m00*coord.x + translate_inv.m01*coord.y + translate_inv.m02*coord.z + translate_inv.m03*coord.w,
                                           translate_inv.m10*coord.x + translate_inv.m11*coord.y + translate_inv.m12*coord.z + translate_inv.m13*coord.w,
                                           translate_inv.m20*coord.x + translate_inv.m21*coord.y + translate_inv.m22*coord.z + translate_inv.m23*coord.w,
                                           1);
            //Set as Vertices
            newVertices[i] = (float) newCoord.x;
            newVertices[i+1] = (float) newCoord.y;
            newVertices[i+2] = (float) newCoord.z;
        } 
        return newVertices;
    }
    
    /**
     * Rotated a Center Coordinate with the Rodrigues Rotation Formula TODO: Remove code duplication from rotateAroundVector
     * @param d the angle around which it should be rotated
     * @param u the vector around it will be rotated
     * @param center the center for the translation back to origin
     * @param center2 the center which should be rotated
     * @return 
     */
    public Coord3d rotateCenter(float d, Coord3d u, Coord3d center, Coord3d center2){
            float w = (float) Math.toRadians(d);
            double cos = Math.cos(w);
            double cos_min = (1-Math.cos(w));
            double sin = Math.sin(w);
            u = u.normalizeTo(1);
            //Create rotation Matrix
            Matrix4d rotate = new Matrix4d(cos+Math.pow(u.x, 2)*cos_min, u.x*u.y*cos_min-u.z*sin, u.x*u.z*cos_min+u.y*sin, 0,
                                  u.y*u.x*cos_min+u.z*sin, cos+Math.pow(u.y, 2)*cos_min, u.y*u.z*cos_min-u.x*sin, 0,
                                  u.z*u.x*cos_min-u.y*sin, u.z*u.y*cos_min+u.x*sin, cos+Math.pow(u.z, 2)*cos_min, 0,
                                  0,0,0,1);
            Point4d point = new Point4d(center2.x,center2.y,center2.z, 1);
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
            Point4d newCenter = new Point4d(translate.m00*coord.x + translate.m01*coord.y + translate.m02*coord.z + translate.m03*coord.w,
                                    translate.m10*coord.x + translate.m11*coord.y + translate.m12*coord.z + translate.m13*coord.w,
                                    translate.m20*coord.x + translate.m21*coord.y + translate.m22*coord.z + translate.m23*coord.w,
                                    1);  
            return new Coord3d(newCenter.x, newCenter.y, newCenter.z);
    }
}
