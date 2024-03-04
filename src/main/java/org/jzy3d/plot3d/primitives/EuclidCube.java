package org.jzy3d.plot3d.primitives;

import org.jogamp.vecmath.Point3d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.pickable.PickableSphere;
import org.jzy3d.plot3d.text.drawable.DrawableText;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2019, 2023
 * 
 * TODO
 * - labels ungetested
 * - pickable ungetested
 */
public class EuclidCube extends Composite implements Pickable, PickableObjects{	
     
    private PickableSphere sphere;
    private DrawableText label;
    private int pickingId; 

    protected Quad front;
    protected Quad left;
    protected Quad right;
    protected Quad bottom;
    protected Quad top;
    protected Quad back;
    
    public void setData(Point3d origin, float height, float width, float depth,
            Color color, String label, Point3d labelLocation) {
        
        front = new Quad();   
        left = new Quad();   
        right = new Quad();   
        bottom = new Quad();   
        top = new Quad();   
        back = new Quad(); 

        Coord3d back_top_left = new Coord3d();
        back_top_left.add(((float) origin.x)-(depth/2),((float) origin.y)-(width/2), 
                ((float) origin.z)+(height/2));
        Point p_btl;
        p_btl = new Point(back_top_left, Color.GRAY);

        Coord3d back_top_right = new Coord3d();
        back_top_right.add(((float) origin.x)-(depth/2),((float) origin.y)+(width/2), 
                ((float) origin.z)+(height/2));
        Point p_btr;
        p_btr = new Point(back_top_right,Color.GRAY);

        Coord3d back_bottom_left = new Coord3d();
        back_bottom_left.add(((float) origin.x)-(depth/2),((float) origin.y)-(width/2), 
                ((float) origin.z)-(height/2));
        Point p_bbl;
        p_bbl = new Point(back_bottom_left,Color.GRAY);

        Coord3d back_bottom_right = new Coord3d();
        back_bottom_right.add(((float) origin.x)-(depth/2),((float) origin.y)+(width/2), 
                ((float) origin.z)-(height/2));
        Point p_bbr;
        p_bbr = new Point(back_bottom_right,Color.GRAY);

        Coord3d front_top_left = new Coord3d();
        front_top_left.add(((float) origin.x)+(depth/2),((float) origin.y)-(width/2), 
                ((float) origin.z)+(height/2));
        Point p_ftl;
        p_ftl = new Point(front_top_left,Color.GRAY);

        Coord3d front_top_right = new Coord3d();
        front_top_right.add(((float) origin.x)+(depth/2),((float) origin.y)+(width/2), 
                ((float) origin.z)+(height/2));
        Point p_ftr;
        p_ftr = new Point(front_top_right, Color.GRAY);

        Coord3d front_bottom_left = new Coord3d();
        front_bottom_left.add(((float) origin.x)+(depth/2),((float) origin.y)-(width/2), 
                ((float) origin.z)-(height/2));
        Point p_fbl;
        p_fbl = new Point(front_bottom_left,Color.GRAY);

        Coord3d front_bottom_right = new Coord3d();
        front_bottom_right.add(((float) origin.x)+(depth/2),((float) origin.y)+(width/2), 
                ((float) origin.z)-(height/2));
        Point p_fbr;
        p_fbr = new Point(front_bottom_right,Color.GRAY);

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

        // Color.GRAY
        back.setColor(color);
        front.setColor(color);
        left.setColor(color);
        right.setColor(color);
        top.setColor(color);
        bottom.setColor(color);

        add(back);
        add(front);
        add(left);
        add(right);
        add(top);
        add(bottom);
        
        if (label != null){
            this.label = LabelFactory.getInstance().addLabel(labelLocation, label, Color.BLACK);
            this.add(this.label);
        }
        setNewPosition(new Coord3d(origin.x,origin.y,origin.z));
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
    public DrawableTypes getType() {
        return DrawableTypes.SPHERE;
    }

    @Override
    public void setNewPosition(Coord3d position) {
       sphere.setPosition(position);
       label.setPosition(new Coord3d(position.x,position.y,position.z-LabelFactory.getInstance().getOffset()));
    }

    @Override
    public Coord3d getPosition() {
        return sphere.getPosition();
    }
}