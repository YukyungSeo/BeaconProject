package beaconLocation;

public class Locate {
	
	
	String myid;
	double[] distanceX;	// -0.45 ~ 9.45 
	double[] distanceY;	//
	int[] locateX;	//-1,0, 1,2 (0과 2는 padding 영역) 
	int[] locateY;	//0, 1
	int finalLocateX;
	int finalLocateY;
	
	double padding; // 0 ~ 4.5 meter;
	double space; //실험공간 n X n 의 n 미터 
	int distanceSize;
	int locateSize;
	
	double realDistY;
	double realDistX;
	int realLocateX;
	int realLocateY;
	int temp;
	int run;
	int numGrid ;	// 패딩 영역 제외 격자 한 축의 개수 = 2 로 설정
	
	JointProb jointP;
	
	
	public Locate(String id, double realX,double realY) {
		
		
		this.myid=id;
		jointP= new JointProb();
		this.distanceSize=0;
		this.locateSize=0;
		this.space=9.0;
		this.distanceX= new double[300];
		this.distanceY=new double[300];
		this.locateX=new int[300];
		this.locateY=new int[300];
		this.realDistX=realX;
		this.realDistY=realY;
		this.run=0;
		realLocateX=(int)(realX/(space/numGrid));
		realLocateY=(int)(realY/(space/numGrid));
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
		
		//서버 데이터 테스트 전, 가상의 데이터로 테스트
		this.myid="VP01";
		this.distanceX= new double[]{-0.1, 2,4, 6,6,7,8,9.9,10};
		this.distanceY= new double[]{8, 5.3,7.5, 0.2,9.3,-1.1,4.4,2.2,7.7};
		this.distanceSize=distanceX.length;
		
		
	}
	public void filterAndsetLocate() {
		System.out.println(this.myid+"+++++++++++++++++++++++++++++++++++\n"
				
						);
		//distance X,Y에 입력된 공간 거리값을 좌표값으로 바꾸기
		// padding 사이즈에서 벗어난 distance값은 버리기
		int num=0;
		this.run=0;
		double sumdistx=0;
		double sumdisty=0;
		double tempX=0;
		double tempY=0;
		for(int n=0; n<this.distanceSize; n++) {
			System.out.println("distance XY - "+distanceX[n]+" "+distanceY[n]);
			if(distanceX[n]>(space+padding)||distanceX[n]<(-padding)) {
				
			}else {
				
			if(distanceY[n]>(space+padding)||distanceY[n]<(-padding)) {
				
			}else {
				
					}
				
				if(distanceX[n]<=realDistX+padding&&distanceX[n]>=realDistX-padding&&(distanceY[n]<=realDistY+padding)&&(distanceY[n]>=realDistY-padding)) {
					//일단 인접한 1칸 격자거리 이내의 distance 값들을 평균내서 finalLocate에 저장:
					num++;
					sumdistx+=distanceX[n];
					sumdisty+=distanceY[n];
					System.out.println("sumdistX - "+sumdistx+"   sumDIsty - "+sumdisty+" num - "+num);
					
					
					
					
					
					
					
				//각 거리값의 격자 산출
				if(distanceX[n]<0&&(distanceX[n]>=(-padding))) {
					
					locateX[locateSize]=-1;
				}else if(distanceX[n]>=0&&distanceX[n]<this.space) {
					
					locateX[locateSize]=(int)(distanceX[n]/(space/this.numGrid));
					
				}else if(distanceX[n]>=this.space&&distanceX[n]<=(space+padding)) {
					
					locateX[locateSize]=(int)(numGrid);
					
				}if(this.myid.equals("ttt")) {
					
				}
				
				if(distanceY[n]<0&&(distanceY[n]>=(-padding))) {
					
					locateY[locateSize]=-1;
				}else if(distanceY[n]>=0&&distanceY[n]<this.space) {
					
					locateY[locateSize]=(int)(distanceY[n]/(space/this.numGrid));
					
				}else if(distanceY[n]>=this.space&&distanceY[n]<=(space+padding)) {
					
					locateY[locateSize]=(int)(numGrid);
					
				}
				
				
			}else {System.out.println("out");}
				this.locateSize++;
			}
			
	
			
			
		if(num>0) {
			tempX=sumdistx/num;
			
			tempY=sumdisty/num;
			
			
			double templocateX=tempX/(space/numGrid);
			double templocateY=tempY/(space/numGrid);
			
			if(templocateX<0) {
				this.finalLocateX=-1;
			}else {
				this.finalLocateX=(int)templocateX;
			}
				if(templocateY<0) {
					this.finalLocateY=-1;
			}else {
				this.finalLocateY=(int)templocateY;
			}
			
		}
		if(this.myid.equals("lbtest")) {
			}
		for(int i=0; i<this.locateSize; i++) {
		
		}
		
			///////////////////
			
		}
		
	}
	
		
	
	public void setPaddingAndSpaceSize(int grid,double pd,double sp) {
		//패딩 사이즈와 테스트 공간의 사이즈 입력
		this.numGrid=grid;
		this.padding=pd;
		this.space=sp;
	}
	public void setServerData(double distx,double disty) {
		//서버 DB의 데이터로부터 id와 distance 입력
		
		this.distanceX[distanceSize]=distx;
		this.distanceY[distanceSize]=disty;
		distanceSize++;
		
		
	}
	
	public void setFinalLocate() {
		//확률 기반하여 최종 좌표 정하기
		// distanceX,Y 또는 locateX,Y를 이용하여 finalLocate 좌표 1개 생성
		
		//일단 가장 많은 분포 가진 x,y로만 set
		
		
		
	}

}
