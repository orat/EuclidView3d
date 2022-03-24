package org.jzy3d.maths;

import java.text.DecimalFormat;

/**
 *
 * @author rettig
 */
public class RotationMatrix2 {
    
    public double[][] m;
    
    public RotationMatrix2(){
        m = new double[2][2];
        m[0][0] = 1;
        m[1][1] = 1;
        m[2][2] = 1;
    }
    /**
     * Create a rotation matrix which rotates around a
     * given axis threw the origin aber the given 
     * angle.
     * 
     * @param u Axis to rotate around
     * @param angle 
     */
    public RotationMatrix2(Coord3d u, double angle){
        double C = Math.cos(angle);
        double t = 1-C;
        double S = Math.sin(angle);
        m[0][0] = t*u.x*u.x+C;
        m[0][1] = t*u.x*u.y-S*u.z;
        m[0][2] = t*u.x*u.z+S*u.y;
        m[1][0] = t*u.x*u.y+S*u.z;
        m[1][1] = t*u.y*u.y+C;
        m[1][2] = t*u.y*u.z-S*u.x;
        m[2][0] = t*u.x*u.z-S*u.y;
        m[2][1] = t*u.y*u.z+S*u.x;
        m[2][2] = t*u.z*u.z+C;
    }
    public RotationMatrix2(double[][] t){
        if (t.length != 3 || t[0].length != 3) 
             throw new IllegalArgumentException("t.length and t[0].length must be 3!");
        this.m = t;
    }
    
    /**
     * Transform the given vector v by the given matrix m.
     * 
     * @param v
     * @return transformed vector v (the given vector v is not overwritten...).
     * @throws IllegalArgumentException if the given matrix is not quatratic or its
     * dimensions is not identical with the dimension of the given vector v.
     */
    public double[] transform(double[] v){
        if (m.length != v.length || m.length != m[0].length) 
            throw new IllegalArgumentException("t.length must be t[0].length and must be v.length!");
        double[] result = new double[m.length];
        for (int row=0;row<m.length;row++){
            for (int col=0;col<m.length;col++){
                result[row] += m[row][col] * v[col];
            }
        }
        return result;
    }
    
    public double[][] transform(double[][] v){
        double[][] result = new double[m.length][m.length];
        for (int row=0;row<m.length;row++){
            for (int col=0;col<m.length;col++){
                double value=0;
                for (int i=0;i<m.length;i++){
                    value += m[row][i]*v[i][col];
                }
                result[row][col] = value;
            }
        }
        return result;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // Zeile 0,1
        for (int row=0;row<2;row++){
            for (int col=0;col<2;col++){
                sb.append(format(m[row][col]));
                sb.append(" ");
            }
            sb.append(format(m[row][2]));
            sb.append(";");
        }
        // Zeile 2
        for (int col=0;col<2;col++){
                sb.append(format(m[2][col]));
                sb.append(" ");
        }
        sb.append(format(m[2][2]));
        sb.append("]");
        return sb.toString();
    }
    
    public static String format(double i){
	DecimalFormat f = new DecimalFormat("#0.00");
	double toFormat = ((double)Math.round(i*100))/100;
	return f.format(toFormat);
    }
}
