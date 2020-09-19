package beaconLocation;

public class IDandDIST {
	String id;
	double realx;
	double realy;
	
	IDandDIST(String iid, double x, double y){
		this.id=iid;
		this.realx=x;
		this.realy=y;
	}
	public boolean check(String iid, double x, double y){
		if(iid.equals(this.id)&&this.realx==x&&this.realy==y) {
			
			return true;
		}else return false;
		
	}

}
