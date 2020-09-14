package com.example.beaconapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beaconapp.Beacon.BeaconScanActivity;
import com.example.beaconapp.Beacon.Info;

public class MainActivity extends AppCompatActivity {
    // layout var
    EditText idText;
    EditText timeText;
    EditText xText;
    EditText yText;
    Button startButton;

    // permission var
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "Beacontest";

    // other var
    String id;
    String Mtime;
    String real_x;
    String real_y;
    Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = Info.getInstance();

        //permission check
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access" );
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok,null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        startButton = findViewById(R.id.start_button);

//        setBeacon();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText();
                Intent intent = new Intent(MainActivity.this, BeaconScanActivity.class);
                startActivity(intent);
//                beaconScan = new BeaconScan(id,real_x,real_y);
//                Handler handler = new Handler();
//                beaconScan.startScan();
//                startButton.setEnabled(false);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        beaconScan.stopScan();
//                        startButton.setEnabled(true);
//                    }
//                },getTime());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    public void setBeacon(){
//        //수신 구역 설정 - 서버에서 받아와야함
//        region = new BeaconRegion("ranged region",
//                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
//                12313, 3040); // 본인이 연결할 beacon
//        beaconManager = new BeaconManager(this);
//    }

    public void setText(){
        idText = findViewById(R.id.edit_id_text);
        timeText = findViewById(R.id.edit_m_time_text);
        xText = findViewById(R.id.edit_x_text);
        yText = findViewById(R.id.edit_y_text);
        id = idText.getText().toString();
        Mtime = timeText.getText().toString();
        real_x = xText.getText().toString();
        real_y = yText.getText().toString();
        info.setId(id);
        info.setReal_x(real_x);
        info.setReal_y(real_y);
        info.setTime(Mtime);
    }

    public int getTime(){
        return Integer.parseInt(Mtime) * 1000;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }



}