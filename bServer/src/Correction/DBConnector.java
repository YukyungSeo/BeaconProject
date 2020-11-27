package Correction;

import java.sql.*;
import java.util.ArrayList;

public class DBConnector {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://168.188.129.191:3306/ACUB?autoReconnect=true&useSSL=false";

    String dbUser = "juha";
    String dbPasswd = "1234";
    Connection con;

    boolean test = true;

    public ArrayList getBeaconLocation() throws ClassNotFoundException {
        System.out.println("get Ble User Info & Location Data ");
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, dbUser, dbPasswd);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            Statement sts = con.createStatement();

            String sql = "SELECT * FROM beacon_location";

            ResultSet rs = sts.executeQuery(sql);

            ArrayList<Phone> phoneArrayList = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                int x = rs.getInt("x");
                int y = rs.getInt("y");

                phoneArrayList.add(new Phone(id, x, y));
            }
            return phoneArrayList;
        } catch (SQLException s) {
            System.out.println(s);
        }
        return null;
    }

    public double getDistance(String phoneSender, String phoneReceiver) throws ClassNotFoundException {
        System.out.println("get Ble distance Data ");
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, dbUser, dbPasswd);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            Statement distance_sts = con.createStatement();

            String distance_sql = "SELECT * FROM ble_distance WHERE id1=\'" + phoneSender + "\' and id2=\'" + phoneReceiver +"\'";

            ResultSet distance_rs = distance_sts.executeQuery(distance_sql);

            while (distance_rs.next()) {
                double distance = distance_rs.getDouble("dist");
                return distance;
            }
        } catch (SQLException s) {
            System.out.println(s);
        }
        return -1.0;
    }
}
