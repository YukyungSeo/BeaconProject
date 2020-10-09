package com.example.bletest;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BleScanner {

    private final static String TAG="LOG --- ";
    private final static int REQUEST_ENABLE_BT= 1;
    private final static int REQUEST_FINE_LOCATION= 2;
    private BluetoothAdapter ble_adapter_;
    public boolean is_scanning_= false;
    public static String UUID_tempt ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    private Map<String, BluetoothDevice> scan_results_;
    public int scdSize;
    private HashMap<String, INFO> myResult;     // --> <ID,info>
    public HashMap<String,INFO> getScanned(){
        return this.myResult;
    }
    public String[] ids(){
        return this.ids;
    }
    public int getAddrsSize(){
        return this.scdSize;
    }
    private ScanCallback scan_cb_;
    private BluetoothLeScanner ble_scanner_;
    ScanFilter.Builder sfb;
    private String[] ids;
    Context thisContext=null;
    FragmentActivity thisFA;
    String myid;
    boolean benchmark;

    public class INFO{
        String mac;
        boolean bench;
        int sigSize;
        int[] rssi;
        int[] txp;

        public String mac(){
            return this.mac;
        }
        public int[] RSSI(){
            return this.rssi;
        }
        public int[] TX(){ return this.txp;}
        public INFO(String macc,boolean bm){
            this.mac=macc;
            this.sigSize=0;
            this.bench=bm;
            this.rssi=new int[100];
            this.txp=new int[100];


        }
        public void stackSignal(int rssiw, int txpw){
            if(sigSize<100) {
                this.rssi[sigSize] = rssiw;
                this.txp[sigSize] = txpw;
                sigSize ++;
            }else{
                Log.d(TAG, "SIGNAL STACK FULL");
            }
        }
        public void sigSizeUp(){
            if(sigSize<20000) {
                sigSize++;
            }
        }
    }


    public BleScanner(BluetoothManager bm, Context ctx, FragmentActivity fa){

        this.thisContext=ctx;
        this.thisFA=fa;
        ble_adapter_=bm.getAdapter();
        this.myResult= new HashMap<>();
        this.scdSize=0;
        this.ids=new String[100];
        this.myid="NULLID";
    }
    public void stopScan(){
        if(this.is_scanning_) {
            ble_scanner_.stopScan(scan_cb_);
            this.is_scanning_ = false;
            System.out.println(TAG + " SCAN STOP");
        }
    }
    public void startScan(String id,boolean bm) {
        this.benchmark=bm;

        this.myResult.clear();
        this.scdSize=0;
        this.ids=new String[100];
        this.myid=id;
        if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {

            requestEnableBLE(this.thisFA);
            return;
        }
        if (thisContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission(this.thisFA);
            return;
        }
        scan_results_= new HashMap<>();
        scan_cb_= new BLEScanCallback( scan_results_ );
        List<ScanFilter> filters = new ArrayList<>();
        //Scan Filtering 이 동작 안 됨. 원인 불명.
        /*
        sfb=new ScanFilter.Builder();
        byte[] mask=new byte[]{0x00};
        sfb.setServiceData(ParcelUuid.fromString(UUID_tempt),mask,mask);
        ScanFilter sf1=sfb.build();
       filters.add(sf1);
       */
        ScanSettings settings= new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
        ble_scanner_ =BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        ble_scanner_.startScan( filters, settings, scan_cb_ );
        is_scanning_= true;
    }

    private class BLEScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> cb_scan_results_;

        BLEScanCallback(Map<String, BluetoothDevice> _scan_results) {
            cb_scan_results_ = _scan_results;
        }

        @Override
        public void onScanResult(int _callback_type, ScanResult _result) {
            Log.d(TAG, "onScanResult");
            super.onScanResult(_callback_type, _result);

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
            Log.e(TAG, "onScanFailed - code : " + _error);
        }

        private void addScanResult(ScanResult _result) {
            System.out.println(TAG+"onAddScanResult");
            BluetoothDevice device = _result.getDevice();

            int rs=_result.getRssi();
            int yy=_result.getScanRecord().getTxPowerLevel();
            int tx=txLevelToPower(yy);
            String address= _result.getDevice().getAddress();
            String tempID="NONE";
            //scan_results_.put(address,device);

            //*****************************************************************************************************************
            byte[] sdata;

            Map<ParcelUuid,byte[]> tempMap= _result.getScanRecord().getServiceData();
            if(tempMap.containsKey(ParcelUuid.fromString(UUID_tempt))){

                sdata=_result.getScanRecord().getServiceData().get(ParcelUuid.fromString(UUID_tempt));
                tempID=new String(sdata, StandardCharsets.UTF_8);
                address=tempID;
                System.out.println("SCANNED [ "+ address+" - NONBENCH ]   Device Size - "+myResult.size());
                PHPConnect connect = new PHPConnect();
                String URL = "http://168.188.129.191/send_ble_data.php?my_id="+myid+"&other_id="+tempID+"&rssi="+rs;
                connect.execute(URL);
                if(myResult.containsKey(address)){
                    myResult.get(address).sigSizeUp();
                }else{

                    ids[scdSize]=address;
                    INFO info= new INFO(address,false);
                    scdSize++;
                    info.sigSizeUp();
                    myResult.put(address,info);
                }
            }else{
                System.out.println("SCANNED [ "+ address+" ]  - UNNASSASARY DEVICE ");

            }
            //*****************************************************************************************************************






        }
    }

    public int txLevelToPower(int level){
        int txPw=0;
        switch(level){
            case 0:
                txPw=-30;
                break;
            case 1:
                txPw=-20;
                break;
            case 2:
                txPw=-16;
                break;
            case 3:
                txPw=-12;
                break;
            case 4:
                txPw=-18;
                break;
            case 5:
                txPw=-14;
                break;
            case 6:
                txPw=0;
                break;
            case 7:
                txPw=4;
                break;

        }
        return txPw;
    }
    public void setResutText(TextView tv){
        String temptxt="";
        int n=this.scdSize;
        String tempaddr="";
        int tempsigsize=0;
        String tempbenchtxt="";
            temptxt+="END\n";

        for(int i=0; i<n; i++){
            tempaddr=myResult.get(this.ids()[i]).mac;
            tempsigsize=myResult.get(this.ids()[i]).sigSize;
            temptxt+="{ "+tempaddr+"/"+"/"+tempsigsize+" } ,";
        }



        tv.setText(temptxt);

    }
    private void requestEnableBLE(FragmentActivity thisfa) {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        thisfa.startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );

    }

    private void requestLocationPermission(FragmentActivity thisfa) {
        thisfa.requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
    }
}
