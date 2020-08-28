package pack;
import java.io.IOException;

public class Main {
	
	static String TESTMODE_BENCHSET="benchsettest";
	static String TESTMODE_TXT="txttest";
	
	public static void main(String args[]) throws IOException {
		ComputeTester tester= new ComputeTester();
		tester.startTest(TESTMODE_BENCHSET,"./computeBleResult.csv");
	}
	
	
	
	
}
