package pack;

public class SplineGraph {	// N�� ������ ���� ���ö��� ��� �׸��� Ŭ����.
	
	double[] dotX;
	double[] dotY;
	int dotSize;
	
	public SplineGraph() {
		dotX=new double[150];
		dotY=new double[150];
		dotSize=0;
		
	}
	
	public void setDots(double[] xs, double[] ys) {
		for(int i=0; i<xs.length; i++) {
			if(xs.length<150) {
				this.dotX[i]=ComputingMethods.cutDecimalPoint(xs[i]);
				this.dotY[i]=ComputingMethods.cutDecimalPoint(ys[i]);
				this.dotSize++;
			}else {
				i=xs.length;
			}
			
		}
		
	}
	
	public void setGraph() {
		
	}
	public double graphOutput(double inputX) {
		double outputY=0.0;
		
		
		
		return ComputingMethods.cutDecimalPoint(outputY);
	}

}
