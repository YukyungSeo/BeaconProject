package beaconLocation;

public class IDandDIST {
	String id;
	
	public IDandDIST(String mid) {
		this.id=mid;
	}
	public boolean check(String iid){
		if(iid.equals(this.id)) {
			
			return true;
		}else return false;
		
	}

}
