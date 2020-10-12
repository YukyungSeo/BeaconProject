package beaconLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

public class DataConnector_splineData {
	public void getSplineData(String mid, String mac)  throws ClassNotFoundException{
		try{
	       	 Class.forName(driver);
	       	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
	         } catch(SQLException e) {
	       	   System.out.println(e.getMessage());
	       	}
		
		
	}
	public void insertSplineData() throws ClassNotFoundException{
		try{
       	 Class.forName(driver);
       	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
         } catch(SQLException e) {
       	   System.out.println(e.getMessage());
       	}
             try{
            	 for(int k=0; k<this.settingSize; k++) {
            		 if(bss[k].maked) {
            			 
            		 System.out.println("insert bss "+k);
            	for(int i=0; i<=10; i++) {
            	double sprssi=bss[k].splines.getPoint(i*0.1)[0];
            	double spdist=bss[k].splines.getPoint(i*0.1)[1];
            	String rssi=String.format("%.3f",sprssi);
            	String dist=String.format("%.3f",spdist);
            	String id =bss[k].id;
          		String mac = bss[k].mac;
            	String sql1="INSERT INTO beacon_spline (id,mac,rssi,dist) VALUES (?,?,?,?)";
            	System.out.println("insert"+rssi+dist+id+mac);
        		PreparedStatement  sts1 = (PreparedStatement) con.prepareStatement(sql1);
        		sts1.setString(1,id);//"+lct[y].myid+","+Integer.toString(lct[y].finalLocateX)+","+Integer.toString(lct[y].finalLocateY)+"
        		sts1.setString(2,mac);
        		sts1.setString(3,rssi);
        		sts1.setString(4,dist);
        		sts1.execute();
            			}
            		 }
            	}
           	 }catch (SQLException s){
                 System.out.println("SQL statement is not executed!");
                 System.out.println(s);
                 }
	}
	public void makeSpline() {
		System.out.println("setting size "+this.settingSize);
		for(int k=0; k<this.settingSize; k++) {
    		this.bss[k].makeSpline();
    	}
	}
	public void setRawData() throws ClassNotFoundException {
          try{
        	 Class.forName(driver);
        	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
          } catch(SQLException e) {
        	   System.out.println(e.getMessage());
        	}
              try{
              Statement sts1 = con.createStatement();
              String sql1="SELECT id, B1_MAC, B1_RSSI, B2_MAC, B2_RSSI, B3_MAC, B3_RSSI, B4_MAC, B4_RSSI FROM beacon_testset";
              ResultSet rsb = sts1.executeQuery(sql1);
              
             if(rsb!=null) {
            	 while (rsb.next()) {
            		 System.out.println("read dt ");
         			String mid =rsb.getString("id");
         			if(mid.contains("m")) {	//스플라인 아이디 형식 - "sp-8-pid"
         				String mmid="gal";
         				 System.out.println("read gal ");
         			String b1= rsb.getString("B1_MAC");
         			String r1= rsb.getString("B1_RSSI");
         			String b2= rsb.getString("B2_MAC");
         			String r2= rsb.getString("B2_RSSI");
         			String b3= rsb.getString("B3_MAC");
         			String r3= rsb.getString("B3_RSSI");
         			String b4= rsb.getString("B4_MAC");
         			String r4= rsb.getString("B4_RSSI");
     				beaconInfo[] bi= new beaconInfo[4];
     				bi[0]= new beaconInfo(mmid,b1,Integer.parseInt(r1));
     				bi[1]=new beaconInfo(mmid,b2,Integer.parseInt(r2));
     				bi[2]=new beaconInfo(mmid,b3,Integer.parseInt(r3));
     				bi[3]=new beaconInfo(mmid,b4,Integer.parseInt(r4));
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
     					
     					int meter=Integer.parseInt(Character.toString(mid.charAt(0)));
     					if(this.bss[0]==null) {
     						bss[0]=new beaconSplineSetting(mmid,b1);
     						bss[0].insertSIG(Integer.parseInt(r1),meter);
     						bss[1]=new beaconSplineSetting(mmid,b2);
     						bss[1].insertSIG(Integer.parseInt(r2),meter);
     						bss[2]=new beaconSplineSetting(mmid,b3);
     						bss[2].insertSIG(Integer.parseInt(r3),meter);
     						bss[3]=new beaconSplineSetting(mmid,b4);
     						bss[3].insertSIG(Integer.parseInt(r4),meter);
     						settingSize=settingSize+4;
     					}else {
     						
     						boolean same=false;
     						for(int u=0; u<settingSize; u++) {
     							if(bss[u].check(mmid,b1)) {
     								same=true;
     								bss[u].insertSIG(Integer.parseInt(r1),meter);
     								u=settingSize;
     							}
     						}
     						if(!same) {
     							bss[settingSize]=new beaconSplineSetting(mmid,b1);
     							bss[settingSize].insertSIG(Integer.parseInt(r1),meter);
     							settingSize++;
     						}
     						same=false;
     						for(int u=0; u<settingSize; u++) {
     							if(bss[u].check(mmid,b2)) {
     								same=true;
     								bss[u].insertSIG(Integer.parseInt(r2),meter);
     								u=settingSize;
     							}
     							
     						}
     						if(!same) {
     							bss[settingSize]=new beaconSplineSetting(mmid,b2);
     							bss[settingSize].insertSIG(Integer.parseInt(r1),meter);
     							settingSize++;
     						}
     						same=false;
     						for(int u=0; u<settingSize; u++) {
     							if(bss[u].check(mmid,b3)) {
     								same=true;
     								bss[u].insertSIG(Integer.parseInt(r3),meter);
     								u=settingSize;
     							}
     						}
     						if(!same) {
     							bss[settingSize]=new beaconSplineSetting(mmid,b3);
     							bss[settingSize].insertSIG(Integer.parseInt(r3),meter);
     							settingSize++;
     						}same=false;
     						for(int u=0; u<settingSize; u++) {
     							if(bss[u].check(mmid,b4)) {
     								same=true;
     								bss[u].insertSIG(Integer.parseInt(r4),meter);
     								u=settingSize;
     							}
     						}
     						if(!same) {
     							bss[settingSize]=new beaconSplineSetting(mmid,b4);
     							bss[settingSize].insertSIG(Integer.parseInt(r4),meter);
     							settingSize++;
     						}
     					}
                 }
            	 }
            	 }
                 }
              catch (SQLException s){
              System.out.println("SQL statement is not executed! 111");
              }
              
              }
	String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;
    public beaconSplineSetting[] bss;
   public  int settingSize;
   
	public DataConnector_splineData() {
		bss=new beaconSplineSetting[1000];
		settingSize=0;
	}
}
