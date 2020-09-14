package com.example.beaconapp.Beacon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.beaconapp.R;
import com.example.beaconapp.Server.PHPConnect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

public class BeaconScanActivity extends AppCompatActivity {
    public static final int SIGNAL_LIMIT = 50;
    public static final int BEACON_NUM = 4;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    String id;
    String real_x;
    String real_y;

    Boolean status = false;

    Beacon[] ConnectedBeacon;
    Beacon[] suitableBeacon;
    double[][] BeaconsPackets = new double[BEACON_NUM][SIGNAL_LIMIT];

    ArrayList<Double> selectedValues = new ArrayList<>();

    Info info;

    int FLSize = 0;
    int FMSize = 0;
    int FRSize = 0;
    int MLSize = 0;
    int MRSize = 0;
    int BLSize = 0;
    int BMSize = 0;
    int BRSize = 0;

    double n = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        info = Info.getInstance();

        this.id = info.getId();
        this.real_x = info.getReal_x();
        this.real_y = info.getReal_y();
        setting();

        Handler handler = new Handler();
        startScan();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
                System.out.println("END");
            }
        },Integer.parseInt(info.getTime()));
    }

    public void setting(){

        createSuitableBeacon();

        beaconManager = new BeaconManager(getApplicationContext());

        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                12313, 3040); // 본인이 연결할 beacon

        beaconManager.setRangingListener((new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(!status){
                    return;
                }
                if(!beacons.isEmpty()){
                    if(beacons.size()<3){
                        Log.d("AttendenceCheck","less than 3 beacons");
                        return;
                    }
                    ConnectedBeacon = new Beacon[beacons.size()];

                    //RSSI값이 제일 큰 애들 (절댓값을 취했을때 작은 값)을 구함
                    for(int i=0; i<beacons.size();i++){
                        selectMostShortestBeacons(beacons.get(i));
                    }

                    int[] regionXY = getRegion();
                    //값을 계산해서 cal_x랑 cal_y에 삽입.
                    if(exceptUnavailablePosition(regionXY)){
                        //근데 cal_x랑 cal_y가 너무 튀면 보내지말고, 출력문 출력하자
                        int cal_x = regionXY[0];
                        int cal_y = regionXY[1];
                        //최종 값 뜨면 서버에 전송
                        sendDataToServer(cal_x,cal_y);
                    }

                } else {
                    Log.d("AttencanceCheck","no signal");
                }
            }
        }));

    }

    private boolean exceptUnavailablePosition(int[] regionXY) {
        //true면 정상 값, false면 비정상 값
        //값이 어떻게 튀는지를 봐야 측정 가능할듯...
        System.out.println("측정된 x값 *****"+regionXY[0]);
        System.out.println("측정된 y값 *****"+regionXY[1]);
        return true;
    }

    private void sendDataToServer(int cal_x, int cal_y) {
        PHPConnect connect = new PHPConnect();

        String URL = "http://168.188.129.191/new_send_beacon_data.php?id="+id
                +"&real_x="+real_x
                +"&real_y="+real_y
                +"&cal_x"+cal_x
                +"&cal_y"+cal_y
                +"&B1_MAC="+suitableBeacon[0].getMacAddress()+"&B1_RSSI="+suitableBeacon[0].getRssi()
                +"&B2_MAC="+suitableBeacon[1].getMacAddress()+"&B2_RSSI="+suitableBeacon[1].getRssi()
                +"&B3_MAC="+suitableBeacon[2].getMacAddress()+"&B3_RSSI="+suitableBeacon[2].getRssi();

        connect.execute(URL);
    }


    private void selectMostShortestBeacons(Beacon beacon) {
        for(int i=0; i<3; i++){
            if(suitableBeacon[i].equals(null)){
                suitableBeacon[i] = beacon;
            }
            if(beacon.getRssi()>suitableBeacon[i].getRssi()){
                suitableBeacon[i] = beacon;
            }
        }
    }

    private void createSuitableBeacon(){
        suitableBeacon = new Beacon[3];
    }

    private void deleteSuitableBeacon(){
        suitableBeacon = null;
    }

    public void startScan(){
        status = true;
    }

    public void stopScan(){
        beaconManager.disconnect();
        status = false;
        Log.d("AttendanceCheck", "stopbeacon");
        deleteSuitableBeacon();
    }

    public int[] getRegion(){
        Point2D shortest1 = getPoint2D(suitableBeacon[0]);
        Point2D shortest2 = getPoint2D(suitableBeacon[1]);
        Point2D shortest3 = getPoint2D(suitableBeacon[2]);

        Point2D triPoint = Trilateration.getTrilateration(shortest1,shortest2,shortest3);

        int[] region = new int[2];
        region[0] = (int) triPoint.getX();
        region[1] = (int) triPoint.getY();

        return region;
    }

    private Point2D getPoint2D(Beacon beacon) {
        Point2D point2D = new Point2D();
        double TXpower = beacon.getMeasuredPower();
        double rssi = beacon.getRssi();

        switch (beacon.getMacAddress().toString()){
            case "left front mac address":
                point2D.setX(0);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case "right front mac address":
                point2D.setX(0);
                point2D.setY(9);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case "left back mac address":
                point2D.setX(9);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case "right back mac address":
                point2D.setX(9);
                point2D.setY(9);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            default:
                Log.d("beacon","wrong beacon sign");
        }

        return point2D;
    }
}