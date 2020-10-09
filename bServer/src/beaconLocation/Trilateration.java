package beaconLocation;

import lemmingapex.NonLinearLeastSquaresSolver;
import lemmingapex.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;



public class Trilateration {
	
	static final String LeftFrontBeacon = "[EB:6E:33:1D:98:21]" ;   //"[E2:D7:D0:CF:90:3E]";   //분홍색
	static final String RightFrontBeacon = "[F0:34:3C:EF:45:80]"; //"[F6:E4:00:F5:00:29]";   //노란색
	static final String LeftBackBeacon = "[F0:BB:B4:1F:28:5A]";    //노란색
	static final String RightBackBeacon = "[F9:FB:14:46:7C:41]";  //보라색
	
	
	
	
	public int[] getRegion(beaconInfo[] bs){

		 Point2D shortest1 = getPoint2D(bs[0]);
	        Point2D shortest2 = getPoint2D(bs[1]);
	        Point2D shortest3 = getPoint2D(bs[2]);
	        Point2D shortest4 = getPoint2D(bs[3]);

	        double[][] positions = new double[][] {
	                {shortest1.getX(),shortest1.getY()},
	                {shortest2.getX(),shortest2.getY()},
	                {shortest3.getX(),shortest3.getY()},
	                {shortest4.getX(),shortest4.getY()}};

        double[] distances = new double[] {
                shortest1.getDistance(),
                shortest2.getDistance(),
                shortest3.getDistance(),
                shortest4.getDistance()};

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions,distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

//        Point2D triPoint = Trilateration.getTrilateration(shortest1,shortest2,shortest3);

        double[] temp = optimum.getPoint().toArray();
        int[] region = new int[temp.length];

        for(int i=0; i<region.length;i++){
            region[i] = (int) temp[i];
        }
        return region;
    }
	
	private  Point2D getPoint2D(beaconInfo bs) {
        Point2D point2D = new Point2D();
        double TXpower = -76;
        double n = 2;
        double rssi = bs.rssi;

        switch (bs.mac){
            case LeftFrontBeacon:
            	//System.out.println("beacon, alright");
                point2D.setX(0);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case RightFrontBeacon:
                point2D.setX(0);
                point2D.setY(9);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case LeftBackBeacon:
                point2D.setX(9);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case RightBackBeacon:
                point2D.setX(9);
                point2D.setY(9);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            default:
            	break;
                //System.out.println("beacon, wrong beacon sign");
        }

        return point2D;
    }

}
