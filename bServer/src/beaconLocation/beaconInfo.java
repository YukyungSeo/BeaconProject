package beaconLocation;

public class beaconInfo {
	String pid;
	String mac;
	int rssi;
	double distanceBySpline;
	public beaconInfo(String id,String m, int rssii) {
		this.pid=id;
		this.mac=m;
		this.rssi=rssii;
	}
	public void setdistanceBySpline(double s) {
		this.distanceBySpline=s;
	}
}
