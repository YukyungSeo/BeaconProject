package beaconLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;

import Correction.Phone;

public class DataRemoteConnector_sigdata {	//서버의 데이터베이스에 접근하여 데이터 불러오고 객체에 저장
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
   
    
    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;
    
    IDandDIST[] mids;
    Locate[] locateById;
    
    
	int midSize;
    Trilateration tri;
	public DataRemoteConnector_sigdata() {
		this.phoneArr= new ArrayList<Phone>();
		mids= new IDandDIST[50];
		midSize=0;
		tri=new Trilateration();
		locateById= new Locate[100];
	}
	public IDandDIST[] myids() {
		return this.mids;
	}
	public int midSize() {
		return this.midSize;
	}
	
	
	
	public void connect1_readData() throws ClassNotFoundException {
          try{
        	
        	 Class.forName(driver);
        	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
          } catch(SQLException e) {
        	   System.out.println(e.getMessage());
        	}
          
          Locate[] lct =this.locateById;
              try{
              Statement sts1 = con.createStatement();
              
              
              String sql1="SELECT id, B1_MAC, B1_RSSI, B2_MAC, B2_RSSI, B3_MAC, B3_RSSI, B4_MAC, B4_RSSI FROM beacon_testset";
              

              
              ResultSet rsb = sts1.executeQuery(sql1);
             if(rsb!=null) {
            	 
            	 while (rsb.next()) {
            		 
            				
         			String mid =rsb.getString("id");
         			
         			if(mid.equals("gal")) {
         				System.out.println(mid);
         				
         			String b1= rsb.getString("B1_MAC");
         			String r1= rsb.getString("B1_RSSI");
         			String b2= rsb.getString("B2_MAC");
         			String r2= rsb.getString("B2_RSSI");
         			String b3= rsb.getString("B3_MAC");
         			String r3= rsb.getString("B3_RSSI");
         			String b4= rsb.getString("B4_MAC");
         			String r4= rsb.getString("B4_RSSI");
         			if(r4!=null) {
         			
         			int ind=findSameId(mid);
         			//
     				beaconInfo[] bi= new beaconInfo[4];
     			
     				bi[0]= new beaconInfo(mid,b1,Integer.parseInt(r1));
     				bi[1]=new beaconInfo(mid,b2,Integer.parseInt(r2));
     				bi[2]=new beaconInfo(mid,b3,Integer.parseInt(r3));
     				bi[3]=new beaconInfo(mid,b4,Integer.parseInt(r4));
     				
     				
     				int n=0;
     						while(n<4) {
     							
     							
     					
     				
     					for(int j=3; j>0; j--) {
     					if(bi[j].rssi>bi[j-1].rssi) {
     						beaconInfo temp = bi[j];
     						bi[j]=bi[j-1];
     						bi[j-1]=temp;
     					}
     						
     					}
     					
     					n++;
     				}
         			
     				
     				int[] yx= tri.getRegion(bi,true);
     				int x= yx[1];
     				int y=yx[0];
     				System.out.println("xxxxx"+x);
     				System.out.println("yyyyy"+y);
     				
         			if(ind==-1) {
         				this.mids[midSize]=new IDandDIST(mid);
         				lct[midSize]= new Locate(mid);
         				
         				lct[midSize].setServerData(x,y);
         				this.midSize++;
         				
         			}else {
         				lct[ind].setServerData(x,y);
         				
         			}
         			
         			
         			
                 }
            	 }
            	 }
                      }
            	  
            	  
              
              
              }
              catch (SQLException s){
              System.out.println("SQL statement is not executed! 222");
              }
              
	}
	public void setFinalLocation() {
		
		if(locateById[0]!=null) {
			for(int n=0; n<midSize; n++) {
				
				locateById[n].setPaddingAndSpaceSize(3,1,9); //grid, padding, space
				locateById[n].setFinalLocate();
				phoneArr.add(new Phone(locateById[n].myid,locateById[n].finalLocateX,locateById[n].finalLocateY));
				
				
						
			}
		}
		
	}
	public void connect2_insertFinalLocation() throws ClassNotFoundException {
		Locate[] lct =this.locateById;
		
        try{
      	
      	 Class.forName(driver);
      	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
        } catch(SQLException e) {
      	   System.out.println(e.getMessage());
      	}
        
 
            try{
          	  for(int y=0; y<midSize; y++) {
          		System.out.println("insertFinal"+y);
          		  String sql1="INSERT INTO beacon_location (id,x,y) VALUES (?,?,?)";
          		  PreparedStatement  sts1 = (PreparedStatement) con.prepareStatement(sql1);
          		  sts1.setString(1,lct[y].myid);
          		  sts1.setString(2,Integer.toString(lct[y].finalLocateX));
          		  sts1.setString(3,Integer.toString(lct[y].finalLocateY));
            sts1.executeUpdate();

          	  }
          	  
          	  
            
            
            }
            catch (SQLException s){
            System.out.println("SQL statement is not executed!");
            }
            
	}
	private int findSameId(String newId) {
		int ind=-1;
		for(int i=0; i<this.midSize; i++) {
			if(this.mids[i].check(newId)) {
				ind=i;
				i=this.midSize;
			}
			
		}
		
		
		return ind;
		
	}
	ArrayList<Phone> phoneArr;
	
	
	public ArrayList<Phone> getPhoneArr(){
		return this.phoneArr;
	}
	

}
