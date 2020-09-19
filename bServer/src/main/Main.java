package main;
import java.io.IOException;

import pack.ComputeTester;

public class Main {
	
	static String TESTMODE_BENCHSET="benchsettest";
	static String TESTMODE_TXT="txttest";
	public static void main(String args[]) throws IOException {
		ComputeTester tester= new ComputeTester();
		tester.startTest(TESTMODE_BENCHSET,"C.\temp_bleResult.csv");
	}
}
