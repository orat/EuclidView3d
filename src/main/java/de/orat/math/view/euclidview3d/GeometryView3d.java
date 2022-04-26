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
    private final float labelOffset = 0.5f;
    
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
        Sphere sphere = new Sphere(new Coord3d(location.x,location.x,location.z),(float) (width/2), 20, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeDisplayed(false);
        //Point point = new Point(new Coord3d(location.x,location.x,location.z), color, width);
        chart.add(sphere);
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
        Sphere sphere = new Sphere(new Coord3d(location.x,location.x,location.z),
                (float) Math.sqrt(Math.abs(squaredSize)),10, color);
        sphere.setPolygonOffsetFillEnable(false);
        sphere.setWireframeColor(Color.BLACK);
        chart.add(sphere);
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
     * @param radius radius of the circle
     * @param color color of the circle
     */
    public void addCircle(Point3d origin, Vector3d direction, float radius ,Color color){
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
        if(smalest_value > direction.y){
            smalest = 1; 
            smalest_value = (float) direction.y;
        }
        if(smalest_value > direction.z){
            smalest = 2; 
            smalest_value = (float) direction.z;
        }
        Vector3d w = new Vector3d(1,0,0);
        if(smalest == 0){
            w = new Vector3d(1,0,0);
        } else if(smalest == 1){
            w = new Vector3d(0,1,0);
        } else {
            w = new Vector3d(0,0,1);
        }
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
     * * @param label the text of the label of the arrow
     */
    public void addArrow(Point3d location, Vector3d direction, float length, float radius, Color color, String label){
        Arrow arrow = new Arrow();
        arrow.setData(Utils2.createVector3d(new Coord3d(location.x,location.y,location.z), 
                    new Coord3d(direction.x,direction.y,direction.z), length), radius,10,0, color);
        arrow.setWireframeDisplayed(false);
        chart.add(arrow);
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
        Plane plane = new Plane();
        plane.setData(location, dir1, dir2, color);
        plane.setPolygonOffsetFillEnable(false);
        plane.setWireframeDisplayed(true);
        chart.add(plane);
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
        
        chart = initializeChart(q);
        
        
        //chart = myfactory.newChart(q);
        chart.getView().setSquared(false);
        chart.getView().setBackgroundColor(Color.WHITE);
        chart.getView().getAxis().getLayout().setMainColor(Color.BLACK);

        //Light light = chart.addLightOnCamera();
        Light light = chart.addLight(chart.getView().getBounds().getCorners().getXmaxYmaxZmax());
        
        //addPoint(new Point3d(1,1,1), Color.BLUE, 0.6f, "Point1");
        addSphere(new Point3d(20,20,20), 10, Color.ORANGE, "Sphere1");
        
        addPlane(new Point3d(5d,5d,5d), new Vector3d(0d,0d,5d), new Vector3d(5d,0d,0d), Color.RED, "Plane1");
        
        addArrow(new Point3d(0d, 0d, 0d), new Vector3d(0d,0d,2d), 3f, 0.5f, Color.CYAN, "Arrow1");
        
        addLabel(new Point3d(10d, 10d, 10d), "Label", Color.BLACK);
        addCircle(new Point3d(0,0,0), new Vector3d(0,0,1),5,Color.RED);
    }
    
    
}
