package beaconLocation;

import bleDistance.DISTANCE;
import bleDistance.SplineGraph;

public class beaconSplineSetting {
	public String id;
	public String mac;
	double[] rssi;
	double[] distance;
	int[] rssisize;
	int allsize;
	SplineGraph splines;
	boolean maked;
	
	public beaconSplineSetting(String i, String m) {
		maked=false;
		this.id=i;
		this.mac=m;
		this.rssisize=new int[50];
		this.allsize=0;
		this.splines= new SplineGraph();
		this.distance=new double[50];
		this.rssi=new double[50];
	}
	public DISTANCE distToEnum(double dist) {
		DISTANCE distenum=DISTANCE.dst_null;
		if(dist==1) {
			distenum=DISTANCE.dst_1m;
		}else if(dist==2) {
			distenum=DISTANCE.dst_2m;
		}else if(dist==3) {
			distenum=DISTANCE.dst_3m;
		}else if(dist==5) {
			distenum=DISTANCE.dst_5m;
		}else if(dist==6) {distenum=DISTANCE.dst_6m;
			
		}else if(dist==9) {
			distenum=DISTANCE.dst_9m;
		}else if(dist==7) {
			distenum=DISTANCE.dst_7m;
		}else if(dist==4) {
			distenum=DISTANCE.dst_4m;
		}else if(dist==8) {
			distenum=DISTANCE.dst_8m;
		}
		return distenum;
		
	}
	
	public void insertSIG(double rssi, double dist) {
		boolean findSameDist=false;
		for(int i=0; i<this.allsize; i++) {
			if(distance[i]==dist) {
				i=this.allsize;
				findSameDist=true;
				this.rssi[i]=(this.rssi[i]+rssi)/(this.rssisize[i]+1);
				
				
			}
		}
		if(!findSameDist) {
			distance[allsize]=dist;
			this.rssi[allsize]=rssi;
			this.rssisize[allsize]=1;
			allsize++;
		}
		
		
		
	}
	public double insertMiddleT() {
		return this.splines.insertMiddleT();
	}
	public double[] insertX( double x) {
		if(this.splines.getSplineX()==null) {
			return new double[] {-1,-1};
		}else {
		return this.splines.insert(x);
		}
		
	}
	public void makeSpline() {
			if(this.allsize>2) {
			this.splines.DataToSpline_beacon(this.rssi,this.distance,this.allsize);
			maked=true;
			}else {
				maked=false;
			}
		
	}
	public boolean check(String mid, String macc) {
		if(this.id.equals(mid)&&this.mac.equals(macc)) {
			return true;
		}else {return false;}
	}
}
