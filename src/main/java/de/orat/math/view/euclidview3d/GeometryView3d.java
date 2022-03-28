package de.orat.math.view.euclidview3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils2;
import org.jzy3d.plot3d.primitives.Arrow;
import org.jzy3d.plot3d.primitives.CroppableLineStrip;
import org.jzy3d.plot3d.primitives.Line;
import org.jzy3d.plot3d.primitives.Plane;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryView3d {

    private Chart chart;
    
    public static void main(String[] args) {
        GeometryView3d viewer = new GeometryView3d();
        viewer.addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f);
        viewer.addSphere(null, new Point3d(20,20,20), 10, Color.ORANGE);
        viewer.open();
    }
    
    /**
     * Add a point to the 3d view.
     * 
     * @param p
     * @param color 
     * @param width 
     */
    public void addPoint(Point3d p, Color color, float width){
        //double radius = 0.6;
        //Sphere sphere = new Sphere(new Coord3d(p.x,p.x,p.z),(float)radius,20,color);
        //sphere.setPolygonOffsetFillEnable(false);
        //chart.add(sphere);
        
        //float width = 0.6f;
        Point point = new Point(new Coord3d(p.x,p.x,p.z), color, width);
        chart.add(point);
    }
    
    /**
     * Add a sphere to the 3d view.
     * 
     * @param attitude
     * @param location
     * @param squaredSize
     * @param color 
     */
    public void addSphere(Vector3d attitude, Point3d location, double squaredSize, Color color){
        Sphere sphere = new Sphere(new Coord3d(location.x,location.x,location.z),
                (float) Math.sqrt(Math.abs(squaredSize)),20, color);
        sphere.setPolygonOffsetFillEnable(false);
        chart.add(sphere);
    }
  
    /**
     * Add a line to the 3d view.
     * 
     * @param attitude
     * @param location
     * @param color
     * @param radius
     * @param length weglassen und die Länge anhand des Volumens der view bestimmen
     */
    public void addLine(Vector3d attitude, Point3d location, Color color, float radius, float length){
        addLine(location, 
            new Point3d(location.x+attitude.x*length, 
                        location.y+attitude.y*length, 
                        location.z+attitude.z*length), radius, color);
    }
    
    /**
     * Add a line to the 3d view.
     * 
     * @param p1 start point of the cylinder
     * @param p2 end point of the cylinder
     * @param radius radius of the cylinder
     * @param color 
     */
    public void addLine(Point3d p1, Point3d p2, float radius, Color color){
        org.jzy3d.maths.Vector3d vec = 
                new org.jzy3d.maths.Vector3d(new Coord3d(p1.x,p1.y,p1.z),new Coord3d(p2.x, p2.y, p2.z));
        Line line = new Line();
        line.setData(vec, radius, 10, 0, color);
        chart.add(line);
    }
    
    /**
     * add circle to the 3d view.
     * 
     * @param origin origin of the circle
     * @param direction normal vector of the plane the circle lays in
     * @param color color of the circle
     */
    public void addCircle(Point3d origin, Vector3d direction, Color color){
        int slices;
        int rings = 100;
        CroppableLineStrip lineStrip = new CroppableLineStrip();
        for (int i=0;i<rings;i++){
            //TODO
        }
        //lineStrip.setWireframeColor(Color.BLACK);
        //lineStrip.add(new Point( new Coord3d(p1.x,p1.y,p1.z)));
        //lineStrip.add(new Point( new Coord3d(p2.x,p2.y,p2.z)));
        //chart.add(lineStrip);
        
    }
    
    /**
     * Add an arrow to the 3d view.
     * 
     * @param location midpoint of the arrow
     * @param direction direction of the arrow
     * @param length length of the arrow
     * @param radius radius of the arrow
     * @param color color of the arrow
     */
    public void addArrow(Point3d location, Vector3d direction, float length, float radius, Color color){
        Arrow arrow = new Arrow();
        arrow.setData(Utils2.createVector3d(new Coord3d(location.x,location.y,location.z), 
                    new Coord3d(direction.x,direction.y,direction.z), length), radius,10,0, color);
        arrow.setWireframeDisplayed(false);
    }
    /**
     * Add a plane to the 3d view.
     * 
     * @param location first point of the plane
     * @param dir1 vector which is added to the first point to get the second point
     * @param dir2 vector which is added to the second point to get the third point and which is added to the location to get the forth point
     * @param color color of the plane
     */
    public void addPlane(Point3d location, Vector3d dir1, Vector3d dir2, Color color){
        Plane plane = new Plane();
        plane.setData(location, dir1, dir2, color);
    }
    
    public GeometryView3d(){
        ChartFactory factory = new AWTChartFactory();
        //ChartFactory factory = new EmulGLChartFactory();

        // Emulgl will show limitations
        // 1-wireframe and face do not mix cleanly (polygon offset fill)
        // 2-wireframe color tend to saturate (here in green)

        Quality q = Quality.Advanced(); 
        q.setDepthActivated(true);
        q.setAlphaActivated(true);
        q.setAnimated(false); 
        q.setHiDPIEnabled(true); 
        
        chart = factory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);

        //Light light = chart.addLightOnCamera();
        Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
    }

    public void open(){
        chart.open();
        chart.addMouseCameraController(); // bessser nur addMouse() verwenden?
    }
    public void close(){
        chart.dispose();
    }
}
