package de.orat.math.view.euclidview3d;

import java.awt.Component;
import javax.swing.JSlider;
import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector4f;
import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.controllers.mouse.picking.IObjectPickedListener;
import org.jzy3d.chart.controllers.mouse.picking.NewtMousePickingController;
import org.jzy3d.chart.controllers.mouse.picking.PickingSupport;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.chart.factories.NewtChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.io.obj.OBJFileLoader;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Scale;
import org.jzy3d.maths.Utils2;
import org.jzy3d.painters.IPainter;
import org.jzy3d.plot3d.primitives.Arrow;
import org.jzy3d.plot3d.primitives.ChessFloor;
import org.jzy3d.plot3d.primitives.DrawableTypes;
import org.jzy3d.plot3d.primitives.EuclidCircle;
import org.jzy3d.plot3d.primitives.EuclidPart;
import org.jzy3d.plot3d.primitives.EuclidPlane;
import org.jzy3d.plot3d.primitives.EuclidRobot;
import org.jzy3d.plot3d.primitives.EuclidPart;
import org.jzy3d.plot3d.primitives.EuclidSkeleton;
import org.jzy3d.plot3d.primitives.EuclidSphere;
import org.jzy3d.plot3d.primitives.LabelFactory;
import org.jzy3d.plot3d.primitives.Line;
import org.jzy3d.plot3d.primitives.PickableObjects;
import org.jzy3d.plot3d.primitives.RobotType;
import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.vbo.drawable.DrawableVBO;
import org.jzy3d.plot3d.rendering.canvas.CanvasNewtAwt;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.view.Camera;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryView3d extends AbstractAnalysis {

    //private Chart chart;
    private int pickingId = 0;
    private ArrayList<PickableObjects> pickableObjects = new ArrayList();
    private ArrayList<PickableObjects> pickingSupportList = new ArrayList();
    private PickingSupport pickingSupport;
    private NewtCameraMouseController cameraMouse;
    private ObjectLoader colladaLoader;
    private static ArrayList<EuclidRobot> robotList = new ArrayList();
    private static ArrayList<EuclidSkeleton> skeletonList = new ArrayList();
    
    /**
     * Constructor for a GeometryView3d to get created by a NewtChartFactory.
     */
    public GeometryView3d(){
        super(new NewtChartFactory());  
    }
    
    
    public static void main(String[] args) throws Exception {
        GeometryView3d gv = new GeometryView3d();
        AnalysisLauncher.open(gv);
        //Robots have to be rotated after initialisation.
        gv.rotateRobotsCoordsystem();
        gv.setRobotsDH();
        gv.setUpRobotMovement();
        gv.setUpSkeletons();
        //gv.skeletonCenters();
        //GeometryView3d viewer = new GeometryView3d();
        //viewer.open();
    }
    
    private void skeletonCenters(){
        for(EuclidSkeleton s: skeletonList){
            for(EuclidPart part: s.getParts()){
                Point3d p = new Point3d(part.getCenter().x, part.getCenter().y, part.getCenter().z);
                System.out.println(p);
                addPoint(p,Color.RED,0.2f, "");
            }
        }
    }
    
    /**
     * Rotate all Robots to have the Z-Vector as the UP Vector.
     */
    private static void rotateRobotsCoordsystem(){
        for(EuclidRobot robot: robotList){
            robot.rotateCoordSystem();
        }
    }
    
    /**
     * Set the robot right to its DH parameters
     */ 
    private static void setRobotsDH(){
        for(EuclidRobot robot: robotList){
            robot.moveDH();
        }
    } 
    
    /**
     * Set up the movement of the robot per sliders
     */
    private void setUpRobotMovement(){
        CanvasNewtAwt c = (CanvasNewtAwt) chart.getCanvas();
        Component comp = c.getComponent(0);
        c.remove(comp);
        JPanel p = new JPanel();
        p.add(comp);
        p.setLayout(new BoxLayout(p, 1));
        for(int j = 0; j < robotList.size(); j++){
            int number = j + 1;
            String string = "Robot Number " + number;
            JLabel label = new JLabel(string);
            p.add(label);
            for(int i = 1; i < 7; i++){
                JSlider slider = new JSlider();
                slider.setMaximum(360);
                slider.setMinimum(0);
                slider.setVisible(true);
                slider.setValue((int) robotList.get(0).getDHs().get(i).getTheta());
                final int ix = i;
                slider.addChangeListener(new ChangeListener(){ 
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        JSlider source = (JSlider)e.getSource();
                        robotList.get(0).setTheta(ix, source.getValue());
                    }   
                });
                p.add(slider);
            }
        }
        
            p.setVisible(true);
            c.add(p); 
    }
    
    private void setUpSkeletons(){
        for(EuclidSkeleton skeleton: skeletonList){
            skeleton.setUpSkeleton();
        }
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
        Point3d labelLocation = new Point3d(location.x, location.y,location.z - (width/2) - LabelFactory.getInstance().getOffset());
        EuclidSphere sphere = new EuclidSphere();
        sphere.setData(location, (float) (width/2), 20, color, label, labelLocation);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeDisplayed(false);
        sphere.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(sphere, sphere);
        chart.add(sphere);
        pickingSupportList.add(sphere);
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
        EuclidSphere sphere = new EuclidSphere();
        Point3d labelLocation = new Point3d(location.x,location.y, location.z - Math.sqrt(Math.abs(squaredSize)) - LabelFactory.getInstance().getOffset());
        sphere.setData(location,(float) Math.sqrt(Math.abs(squaredSize)),10, color, label, labelLocation);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeColor(Color.BLACK);
        sphere.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(sphere, sphere);
        chart.add(sphere);
        pickingSupportList.add(sphere);
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
        Line line = new Line();
        line.setData(p1,p2, radius, 10, 0, color, label);
        line.setPickingId(pickingId++);
        pickingSupport.registerDrawableObject(line, line);
        chart.add(line);
        pickingSupportList.add(line);
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
     * @param label the label for the circle
     */
    public void addCircle(Point3d origin, Vector3d direction, float radius ,Color color, String label){
        EuclidCircle circle = new EuclidCircle();
        circle.setData(origin, direction, radius, color, label);
        circle.setPickingId(pickingId++);
        pickingSupport.registerPickableObject(circle, circle);
        chart.add(circle);
        pickingSupportList.add(circle);
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
        Point3d labelLocation = new Point3d(location.x, location.y - radius - LabelFactory.getInstance().getOffset(), location.z);
        arrow.setData(Utils2.createVector3d(new Coord3d(location.x,location.y,location.z), 
                    new Coord3d(direction.x,direction.y,direction.z), length), radius,10,0, color, label);
        arrow.setWireframeDisplayed(false);
        arrow.setPickingId(pickingId++);
        pickingSupport.registerPickableObject(arrow, arrow);
        chart.add(arrow);
        pickingSupportList.add(arrow);
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
        plane.setData(location, dir1, dir2, color, label);
        plane.setPolygonOffsetFillEnable(false);
        plane.setWireframeDisplayed(true);
        pickingSupport.registerDrawableObject(plane, plane);
        plane.setPickingId(pickingId++);
        chart.add(plane);
        pickingSupportList.add(plane);
    }
    
    /**
     * Add a label with a text to the 3d view.
     * 
     * @param location the location of the label
     * @param text the text of the label
     * @param color color of the text
     */
    public void addLabel(Point3d location, String text, Color color){
         chart.add(LabelFactory.getInstance().addLabel(location, text, Color.BLACK));
    }
    
    
    /**
     * Add a COLLADA (.dae) File Object to the Scene.
     * 
     * @param path the path to the COLLADA File
     */
    public void addCOLLADA(String path){
        EuclidPart part = colladaLoader.getCOLLADA(path); 
        part.setChart(chart);
        part.drawPart();
    }
    
    /**
     * Adds a robot to the chart from COLLADA (.dae) files
     * @param paths The paths to the Collada files for the robot as a List
     */
    public void addRobot(List<String> paths){
        EuclidRobot robot = new EuclidRobot(chart, RobotType.notype);
        robot.setData(paths);
        robotList.add(robot);
        robot.addToChartParts();
    }
    
    /**
     * Add a UR5e Robot.
     * 
     * @param paths
     * @param delta_theta_rad 
     */
    public void addRobotUR5e(List<String> paths,double[] delta_theta_rad){ 
        double[] delta_a_m = new double[]{0d, 0.000156734465764371306, 0.109039760794650886, 0.00135049423466820917, 0.30167176077633267e-05, 8.98147062591837358e-05, 0};
        double[] delta_d_m = new double[]{0d, -7.63582045015809285e-05, 136.026368377065324, -130.146527922606964, 0.12049886607637639, -0.13561334270734671, -0.000218168195914358876};
        double[] delta_alpha_rad= new double[]{0d, -0.000849612070594307767, 0.00209120614311242205, 0.0044565542371754396, -0.000376815598678081898, 0.000480742313784698894, 0};
        
        EuclidRobot robot = new EuclidRobot(chart, RobotType.UR5e);
        robot.setData(paths, delta_theta_rad, delta_alpha_rad, delta_d_m, delta_a_m);
        robotList.add(robot);
        robot.addToChartParts();
    }

    
    public void addSkeleton(String path, String xml){
        EuclidSkeleton skeleton = new EuclidSkeleton(path, xml, chart);
        skeletonList.add(skeleton);
        skeleton.drawOnChart();
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
        //q.setAlphaActivated(false);
        q.setAnimated(false); 
        q.setHiDPIEnabled(true); 
        q.setDisableDepthBufferWhenAlpha(false);
        
        //chart = initializeChart(q);       
        
        chart = new Chart(this.getFactory(), q);
        
        //chart = myfactory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);
        //Add the ChessFloor and set size
        
        /*
        this.setUpChessFloor(20.f);
        chart.getScene().getGraph().addGraphListener(new GraphListener() {
            @Override
            public void onMountAll() {
                updateChessFloor();
            }
        });
        */
        
        //Set up ObjectLoader and Mouse
        colladaLoader = ObjectLoader.getLoader();
        setUpMouse();
        //Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
        //light.setType(Light.Type.POSITIONAL);
        Light light = chart.addLightOnCamera();
        
        addSkeleton("data/golembones/golembones.obj", "StickFigure.xml");
        
        /**
        addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f, "Point1");
        addSphere(new Point3d(20,20,20), 10, Color.ORANGE, "Sphere1");
        
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        
        addArrow(new Point3d(0d, 0d, 0d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        
        addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
        addCircle(new Point3d(20,20,20), new Vector3d(0,0,1),10,Color.RED, "Circle");
        
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        
        addPlane(new Point3d(0,1,5), new Vector3d(0,-10,0), new Vector3d(-10,0,0), Color.ORANGE, "ClipPlane");
        addPoint(new Point3d(0,0,0), Color.BLUE, 0.6f, "Point1");
        addPoint(new Point3d(1,10,1), Color.BLUE, 0.6f, "Point3");
        addPoint(new Point3d(20,20,20), Color.BLUE, 0.6f, "Point2");    
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        addLine(new Vector3d(0d,0d,-1d), new Point3d(3d,0d,3d), Color.CYAN, 0.2f, 10, "ClipLinie");
        addArrow(new Point3d(7d, 7d, 7d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        **/
        
        /*
        double[] delta_theta_rad = new double[]{0d,0d,0d,0d,0d,0d,0d};
        //double[] delta_theta_rad = new double[]{0d, -8.27430119976213518e-08, 0.732984551101984239, 5.46919521494736127, 0.0810043775014757245, -3.53724730506321805e-07, -9.97447025669062626e-08};
        double[] delta_a_m = new double[]{0d, 0, -425, -392.2, 0, 0, 0};
        double[] delta_d_m = new double[]{0d, 162.5, 0, 0, 133.3, 997, 996};
        double[] delta_alpha_rad= new double[]{0d, Math.PI/2, 0, 0, Math.PI/2, Math.PI/2, 0};
        */
        /*
        double[] delta_theta_rad = new double[]{0d,0d,0d,0d,0d,0d,0d};      

        ArrayList<String> pathList = new ArrayList<String>();
        pathList.add("data/objfiles/base.dae");
        pathList.add("data/objfiles/shoulder.dae");
        pathList.add("data/objfiles/upperarm.dae");
        pathList.add("data/objfiles/forearm.dae");
        pathList.add("data/objfiles/wrist1.dae");
        pathList.add("data/objfiles/wrist2.dae");
        pathList.add("data/objfiles/wrist3.dae");
        addRobotUR5e(pathList, delta_theta_rad);
        */
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
        
        @Override
        public void mouseDragged(com.jogamp.newt.event.MouseEvent e){          
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
                        } else if(chart.getView().getCamera().getEye().y>chart.getView().getBounds().getYmax()){
                            depthRange = 0.4f;
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
    
    /**
     * Set up the ChessFloor size
     * @param length The length of one square
     */
    private void setUpChessFloor(float length){
        ChessFloor.getSingelton(chart, length);
    }
    
    /**
     * Updates the ChessFloor
     */
    private void updateChessFloor(){
        ChessFloor.getSingelton(chart);
    }
    
    /**
     * Removes the ChessFloor
     */
    private void removeChessFloor(){
        chart.remove(ChessFloor.getSingelton(chart));
    }
}
