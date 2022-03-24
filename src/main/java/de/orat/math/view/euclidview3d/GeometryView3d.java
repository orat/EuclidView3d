package de.orat.math.view.euclidview3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Line;
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
        viewer.addPoint(new Point3d(1,1,1), Color.BLUE);
        viewer.addSphere(null, new Point3d(20,20,20), 10, Color.ORANGE);
        viewer.open();
    }
    
    public void addPoint(Point3d p, Color color){
        //double radius = 0.6;
        //Sphere sphere = new Sphere(new Coord3d(p.x,p.x,p.z),(float)radius,20,color);
        //sphere.setPolygonOffsetFillEnable(false);
        //chart.add(sphere);
        
        float width = 0.6f;
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
     * #
     * @param p1
     * @param p2
     * @param radius
     * @param color 
     */
    public void addLine(Point3d p1, Point3d p2, float radius, Color color){
        //CroppableLineStrip lineStrip = new CroppableLineStrip();
        //lineStrip.setWireframeColor(Color.BLACK);
        //lineStrip.add(new Point( new Coord3d(p1.x,p1.y,p1.z)));
        //lineStrip.add(new Point( new Coord3d(p2.x,p2.y,p2.z)));
        //chart.add(lineStrip);
        
        org.jzy3d.maths.Vector3d vec = 
                new org.jzy3d.maths.Vector3d(new Coord3d(p1.x,p1.y,p1.z),new Coord3d(p2.x, p2.y, p2.z));
        Line line = new Line();
        line.setData(vec, radius, 10, 0, color);
        chart.add(line);
    }
    
    public void addCircle(Point3d origin, Vector3d direction, Color color){
        //circle.setPolygonOffsetFillEnable(false);
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
