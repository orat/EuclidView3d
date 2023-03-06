/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class to create the HashMap for a skeleton to know which parts are attached to each other. So that EuclidSkeleton is not so bloated with code.
 * @author Dominik Scharnagl
 */
public class EuclidSkeletonSetup {
    
    private static HashMap<String, List<String>> attached = new HashMap<String, List<String>>();
    private static List<String> list = new ArrayList<String>();
    
     /**
     * Set up a hashmap to know which part is attached to which.
     */
    public static HashMap<String, List<String>> setUpAttached(){
        attached = new HashMap<String, List<String>>();
        //Upper Body
        //thorax
        list = new ArrayList<String>();
        list.add("head");
        list.add("righthumerus");
        list.add("rightradius");
        list.add("righthand");
        list.add("lefthumerus");
        list.add("leftradius");
        list.add("lefthand");
        list.add("leftclavicle");
        list.add("rightclavicle");
        attached.put("thorax", list);
        //clavicle
        list = new ArrayList<String>();
        list.add("righthumerus");
        list.add("rightradius");
        list.add("righthand");
        attached.put("rightclavicle", list);
        list = new ArrayList<String>();
        list.add("lefthumerus");
        list.add("leftradius");
        list.add("lefthand");
        attached.put("leftclavicle", list);
        //humerus
        list = new ArrayList<String>();
        list.add("rightradius");
        list.add("righthand");
        attached.put("righthumerus", list);
        list = new ArrayList<String>();
        list.add("leftradius");
        list.add("lefthand");
        attached.put("lefthumerus", list);
        //radius
        list = new ArrayList<String>();
        list.add("righthand");
        attached.put("rightradius", list);
        list = new ArrayList<String>();
        list.add("lefthand");
        attached.put("leftradius", list);
        
        //Lower Body
        //femur
        list = new ArrayList<String>();
        list.add("righttibia");
        list.add("rightfoot");
        attached.put("rightfemur", list);
        list = new ArrayList<String>();
        list.add("lefttibia");
        list.add("leftfoot");
        attached.put("leftfemur", list);
        //tibia
        list = new ArrayList<String>();
        list.add("rightfoot");
        attached.put("righttibia", list);
        list = new ArrayList<String>();
        list.add("leftfoot");
        attached.put("lefttibia", list);
        
        return attached;
    }
}
