package beaconLocation;

import java.util.ArrayList;

import Correction.Phone;
public class ComputeBeaconTest {
	ArrayList<Phone> phoneArr;
	public ComputeBeaconTest() {
		this.phoneArr=null;
	}
	public ArrayList<Phone> getPhoneArr(){
		return this.phoneArr;
	}
	public void test() {
		
		Locate[] locateById= new Locate[50];
		int idsize=0;
		phoneArr= new ArrayList<Phone>();
		
		DataRemoteConnector_beacons dtConn = new DataRemoteConnector_beacons();
		try {
			dtConn.connect(locateById);
			idsize=dtConn.midSize();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(locateById[0]!=null) {
			for(int n=0; n<idsize; n++) {
				locateById[n].setPaddingAndSpaceSize(9,3,9); //grid, padding, space
				locateById[n].filterAndsetLocate();
				phoneArr.add(new Phone(locateById[n].myid,locateById[n].finalLocateX,locateById[n].finalLocateY));
				/*
				System.out.println(
						
						"compute Locate XY  - "+ locateById[n].finalLocateX+" "+locateById[n].finalLocateY+"\n"
						+"real XY  - "+ locateById[n].realDistX+" "+locateById[n].realDistY+"\n"
								+"padding - "+locateById[n].padding+"\n"
								+"space - "+locateById[n].space+"\n"
						+"grid size - "+locateById[n].numGrid+"\n"
						+"locate size - "+locateById[n].locateSize+"\n"
						);//locateById[n].setFinalLocate();
						*/
			}
			
		}
		
	}
	
	public static void main(String args[]) {
		
		
		
		
		
	}
	
	
	
	
	
	

}
