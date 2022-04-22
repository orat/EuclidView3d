package de.orat.math.view.euclidview3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.chart.factories.NewtChartFactory;
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
import org.jzy3d.plot3d.text.drawable.DrawableText;

/**
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class GeometryView3d extends AWTAbstractAnalysis {

    //private Chart chart;
    
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
     */
    public void addPoint(Point3d location, Color color, float width){
        //double radius = 0.6;
        Sphere sphere = new Sphere(new Coord3d(location.x,location.x,location.z),(float) (width/2), 20, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeDisplayed(false);
        //Point point = new Point(new Coord3d(location.x,location.x,location.z), color, width);
        chart.add(sphere);
    }
    
    /**
     * Add a sphere to the 3d view.
     * 
     * @param location
     * @param squaredSize
     * @param color 
     */
    public void addSphere(Point3d location, double squaredSize, Color color){
        Sphere sphere = new Sphere(new Coord3d(location.x,location.x,location.z),
                (float) Math.sqrt(Math.abs(squaredSize)),10, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeColor(Color.BLACK);
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
                new org.jzy3d.maths.Vector3d(new Coord3d(p1.x,p1.y,p1.z), new Coord3d(p2.x, p2.y, p2.z));
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
        chart.add(lineStrip);
        
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
        chart.add(arrow);
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
        plane.setPolygonOffsetFillEnable(false);
        plane.setWireframeDisplayed(true);
        chart.add(plane);
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
        
        chart = initializeChart(q);
        
        
        //chart = myfactory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);

        //Light light = chart.addLightOnCamera();
        Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
        
        addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f);
        addSphere(new Point3d(20,20,20), 10, Color.ORANGE);
        
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED );
        
        addArrow(new Point3d(3d, 3d, 3d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN);
        
        addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
    }
}
