package com.example.bletest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    BluetoothManager bmgr;
    BleScanner scr;
    BleAdvertiser advr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bmgr=(BluetoothManager)this.getApplicationContext().getSystemService( Context.BLUETOOTH_SERVICE );
        scr= new BleScanner(bmgr,this.getApplicationContext(), this);
        advr = new BleAdvertiser(this.getApplicationContext());
        advr.advertising1("MYID");
        scr.startScan("MYID");

        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run() {
               advr.stopAdvertising();
               scr.stopScan();

            }
        }, 10000); //60000 == 1분 //660000 == 11분

    }
}