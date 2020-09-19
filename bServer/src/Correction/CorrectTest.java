package Correction;

import java.io.IOException;
import beaconLocation.ComputeBeaconTest;
import bleDistance.ComputeTester;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CorrectTest {
    static String TESTMODE_BENCHSET="benchsettest";
    static String TESTMODE_TXT="txttest";

    public static void main (String args[]) throws IOException {
    	
    	ComputeBeaconTest beaconTester = new ComputeBeaconTest();
    	beaconTester.test();
    	ArrayList<Phone> phoneList = beaconTester.getPhoneArr();	//use this
    	
        ComputeTester tester= new ComputeTester();
        tester.startTest(TESTMODE_BENCHSET,".\temp_bleResult.csv");
        Correct correct = new Correct(2, 2, 9, 9, fillList(),tester.getMSpline());
        correct.run();


    }
    
    private static ArrayList fillList(){
       ArrayList tmp  = new ArrayList();
       // 梨꾩썙�빞�븿

       return tmp;
    }
}
