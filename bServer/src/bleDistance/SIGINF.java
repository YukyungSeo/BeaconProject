package bleDistance;


public class SIGINF {	//.. Signal Information

	
	public SIGINF() {
		
		rSize=0;
		fSize=0;
		rssi=new int[4000];
		filteredRssi=new double[4000];
		filter = new KalmanFilter(0.0f);
		weight=1;
		txP=-20;
		findN=0;
		averageRssi=1;
		averageFrssi=1;
		count=0;
		
	}
	public SIGINF(String tst) {
		
		rSize=0;
		fSize=0;
		rssi=new int[] {-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12,-10,-15,-11,-12,-12,-12};
		rSize=rssi.length;
		distance=99;
		filteredRssi=new double[4000];
		filter = new KalmanFilter(0.0f);
		weight=1;
		txP=-20;
		findN=0;
		averageRssi=1;
		averageFrssi=1;
		
	}
	public int fsize() {
		return this.fSize;
	}
	public int rSize;
	int fSize;
	int count;
	
	int txP;
	
	int[] rssi;
	double[] filteredRssi; 
	
	double weight;
	double findN;
	
	double averageRssi;
	double averageFrssi;
	
	public double getAveRssi() {
		return this.averageRssi;
	}
	public double getAveFrssi() {
		return this.averageFrssi;
	}
	public void setN(double n) {
		this.findN=n;
		
	}
	public double getN() {
		return this.findN;
	}
	
	  private KalmanFilter filter;
	  public KalmanFilter getFilter() {
	        return filter;
	    }
	    public void setFilter(KalmanFilter filter) {
	        this.filter = filter;
	    }
	
	double distance;
	
	public double getWeight() {
		return this.weight;
	}
	
	public void stackRSSI(int rssi) {
		if(count<30) {
			count++;
		}else {
		if(rSize<4000) {
		this.rssi[rSize]=rssi;
		this.rSize++;
		}
		}
		
	}
	public void stackFRSSI(double d) {
		if(fSize<4000) {
			this.filteredRssi[fSize]=d;
			this.fSize++;
			}
	}
	
	public void setTxp(int TX) {
		this.txP=TX;
	}
	
	public void setRssi(int[] Rssi) {
		this.rssi= Rssi;
	}
	public void setFilteredRssi(double[] filteded){
		this.filteredRssi=filteded;
	}
	public void setDistance(double dist) {
		this.distance=dist;
	}
	public int getTxp() {
		return this.txP;
	}
	public double[] getFilteredRssi(){
		return this.filteredRssi;
	}
	public int[] getRssi() {
		return this.rssi;
	}
	public double getDistance() {
		return this.distance;
	}
	public double getNewFSssi() {
		if(this.fSize>0) {
			return this.filteredRssi[fSize-1];
		}else return 0;
		
		
	}
	

}
