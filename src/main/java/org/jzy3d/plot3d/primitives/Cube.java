package org.jzy3d.plot3d.primitives;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point; 



/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2019
 */
public class Cube extends Composite {	
    
    protected Quad front;
    protected Quad left;
    protected Quad right;
    protected Quad bottom;
    protected Quad top;
    protected Quad back;
    
    public void drawCube(float height, float width, float depth, float center_x, float center_y, float center_z)
    {
     front = new Quad();   
     left = new Quad();   
     right = new Quad();   
     bottom = new Quad();   
     top = new Quad();   
     back = new Quad(); 
     
     //Set needed points for Cube 
     Coord3d back_top_left;
     back_top_left = new Coord3d();
     back_top_left.add(center_x-(depth/2),center_y-(width/2), center_z+(height/2));
     Point p_btl;
     p_btl = new Point(back_top_left, Color.GRAY);
     //p_btl.setCoord(back_top_left);
     
     Coord3d back_top_right;
     back_top_right = new Coord3d();
     back_top_right.add(center_x-(depth/2),center_y+(width/2), center_z+(height/2));
     Point p_btr;
     p_btr = new Point(back_top_right,Color.GRAY);
     //p_btr.setCoord(back_top_right);
     
     Coord3d back_bottom_left;
     back_bottom_left = new Coord3d();
     back_bottom_left.add(center_x-(depth/2),center_y-(width/2), center_z-(height/2));
     Point p_bbl;
     p_bbl = new Point(back_bottom_left,Color.GRAY);
     //p_bbl.setCoord(back_bottom_left);
     
     Coord3d back_bottom_right;
     back_bottom_right = new Coord3d();
     back_bottom_right.add(center_x-(depth/2),center_y+(width/2), center_z-(height/2));
     Point p_bbr;
     p_bbr = new Point(back_bottom_right,Color.GRAY);
     //p_bbr.setCoord(back_bottom_right);
     
     Coord3d front_top_left;
     front_top_left = new Coord3d();
     front_top_left.add(center_x+(depth/2),center_y-(width/2), center_z+(height/2));
     Point p_ftl;
     p_ftl = new Point(front_top_left,Color.GRAY);
     //p_ftl.setCoord(front_top_left);
     
     Coord3d front_top_right;
     front_top_right = new Coord3d();
     front_top_right.add(center_x+(depth/2),center_y+(width/2), center_z+(height/2));
     Point p_ftr;
     p_ftr = new Point(front_top_right, Color.GRAY);
     //p_ftr.setCoord(front_top_right);
     
     Coord3d front_bottom_left;
     front_bottom_left = new Coord3d();
     front_bottom_left.add(center_x+(depth/2),center_y-(width/2), center_z-(height/2));
     Point p_fbl;
     p_fbl = new Point(front_bottom_left,Color.GRAY);
     //p_fbl.setCoord(front_bottom_left);
     
     Coord3d front_bottom_right;
     front_bottom_right = new Coord3d();
     front_bottom_right.add(center_x+(depth/2),center_y+(width/2), center_z-(height/2));
     Point p_fbr;
     p_fbr = new Point(front_bottom_right,Color.GRAY);
    // p_fbr.setCoord(front_bottom_right);     
     
     back.add(p_btl);
     back.add(p_btr);
     back.add(p_bbl);
     back.add(p_bbr);

     front.add(p_btl);
     front.add(p_btr);
     front.add(p_bbl);
     front.add(p_bbr);

     bottom.add(p_fbl);
     bottom.add(p_fbr);
     bottom.add(p_bbl);
     bottom.add(p_bbr);  
     
     top.add(p_btl);
     top.add(p_btr);
     top.add(p_ftl);
     top.add(p_ftr);

     left.add(p_btl);
     left.add(p_ftl);
     left.add(p_bbl);
     left.add(p_fbl);
     
     right.add(p_ftr);
     right.add(p_btr);
     right.add(p_fbr);
     right.add(p_bbr);
     
     back.setColor(Color.GRAY);
     front.setColor(Color.GRAY);
     left.setColor(Color.GRAY);
     right.setColor(Color.GRAY);
     top.setColor(Color.GRAY);
     bottom.setColor(Color.GRAY);
     
     add(back);
     add(front);
     add(left);
     add(right);
     add(top);
     add(bottom);
     

    
    }
}