package com.example.beaconapp.Beacon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.beaconapp.R;
import com.example.beaconapp.Server.BeaconDTO;
import com.example.beaconapp.Server.benchDTO;
import com.example.beaconapp.Server.RetrofitClient;

import java.util.List;
import java.util.UUID;

public class BeaconScanActivity extends AppCompatActivity {
    private BeaconManager beaconManager;
    private BeaconRegion region;

    String id;
    String setname;
    String mdate;
    String stime;
    String etime;
    Boolean status = false;

    Beacon[] arrangedBeacon;
    final int beaconSize = 4;

    Info info;

    final String LeftFrontBeacon = "[EB:6E:33:1D:98:21]";   //"[E2:D7:D0:CF:90:3E]";   //분홍색
    final String RightFrontBeacon = "[F0:34:3C:EF:45:80]"; //"[F6:E4:00:F5:00:29]";   //노란색
    final String LeftBackBeacon = "[F0:BB:B4:1F:28:5A]";    //노란색
    final String RightBackBeacon = "[F9:FB:14:46:7C:41]";  //보라색

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        info = Info.getInstance();

        this.id = info.getId();
        this.setname=info.setName;
        setting();

        handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startScan();
                stime= RetrofitClient.getTime();
            }
        });
        thread.setDaemon(true); //Activity가 종료하면, 생성된 thread도 함께 종료되도록
        thread.start();

        Log.d("START", "scan start");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
                Log.d("END", "scan stop");
                etime= RetrofitClient.getTime();
                mdate=RetrofitClient.getDate();
                sendBenchToServer();

                finish();
            }
        }, Integer.parseInt(info.getTime()) * 1000);

    }

    public void setting() {
        beaconManager = new BeaconManager(this);
        arrangedBeacon = new Beacon[beaconSize];

        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                12313, 3040); // 본인이 연결할 beacon

        beaconManager.setRangingListener((new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                System.out.println("log "+beaconSize);
                for (Beacon beacon: beacons) {
                    System.out.println("log - "+beacon.getMacAddress());

                }
                if (!status) {
                    return;
                }
                if (!beacons.isEmpty()) {
                    if (beacons.size() < beaconSize) {
                       // Log.d("AttendenceCheck", "less than 3 beacons");
                        return;
                    }
                    // beacon 정렬
                    for (Beacon beacon: beacons)
                        {

                        arrangeBeacon(beacon);
                    }

                    sendDataToServer();
                } else {
                    Log.d("AttencanceCheck", "no signal");
                }
            }
        }));

    }

    private void sendBenchToServer() {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.init2();
        System.out.println("send bench : "+id+setname+mdate+stime+etime);
        benchDTO bDTO = new benchDTO(id,setname,mdate,stime,etime);

        retrofitClient.sendBench(bDTO);

    }

    private void sendDataToServer() {
                RetrofitClient retrofitClient = new RetrofitClient();
                retrofitClient.init();

                BeaconDTO beaconDTO = new BeaconDTO(id,
                        arrangedBeacon[0].getMacAddress().toString(),String.valueOf(arrangedBeacon[0].getRssi()),
                        arrangedBeacon[1].getMacAddress().toString(),String.valueOf(arrangedBeacon[1].getRssi()),
                        arrangedBeacon[2].getMacAddress().toString(),String.valueOf(arrangedBeacon[2].getRssi()),
                        arrangedBeacon[3].getMacAddress().toString(),String.valueOf(arrangedBeacon[3].getRssi()),
                        retrofitClient.getDate(),retrofitClient.getTime());

                retrofitClient.SendBeacons(beaconDTO);

    }

    private void deleteArrangedBeacon() {
        arrangedBeacon = null;
    }

    public void startScan() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        status = true;
    }

    public void stopScan() {
        beaconManager.disconnect();
        status = false;
        Log.d("AttendanceCheck", "stopbeacon");
        deleteArrangedBeacon();
    }

    private void arrangeBeacon (Beacon beacon) {
        switch (beacon.getMacAddress().toString()){
            case LeftFrontBeacon:
                arrangedBeacon[0] = beacon;
                break;
            case RightFrontBeacon:
                arrangedBeacon[1] = beacon;
                break;
            case LeftBackBeacon:
                arrangedBeacon[2] = beacon;
                break;
            case RightBackBeacon:
                arrangedBeacon[3] = beacon;
                break;
            default:
                Log.d("beacon","wrong beacon sign");
        }
    }
}