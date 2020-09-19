package beaconLocation;

public class JointProb {
	//결합확률질량함수 계산
	
	 static int factorial(int n) {
	      int fact = 1;
	      int i = 1;
	      while(i <= n) {
	         fact *= i;
	         i++;
	      }
	      return fact;
	   }
	 static double combination(int n, int r ) {
		 return factorial(n) / (factorial(r) * factorial(n-r));
	 }
	 static double permutitaion(int n , int r) {
		 return factorial(n) / factorial(n-r);
	 }

}
