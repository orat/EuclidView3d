/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.orat.math.view.euclidview3d;

import org.jogamp.vecmath.Vector4f;

/**
 *
 * @author Nutzer
 */
public class Material {
    
    private Vector4f ambient;
    private Vector4f diffuse;
    private Vector4f specular;
    private float alpha;
    
    public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, float alpha){
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.alpha = alpha;
    }
    
    public Vector4f getAmbient(){
        return this.ambient;
    }
    
    public Vector4f getDiffuse(){
        return this.diffuse;
    }
    
    public Vector4f getSpecular(){
        return this.specular;
    }
    
    public float getAlpha(){
        return this.alpha;
    }
    
    public void setAmbient(Vector4f ambient){
        this.ambient = ambient;
    }
    
    public void setDiffuse(Vector4f diffuse){
        this.diffuse = diffuse;
    }
    
    public void setSpecular(Vector4f specular){
        this.specular = specular;
    }
    
    public void setAlpha(float alpha){
        this.alpha = alpha;
    }
}
