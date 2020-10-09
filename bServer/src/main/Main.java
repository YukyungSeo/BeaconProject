package main;
import java.io.IOException;
import java.util.ArrayList;

import Correction.Correct;
import Correction.Phone;
import beaconLocation.DataConnector_splineData;
import beaconLocation.DataRemoteConnector_sigdata;
import bleDistance.ComputeTester;

public class Main {
	static String TESTMODE_BENCHSET="benchsettest";
	static String TESTMODE_TXT="txttest";
	static boolean beaconSpline=true;
	static boolean beaconLocation=true;
	static boolean ble=true;
	static boolean correction=false;
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		
		//
		// ... BEACON
		//


		DataConnector_splineData spConn = new DataConnector_splineData();
		DataRemoteConnector_sigdata sigConn = new DataRemoteConnector_sigdata();
		if(beaconSpline) {
			spConn.setRawData();
			spConn.makeSpline();
			spConn.insertSplineData();
		}
		if(beaconLocation) {
			sigConn.connect1_readData();
			sigConn.setFinalLocation();
			sigConn.connect2_insertFinalLocation();
			
		}
		
		
		
		//
		// ... BLE
		//
		ComputeTester tester=null;
		if(ble) {
			tester= new ComputeTester();
			tester.startTest(TESTMODE_BENCHSET,"./temp_bleResult.csv");
		}
	        
		//
		// ... CORRECT
		//
		Correct correct;
		if(correction) {
			correct = new Correct(2, 2, 9, 9, fillList(),tester.getMSpline());
			correct.run();
		}
		
		
     
	}
	 private static ArrayList fillList(){
	       ArrayList tmp  = new ArrayList();
	       // 梨꾩썙�빞�븿

	       return tmp;
	    }
}
