package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.service.BeaconManager;
import com.example.BleSource.BleTester2;

import java.util.UUID;

//*** import for BLE
//*** import for BLE
//import for BLE ***//
//import for BLE ***//

public class MainActivity extends AppCompatActivity {

    TextView id_text;
    Button startButton;
    Button secondB;
    Button thirdB;
    TextView BLEDataText;

    //추가로 변수가 필요할 경우 단톡방에 꼭 말해주세요.
    //push 할때도 꼭 단톡방에 말해주세요.

    //추가 변수들 :
    BeaconManager beaconManager;
    BeaconRegion region;
    BleTester2 bleTester;
    String id;
    String[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.start_button);
        secondB=(Button)findViewById(R.id.second_button);
        thirdB=(Button) findViewById(R.id.third_button);

        BLEDataText = (TextView) findViewById(R.id.BLEDataText);

        // BleTester 객체 생성 :
        bleTester=new BleTester2(this.getApplicationContext(), this);

        //수신 구역 설정 - 서버에서 받아와야함
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                12313, 3040); // 본인이 연결할 beacon
        // baeconManager 생성
        beaconManager = new BeaconManager(this);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //1단계 부분입니다.
                //time limit 설정

                /*
                Handler timer = new Handler(); //Handler 생성
                timer.postDelayed(new Runnable(){ //2초후 쓰레드를 생성하는 postDelayed 메소드
                    public void run(){
                        beaconManager.disconnect();
                        Log.d("AttendanceCheck", "stopbeacon");
                    }
                }, 20000); //60000 == 1분

                // 비콘의 페킷 받음 & 서버 전송
                beaconManager.setRangingListener((new BeaconManager.BeaconRangingListener() {
                    @Override
                    public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                        if(!beacons.isEmpty()){
                            for(int i=0; i<beacons.size(); i++) {
                                Beacon ConnectedBeacon = beacons.get(i);
                                Log.d("AttendanceCheck" + i, ConnectedBeacon.toString());

                                PHPConnect connect = new PHPConnect();
                                String id = id_text.getText().toString();
                                String rssi = ConnectedBeacon.getRssi() + "";
                                String macAddr = ConnectedBeacon.getMacAddress().toString();
                                String URL = "http://168.188.129.191/send_beacon_data.php?id="+id+"&rssi="+rssi+"&macAddr="+macAddr;
                                connect.execute(URL);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "비콘 없음 연결종료", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

                 */


            }
        });

        secondB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);
                id = id_text.getText().toString();
               // bleTester.BleStart(id,BLEDataText);
                bleTester.myAdvertiser().advertising1(id);
                bleTester.setTextView(BLEDataText);
                BLEDataText.setText("ble advertising start");
                Handler timer = new Handler(); //Handler 생성
                timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
                    public void run() {

                        bleTester.myAdvertiser().stopAdvertising();

                    }
                }, 10000);


            }
        });

        thirdB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);
                id = id_text.getText().toString();
                bleTester.myScanner().startScan(id);
                bleTester.setTextView(BLEDataText);
                BLEDataText.setText("ble SCAN start");

                Handler timer = new Handler(); //Handler 생성
                timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
                    public void run() {
                        bleTester.myScanner().stopScan();
                        bleTester. showResult(bleTester.getTextView());

                    }
                }, 10000);


            }

        });


    }

    private void toast_error() {
        Toast.makeText(this,"x,y 좌표로 출력이 안됨", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView BLEDataText3 = (TextView) findViewById(R.id.BLEDataText);
        switch(requestCode){
            case 0: {
                PHPConnect connect = new PHPConnect();
                int[] regionXY = (int[]) data.getSerializableExtra("regionXY");

                String x = "";
                String y = "";
                if(regionXY != null) {
                    x += regionXY[0];   // x값 삽입 해주세요.
                    y += regionXY[1];   // y값 삽입 해주세요.
                    BLEDataText.setText("regionXY: (" + x + ", " + y + "), point2D: (" + regionXY[2] + ", " + regionXY[3] + ")");
                }

                String URL = "http://168.188.129.191/test_save_location.php?id=" + id_text.getText().toString() + "&x=" + x + "&y=" + y;
                connect.execute(URL);
                break;
            }
            case 1:
                break;
            default:
                break;
        }
    }

    public void toast_cant_find_location(){
        Toast.makeText(this,"error", Toast.LENGTH_SHORT).show();
    }
}
