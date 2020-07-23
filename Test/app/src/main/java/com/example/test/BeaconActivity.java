package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {

    public Point2D[] BeaconPoint = new Point2D[BEACON_NUM];

    public static final String FL = "eb6e331d9821";
    public static final String FM = "f0bbb41f285a";
    public static final String FR = "e2d7d0cf903e";
    public static final String ML = "f9fB14467c41";
    public static final String MR = "f6e400f50029";
    public static final String BL = "FF";
    public static final String BM = "FG";
    public static final String BR = "FH";
    public static final int SIGNAL_LIMIT = 50;
    public static final int BEACON_NUM = 8;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    Beacon[] ConnectedBeacon;
    double[][] BeaconsPackets = new double[BEACON_NUM][SIGNAL_LIMIT];

    ArrayList<Double> selectedValues = new ArrayList<>();

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
//        setContentView(R.layout.activity_main);

        for(int i=0; i<BEACON_NUM; i++)
            Arrays.fill(BeaconsPackets[i], 100.0);

        BeaconPoint[0] = new Point2D(0, 0, 0);
        BeaconPoint[1] = new Point2D(4.5, 0, 0);
        BeaconPoint[2] = new Point2D(9, 0, 0);
        BeaconPoint[3] = new Point2D(0, 4.5, 0);
        BeaconPoint[4] = new Point2D(9, 4.5, 0);
        BeaconPoint[5] = new Point2D(0, 4.5, 0);
        BeaconPoint[6] = new Point2D(4.5, 9, 0);
        BeaconPoint[7] = new Point2D(9, 9, 0);

        //time limit 설정
        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable(){ //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run(){
                beaconManager.disconnect();
                Log.d("AttendanceCheck", "stopbeacon");
                getMedian();

                // 삼변축량 & regionXY 계산
                int[] regionXY = getRegion();
                if(regionXY != null)
                    Log.d("AttendanceCheck final region", regionXY[0] + ", " +regionXY[1]);

                // MainActivity에 전송
                Intent data = new Intent();
                data.putExtra("regionXY", regionXY);
                setResult(0, data);

                finish(); // 이 액티비티를 종료
            }
        }, 20000); //60000 == 1분

        //수신 구역 설정
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                12313, 3040); // 본인이 연결할 beacon

        // 비콘의 페킷 받음
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener((new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(!beacons.isEmpty()){
                    ConnectedBeacon = new Beacon[beacons.size()];

                    for(int i=0; i<beacons.size(); i++) {
                        ConnectedBeacon[i] = beacons.get(i);
                        Log.d("AttendanceCheck" + i, ConnectedBeacon[i].toString());
                        getDistance(ConnectedBeacon[i]);
                    }
                } else {
                    Toast.makeText(BeaconActivity.this, "연결종료", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    public void getDistance(Beacon beacon){
        double TXpower = beacon.getMeasuredPower();
        double rssi = beacon.getRssi();
        double distant = Math.pow(10, ((TXpower-rssi)/(n*10)));

        switch (beacon.getMacAddress().toHexString()) {
            case FL:
                if (FLSize < SIGNAL_LIMIT){
                    BeaconsPackets[0][FLSize] = distant;
                    FLSize++;
                }
                break;
            case FM:
                if (FMSize < SIGNAL_LIMIT){
                    BeaconsPackets[1][FMSize] = distant;
                    FMSize++;
                }
                break;
            case FR:
                if (FRSize < SIGNAL_LIMIT) {
                    BeaconsPackets[2][FRSize] = distant;
                    FRSize++;
                }
                break;
            case ML:
                if (MLSize < SIGNAL_LIMIT){
                    BeaconsPackets[3][MLSize] = distant;
                    MLSize++;
                }
                break;
            case MR:
                if (MRSize < SIGNAL_LIMIT) {
                    BeaconsPackets[4][MRSize] = distant;
                    MRSize++;
                }
                break;
            case BL:
                if (BLSize < SIGNAL_LIMIT) {
                    BeaconsPackets[5][BLSize] = distant;
                    BLSize++;
                }
                break;
            case BM:
                if (BMSize < SIGNAL_LIMIT) {
                    BeaconsPackets[6][BMSize] = distant;
                    BMSize++;
                }
                break;
            case BR:
                if (BRSize < SIGNAL_LIMIT) {
                    BeaconsPackets[7][BRSize] = distant;
                    BRSize++;
                }
                break;
            default:
                break;
        }
    }

    public void getMedian(){
        Arrays.sort(BeaconsPackets[0]);
        Arrays.sort(BeaconsPackets[1]);
        Arrays.sort(BeaconsPackets[2]);
        Arrays.sort(BeaconsPackets[3]);
        Arrays.sort(BeaconsPackets[4]);
        Arrays.sort(BeaconsPackets[5]);
        Arrays.sort(BeaconsPackets[6]);
        Arrays.sort(BeaconsPackets[7]);

        //중앙값?
        for(int i=0; i<BEACON_NUM; i++){
//            selectedValues[i] = BeaconsPackets[i][FRSize / 2];
            selectedValues.add(BeaconsPackets[i][FRSize / 2]);
        }
        //평균값? X
        //최빈값?
    }

    public int[] getRegion(){
        // 복사해서 sort한다.
        ArrayList<Double> sortArr = (ArrayList<Double>) selectedValues.clone();
        sortArr.sort(null);

        int shortest1 = selectedValues.indexOf(sortArr.get(0));
        int shortest2 = selectedValues.indexOf(sortArr.get(1));
        int shortest3 = selectedValues.indexOf(sortArr.get(2));

        Point2D triPoint = Trilateration.getTrilateration(BeaconPoint[shortest1], BeaconPoint[shortest2], BeaconPoint[shortest3]);

        int[] region = new int[4];
        region[2] = (int) triPoint.getX();
        region[3] = (int) triPoint.getY();

        if(triPoint.getX() < 3)         region[0] = 0;
        else if (triPoint.getX() < 6)   region[0] = 1;
        else if (triPoint.getX() <= 9)  region[0] = 2;
        else region[0] = 3;

        if(triPoint.getY() < 3)         region[1] = 0;
        else if (triPoint.getY() < 6)   region[1] = 1;
        else if (triPoint.getY() <= 9)  region[1] = 2;
        else region[1] = 3;

        return region;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}

