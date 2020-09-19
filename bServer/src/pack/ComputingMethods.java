package pack;

 public class ComputingMethods {	//��ȣ ��� �Լ���. static method�� ����
	 // ***** filtered rssi�� ���Ͽ� si ��ü�� �����ϰ� ���ŵ� ������ �Ÿ����� ����.
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
	 // ***** param rssi�� N ������ distance�� ����
	 public static double computeDist(double rssi, double N) {
		 return Math.pow(10, ((-20 - rssi) / (10 * N)))*1;
	 }
	// ***** rssi�� ���͸��Ͽ� si ��ü�� ������
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
	 
	// ***** si ��ü�� Į�� ���͸��� rssi(frssi)�� ��հ��� ���Ͽ� ����
	 public static void computeAverageFRSSI(SIGINF si) {
		 double computed=0;
		 for(int p=0; p<si.fSize; p++) {
			 computed+=si.getFilteredRssi()[p];
			 
		 }
		 computed=computed/si.fSize;
		 si.averageFrssi=computed;
		 
	 }
	 
	// ***** frssi ��հ��� �����Ÿ��� N���� ���
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
	 
	 // ***** �Ǽ��� �Ҽ��� 2�ڸ������� �ڸ�
	 public static double cutDecimalPoint(double val) {
		 return Math.floor(val*100)/100;
	 }

}
