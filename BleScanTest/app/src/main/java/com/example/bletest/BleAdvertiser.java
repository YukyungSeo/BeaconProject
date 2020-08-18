package com.example.bletest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.Charset;


public class BleAdvertiser {

    public String TAG="LOG --- ";
    ConnectivityManager mng;
    Context thisContext;

    public static String UUID_tempt ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    public static String UUID_benchmark="CDB7950D-73F1-4D4D-8E47-C090502DBD69";
    BluetoothAdapter adapter;
    BluetoothLeAdvertiser advertiser;
    AdvertiseData.Builder dataBuilder;
    AdvertiseData data;
    AdvertiseSettings st;

    public BleAdvertiser(Context tt){
        callManager(tt);
        this.thisContext=tt;
    }
    void callManager(Context con){
        mng=(ConnectivityManager)con.getSystemService(con.CONNECTIVITY_SERVICE);
    }
    private void setAdapter(){
        adapter = BluetoothAdapter.getDefaultAdapter();
        advertiser = adapter.getBluetoothLeAdvertiser();
    }
    private void setData(String id){

        dataBuilder= new AdvertiseData.Builder();

        //*****************************************************************************************************************
        dataBuilder.addServiceData(ParcelUuid.fromString(UUID_tempt), id.getBytes(Charset.forName("UTF-8")));
        //*****************************************************************************************************************

        dataBuilder.setIncludeTxPowerLevel(true);
        data = dataBuilder.build();

    }
    private void setNonBenchData(String id){
        dataBuilder= new AdvertiseData.Builder();


        //*****************************************************************************************************************
        dataBuilder.addServiceData(ParcelUuid.fromString(UUID_tempt), id.getBytes(Charset.forName("UTF-8")));
        System.out.println("benchmark - build 1");
        //*****************************************************************************************************************


        dataBuilder.setIncludeTxPowerLevel(true);
        data = dataBuilder.build();

    }
    private void setBenchData(String id){

        dataBuilder= new AdvertiseData.Builder();


        //*****************************************************************************************************************
        dataBuilder.addServiceData(ParcelUuid.fromString(UUID_benchmark), id.getBytes(Charset.forName("UTF-8")));
        System.out.println("benchmark - build 2");
        //*****************************************************************************************************************



        dataBuilder.setIncludeTxPowerLevel(true);
        data = dataBuilder.build();

    }
    private void settingAdvertising(){
        st = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();
    }
    private void startAdv(){
        advertiser.startAdvertising(st,data,advertisingCallback);
    }
    AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(TAG, "BLE ADVERTISING success.");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e(TAG, "Advertising onStartFailure: " + errorCode);
            super.onStartFailure(errorCode);
        }
    };
    public void advertising1(String myphoneID,boolean bm) {   //user this function
        this.setAdapter();
        if(bm){
            this.setBenchData(myphoneID);
        }else{
            this.setNonBenchData(myphoneID);
        }
        this.settingAdvertising();
        this.startAdv();


    }
    public void stopAdvertising(){
        advertiser.stopAdvertising(advertisingCallback);

    }
}
