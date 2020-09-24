package testCode;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import bleDistance.ComputingMethods;
import bleDistance.SIGINF;

public class BeaconTest {
	
	public static void main(String args[]) throws ClassNotFoundException {
		BeaconTest test = new BeaconTest();
		test.connect_and_query();
		test.print();
		
	}
	
	 String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
	   
	    
	    String dbUser = "juha";
	    String dbPasswd = "1234";
	    Connection con;
	    int[] testDist= new int[]{2,4,6,8};
	    SIGINF[][] sig = new SIGINF[testDist.length][4];
	    
	    public  BeaconTest() {
	    	for(int i=0; i<this.testDist.length; i++) {
	    		this.sig[i][0]=new SIGINF();
	    		this.sig[i][1]=new SIGINF();
	    		this.sig[i][2]=new SIGINF();
	    		this.sig[i][3]=new SIGINF();
	    		
	    		
	    		
	    	}
	    }
		
	    public void print() {
	    	
	    	 for(int i=0; i<this.testDist.length; i++) {
	    		 ComputingMethods.valueFilter(this.sig[i][0],2);
	    		 ComputingMethods.valueFilter(this.sig[i][1],2);
	    		 ComputingMethods.valueFilter(this.sig[i][2],2);
	    		 ComputingMethods.valueFilter(this.sig[i][3],2);
	    		 ComputingMethods.computeAverageFRSSI(this.sig[i][0]);
	    		 ComputingMethods.computeAverageFRSSI(this.sig[i][1]);
	    		 ComputingMethods.computeAverageFRSSI(this.sig[i][2]);
	    		 ComputingMethods.computeAverageFRSSI(this.sig[i][3]);
	    		 ComputingMethods.computeAverageRSSI(this.sig[i][0]);
	    		 ComputingMethods.computeAverageRSSI(this.sig[i][1]);
	    		 ComputingMethods.computeAverageRSSI(this.sig[i][2]);
	    		 ComputingMethods.computeAverageRSSI(this.sig[i][3]);
	    		 System.out.println(this.sig[i][0].rSize);
	    		 System.out.println("raw mean : "+testDist[i]+"m "+this.sig[i][0].getAveRssi()+", "+this.sig[i][1].getAveRssi()+", "+this.sig[i][2].getAveRssi()+", "+this.sig[i][3].getAveRssi());
	    		 
	    		 System.out.println("filtered : "+testDist[i]+"m "+this.sig[i][0].getAveFrssi()+", "+this.sig[i][1].getAveFrssi()+", "+this.sig[i][2].getAveFrssi()+", "+this.sig[i][3].getAveFrssi());
	    		 
	    		 
	    		 
	    	 }
	    	
	    }
		
		public void connect_and_query() throws ClassNotFoundException {
			  System.out.println("get Ble Test Datas ");
	          try{
	        	
	        	 Class.forName(driver);
	        	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
	          } catch(SQLException e) {
	        	   System.out.println(e.getMessage());
	        	}
	          
	          for(int i=0; i<this.testDist.length; i++) {
	        	  
	          
	          
	              try{
	              Statement testset_sts = con.createStatement();
	              Statement bench_sts = con.createStatement();
	              Statement send_sts=con.createStatement();
	              
	              //System.out.println("sql:select1");
	              
	              String bench_sql="SELECT * FROM beacons where id = "+"'"+testDist[i]+"m-2'";
	              

	              
	              ResultSet rs = bench_sts.executeQuery(bench_sql);
	              //System.out.println("sql:select2");
	              while(rs.next()) {
	            	  
	            	  
	            	  String mac1 = rs.getString("B1_MAC");
	            	  int rssi1=Integer.parseInt(rs.getString("B1_RSSI"));
	            	  String mac2 = rs.getString("B2_MAC");
	            	  int rssi2=Integer.parseInt(rs.getString("B2_RSSI"));
	            	  String mac3 = rs.getString("B3_MAC");
	            	  int rssi3=Integer.parseInt(rs.getString("B3_RSSI"));
	            	  String mac4 = rs.getString("B4_MAC");
	            	  int rssi4=Integer.parseInt(rs.getString("B4_RSSI"));
	            	  
	            	  this.sig[i][0].stackRSSI(rssi1);
	            	  this.sig[i][1].stackRSSI(rssi2);
	            	  this.sig[i][2].stackRSSI(rssi3);
	            	  this.sig[i][3].stackRSSI(rssi4);
	            	  
	              
	              }
	              }
	          
	              catch (SQLException s){
	              System.out.println("SQL statement is not executed!");
	              }
	          }
	              
	          
	          
	          
	          
		}
		

}
