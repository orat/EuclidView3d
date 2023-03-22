/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import org.jzy3d.maths.Coord3d;

/**
 * The interface for a pickable object
 * @author Dominik Scharnagl
 */
public interface PickableObjects {

    /**
     * Returns the Type of the Object
     * @return The DrawableTypes
     */
    public DrawableTypes getType();
    
    /**
     * Sets the position of this drawable. 
     * @param position The position to which the drawable should be moved.
     */
    public void setNewPosition(Coord3d position);
    
    /**
     * Returns the position of the pickable object
     * @return the position of the pickable object
     */
    public Coord3d getPosition();
    
    /**
     * Return the ID of the pickingobject
     * @return the pickingID
     */
    public int getPickingId();
    
}
