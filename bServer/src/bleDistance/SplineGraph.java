package bleDistance;
/*
 * @(#)Spline2D.java
 * 
 * Copyright (c) 2003 Martin Krueger
 * Copyright (c) 2005 David Benson
 *  
 */

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;

import bleDistance.DISTANCE;

/**
 * Interpolates points given in the 2D plane. The resulting spline
 * is a function s: R -> R^2 with parameter t in [0,1].
 * 
 */
public class SplineGraph {

  /** 
   *  Array representing the relative proportion of the total distance
   *  of each point in the line ( i.e. first point is 0.0, end point is
   *  1.0, a point halfway on line is 0.5 ).
   */
  private double[] t;
  private Spline splineX;
  private Spline splineY;
  /**
   * Total length tracing the points on the spline
   */
  private double length;
  
  /**
   * Creates a new Spline2D.
   * @param points
   */
  public SplineGraph(Point2D[] points) {
    double[] x = new double[points.length];
    double[] y = new double[points.length];
    
    for(int i = 0; i< points.length; i++) {
      x[i] = points[i].getX();
      y[i] = points[i].getY(); 
    }
    
    init(x, y);
  }
  
  private double[] initX;
  
  public HashMap<DISTANCE,N> distMap; 
  public void inputMapData(DISTANCE distenum, double n,int sigsize) {
		
	  if(this.distMap.containsKey(distenum)) {
		  distMap.get(distenum).insertSig(n,sigsize);
	  }else {
		  this.distMap.put(distenum, new N(n,sigsize));
		  
		  
	  }
	  
	  
  }
  
  public void MapDataToSpline(String mid, String yid) {
	  	int distmapsize=this.distMap.size();
	  	double[] xx=new double[distmapsize];
	  	double[] yy=new double[distmapsize];
	  	int xsize=0;
	  	Object[] distsett=(this.distMap.keySet().toArray());
	  	DISTANCE[] distset=new DISTANCE[distsett.length];
	  	String allX="myid - "+mid+" // yourid - "+yid+" // all X - ";
	  	for(int u=0; u<distsett.length; u++) {
	  		distset[u]=(DISTANCE)distsett[u];
	  		allX+=enumToDist(distset[u])+", ";
	  	}
	  	
	  	if(distmapsize>=2) {
	  	for(int i=0; i<distmapsize; i++) {
	  		
	  		xx[xsize]=this.distMap.get(distset[i]).getN();
	  		yy[xsize]=enumToDist(distset[i]);
	  		allX+="{X"+xx[xsize]+",Y"+yy[xsize]+"}";
	  		xsize++;
	  		
	  		
	  	}
	  	for(int i=0; i<xx.length; i++) {
		  	System.out.println("before xx ("+i+"):"+xx[i]);
		  	}
	  	double[] yyy=this.sortAscendingOrderByX(xx, yy);
	  	for(int i=0; i<xx.length; i++) {
	  	System.out.println("xx ("+i+"):"+xx[i]);
	  	System.out.println("yyy ("+i+"):"+yyy[i]);
	  	}
	  	this.init(xx,yyy);
	  	}else {
	  		System.out.println("log - dist size too small.");
	  	}
	  	System.out.println(allX);
	  	
  }
  public double[] sortAscendingOrderByX(double[] X,double[] Y) {
	  for(int i=0; i<Y.length; i++) {
		  System.out.println("Y ("+i+"):"+Y[i]);
	  }
	  double[] oldx=X.clone();
  double[] newY=null;
  Arrays.sort(X);
  newY=changeIndexY(oldx,X,Y);
  for(int i=0; i<Y.length; i++) {
	  System.out.println("newY ("+i+"):"+newY[i]);
  }
  return newY;
	  
  }
  public double[] changeIndexY(double[] oldX,double[] newX, double[] oldY) {
	  double tempX=0;
	  double tempY=0;
	  double[] newY=new double[newX.length];
	  for(int i=0; i<oldY.length; i++) {
		  System.out.println ("oldY"+i+":"+oldY[i]);
	  }
	  for(int k=0; k<newX.length; k++) {
		  tempX=oldX[k];
		  tempY=oldY[k];
		  System.out.println("tempX"+tempX+" newX"+newX[k]);
		  if(tempX==newX[k]) {
			  System.out.println("XsameX"+newX[k]);
		  }
		  if(k<newX.length) {
		  for(int i=0; i<newX.length; i++) {
			  if(k==i) {
				  System.out.println("XsamesameX"+newX[k]);
				  newY[i]=tempY;
			  }else {
			  if(tempX==newX[i]) {
				  newY[i]=tempY;
				  System.out.println("tempY find same XXXX"+tempY);
				  i=newX.length;
			  }
			  }
		  }
		  }
	  }
	  
	  return newY;
	  
  }
  
  class N{
	  
	  int sigSize;
	  double N;
	  
	  public N(double nn, int ss) {
		  sigSize=0;
		  N=0.0;
		  this.insertSig(nn, ss);
		  
	  }
	  public double getN() {
		  return this.N;
	  }
	  public void insertSig(double nN, int size) {
		  double newN=0.0;
		  
		  if(size>0) {
			  newN=(nN*size+this.N*this.sigSize)/(size+this.sigSize);
			  this.sigSize+=size;
			  this.N=newN;
		  }
		  
	  }
	  
  }
  public double enumToDist(DISTANCE distenum) {
	  double dist = 0.0;
	  
	  switch(distenum) {
	  case dst_1m : dist=1.01;
	  				break;
	  case dst_2m : dist=2.0;
		break;
	  case dst_3m : dist=3.0;
		break;
	  case dst_5m : dist=5.0;
		break;
	  case dst_6m : dist=6.0;
		break;
	  case dst_9m : dist=9.0;
		break;
	  case dst_7m : dist=7.0;
		break;
		default : 
		break;
	  }
	  return dist;
	  
  }
  
  public double[] insert(double x) {	//use this function. Y=insert(X)
	  double[] output=new double[]{0.0,0.0};
	  double t=0.0;
	  
	  for(int i=0; i<initX.length; i++) {
		  System.out.println("initX ("+i+"): "+initX[i]);
	  }
	  if(this.initX.length>=2) {
	  if((this.initX[0]<this.initX[this.initX.length-1])&&x>=this.initX[0]&&x<=this.initX[this.initX.length-1]) {
		  
		  t=(x-this.initX[0])/(this.initX[this.initX.length-1]-this.initX[0]);
		  System.out.println("t : "+t+" point[0]"+getPoint(t)[0]+"pioint[1]:"+getPoint(t)[1]);
		  output[0]=this.getPoint(t)[0];
	  output[1]=this.getPoint(t)[1];
	  
	  
	  }
	  }
	  return output;
  }
  public double insertMiddleT() {
	  double rt=-1;
	  if(this.initX!=null) {
	  if(this.initX.length>=2) {
	  rt=this.getPoint(0.5)[1];
	  System.out.println("middle T to Distance"+rt);
	  }
	  }else {
		  System.out.println("init X error");
	  }
	  return rt;
	  
  }
  /**
   * Creates a new Spline2D.
   * @param x
   * @param y
   */
  public SplineGraph() {
	  this.distMap=new HashMap<DISTANCE,N>();
	  
  }
  public SplineGraph(double[] x, double[] y) {
    init(x, y);
  }

  private void init(double[] x, double[] y) {
    if (x.length != y.length) {
      throw new IllegalArgumentException("Arrays must have the same length.");
    }
    
    if (x.length < 2) {
      throw new IllegalArgumentException("Spline edges must have at least two points.");
    }
    
    this.initX=x;
    t = new double[x.length];
    t[0] = 0.0; // start point is always 0.0
    
    // Calculate the partial proportions of each section between each set
    // of points and the total length of sum of all sections
    for (int i = 1; i < t.length; i++) {
    	System.out.println("init() xxx("+i+")="+x[i]+"yyy("+i+")="+y[i]);
      double lx = x[i] - x[i-1];
      double ly = y[i] - y[i-1];
      // If either diff is zero there is no point performing the square root
      if ( 0.0 == lx ) {
        t[i] = Math.abs(ly);
      } else if ( 0.0 == ly ) {
        t[i] = Math.abs(lx);
      } else {
        t[i] = Math.sqrt(lx*lx+ly*ly);
      }
      
      length += t[i];
      t[i] += t[i-1];
    }
    
    for(int i = 1; i< (t.length)-1; i++) {
      t[i] = t[i] / length;
    }
    
    t[(t.length)-1] = 1.0; // end point is always 1.0
    
    splineX = new Spline(t, x);
    splineY = new Spline(t, y);
  }

  /**
   * @param t 0 <= t <= 1
   */
  public double[] getPoint(double t) {
    double[] result = new double[2];
    result[0] = splineX.getValue(t);
    result[1] = splineY.getValue(t);

    return result;
  }
  
  /**
   * Used to check the correctness of this spline
   */
  public boolean checkValues() {
    return (splineX.checkValues() && splineY.checkValues());
  }

  public double getDx(double t) {
    return splineX.getDx(t);
  }
  
  public double getDy(double t) {
    return splineY.getDx(t);
  }
  
  public Spline getSplineX() {
    return splineX;
  }
  
  public Spline getSplineY() {
    return splineY;
  }
  
  public double getLength() {
    return length;
  }

}

/* This code is PUBLIC DOMAIN */


/**
 * Interpolates given values by B-Splines.
 * 
 * @author krueger
 */
 class Spline {

  private double[] xx;
  private double[] yy;

  private double[] a;
  private double[] b;
  private double[] c;
  private double[] d;

  /** tracks the last index found since that is mostly commonly the next one used */
  private int storageIndex = 0;

  /**
   * Creates a new Spline.
   * @param xx
   * @param yy
   */
  public Spline(double[] xx, double[] yy) {
    setValues(xx, yy);
  }

  /**
   * Set values for this Spline.
   * @param xx
   * @param yy
   */
  public void setValues(double[] xx, double[] yy) {
    this.xx = xx;
    this.yy = yy;
    if (xx.length > 1) {
      calculateCoefficients();
    }
  }

  /**
   * Returns an interpolated value.
   * @param x
   * @return the interpolated value
   */
  public double getValue(double x) {
	  System.out.println("getvalue x= "+x);
    if (xx.length == 0) {
      return Double.NaN;
    }

    if (xx.length == 1) {
      if (xx[0] == x) {
        return yy[0];
      } else {
        return Double.NaN;
      }
    }

    int index = Arrays.binarySearch(xx, x);
    if (index > 0) {
    	System.out.println("getvalue yy= "+yy[index]);
      return yy[index];
    }

    index = - (index + 1) - 1;
    //TODO linear interpolation or extrapolation
    if (index < 0) {
    	System.out.println("getvalue yy0= "+yy[0]);
      return yy[0];
    }
    double rt= a[index]
    	      + b[index] * (x - xx[index])
    	      + c[index] * Math.pow(x - xx[index], 2)
    	      + d[index] * Math.pow(x - xx[index], 3);
    System.out.println("getvalue return final= "+rt);
    return rt;
  }

  /**
   * Returns an interpolated value. To be used when a long sequence of values
   * are required in order, but ensure checkValues() is called beforehand to
   * ensure the boundary checks from getValue() are made
   * @param x
   * @return the interpolated value
   */
  public double getFastValue(double x) {
    // Fast check to see if previous index is still valid
    if (storageIndex > -1 && storageIndex < xx.length-1 && x > xx[storageIndex] && x < xx[storageIndex + 1]) {

    } else {
      int index = Arrays.binarySearch(xx, x);
      if (index > 0) {
        return yy[index];
      }
      index = - (index + 1) - 1;
      storageIndex = index;
    }
  
    //TODO linear interpolation or extrapolation
    if (storageIndex < 0) {
      return yy[0];
    }
    double value = x - xx[storageIndex];
    return a[storageIndex]
          + b[storageIndex] * value
          + c[storageIndex] * (value * value)
          + d[storageIndex] * (value * value * value);
  }

  /**
   * Used to check the correctness of this spline
   */
  public boolean checkValues() {
    if (xx.length < 2) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Returns the first derivation at x.
   * @param x
   * @return the first derivation at x
   */
  public double getDx(double x) {
    if (xx.length == 0 || xx.length == 1) {
      return 0;
    }

    int index = Arrays.binarySearch(xx, x);
    if (index < 0) {
      index = - (index + 1) - 1;
    }

    return b[index]
      + 2 * c[index] * (x - xx[index])
      + 3 * d[index] * Math.pow(x - xx[index], 2);
  }

  /**
   * Calculates the Spline coefficients.
   */
  private void calculateCoefficients() {
    int N = yy.length;
    a = new double[N];
    b = new double[N];
    c = new double[N];
    d = new double[N];

    if (N == 2) {
      a[0] = yy[0];
      b[0] = yy[1] - yy[0];
      return;
    }

    double[] h = new double[N - 1];
    for (int i = 0; i < N - 1; i++) {
      a[i] = yy[i];
      h[i] = xx[i + 1] - xx[i];
      // h[i] is used for division later, avoid a NaN
      if (h[i] == 0.0) {
        h[i] = 0.01;
      }
    }
    a[N - 1] = yy[N - 1];

    double[][] A = new double[N - 2][N - 2];
    double[] y = new double[N - 2];
    for (int i = 0; i < N - 2; i++) {
      y[i] =
        3
          * ((yy[i + 2] - yy[i + 1]) / h[i
            + 1]
            - (yy[i + 1] - yy[i]) / h[i]);

      A[i][i] = 2 * (h[i] + h[i + 1]);

      if (i > 0) {
        A[i][i - 1] = h[i];
      }

      if (i < N - 3) {
        A[i][i + 1] = h[i + 1];
      }
    }
    solve(A, y);

    for (int i = 0; i < N - 2; i++) {
      c[i + 1] = y[i];
      b[i] = (a[i + 1] - a[i]) / h[i] - (2 * c[i] + c[i + 1]) / 3 * h[i];
      d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
    }
    b[N - 2] =
      (a[N - 1] - a[N - 2]) / h[N
        - 2]
        - (2 * c[N - 2] + c[N - 1]) / 3 * h[N
        - 2];
    d[N - 2] = (c[N - 1] - c[N - 2]) / (3 * h[N - 2]);
  }

  /**
   * Solves Ax=b and stores the solution in b.
   */
  public void solve(double[][] A, double[] b) {
    int n = b.length;
    for (int i = 1; i < n; i++) {
      A[i][i - 1] = A[i][i - 1] / A[i - 1][i - 1];
      A[i][i] = A[i][i] - A[i - 1][i] * A[i][i - 1];
      b[i] = b[i] - A[i][i - 1] * b[i - 1];
    }

    b[n - 1] = b[n - 1] / A[n - 1][n - 1];
    for (int i = b.length - 2; i >= 0; i--) {
      b[i] = (b[i] - A[i][i + 1] * b[i + 1]) / A[i][i];
    }
  }
}