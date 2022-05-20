/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.PickableSphere;

/**
 *
 * @author Nutzer
 */
public class EuclidSphere extends PickableSphere implements PickableObjects{
    
    public EuclidSphere(){
        super();
    }
    
    public EuclidSphere(Coord3d position, float radius, int slicing, Color color){
        super(position,radius,slicing,color);
    }
    
    @Override
    public DrawableTypes getType() {
        return DrawableTypes.SPHERE;
    }
    
    @Override
    public void setNewPosition(Coord3d position){
        this.setPosition(position);
    }
    
}
