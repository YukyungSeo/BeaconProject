package Correction;

import java.io.IOException;
import beaconLocation.DataRemoteConnector_sigdata;
import java.util.ArrayList;

public class CorrectTest {
    public static void main (String args[]) throws IOException, ClassNotFoundException {
        DataRemoteConnector_sigdata dataRemoteConnector_sigdata = new DataRemoteConnector_sigdata();
        ArrayList<Phone> phoneList = dataRemoteConnector_sigdata.getPhoneArr();	//use this

        Correct correct = new Correct(2, 2, 9, 9, phoneList);
        correct.run();
    }
}
