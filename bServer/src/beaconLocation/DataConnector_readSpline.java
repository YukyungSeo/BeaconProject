package beaconLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataConnector_readSpline {
	public void getSplineDataFromServer(String mid, String mac)  throws ClassNotFoundException{
		
		try{
	       	 Class.forName(driver);
	       	  con = DriverManager.getConnection(url, dbUser, dbPasswd);
	         } catch(SQLException e) {
	       	   System.out.println(e.getMessage());
	       	}
		
		try{
            Statement sts1 = con.createStatement();
            String sql1="SELECT id, mac, rssi, dist FROM beacon_spline WHERE id = '"+mid+"' AND mac = '"+mac+"'";
            ResultSet rsb = sts1.executeQuery(sql1);
            this.thisid=mid;
            this.thismac=mac;
            this.bss=null;
            this.bss=new beaconSplineSetting(thisid,thismac);
            while(rsb.next()) {
            	
            	String rssistr = rsb.getString("rssi");
            	String diststr = rsb.getString("dist");
            	
            	double rssi = Double.parseDouble(rssistr);
            	double dist = Double.parseDouble(diststr);
            	
            	this.bss.insertSIG(rssi,dist);
            	
            	
            	
            	
            }
               }
            catch (SQLException s){
            System.out.println("SQL statement is not executed! 111");
            }
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.bss.allsize>2) {
			this.bss.makeSpline();
      	  splineEnabled=true;
        }else {
      	  splineEnabled=false;
        }
		
	}
	public beaconSplineSetting getspst() {
		return this.bss;
		
	}
	public DataConnector_readSpline() {

		splineEnabled=false;
		
	}
	String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";
    String dbUser = "juha";
    String dbPasswd = "1234";
    String thisid;
    public boolean splineEnabled;
    String thismac;
    Connection con;
    beaconSplineSetting bss;

}
