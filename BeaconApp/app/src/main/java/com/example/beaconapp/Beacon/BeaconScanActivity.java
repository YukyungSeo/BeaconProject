package com.example.beaconapp.Beacon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.beaconapp.R;
import com.example.beaconapp.Server.PHPConnect;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

public class BeaconScanActivity extends AppCompatActivity {
    private BeaconManager beaconManager;
    private BeaconRegion region;

    String id;
    String real_x;
    String real_y;
    String cal_x;
    String cal_y;
    Boolean status = false;

    Beacon[] suitableBeacon;
    final int suitabaleBeaconSize=4;

    Info info;

    final String LeftFrontBeacon = "[EB:6E:33:1D:98:21]" ;   //"[E2:D7:D0:CF:90:3E]";   //분홍색
    final String RightFrontBeacon = "[F0:34:3C:EF:45:80]"; //"[F6:E4:00:F5:00:29]";   //노란색
    final String LeftBackBeacon = "[F0:BB:B4:1F:28:5A]";    //노란색
    final String RightBackBeacon = "[F9:FB:14:46:7C:41]";  //보라색

    double n = 2;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        info = Info.getInstance();

        this.id = info.getId();
        this.real_x = info.getReal_x();
        this.real_y = info.getReal_y();
        setting();

        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startScan();
            }
        });
        thread.setDaemon(true); //Activity가 종료하면, 생성된 thread도 함께 종료되도록
        thread.start();
//        startScan();
        Log.d("START", "scan start");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
                Log.d("END", "scan stop");
                finish();
            }
        },Integer.parseInt(info.getTime())*1000);

    }

    public void setting(){
        beaconManager = new BeaconManager(this);

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
                    if(beacons.size()<suitabaleBeaconSize) {
                        Log.d("AttendenceCheck", "less than 3 beacons");
                        return;
                    }

                    //RSSI값이 제일 큰 애들 (절댓값을 취했을때 작은 값)을 구함
                    if(suitableBeacon!=null){
                        deleteSuitableBeacon();
                    }
                    createSuitableBeacon();
                    for(int i=0; i<beacons.size();i++){
                        selectMostShortestBeacons(beacons.get(i));
                    }

                    int[] regionXY = getRegion();
                    //값을 계산해서 cal_x랑 cal_y에 삽입.
                    if(exceptUnavailablePosition(regionXY)){
                        //근데 cal_x랑 cal_y가 너무 튀면 보내지말고, 출력문 출력하자
                        cal_x = String.valueOf(regionXY[1]);
                        cal_y = String.valueOf(regionXY[0]);
                        //최종 값 뜨면 서버에 전송
                        sendDataToServer();
                    }

                } else {
                    Log.d("AttencanceCheck","no signal");
                }
            }
        }));

    }

    private boolean exceptUnavailablePosition(int[] regionXY) {
        if(regionXY[0]<-3 || regionXY[1]<-3){
            Log.d("음수로 튀는 값","x:"+regionXY[0]+" |  y:"+regionXY[1]);
            return false;
        }
        if(regionXY[0]>=12 || regionXY[1]>=12){
            Log.d("양수로 튀는 값","x:"+regionXY[0]+" |  y:"+regionXY[1]);
            return false;
        }
        return true;
    }

    private void sendDataToServer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PHPConnect connect = new PHPConnect();

                String URL = "http://168.188.129.191/new_send_beacon_data.php?id="+id
                        +"&real_x="+real_x
                        +"&real_y="+real_y
                        +"&cal_x="+cal_x
                        +"&cal_y="+cal_y
                        +"&B1_MAC="+suitableBeacon[0].getMacAddress()+"&B1_RSSI="+suitableBeacon[0].getRssi()
                        +"&B2_MAC="+suitableBeacon[1].getMacAddress()+"&B2_RSSI="+suitableBeacon[1].getRssi()
                        +"&B3_MAC="+suitableBeacon[2].getMacAddress()+"&B3_RSSI="+suitableBeacon[2].getRssi();
                connect.execute(URL);
            }
        });
        thread.start();
    }


    private void selectMostShortestBeacons(Beacon beacon) {
        for(int i=0; i<suitabaleBeaconSize; i++){
            if(suitableBeacon[i]==null){
                suitableBeacon[i] = beacon;
                return;
            }
        }
        for(int i=0;i<suitabaleBeaconSize;i++){
            if(beacon.getRssi()>suitableBeacon[i].getRssi()){
                suitableBeacon[i] = beacon;
                return;
            }
        }
    }

    private void createSuitableBeacon(){
        suitableBeacon = new Beacon[suitabaleBeaconSize];
    }

    private void deleteSuitableBeacon(){
        suitableBeacon = null;
    }

    public void startScan(){
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
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
        Point2D shortest4 = getPoint2D(suitableBeacon[3]);

        double[][] positions = new double[][] {
                {shortest1.getX(),shortest1.getY()},
                {shortest2.getX(),shortest2.getY()},
                {shortest3.getX(),shortest3.getY()},
                {shortest4.getX(),shortest4.getY()}};

        double[] distances = new double[] {
                shortest1.getDistance(),
                shortest2.getDistance(),
                shortest3.getDistance(),
                shortest4.getDistance()};

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions,distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

//        Point2D triPoint = Trilateration.getTrilateration(shortest1,shortest2,shortest3);

        double[] temp = optimum.getPoint().toArray();
        int[] region = new int[temp.length];

        for(int i=0; i<region.length;i++){
            region[i] = (int) temp[i];
        }

        Log.d("region","x: "+region[1]+" y: "+region[0]);

        return region;
    }

    private Point2D getPoint2D(Beacon beacon) {
        Point2D point2D = new Point2D();
        double TXpower = beacon.getMeasuredPower();
        double rssi = beacon.getRssi();

        switch (beacon.getMacAddress().toString()){
            case LeftFrontBeacon:
                point2D.setX(0);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case RightFrontBeacon:
                point2D.setX(0);
                point2D.setY(9);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case LeftBackBeacon:
                point2D.setX(9);
                point2D.setY(0);
                point2D.setDistance(Math.pow(10, ((TXpower-rssi)/(n*10))));
                break;
            case RightBackBeacon:
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