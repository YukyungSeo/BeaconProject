package beaconLocation;

public class Locate {
	
	
	String myid;
	double[] distanceX;	// -0.45 ~ 9.45 
	double[] distanceY;	//
	int[] locateX;	//-1,0, 1,2 (0�� 2�� padding ����) 
	int[] locateY;	//0, 1
	int finalLocateX;
	int finalLocateY;
	
	double padding; // 0 ~ 4.5 meter;
	double space; //������� n X n �� n ���� 
	int distanceSize;
	int locateSize;
	
	double realDistY;
	double realDistX;
	int realLocateX;
	int realLocateY;
	int temp;
	int run;
	int numGrid ;	// �е� ���� ���� ���� �� ���� ���� = 2 �� ����
	
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
		
		//���� ������ �׽�Ʈ ��, ������ �����ͷ� �׽�Ʈ
		this.myid="VP01";
		this.distanceX= new double[]{-0.1, 2,4, 6,6,7,8,9.9,10};
		this.distanceY= new double[]{8, 5.3,7.5, 0.2,9.3,-1.1,4.4,2.2,7.7};
		this.distanceSize=distanceX.length;
		
		
	}
	public void filterAndsetLocate() {
		System.out.println(this.myid+"+++++++++++++++++++++++++++++++++++\n"
				
						);
		//distance X,Y�� �Էµ� ���� �Ÿ����� ��ǥ������ �ٲٱ�
		// padding ������� ��� distance���� ������
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
					//�ϴ� ������ 1ĭ ���ڰŸ� �̳��� distance ������ ��ճ��� finalLocate�� ����:
					num++;
					sumdistx+=distanceX[n];
					sumdisty+=distanceY[n];
					System.out.println("sumdistX - "+sumdistx+"   sumDIsty - "+sumdisty+" num - "+num);
					
					
					
					
					
					
					
				//�� �Ÿ����� ���� ����
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
		//�е� ������� �׽�Ʈ ������ ������ �Է�
		this.numGrid=grid;
		this.padding=pd;
		this.space=sp;
	}
	public void setServerData(double distx,double disty) {
		//���� DB�� �����ͷκ��� id�� distance �Է�
		
		this.distanceX[distanceSize]=distx;
		this.distanceY[distanceSize]=disty;
		distanceSize++;
		
		
	}
	
	public void setFinalLocate() {
		//Ȯ�� ����Ͽ� ���� ��ǥ ���ϱ�
		// distanceX,Y �Ǵ� locateX,Y�� �̿��Ͽ� finalLocate ��ǥ 1�� ����
		
		//�ϴ� ���� ���� ���� ���� x,y�θ� set
		
		
		
	}

}
