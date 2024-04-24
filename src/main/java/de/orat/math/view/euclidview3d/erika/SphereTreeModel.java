package de.orat.math.view.euclidview3d.erika;

import de.orat.math.view.euclidview3d.GeometryView3d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import static java.lang.Math.PI;
import org.jzy3d.colors.Color;

/**
 *
 * @author erika
 */

public class SphereTreeModel {
    
    Color translucent = new Color(255,255,255,0.3f);
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    
    public static void main(String[] args)throws Exception {
        
    }
    
    double[] d_m = new double[]{0d, 162.5, 0d, 0d, 133.3, 99.7, 099.6};
    double[] a_m = new double[]{0d, 0d,  -425, -392.2, 0d, 0d, 0d};
    double[] alpha_rad = new double[]{0d, PI/2d, 0d, 0d, PI/2d, -PI/2d, 0d};
    double[] theta_rad = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    
    double r[] = new double[]{90, 90, 90, 65, 70, 70, 65};
    
    /*double[] d_m = new double[]{0d, 163, 0d, 425, 392, 0, 0.1};
    double[] a_m = new double[]{0d, 0d,  138, 131, 0d, 127, 0d};
    double[] alpha_rad = new double[]{0d, PI/2d, 0d, PI/2d,0d,0d, 0d};
    double[] theta_rad = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    */
    public void addtoGeometryView3d (GeometryView3d gv){
        double x[] = new double[7];
        double y[] = new double[7];
        double z[] =  new double[7];
        
        x[0] = 0;
        y[0] = 0; 
        z[0] = 0;
        
        Point3d p0 = new Point3d(x[0], y[0], z[0]);
        gv.addSphere(p0, r[0], translucent, "");
        
        //double tx =0;
        //double ty =0;
        //double tz =0;
        
        double tx[] = new double[7];
        double ty[] = new double[7];
        double tz[] = new double[7];
        
        
        for(int i = 1; i < 7; i++){
            x[i] = 0;
            y[i] = 0; 
            z[i] = 0;
                  
            if(alpha_rad[i] != 0){
                
                
                Vector3d t = new Vector3d(x[i], y[i], z[i]);
                double[] m = Kreuzprodukt(RotationMatrixX(alpha_rad[i]),t);
               
                x[i] = m[0];
                y[i] = m[1];
                z[i] = m[2];
                
                
            }
            
            if(d_m[i] != 0){
                z[i] = d_m[i];
            }
            
            if(a_m[i] != 0){
                x[i] = (x[i] + a_m[i]);
            }
            System.out.println("Vektoren aus dh:");   
            System.out.println(x[i]);
            System.out.println(y[i]);
            System.out.println(z[i]);
            /*
            if ( i==4){
                Vector3d tt = new Vector3d(x[i], y[i], z[i]);
                double[] n = Kreuzprodukt(RotationMatrixX(alpha_rad[i]),tt);
                tx += n[0];
                ty += n[1];
                tz += n[2];
            } 
            else if ( i==5){
                Vector3d tt = new Vector3d(x[i], y[i], z[i]);
                double[] n = Kreuzprodukt(RotationMatrixX(alpha_rad[i-1]),tt);
                Vector3d ttt = new Vector3d(n[0], n[1], n[2]);
                double[] nn = Kreuzprodukt(RotationMatrixX(alpha_rad[i-1]),ttt);
                tx += nn[0];
                ty += nn[1];
                tz += nn[2];
            } 
            else if ( i==6){
                Vector3d tt = new Vector3d(x[i], y[i], z[i]);
                double[] n = Kreuzprodukt(RotationMatrixX(alpha_rad[i-1]),tt);
                
                Vector3d tttt = new Vector3d(n[0], n[1], n[2]);
                double[] nnn = Kreuzprodukt(RotationMatrixX(alpha_rad[i-2]),tttt);
                
                Vector3d ttt = new Vector3d(nnn[0], nnn[1], nnn[2]);
                double[] nn = Kreuzprodukt(RotationMatrixX(alpha_rad[i-2]),ttt);
                tx += nn[0];
                ty += nn[1];
                tz += nn[2];
            } 
            else{
            
            tx += x[i];
            ty += y[i];
            tz += z[i];
            }
           */
            
            if(i >2 && alpha_rad[i] != 0){
                double n[] = new double[3];
                n[0] = x[i];
                n[1] = y[i];
                n[2] = z[i];
                for(int j = i; alpha_rad[j]!=0; j--){
                    Vector3d t = new Vector3d(n[0], n[1], n[2]);
                    n = Kreuzprodukt(RotationMatrixX(alpha_rad[j]),t);
                    
                    tx[i] = tx[i-1] + n[0];
                    ty[i] = ty[i-1] + n[1];
                    tz[i] = tz[i-1] + n[2];
                    
                }
                   
            } else{
                tx[i] = tx[i-1] + x[i];
                ty[i] = ty[i-1] + y[i];
                tz[i] = tz[i-1] + z[i];
            }
            
            System.out.println("Kugelmittelpunkte:");   
            System.out.println(tx[i]);
            System.out.println(ty[i]);
            System.out.println(tz[i]);
            
            
            Point3d SphereCenter = new Point3d(tx[i], ty[i], tz[i]);
            gv.addSphere(SphereCenter, r[i], translucent, "");
            
        }
        Point3d SphereCenter7 = new Point3d(tx[1], ty[1]-140, tz[1]);
        gv.addSphere(SphereCenter7, 90, translucent, "");
        Point3d SphereCenter8 = new Point3d(tx[1]-120, ty[1]-140, tz[1]);
        gv.addSphere(SphereCenter8, 65, translucent, "");
        Point3d SphereCenter9 = new Point3d(tx[1]-210, ty[1]-140, tz[1]);
        gv.addSphere(SphereCenter9, 65, translucent, "");
        Point3d SphereCenter10 = new Point3d(tx[1]-300, ty[1]-140, tz[1]);
        gv.addSphere(SphereCenter10, 65, translucent, "");
        Point3d SphereCenter11 = new Point3d(tx[1]-425, ty[1]-140, tz[1]);
        gv.addSphere(SphereCenter11, 90, translucent, "");
        
        Point3d SphereCenter12 = new Point3d(tx[2]-100, ty[2], tz[2]);
        gv.addSphere(SphereCenter12, 65, translucent, "");
        Point3d SphereCenter13 = new Point3d(tx[2]-200, ty[2], tz[2]);
        gv.addSphere(SphereCenter13, 65, translucent, "");
        Point3d SphereCenter14 = new Point3d(tx[2]-300, ty[2], tz[2]);
        gv.addSphere(SphereCenter14, 65, translucent, "");
        
    }
    
    /*public double[] BerechnungInS0(double[][] m, Vector3d p){
        double[]erg = new double[3];
        erg[0] = m[0][0]*p.x + m[0][1]*p.y + m[0][2]*p.z + m[0][3]*1;
        erg[1] = m[1][0]*p.x + m[1][1]*p.y + m[1][2]*p.z + m[1][3]*1;
        erg[2] = m[2][0]*p.x + m[2][1]*p.y + m[2][2]*p.z + m[2][3]*1;
        return erg;
    }
    
    public double[][] TransformationMatrix(double [][] rotationsmatrix, Vector3d translation){
        double[][] t = new double[3][4];
        t[0][0] = rotationsmatrix[0][0];
        t[0][1] = rotationsmatrix[0][1];
        t[0][2] = rotationsmatrix[0][2];
        t[0][3] = translation.x;
        t[1][0] = rotationsmatrix[1][0];
        t[1][1] = rotationsmatrix[1][1];
        t[1][2] = rotationsmatrix[1][2];
        t[1][3] = translation.y;
        t[2][0] = rotationsmatrix[2][0];
        t[2][1] = rotationsmatrix[2][1];
        t[2][2] = rotationsmatrix[2][2];
        t[2][3] = translation.z;
        /*t[3][0] = 0;
        t[3][1] = 0;
        t[3][2] = 0;
        t[3][3] = 1;*/
    /*    
        return t;
    }
    */
    public double[] Kreuzprodukt(double[][] m, Vector3d p){
        double[]erg = new double[3];
        erg[0] = m[0][0]*p.x + m[0][1]*p.y + m[0][2]*p.z;
        erg[1] = m[1][0]*p.x + m[1][1]*p.y + m[1][2]*p.z;
        erg[2] = m[2][0]*p.x + m[2][1]*p.y + m[2][2]*p.z;
        return erg;
    }
    
    
    public double[][] RotationMatrixX(double angle){
        double[][] m = new double[3][3];
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[1][0] = 0;
        m[1][1] = Math.cos(angle);
        m[1][2] = -Math.sin(angle);
        m[2][0] = 0;
        m[2][1] = Math.sin(angle);
        m[2][2] = Math.cos(angle);
        return m;
    }
    
    /*public double[][] RotationMatrixY(double angle){
        double[][] m = new double[3][3];
        m[0][0] = Math.cos(angle);
        m[0][1] = 0;
        m[0][2] = Math.sin(angle);
        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[2][0] = -Math.sin(angle);
        m[2][1] = 0;
        m[2][2] = Math.cos(angle);
        return m;
    }*/
    
    public double[][] RotationMatrixZ(double angle){
        double[][] m = new double[3][3];
        m[0][0] = Math.cos(angle);
        m[0][1] = -Math.sin(angle);
        m[0][2] = 0;
        m[1][0] = Math.sin(angle);
        m[1][1] = Math.cos(angle);
        m[1][2] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        return m;
    }

}
