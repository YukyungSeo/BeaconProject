package pack;

 public class ComputingMethods {	//신호 계산 함수들. static method만 있음
	 // ***** filtered rssi를 구하여 si 객체에 저장하고 갱신될 때마다 거리값을 구함.
	 public static void valueFilter(SIGINF si,double N){
		
		 
	    	int rssi=0;
	    	for(int p=0; p<si.rSize; p++) {
	    		rssi=si.rssi[p];
	    		if(p>10) {
	    			si.stackFRSSI((rssi* (p - 1) / p) + (si.getFilter().update(rssi) / p));
	    			si.setDistance(Math.pow(10, ((si.getTxp() - si.getNewFSssi()) / (10 * N)))*si.getWeight());
	    			
	    		}else if(p==10){
	    			 si.stackFRSSI((si.rssi[4] + si.rssi[5]) / 2);
	    			 si.getFilter().update(si.getNewFSssi());
	    			
	    		}
	    	}
	    }
	 // ***** param rssi와 N 값으로 distance를 구함
	 public static double computeDist(double rssi, double N) {
		 return Math.pow(10, ((-20 - rssi) / (10 * N)))*1;
	 }
	// ***** rssi만 필터링하여 si 객체에 저장함
	 public static void valueFilteredRssi(SIGINF si) {
		 int rssi=0;
	    	for(int p=0; p<si.rSize; p++) {
	    		rssi=si.rssi[p];
	    		if(p>10) {
	    			si.stackFRSSI((rssi* (p - 1) / p) + (si.getFilter().update(rssi) / p));
	    			
	    		}else if(p==10){
	    			 si.stackFRSSI((si.rssi[4] + si.rssi[5]) / 2);
	    			 si.getFilter().update(si.getNewFSssi());
	    		}
	    		
	    	}
	 }
	 
	// ***** si 객체의 칼만 필터링한 rssi(frssi)의 평균값을 구하여 저장
	 public static void computeAverageFRSSI(SIGINF si) {
		 double computed=0;
		 for(int p=0; p<si.fSize; p++) {
			 computed+=si.getFilteredRssi()[p];
			 
		 }
		 computed=computed/si.fSize;
		 si.averageFrssi=computed;
		 
	 }
	 
	// ***** frssi 평균값과 실제거리로 N값을 계산
	 public static void computeN(SIGINF si, int realDist) {
		 
		 double c_N=0;
		 double c_rssi=si.averageFrssi;
		 double c_dist=realDist; //realDistance!!
		 double c_txp=si.txP;
		 double c_weight=si.weight;
		 
		 double A=c_txp-c_rssi;
		 
		 double B=0.1;
		 double C=Math.log10(c_dist/c_weight);
		 C=1/C;
		 c_N=A*B*C;
		 
		 si.setN(c_N);
	 }
	 
	 // ***** 실수의 소수점 2자리까지만 자름
	 public static double cutDecimalPoint(double val) {
		 return Math.floor(val*100)/100;
	 }

}
