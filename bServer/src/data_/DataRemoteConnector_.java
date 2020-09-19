package data_;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;

public class DataRemoteConnector_ {	//서버의 데이터베이스에 접근하여 데이터 불러오고 객체에 저장
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
   
    
    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;
    
    boolean test = true;
    
	
	
	public void connect_and_query() throws ClassNotFoundException {
		  System.out.println("get Ble Test Datas ");
          try{
        	
        	 Class.forName(driver);
        	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
          } catch(SQLException e) {
        	   System.out.println(e.getMessage());
        	}
          
          if(test) {
              try{
              Statement testset_sts = con.createStatement();
              Statement bench_sts = con.createStatement();
              Statement send_sts=con.createStatement();
              
              //System.out.println("sql:select1");
              
              String bench_sql="SELECT * FROM benchmark_ble";
              

              
              ResultSet bench_rs = bench_sts.executeQuery(bench_sql);
              //System.out.println("sql:select2");
              while(bench_rs.next()) {
            	  String bmid = bench_rs.getString("bm_id");
            	  //te date = rs2.getDate("Date");
            	  int rDist=bench_rs.getInt("dist");
            	  Time sTime = bench_rs.getTime("sTime");
            	  Time eTime = bench_rs.getTime("eTime");
            	  Date date=bench_rs.getDate("mdate");
            	  String testset_sql="SELECT * FROM record_ble WHERE mTime > '"+sTime.toString()+"' AND mTime < '"+eTime.toString()+"'";
             	 //String testset_sql="SELECT * FROM testset_ble WHERE mTime > '"+sTime.toString()+"' AND mTime < '"+eTime.toString()+"'";
            	  ResultSet testset_rs    = testset_sts.executeQuery(testset_sql);
            	  //System.out.println("sql:select3");
            	  if(testset_rs!=null) {
            		  while(testset_rs.next()) {
            			  
            				String myid= testset_rs.getString("my_id");
            				String yourid=testset_rs.getString("other_id");
            				int rssi=testset_rs.getInt("rssi");
            				String send_sql="INSERT INTO  * FROM benchmark_ble";
            				send_sts.executeQuery(send_sql);
            				
            			  
            		  }
                      System.out.println("DB - "+sTime+" // bench :"+bmid );

                      }
            	  
            	  
              
              }
              }
              catch (SQLException s){
              System.out.println("SQL statement is not executed!");
              }
              }
	}
	
	

}
