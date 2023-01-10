/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.pickable.PickableSphere;
import org.jzy3d.plot3d.text.drawable.DrawableText;

/**
 *
 * @author Nutzer
 */
public class EuclidSphere extends Composite implements Pickable, PickableObjects{
    
    private PickableSphere sphere;
    private DrawableText label;
    private int pickingId; 

    public void setData(Point3d position, float radius, int slicing, Color color, String label, Point3d labelLocation){
        sphere = new PickableSphere(new Coord3d(position.x,position.y,position.z),radius,slicing,color);
        this.add(sphere);
        this.label = LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK);
        this.add(this.label);
        setNewPosition(new Coord3d(position.x,position.y,position.z));
    }
    
    @Override
    public DrawableTypes getType() {
        return DrawableTypes.SPHERE;
    }
    
    @Override
    public void setNewPosition(Coord3d position){
        sphere.setPosition(position);
        label.setPosition(new Coord3d(position.x,position.y,position.z-LabelFactory.getInstance().getOffset()));
    }

    @Override
    public void setPickingId(int i) {
       this.pickingId = i;
    }

    @Override
    public int getPickingId() {
        return this.pickingId;
    }

    @Override
    public Coord3d getPosition() {
        return sphere.getPosition();
    }
    
}
