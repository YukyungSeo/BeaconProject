package com.example.test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {
    public static final String FR = "eb6e331d9821";
    public static final String FM = "f0bbb41f285a";
    public static final String FL = "FC";
    public static final String MR = "FD";
    public static final String ML = "FE";
    public static final String BR = "FF";
    public static final String BL = "FG";
    public static final String BM = "FH";
    public static final int SIGNAL_LIMIT = 50;
    public static final int BEACON_NUM = 8;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    Beacon[] ConnectedBeacon;
    double[][] BeaconsPackets = new double[BEACON_NUM][SIGNAL_LIMIT];

    double[] selectedValues = new double[BEACON_NUM];

    int FRSize = 0;
    int FMSize = 0;
    int FLSize = 0;
    int MRSize = 0;
    int MLSize = 0;
    int BRSize = 0;
    int BMSize = 0;
    int BLSize = 0;

    double n = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        //time limit 설정
        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable(){ //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run(){
                beaconManager.disconnect();
                Log.d("AttendanceCheck", "stopbeacon");
                getMedian();
                finish(); // 이 액티비티를 종료
            }
        }, 60000); //60000 == 1분

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
            case FR:
                if (FRSize < SIGNAL_LIMIT){
                    BeaconsPackets[0][FRSize] = distant;
                    FRSize++;
                }
                break;
            case FM:
                if (FMSize < SIGNAL_LIMIT){
                    BeaconsPackets[1][FMSize] = distant;
                    FMSize++;
                }
                break;
            case FL:
                if (FLSize < SIGNAL_LIMIT) {
                    BeaconsPackets[2][FLSize] = distant;
                    FLSize++;
                }
                break;
            case MR:
                if (MRSize < SIGNAL_LIMIT) {
                    BeaconsPackets[3][MRSize] = distant;
                    MRSize++;
                }
                break;
            case ML:
                if (MLSize < SIGNAL_LIMIT){
                    BeaconsPackets[4][MLSize] = distant;
                    MLSize++;
                }
                break;
            case BR:
                if (BRSize < SIGNAL_LIMIT) {
                    BeaconsPackets[5][BRSize] = distant;
                    BRSize++;
                }
                break;
            case BM:
                if (BMSize < SIGNAL_LIMIT) {
                    BeaconsPackets[6][BMSize] = distant;
                    BMSize++;
                }
                break;
            case BL:
                if (BLSize < SIGNAL_LIMIT) {
                    BeaconsPackets[7][BLSize] = distant;
                    BLSize++;
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
            selectedValues[i] = BeaconsPackets[i][SIGNAL_LIMIT -(FRSize/2)];
        }
        //평균값? X
        //최빈값?
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

