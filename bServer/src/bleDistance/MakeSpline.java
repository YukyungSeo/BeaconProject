package bleDistance;


public class MakeSpline {
	
	String[] myids;
	String[] yourids;
	SplineGraph[] splines;
	int size;
	
	public MakeSpline() {
		this.size=0;
		this.myids=new String[40];
		this.yourids= new String[40];
		this.splines= new SplineGraph[40];
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
	
	public void checkIDandInsertSIG(String mid, String yid, double distance, double nnn, int sigsize) {
		if(this.size==0) {
			this.myids[0]=mid;
			this.yourids[0]=yid;
			this.size=1;
			this.splines[0]=new SplineGraph();
		}else {
			boolean findsameid=false;
		for (int i=0; i<this.size; i++){
			if(this.myids[i].equals(mid)&&this.yourids[i].equals(yid)) {
				findsameid=true;
				
				this.splines[i].inputMapData( distToEnum(distance), nnn,sigsize);
				i=this.size;
				
			}}
			if(!findsameid) {
				this.myids[this.size]=mid;
				this.yourids[this.size]=yid;
				this.splines[this.size]=new SplineGraph();
				this.splines[this.size].inputMapData( distToEnum(distance), nnn,sigsize);
				this.size++;
			}
		}
	}
	public double insertMiddleT(int ind) {
		return this.splines[ind].insertMiddleT();
	}
	public double[] checkINDEXandInsertX(int ind, double x) {
		if(this.splines[ind].getSplineX()==null) {
			return new double[] {-1,-1};
		}else {
		return this.splines[ind].insert(x);
		}
		
	}
	public double[] checkIDandInsertX(String mid, String yid,double x) {
		
		int index=-1;
		for(int i=0; i<this.size; i++) {
			if(this.myids[i].equals(mid)&&this.yourids[i].equals(yid)) {
				index=i;
				i=this.size;
			}
		}
		if(index>-1) {
			if(this.splines[index].getSplineX()==null) {
				return new double[] {-1,-1};
			}else {
			return this.splines[index].insert(x);
			}
		}else {
			return new double[] {-1,-1};
		}
		
	}
	public void makeSpline() {
		for(int i=0; i<this.size; i++) {
			this.splines[i].MapDataToSpline(this.myids[i], this.yourids[i]);
		}
	}
	
	

}
