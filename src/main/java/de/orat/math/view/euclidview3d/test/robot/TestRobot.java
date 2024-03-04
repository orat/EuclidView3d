package de.orat.math.view.euclidview3d.test.robot;

import static java.lang.Math.PI;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.ChartFactory;
import org.jzy3d.chart.factories.SwingChartFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Arrow;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author Oliver Rettig (Oliver.Rettig@orat.de)
 */
public class TestRobot extends AbstractAnalysis {
    
    Color darkred = new Color(160,0,0,0.5f);
    //Color green = new Color(34,139,34);

    public TestRobot() {
        super(new ChartFactory());
    }
    
    public static void main(String[] args) throws Exception {
        TestRobot d = new TestRobot();
        AnalysisLauncher.open(d);
    }
    
    @Override
    public void init() {
      
        Quality q = Quality.Advanced(); 
        q.setDepthActivated(true);
        q.setAlphaActivated(true);
        q.setAnimated(false); 
        q.setHiDPIEnabled(true); 
        
        // Create a chart Quality.Advanced()
        chart = new SwingChartFactory().newChart(q);
        chart.getView().setSquared(false);


        // die letzten Werte für alpha, a bzw. den korrespondierenden delta-Werte ist 0,
        // da das letzte Koordinatensystem einfach nur in Achsenrichtung um d verschoben ist

        // added zero values at the beginning to represent the basis coordinate system, needed to calculate mDH values
        // Werkskalibration UR5e
        double[] delta_theta_rad = new double[]{0d, -8.27430119976213518e-08, 0.732984551101984239, 5.46919521494736127, 0.0810043775014757245, -3.53724730506321805e-07, -9.97447025669062626e-08};
        double[] delta_a_m = new double[]{0d, 0.000156734465764371306, 0.109039760794650886, 0.00135049423466820917, 6.30167176077633267e-05, 8.98147062591837358e-05, 0};
        double[] delta_d_m = new double[]{0d, -7.63582045015809285e-05, 136.026368377065324, -143.146527922606964, 7.12049886607637639, -6.13561334270734671e-05, -0.000218168195914358876};
        double[] delta_alpha_rad= new double[]{0d, -0.000849612070594307767, 0.00209120614311242205, 0.0044565542371754396, -0.000376815598678081898, 0.000480742313784698894, 0};

        // Stuttgarter Werte bestimmt aus 20221115A
        /*double[] delta_theta_rad = new double[]{0, -6.0717e-04,  7.3364e-01,  5.4693e+00,  8.1761e-02, -1.7036e-04, 3.6291e-04};
        double[] delta_a_m = new double[]{0, 4.1994e-04,  1.0786e-01, -7.8398e-05,  9.4018e-04,  2.4728e-04, 1.0543e-04};
        double[] delta_d_m = new double[]{0, 8.0152e-04,  1.3603e+02, -1.4315e+02,  7.1207e+00,  2.0997e-04, -1.0959e-03};
        double[] delta_alpha_rad = new double[]{0, 0.0010,  0.0021,  0.0044, -0.0005,  0.0008,  0.0000};
        */
        double[] d_n_m_ = new double[]{0d, 0.1625, 0d, 0d, 0.1333, 0.0997, 0.0996};

        // Die zu bestimmenden Werte auf NaN gesetzt. Das sind die, bei denen
        // die nachfolgende Achse nahezu senkrecht steht.
        // Achtung: Länge des Arrays ist um 1 kleiner als die anderen, da der erste Wert d1,
        // Es gibt keinen d0-Wert
        double[] d_n_m = new double[]{Double.NaN, 0d, 0d, Double.NaN, Double.NaN, 0.0996-0.000218168195914358876};

        // use the signs of the nominal values to define the directions of a --> theta, a, alpha
        double[] a_n_m = new double[]{0d, 0d,  -0.425, -0.3922, 0d, 0d, 0d};
        // a0-a6
        // Vorzeichen von a1 ist auf UR-Seite nicht zu erkennen, aber a2-4 negativ, a5-6 positiv
        // DH4 ist damit seltsamerweise falsch
        //boolean[] sign_r = new boolean[]{false, true, true, true, true, true, true};
        boolean[] sign_r = new boolean[]{false, true, true, false, false, false, false};

        double[] alpha_n_rad = new double[]{0d, PI/2d, 0d, 0d, PI/2d, -PI/2d, 0d};

        // DH-Parameter inclusive manufacturer calibration offsets
        double[] d_m = new double[7];
        double[] a_m = new double[7];
        double[] alpha_rad = new double[7];
        double[] theta_rad = new double[7];
        for (int i=0;i<7;i++){
            d_m[i] = d_n_m_[i] + delta_d_m[i];
            a_m[i] = a_n_m[i]  + delta_a_m[i];
            alpha_rad[i] = alpha_n_rad[i] + delta_alpha_rad[i];
            theta_rad[i] = delta_theta_rad[i];
        }

        // geht so nicht, da auch bei den Positionen an denen die nominallen r-Werte
        // 0 sind könnte das Vorzeichen geflippt werden müssen
        /*for (int i=0;i<6;i++){
            sign_r[i] = a_n_m[i] < 0;
        }*/

        DHAxes axes = determineDHAxesFromDH(theta_rad, alpha_rad, d_m, a_m);
        float radius = (float) 0.01;
        for (int i=0;i<axes.z.length;i++){
            Arrow arrow = new Arrow();
            //pos, dir, length
            double x = axes.c[i].x;
            double y = axes.c[i].y;
            double z = axes.c[i].z;
            double dx = axes.z[i].x;
            double dy = axes.z[i].y;
            double dz = axes.z[i].z;
            if (Math.abs(axes.c[i].y) > 1){
                System.out.println("Axis["+String.valueOf(i)+"] moved to o(y=0)!");
                // oder Ursprung so verschieben, dass y=0 wird
                double t = -y/dy;
                x +=t*dx;
                y = 0;
                z +=t*dz;
            }
            float length = 0.4f;
            // der richtungsvektor sollte die Length 4 haben
            Vector3d dir = new Vector3d(dx,dy,dz);
            dir.normalize();
            dir.scale(length);
            arrow.setData(new Point3d(x,y,z), dir,radius,10,0, darkred, "label");
                arrow.setWireframeDisplayed(false);
            chart.add(arrow);
            
            // x-axes
            //TODO
            // auskommentiert da das so nicht mehr geht, Änderungen wie sieh oben benötigt
            /*arrow = new Arrow();
            float xlength = 0.1f;
            arrow.setData(Utils2.createVector3d(new Coord3d(x,y,z), 
                    new Coord3d(axes.x[i].x,axes.x[i].y,axes.x[i].z), xlength),radius,10,0, Color.BLUE, "label");
                arrow.setWireframeDisplayed(false);
            chart.add(arrow);*/
        }

        /*double radius = 2;
        Sphere sphere = new Sphere(new Coord3d(0,0,0),(float)radius,15,darkred);
        sphere.setWireframeColor(darkred);
        
        // hat alles nichts gebracht
        sphere.setFaceDisplayed(true);
        sphere.setWireframeColor(Color.CYAN);
        sphere.setWireframeDisplayed(true);
        sphere.setWireframeWidth(2);
        sphere.setReflectLight(true);
        chart.add(sphere);*/
        
        // This addition keeps the aspect ratio of the X and Y data
        // but makes X and Z square
        //chart.getView().setSquarifier(new XZSquarifier());
        //chart.getView().setSquared(true);
        
       
    }
    
    public record DHAxes(Vector3d[] z, Vector3d[] x, Point3d[] c){}
    
    /**
     * Determine DH-axes from DH parameters.
     * 
     * @param theta with 0 as first value in [°] (7 values for UR5e)
     * @param alpha with 0 as first value in [°] (7 values for UR5e)
     * @param d with 0 as first value in [m]
     * @param r with 0 as first value in [m]
     * @return axes of all of the DH frames.
     */
    public static DHAxes determineDHAxesFromDH(double[] theta, double[] alpha, double[] d, 
                                       /*double[] dn,*/ double[] r/*, boolean[] signR*/){
        
        Vector3d[] z = new Vector3d[alpha.length];
        Vector3d[] x = new Vector3d[alpha.length]; // reale Ausrichtung der x-Achsen
        Point3d[]  c = new Point3d[alpha.length];
        
        System.out.println("Denavit Hartenberg:");
        System.out.println("------------------");
        
        //  Basis-Koordinatensystem
        
        z[0] = new Vector3d(0d,0d,1d); // Ausrichtung er ersten Drehachse
        // x[0] zeigt von der vorherigen Achse, also dem Basis-System auf die erste
        // Achse also Globe2Base - um keine unnötigen delta-thetas zu erzeugen, sollte
        // x[0] in Richtung von z[2] in der neutral-pose zeigen, die die nach z[1] folgenden zweite joint-axis
        // sollte ja in der neutral-pose gerade die Ausgangs-x-Richtung definieren
        x[0] = new Vector3d(1d,0d,0d);
        c[0] = new Point3d(0d,0d,0d);
        System.out.println("o0= ("+String.valueOf(c[0].x)+","+String.valueOf(c[0].y)+","+String.valueOf(c[0].z)+")");
        System.out.println("z0= ("+String.valueOf(z[0].x)+","+String.valueOf(z[0].y)+","+String.valueOf(z[0].z)+")");
        
        Matrix4d dhm = new Matrix4d();
        dhm.setIdentity();
        
        //TODO
        // unklar, wie ich das Vorzeichen im Modell von a2-a4 berücksichten muss, 
        // außerdem unklar, warum a5-a6 kein Vorzeichen definiert
        // vermutlich muss ich beim Erstellen der matrix die Vorzeichen gar nicht berücksichtigen!!!!
        
        // 
        for (int i=1;i<alpha.length;i++){
            System.out.println("Input("+String.valueOf(i)+"): alpha="+String.valueOf(alpha[i]*180d/PI)+
                    "°, theta="+String.valueOf(theta[i]*180d/PI)+
                    "°, d="+String.valueOf(d[i]*1000d)+"mm, r="+String.valueOf(r[i]*1000)+"mm");
            
            // origin in lokalen Koordinaten
            Point3d cc = new Point3d(0d,0d,0d);
            // z-Richtung in lokalen Koordinaten
            Vector3d zz = new Vector3d(0d,0d,1d);
            // x-Richtung in lokalen Koordinaten
            Vector3d xx = new Vector3d(1d,0d,0d);
        
            dhm.mul(new DH(theta[i], alpha[i], d[i], r[i]).toMatrix4d());
            
            dhm.transform(cc);
            System.out.println("o"+String.valueOf(i)+"= ("+String.valueOf(cc.x)+","+String.valueOf(cc.y)+","+String.valueOf(cc.z)+")");
            c[i] = new Point3d(cc.x,cc.y,cc.z);
            
            dhm.transform(zz);
            System.out.println("z"+String.valueOf(i)+"= ("+String.valueOf(zz.x)+","+String.valueOf(zz.y)+","+String.valueOf(zz.z)+")");
            z[i] = new Vector3d(zz);
            
            dhm.transform(xx);
            System.out.println("x"+String.valueOf(i)+"= ("+String.valueOf(xx.x)+","+String.valueOf(xx.y)+","+String.valueOf(xx.z)+")");
            x[i] = new Vector3d(xx);
        }
        
        return new DHAxes(z,x,c);
    }
}
