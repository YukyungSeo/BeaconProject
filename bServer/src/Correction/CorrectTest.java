package Correction;

import java.io.IOException;
import beaconLocation.ComputeBeaconTest;
import java.util.ArrayList;

import pack.ComputeTester;

public class CorrectTest {
    static String TESTMODE_BENCHSET="benchsettest";
    static String TESTMODE_TXT="txttest";

    public static void main (String args[]) throws IOException, ClassNotFoundException {
        ComputeBeaconTest beaconTester = new ComputeBeaconTest();
        beaconTester.test();
        ArrayList<Phone> phoneList = beaconTester.getPhoneArr();	//use this

        ComputeTester tester= new ComputeTester();
        tester.startTest(TESTMODE_BENCHSET,"C.\temp_bleResult.csv");

        Correct correct = new Correct(2, 2, 9, 9, phoneList,tester.getMSpline());
        correct.run();
    }
}
