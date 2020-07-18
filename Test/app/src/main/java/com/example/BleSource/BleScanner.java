package com.example.BleSource;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import android.support.v7.app.AppCompatActivity;

public class BleScanner {

    private final static String TAG="Central";
    private final static int REQUEST_ENABLE_BT= 1;
    private final static int REQUEST_FINE_LOCATION= 2;
    private BluetoothAdapter ble_adapter_;
    private boolean is_scanning_= false;

    private boolean connected_= false;
    private Map<String, BluetoothDevice> scan_results_;
    private ScanCallback scan_cb_;
    private BluetoothLeScanner ble_scanner_;
    private Handler scan_handler_;
    public static String UUID_tempt ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    public static ParcelUuid pUuid = new ParcelUuid(UUID.fromString(UUID_tempt));
    String temp;

    Context thisContext=null;
    FragmentActivity thisFA;

    public BleScanner(Context ctx, FragmentActivity fa){

        this.thisContext=ctx;
        this.thisFA=fa;
    }

    public ScanCallback scanCB(){
        return this.scan_cb_;
    }

    public void startScan() {
        // 스캔 가능한지 체크
        if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {
            Log.d("dddd","adapter not enable hjhj");
            requestEnableBLE(this.thisFA);
            return;
        }else{
            Log.d("dddd","adapter okok hjhj");

        }
        // 블루투스 권한 체크
        if (thisContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("dddd","bluetooth not enable hjhj");
            requestLocationPermission(this.thisFA);
            return;
        }else{
            Log.d("dddd","bluetooth okok hjhj");

        }

        List<ScanFilter>filters = new ArrayList<>();
        /*ScanFilter mscanfilter = new ScanFilter.Builder().setServiceUuid(pUuid).build();
        filters.add(mscanfilter);*/

        ScanSettings settings= new ScanSettings.Builder().setScanMode( ScanSettings.SCAN_MODE_LOW_POWER ).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        //ble 스캐너 설정
        scan_results_= new HashMap<>();
        scan_cb_= new BLEScanCallback( scan_results_ );
        ble_scanner_ =BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        ble_scanner_.startScan( filters, settings, scan_cb_ );
        is_scanning_= true;
    }


    private void requestEnableBLE(FragmentActivity thisfa) {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        thisfa.startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );

    }

    private void requestLocationPermission(FragmentActivity thisfa) {
        thisfa.requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
    }


    private class BLEScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> cb_scan_results_;

        BLEScanCallback() {

        }

        //스캔 결과 저장
        BLEScanCallback(Map<String, BluetoothDevice> _scan_results) {
            cb_scan_results_ = _scan_results;
        }

        @Override
        //스캔 결과 띄워주는 함수
        //함수 내부에 원하는 코드 작성
        public void onScanResult(int _callback_type, ScanResult _result) {
            Log.d(TAG, "onScanResult");
            super.onScanResult(_callback_type, _result);

            /*if(_result == null || _result.getDevice()==null || TextUtils.isEmpty(_result.getDevice().getName())) {
                StringBuilder builder = new StringBuilder(_result.getDevice().getName());
                builder.append("\n").append(new String(_result.getScanRecord().getServiceData(_result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));
                System.out.print(builder.toString());
            }else{
                System.out.print("not found");
            }*/

            addScanResult(_result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> _results) {
            for (ScanResult result : _results) {
                addScanResult(result);
            }
        }

        @Override
        public void onScanFailed(int _error) {
            Log.e(TAG, "BLE scan failed with code " + _error);
        }

        //스캔 결과값 출력을 위해 리스트에 저장
        //원하는 값을 빼내서 리스트에 저장하면 됨
        private void addScanResult(ScanResult _result) {
            // get scanned device
            BluetoothDevice device = _result.getDevice();
            // get scanned device MAC address
            String device_address = device.getAddress(); //mac
            // add the device to the result list
            cb_scan_results_.put(device_address, device);
            // log
            Log.d("hjhj ", String.valueOf(cb_scan_results_.size()));
            Log.d("hjaddr ", String.valueOf(device_address));
            //System.out.println(_result);
            //System.out.println(device);
            //Log.d( TAG, "scan results device2: " + device_address );
            //temp = device_address;
        }



        private void printScanResult() {

            for (int i = 0; i < cb_scan_results_.size(); i++) {


            }


        }
    }


}
