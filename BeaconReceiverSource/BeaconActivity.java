package com.example.beaconproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;

    private TextView tvId;

    private boolean isConnected;
    Beacon[] ConnectedBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvId = (TextView) findViewById(R.id.tvId);

        beaconManager = new BeaconManager(this);

        // 비콘의 수신 범위를 갱신 받음
        beaconManager.setRangingListener((new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(!beacons.isEmpty()){
                    ConnectedBeacon = new Beacon[beacons.size()];

                    for(int i=0; i<beacons.size(); i++) {
                        ConnectedBeacon[i] = beacons.get(i);
                        Log.d("AttendanceCheck" + i, ConnectedBeacon[i].toString());

                    }

//                    tvId.setText(firstBeacon.getRssi() + ", " + secondBeacon.getRssi());

//                    if(!isConnected && secondBeacon.getRssi() > -70){
//                        isConnected = true;
//
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(BeaconActivity.this);
//                        dialog .setTitle("알림")
//                                .setMessage("second 비콘 연결")
//                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }).create().show();
//                    }
                } else {
                    Toast.makeText(BeaconActivity.this, "연결종료", Toast.LENGTH_SHORT).show();
                }
            }
        }));

        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                12313, 3040); // 본인이 연결할 beacon
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

