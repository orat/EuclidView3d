package de.orat.math.view.euclidview3d;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.controllers.mouse.picking.IObjectPickedListener;
import org.jzy3d.chart.controllers.mouse.picking.NewtMousePickingController;
import org.jzy3d.chart.controllers.mouse.picking.PickingSupport;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.chart.factories.NewtChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils2;
import org.jzy3d.painters.IPainter;
import org.jzy3d.plot3d.primitives.Arrow;
import org.jzy3d.plot3d.primitives.CroppableLineStrip;
import org.jzy3d.plot3d.primitives.DrawableTypes;
import org.jzy3d.plot3d.primitives.EuclidPlane;
import org.jzy3d.plot3d.primitives.EuclidSphere;
import org.jzy3d.plot3d.primitives.Line;
import org.jzy3d.plot3d.primitives.PickableObjects;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.text.drawable.DrawableText;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryView3d extends AbstractAnalysis {

    //private Chart chart;
    private final float labelOffset = 0.5f;
    private int pickingId = 0;
    private ArrayList<PickableObjects> pickableObjects = new ArrayList();
    private ArrayList<PickableObjects> pickingSupportList = new ArrayList();
    private PickingSupport pickingSupport;
    private NewtCameraMouseController cameraMouse;
    
    /**
     * Constructor for a GeometryView3d to get created by a NewtChartFactory.
     */
    public GeometryView3d(){
        super(new NewtChartFactory());
    }
    
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new GeometryView3d());
        //GeometryView3d viewer = new GeometryView3d();
        //viewer.open();
    }
    
    /**
     * Add a point to the 3d view.
     * 
     * @param location locattion of the point
     * @param color color of the point
     * @param width width of the point
     * @param label the text of the label of the point
     */
    public void addPoint(Point3d location, Color color, float width, String label){
        //double radius = 0.6;
        EuclidSphere sphere = new EuclidSphere(new Coord3d(location.x,location.y,location.z),(float) (width/2), 20, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeDisplayed(false);
        sphere.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(sphere, sphere);
        //Point point = new Point(new Coord3d(location.x,location.x,location.z), color, width);
        chart.add(sphere);
        pickingSupportList.add(sphere);
        Point3d labelLocation = new Point3d(location.x, location.y,location.z - (width/2) - labelOffset);
        addLabel(labelLocation,label, Color.BLACK);
    }
    
    /**
     * Add a sphere to the 3d view.
     * 
     * @param location
     * @param squaredSize
     * @param color 
     * @param label the text of the label of the sphere
     */
    public void addSphere(Point3d location, double squaredSize, Color color, String label){
        EuclidSphere sphere = new EuclidSphere(new Coord3d(location.x,location.x,location.z),
                (float) Math.sqrt(Math.abs(squaredSize)),10, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeColor(Color.BLACK);
        sphere.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(sphere, sphere);
        chart.add(sphere);
        pickingSupportList.add(sphere);
        Point3d labelLocation = new Point3d(location.x,location.y, location.z - Math.sqrt(Math.abs(squaredSize)) - labelOffset);
        addLabel(labelLocation, label, Color.BLACK);
    }
  
    /**
     * Add a line to the 3d view.
     * 
     * @param attitude
     * @param location
     * @param color
     * @param radius
     * @param length weglassen und die Länge anhand des Volumens der view bestimmen
     * @param label
     */
    public void addLine(Vector3d attitude, Point3d location, Color color, float radius, float length, String label){
        addLine(location, 
            new Point3d(location.x+attitude.x*length, 
                        location.y+attitude.y*length, 
                        location.z+attitude.z*length), radius, color, label);
    }
    
    /**
     * Add a line to the 3d view.
     * 
     * @param p1 start point of the cylinder
     * @param p2 end point of the cylinder
     * @param radius radius of the cylinder
     * @param color color of the line
     * @param label the label of the line
     */
    public void addLine(Point3d p1, Point3d p2, float radius, Color color, String label){
        p1 = clipPoint(p1);
        p2 = clipPoint(p2);
        org.jzy3d.maths.Vector3d vec = 
                new org.jzy3d.maths.Vector3d(new Coord3d(p1.x,p1.y,p1.z), new Coord3d(p2.x, p2.y, p2.z));
        Line line = new Line();
        line.setData(p1,p2, radius, 10, 0, color);
        line.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(line, line);
        chart.add(line);
        pickingSupportList.add(line);
        Vector3d negative_direction = new Vector3d(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
        negative_direction.scale((2*labelOffset)/negative_direction.length());
        Point3d labelLocation = new Point3d(p1.x+negative_direction.x, p1.y+negative_direction.y,p1.z+negative_direction.z);
        addLabel(labelLocation, label, Color.BLACK);
    }
    
    /**
     * Clips a point.
     * 
     * Projection of the point to the bounding box.
     * 
     * @param point The point which should be clipped
     * @return the clipped point
     */
    public Point3d clipPoint(Point3d point){
        BoundingBox3d bounds = chart.getView().getAxis().getBounds();
        if (point.x < bounds.getXmin()){
            point.x = bounds.getXmin();
        } else if (point.x > bounds.getXmax()){
            point.x = bounds.getXmax();
        }
        if (point.y < bounds.getYmin()){
            point.y = bounds.getYmin();
        } else if (point.y > bounds.getYmax()){
            point.y = bounds.getYmax();
        } 
        if (point.z < bounds.getZmin()){
            point.z = bounds.getZmin();
        } else if (point.z > bounds.getZmax()){
            point.z = bounds.getZmax();
        }
        return point;
    }
    
    
    /**
     * add circle to the 3d view.
     * 
     * @param origin origin of the circle
     * @param direction normal vector of the plane the circle lays in
     * @param radius radius of the circle
     * @param color color of the circle
     * @param label
     */
    public void addCircle(Point3d origin, Vector3d direction, float radius ,Color color, String label){
        float rings = 100.f;
        CroppableLineStrip lineStrip = new CroppableLineStrip();
        //get the orthogonal vectors to the direction to get the plane for the circle
        direction.normalize();
        Vector3d[] plane = getOrthogonalsToDirection(direction);
        Coord3d p1 = new Coord3d(origin.x+plane[1].x, origin.y+plane[1].y, origin.z+plane[1].z);
        //Calculate the first point from the circle. Scale the vector between the origin and a point p1 on the plane to the radius.
        Vector3d vec_p1_origin = new Vector3d(p1.x-origin.x, p1.y-origin.y, p1.z-origin.z);
        double length = vec_p1_origin.length();
        float ratio = (float) radius/ (float) length;
        vec_p1_origin.scale(ratio);
        //rotate the first point around the direction and the points to the strip
        Coord3d firstPoint = new Coord3d(origin.x+vec_p1_origin.x, origin.y+vec_p1_origin.y, origin.z+vec_p1_origin.z);
        Coord3d rotateAround = new Coord3d(direction.x, direction.y, direction.z);
        float rotationStep = 360.f/rings;
        float degree_now = 0.f;
        for (int i=0;i<rings;i++){
            lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
            degree_now += rotationStep;
        }
        lineStrip.add(firstPoint.rotate(degree_now, rotateAround));
        lineStrip.setWireframeColor(color);
        chart.add(lineStrip);
        Vector3d origin_firstPoint = new Vector3d(origin.x+firstPoint.x, origin.y+firstPoint.y, origin.z+firstPoint.z);
        ratio = (float) (labelOffset+origin_firstPoint.length())/(float) origin_firstPoint.length();
        vec_p1_origin.scale(ratio);
        Point3d labelLocation = new Point3d(p1.x+vec_p1_origin.x, p1.y+vec_p1_origin.y,p1.z+vec_p1_origin.z);
        addLabel(labelLocation, label, Color.BLACK);
    }
    
    /**Calculates the orthogonal vectors to a normalized vector
     * 
     * @param direction the vector to which the orthogonal vectors should be calculated
     * @return the orthogonal basis with the direction and its 2 orthogonal vectors
     */
    private Vector3d[] getOrthogonalsToDirection(Vector3d direction){
        Vector3d[] orthogonals = new Vector3d[3];
        orthogonals[0] = direction;
        int smalest = 0;
        float smalest_value = (float) direction.x; 
        if (smalest_value > direction.y){
            smalest = 1; 
            smalest_value = (float) direction.y;
        }
        if (smalest_value > direction.z){
            smalest = 2; 
            smalest_value = (float) direction.z;
        }
        
        //FIXME
        // smalest_value not used
        
        Vector3d w = switch (smalest) {
            case 0 -> new Vector3d(1,0,0);
            case 1 -> new Vector3d(0,1,0);
            default -> new Vector3d(0,0,1);
        };
        Vector3d u = new Vector3d(0,0,0);
        u.cross(w, direction);
        orthogonals[1] = u;
        Vector3d v = new Vector3d(0,0,0);
        v.cross(direction, u);
        orthogonals[2] = v;
        return orthogonals;
    }
    
    /**
     * Add an arrow to the 3d view.
     * 
     * @param location midpoint of the arrow
     * @param direction direction of the arrow
     * @param length length of the arrow
     * @param radius radius of the arrow
     * @param color color of the arrow
     * @param label the text of the label of the arrow
     */
    public void addArrow(Point3d location, Vector3d direction, float length, float radius, Color color, String label){
        Arrow arrow = new Arrow();
        arrow.setData(Utils2.createVector3d(new Coord3d(location.x,location.y,location.z), 
                    new Coord3d(direction.x,direction.y,direction.z), length), radius,10,0, color);
        arrow.setWireframeDisplayed(false);
        arrow.setPickingId(pickingId++);
        pickingSupport.registerPickableObject(arrow, arrow);
        chart.add(arrow);
        pickingSupportList.add(arrow);
        Point3d labelLocation = new Point3d(location.x, location.y - radius - labelOffset, location.z);
        addLabel(labelLocation, label, Color.BLACK);
    }
    /**
     * Add a plane to the 3d view.
     * 
     * @param location first point of the plane
     * @param dir1 vector which is added to the first point to get the second point
     * @param dir2 vector which is added to the second point to get the third point and which is added to the location to get the forth point
     * @param color color of the plane
     * @param label the text of the label of the plane
     */
    public void addPlane(Point3d location, Vector3d dir1, Vector3d dir2, Color color, String label){
        location = clipPoint(location);
        Point3d p1 = new Point3d(location.x+dir1.x,location.y+dir1.y, location.z+dir1.z);
        Point3d p2 = new Point3d(location.x+dir2.x,location.y+dir2.y, location.z+dir2.z);
        p1 = clipPoint(p1);
        p2 = clipPoint(p2);
        dir1 = new Vector3d(p1.x-location.x, p1.y-location.y, p1.z-location.z);
        dir2 = new Vector3d(p2.x-location.x, p2.y-location.y, p2.z-location.z);
        EuclidPlane plane = new EuclidPlane();
        plane.setData(location, dir1, dir2, color);
        plane.setPolygonOffsetFillEnable(false);
        plane.setWireframeDisplayed(true);
        pickingSupport.registerDrawableObject(plane, plane);
        plane.setPickingId(pickingId++);
        chart.add(plane);
        pickingSupportList.add(plane);
        Coord3d lowestPoint = plane.getCoordArray()[0];
        for(Coord3d coord: plane.getCoordArray()){
            if(coord.z < lowestPoint.z){
                lowestPoint = coord;
            }
        }
        Point3d labelLocation = new Point3d(lowestPoint.x, lowestPoint.y, lowestPoint.z - labelOffset);
        addLabel(labelLocation, label, Color.BLACK);   
    }
    
    /**
     * Add a label with a text to the 3d view.
     * 
     * @param location the location of the label
     * @param text the text of the label
     * @param color color of the text
     */
    public void addLabel(Point3d location, String text, Color color){
         Coord3d coord3d = new Coord3d();
         coord3d.set((float) location.x, (float) location.y, (float) location.z);
         DrawableText label = new DrawableText(text, coord3d, color);
         chart.add(label);
    }
    
    
    /*public GeometryView3d(){
        
        
        //AWTChartFactory myfactory = new AWTChartFactory();
        //NewtChartFactory factory = new NewtChartFactory();
        //ChartFactory factory = new EmulGLChartFactory();

        // Emulgl will show limitations
        // 1-wireframe and face do not mix cleanly (polygon offset fill)
        // 2-wireframe color tend to saturate (here in green)

      
    }*/

    public void open(){
        chart.open();
        chart.addMouseCameraController(); // bessser nur addMouse() verwenden?
    }
    public void close(){
        chart.dispose();
    }

    @Override
    public void init() throws Exception {
        
        Quality q = Quality.Advanced(); 
        q.setDepthActivated(true);
        q.setAlphaActivated(true);
        q.setAnimated(false); 
        q.setHiDPIEnabled(true); 
        
        //chart = initializeChart(q);       
        
        chart = new Chart(this.getFactory(), q);
        
        //chart = myfactory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);

        
        setUpMouse();
        //Light light = chart.addLightOnCamera();
        Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
        
        /*
        addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f, "Point1");
        addSphere(new Point3d(20,20,20), 10, Color.ORANGE, "Sphere1");
        
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        
        addArrow(new Point3d(0d, 0d, 0d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        
        addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
        addCircle(new Point3d(0,0,0), new Vector3d(0,0,1),5,Color.RED, "Circle");
        
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        
        addPlane(new Point3d(0,1,5), new Vector3d(0,-10,0), new Vector3d(-10,0,0), Color.ORANGE, "ClipPlane");
        */
        addPoint(new Point3d(0,0,0), Color.BLUE, 0.6f, "Point1");
        addPoint(new Point3d(1,10,1), Color.BLUE, 0.6f, "Point3");
        addPoint(new Point3d(20,20,20), Color.BLUE, 0.6f, "Point2");    
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        addArrow(new Point3d(7d, 7d, 7d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
    }
    
    /**
     * Sets up the mouse for picking
     */
    private void setUpMouse(){
        pickingSupport = new PickingSupport();
        pickingSupport.addObjectPickedListener(new EuclidPickListener());
        NewtMouse m = new NewtMouse();
        m.setPickingSupport(pickingSupport);
        m.register(chart);
    }
    
    /**
     * The MouseController for the picking
     */
    private class NewtMouse extends NewtMousePickingController{
        
        Coord3d currentMouse = null;
        
        @Override
        public void mouseMoved(com.jogamp.newt.event.MouseEvent e){
           //So hovering over a pickable Object doesn't select it when hovering over a pickable object
        }   
        
        private float clampToOneZero(float x){
            if(x>1.f){
                return 1.f;
            } else if(x<0.f){
                return 0.f;
            }
            return x;
        }
        
        @Override
        public void mouseDragged(com.jogamp.newt.event.MouseEvent e){
            BoundingBox3d bounds = chart.getView().getBounds();          
            if (!pickableObjects.isEmpty()){
                if(e.getButton() == 1){
                    for(PickableObjects p: pickableObjects){
                        int yflip = -e.getY() + chart.getCanvas().getRendererHeight();
                        Camera camera = chart.getView().getCamera();
                        IPainter painter = chart.getPainter();
			    
                        painter.acquireGL();

                        // 2D to 3D
                        float depthRange = 0.5f;// between 0 and 1, see gluUnproject
                        if(chart.getView().getCamera().getEye().x<0){
                            depthRange = 0.66f;
                        }
                        currentMouse = new Coord3d(e.getX(), yflip, depthRange);
                
                        Coord3d pos = camera.screenToModel(chart.getPainter(), currentMouse);
                        painter.releaseGL();
                        Point3d clippedPos = clipPoint(new Point3d(pos.x,pos.y,pos.z));
                        moveObject(new Coord3d(clippedPos.x,clippedPos.y,clippedPos.z), p);
                    }
                    chart.render();
                }
            }
        }

        @Override
        public void mouseReleased(com.jogamp.newt.event.MouseEvent e){
            if (!pickableObjects.isEmpty()){
                for(PickableObjects object: pickableObjects){
                    if(object.getType().equals(DrawableTypes.PLANE)){
                        pickObject(object);
                    }
                }
                pickableObjects.clear();
                cameraMouse.register(chart);
            }    
        }
    }
    
    /**
     * The Listener on a picked object.
     */
    private class EuclidPickListener implements IObjectPickedListener {
        @Override
        public void objectPicked(List<? extends Object> list, PickingSupport ps) {
            if(!list.isEmpty()){
                pickableObjects.clear();
                for(Object o: list){
                    pickableObjects.add((PickableObjects) o);
                }
                if(cameraMouse == null){
                    cameraMouse = (NewtCameraMouseController) chart.getMouse();   
                }
                cameraMouse.unregister(chart);
            }
        }
    }
    
    /**
     * Moves a pickableObject
     * @param position the position to which the object should be moved
     * @param object the object
     */
    public void moveObject(Coord3d position, PickableObjects object){
        if(object != null){
            object.setNewPosition(position);
        }
    }
    
    /**
     * Adds a moved object to the pickingSupport.
     * @param plane the moved object
     */
    private void pickObject(PickableObjects plane){
        pickingSupport.unRegisterAllPickableObjects();
        PickableObjects removeObject = null;
        for(PickableObjects object: pickingSupportList){
            if(object.getPickingId() == plane.getPickingId()){
                removeObject = object;
            }
        }
        if(removeObject != null){
            pickingSupportList.remove(removeObject);
            pickingSupportList.add(plane);
        }
        for(PickableObjects object: pickingSupportList){
            pickingSupport.registerPickableObject((Pickable) object, (Pickable) object);
        }
    }
}
