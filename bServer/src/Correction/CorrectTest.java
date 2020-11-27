package Correction;

import java.io.IOException;
import java.util.ArrayList;

public class CorrectTest {
//    static String TESTMODE_BENCHSET="benchsettest";
//    static String TESTMODE_TXT="txttest";

    public static void main (String args[]) throws IOException, ClassNotFoundException {
//        ComputeBeaconTest beaconTester = new ComputeBeaconTest();
//        beaconTester.test();
//        ArrayList<Phone> phoneList = beaconTester.getPhoneArr();	//use this
//
//        ComputeTester tester= new ComputeTester();
//        tester.startTest(TESTMODE_BENCHSET,"C.\temp_bleResult.csv");

        DBConnector dbConnector = new DBConnector();
        ArrayList<Phone> phoneList = dbConnector.getBeaconLocation();

        if(phoneList != null){
            Correct correct = new Correct(2, 2, 9, 9, phoneList);
            correct.run();
        }
    }
}
