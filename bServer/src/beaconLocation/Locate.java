package beaconLocation;

public class Locate {
	
	
	String myid;
	double[] distanceX;	// -0.45 ~ 9.45 
	double[] distanceY;	//
	int finalLocateX;
	int finalLocateY;
	
	double padding; // 0 ~ 4.5 meter;
	double space; //실험공간 n X n 의 n 미터 
	int distanceSize;
	
	int temp;
	int run;
	int numGrid ;	// 패딩 영역 제외 격자 한 축의 개수 = 2 로 설정
	
	JointProb jointP;
	
	
	public Locate(String id) {
		
		
		this.myid=id;
		jointP= new JointProb();
		this.distanceSize=0;
		this.space=9.0;
		this.distanceX= new double[1000];
		this.distanceY=new double[1000];
		
		this.run=0;
		this.temp=0;
	
	}
	

	public int[] getFinalLocate() {
		
		return new int[]{this. finalLocateX, this.finalLocateY};
	}
	public Locate getLocateById(String mid) {
		
		if(this.myid.equals(mid)) {
			return this;
		}else {
			return null;
		}
	}
	
	public void setVirtualData() {
		
	}
	
		
	
	public void setPaddingAndSpaceSize(int grid,double pd,double sp) {
		//패딩 사이즈와 테스트 공간의 사이즈 입력
		this.numGrid=grid;
		this.padding=pd;
		this.space=sp;
	}
	public void setServerData(double distx,double disty) {
		//서버 DB의 데이터로부터 id와 distance 입력
		
		if(distx>=-this.padding&&disty>=-this.padding&&distx<this.padding+space&&disty<this.padding+space) {
		System.out.println(distanceSize+ "distx"+distx);
		this.distanceX[distanceSize]=distx;
		this.distanceY[distanceSize]=disty;
		distanceSize++;
		
		}
	}
	
	public void setFinalLocate() {
		
		int sumDistanceX=0;
		int sumDistanceY=0;
		
		//distance To Location:
		for(int i=0; i<this.distanceSize; i++) {
			System.out.println("setfinallocate distance"+this.distanceX[i]+" "+this.distanceY[i]);
			sumDistanceX+=this.distanceX[i];
			sumDistanceY+=this.distanceY[i];
		}
		double finalDistanceX=sumDistanceX/this.distanceSize;

		double finalDistanceY=sumDistanceY/this.distanceSize;
		
		int tempx=(int)(finalDistanceX/(space/numGrid));
		int tempy=(int)(finalDistanceY/(space/numGrid));
		
		double tempxx=finalDistanceX/(space/numGrid);
		double tempyy=finalDistanceY/(space/numGrid);
		
		if(tempxx>=tempx+0.5) {
			this.finalLocateX=tempx+1;
			
		}else {
			this.finalLocateX=tempx;
		}
		if(tempyy>=tempy+0.5) {
			this.finalLocateY=tempy+1;
			
		}else {
			this.finalLocateY=tempx;
		}
		
		
		
		
		
	}

}
