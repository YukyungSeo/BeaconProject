package beaconLocation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;

public class DataRemoteConnector_beacons {	//서버의 데이터베이스에 접근하여 데이터 불러오고 객체에 저장
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
   
    
    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;
    
    IDandDIST[] mids;
	int midSize;
    
	public DataRemoteConnector_beacons() {
		mids= new IDandDIST[50];
		midSize=0;
	}
	public IDandDIST[] myids() {
		return this.mids;
	}
	public int midSize() {
		return this.midSize;
	}
	
	
	
	
	
	public void connect(Locate[] lct ) throws ClassNotFoundException {
		  System.out.println("get beacons Test Datas ");
          try{
        	
        	 Class.forName(driver);
        	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
          } catch(SQLException e) {
        	   System.out.println(e.getMessage());
        	}
          
   
              try{
              Statement sts1 = con.createStatement();
              
              
              String sql1="SELECT id,real_x, real_y, cal_x, cal_y FROM beacons";
              System.out.println("connect");

              
              ResultSet rsb = sts1.executeQuery(sql1);
              System.out.println("query");
             if(rsb!=null) {
            	 
            	 while (rsb.next()) {
            		 String realx="";
            		 String realy="";
            		 if(rsb.getString("real_x").contains("x")||rsb.getString("real_x").contains("y") ) {
            			 realx="10000";
            			 realy="9999";
            		 }else {
            			realx=rsb.getString("real_x");
            			realy=rsb.getString("real_y");
            		 }
            		 
            				 
         			String mid =rsb.getString("id");
         			String calx= rsb.getString("cal_x");
         			String caly=rsb.getString("cal_y");
         			
         			double rx=Double.parseDouble(realx);
         			double ry=Double.parseDouble(realy);
         			int ind=findSameIdandDIST(mid,rx,ry);
         			if(ind==-1) {
         				this.mids[midSize]=new IDandDIST(mid,rx,ry);
         				System.out.println("db - "+mid+" "+rx+" "+ry);
         				lct[midSize]= new Locate(mid,rx,ry);
         				lct[midSize].setServerData(Double.parseDouble(calx),Double.parseDouble(caly));
         				this.midSize++;
         				
         			}else {
         				lct[ind].setServerData(Double.parseDouble(calx),Double.parseDouble(caly));
         				
         			}
         			
         			
         			
                 }
                      System.out.println("get beacon data" );

                      }
            	  
            	  
              
              
              }
              catch (SQLException s){
              System.out.println("SQL statement is not executed!");
              }
              
	}
	
	public int findSameIdandDIST(String newId,double xx, double yy) {
		int ind=-1;
		for(int i=0; i<this.midSize; i++) {
			if(this.mids[i].check(newId,xx,yy)) {
				ind=i;
				i=this.midSize;
			}
			
		}
		
		
		return ind;
		
	}
	

}
