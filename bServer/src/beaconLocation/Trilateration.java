package beaconLocation;

public class Trilateration {

	static final String LeftFrontBeacon = "[EB:6E:33:1D:98:21]";   //"[E2:D7:D0:CF:90:3E]";   //��ȫ��
	static final String RightFrontBeacon = "[F0:34:3C:EF:45:80]"; //"[F6:E4:00:F5:00:29]";   //�����
	static final String LeftBackBeacon = "[F0:BB:B4:1F:28:5A]";    //�����
	static final String RightBackBeacon = "[F9:FB:14:46:7C:41]";  //�����


	DataConnector_readSpline conn;

	public Trilateration() {
		this.conn = new DataConnector_readSpline();
	}

	public void setID(String myid, String mac) {
		this.my_id = myid;
		this._mac = mac;
	}

	String my_id;
	String _mac;
	double XLength = 9.0;
	double YLength = 9.0;
	beaconInfo[] arrangedBeacon;


	public double[] getRegion(beaconInfo[] bs, boolean b) {

		if (b) {
			String logestone = longest(bs).getMac();
			double x, y;
			double leftfont = arrangedBeacon[0].getDistanceBySpline();
			double rightfront = arrangedBeacon[1].getDistanceBySpline();
			double leftback = arrangedBeacon[2].getDistanceBySpline();
			double rightback = arrangedBeacon[3].getDistanceBySpline();

			if (logestone.equals(LeftBackBeacon)) {
				x = (Math.pow(leftfont, 2) - Math.pow(rightfront, 2) + Math.pow(XLength, 2)) / (2 * XLength);
				y = (Math.pow(rightfront, 2) - Math.pow(rightback, 2) + Math.pow(YLength, 2)) / (2 * YLength);
			} else if (logestone.equals(RightBackBeacon)) {
				x = (Math.pow(leftfont, 2) - Math.pow(rightfront, 2) + Math.pow(XLength, 2)) / (2 * XLength);
				y = (Math.pow(leftfont, 2) - Math.pow(leftback, 2) + Math.pow(YLength, 2)) / (2 * YLength);
			} else if (logestone.equals(LeftFrontBeacon)) {
				x = (Math.pow(leftback, 2) - Math.pow(rightback, 2) + Math.pow(XLength, 2)) / (2 * XLength);
				y = (Math.pow(rightfront, 2) - Math.pow(rightback, 2) + Math.pow(YLength, 2)) / (2 * YLength);
			} else {
				x = (Math.pow(leftback, 2) - Math.pow(rightback, 2) + Math.pow(XLength, 2)) / (2 * XLength);
				y = (Math.pow(leftfont, 2) - Math.pow(leftback, 2) + Math.pow(YLength, 2)) / (2 * YLength);
			}

			double[] region = new double[2];
			region[0] = x;
			region[1] = y;

			return region;

		}
		return null;
	}

	private beaconInfo longest(beaconInfo[] bs){
		beaconInfo longone = bs[0];
		for(int i=1; i<4; i++){
			if(bs[i].getDistanceBySpline() > longone.getDistanceBySpline())
				longone = bs[i];
		}
		return longone;
	}

	private void arrangeBeacon (beaconInfo beacon) {
		switch (beacon.getMac()){
			case LeftFrontBeacon:
				arrangedBeacon[0] = beacon;
				break;
			case RightFrontBeacon:
				arrangedBeacon[1] = beacon;
				break;
			case LeftBackBeacon:
				arrangedBeacon[2] = beacon;
				break;
			case RightBackBeacon:
				arrangedBeacon[3] = beacon;
				break;
			default:
				System.out.println("wrong beacon sign");
		}
	}
}
