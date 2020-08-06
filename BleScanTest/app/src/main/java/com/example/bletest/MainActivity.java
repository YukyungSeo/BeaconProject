package com.example.bletest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothManager bmgr=(BluetoothManager)this.getApplicationContext().getSystemService( Context.BLUETOOTH_SERVICE );
        BleScanner scr= new BleScanner(bmgr,this.getApplicationContext(), this);
        scr.startScan();

    }
}