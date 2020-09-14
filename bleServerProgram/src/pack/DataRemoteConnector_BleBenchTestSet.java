package pack;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;

public class DataRemoteConnector_BleBenchTestSet {	//서버의 데이터베이스에 접근하여 데이터 불러오고 객체에 저장
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
   
    
    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;
    
    boolean test = true;
    
    private IDPAIR[] pairs;
	int pairsize;
    
	public DataRemoteConnector_BleBenchTestSet() {
		this.pairsize=0;
		pairs=new IDPAIR[200];
		
	}
	
	public int getPairSize() {
		return this.pairsize;
	}
	public IDPAIR[] getPairs() {
		return this.pairs;
	}
	
	
	public void connectANDquery( HashMap<IDPAIR,SIGINF> map) throws ClassNotFoundException {
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
              
              //System.out.println("sql:select1");
              
              String bench_sql="SELECT * FROM benchmark_ble";
              

              
              ResultSet bench_rs = bench_sts.executeQuery(bench_sql);
              //System.out.println("sql:select2");
              int cnt=0;
              while(bench_rs.next()) {
            	  System.out.println(++cnt);
            	  String bmid = bench_rs.getString("bm_id");
            	  //te date = rs2.getDate("Date");
            	  int rDist=bench_rs.getInt("dist");
            	  Time sTime = bench_rs.getTime("sTime");
            	  Time eTime = bench_rs.getTime("eTime");
            	  Date date=bench_rs.getDate("mdate");
            	  
            	 // System.out.println(date.toString());
            	  
            	  /*
            	String sMinute= sTime.toString().split(":")[1];
            	String sSecond=sTime.toString().split(":")[2];
            	  int sHour = sTime.getHours();
            	  sHour=sHour+12;
            	  String tempstime=""+sHour+":"+sMinute+":"+sSecond;
            	  System.out.println(tempstime);
            	  //
            	  String eMinute= eTime.toString().split(":")[1];
              	String eSecond=eTime.toString().split(":")[2];
              	  int eHour = eTime.getHours();
              	  sHour=sHour+12;
              	  String tempstime=""+sHour+":"+sMinute+":"+sSecond;
              	  System.out.println(tempstime);
            	  //
            	   *
            	   */
            	  
            	  
            	  System.out.println(sTime.toString());
            	  System.out.println(eTime.toString());
            	  String testset_sql="SELECT * FROM testset_ble WHERE mTime > '"+sTime.toString()+"' AND mTime < '"+eTime.toString()+"'";
             	 //String testset_sql="SELECT * FROM testset_ble WHERE mTime > '"+sTime.toString()+"' AND mTime < '"+eTime.toString()+"'";
            	  ResultSet testset_rs    = testset_sts.executeQuery(testset_sql);
            	  //System.out.println("sql:select3");
            	  if(testset_rs!=null) {
                      getData(testset_rs,map,sTime,eTime,bmid,rDist,date);
                      System.out.println(sTime+" // "+bmid+" //"+testset_rs.getRow());

                      }
            	  
            	  
              
              }
              }
              catch (SQLException s){
              System.out.println("SQL statement is not executed!");
              }
              }
	}
	
	private void getData(ResultSet rs,HashMap<IDPAIR,SIGINF> mm,Time stime,Time etime,String bid,int rDist,Date dt) throws SQLException {
		System.out.println(bid);
		int cnt2=0;
		while (rs.next()) {
			++cnt2;
			String benchid =bid;
			String myid= rs.getString("my_id");
			String yourid=rs.getString("other_id");
			int dist=rDist;
			int rssi=rs.getInt("rssi");
			Time start_t=stime;
			Time finish_t=etime;
			String date=dt.toString();
			boolean findsamepair=false;
			
			for(int i=0; i<this.pairsize; i++) {
				if(this.pairs[i].isSame(benchid,myid,yourid,start_t)) {
					findsamepair=true;
					mm.get(pairs[i]).stackRSSI(rssi);
					i=this.pairsize;
				}
			}
			
			if(!findsamepair) {
				IDPAIR tempidpair = new IDPAIR(benchid,myid,yourid);
				tempidpair.setDistance(dist);
				tempidpair.setTime(start_t, finish_t);
				tempidpair.setDate(date);
				SIGINF tempsig = new SIGINF();
				tempsig.stackRSSI(rssi);
				this.pairs[pairsize]=tempidpair;
				pairsize++;
				mm.put(tempidpair,tempsig);
			}
        }
		System.out.println("count2 "+cnt2);
	}
	

}
