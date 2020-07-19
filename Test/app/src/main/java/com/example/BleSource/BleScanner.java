package com.example.BleSource;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.BleSource.BleAdvertiser.UUID_tempt;

//import android.support.v7.app.AppCompatActivity;

public class BleScanner {

    private final static String TAG="hjhjsc";
    private final static int REQUEST_ENABLE_BT= 1;
    private final static int REQUEST_FINE_LOCATION= 2;
    private BluetoothAdapter ble_adapter_;
    private boolean is_scanning_= false;

    private ScanRecord record;

    private boolean connected_= false;
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
    private ScanResult sresult;

    private BluetoothLeScanner ble_scanner_;
    private Handler scan_handler_;
    ScanFilter.Builder sfb;
    private String[] ids;


    public static ParcelUuid pUuid = new ParcelUuid(UUID.fromString(UUID_tempt));
    String temp;
    public class INFO{
       // String id;
        String mac;

        int sigSize;
        int[] rssi;
        int[] txp;

        public String mac(){
            return this.mac;
        }
        public int[] RSSI(){
            return this.rssi;
        }
        public int[] TX(){
        return this.txp;}
        public INFO(String macc){
            this.mac=macc;
            this.sigSize=0;
            this.rssi=new int[100];
            this.txp=new int[100];


        }
        public void printSignal(){
            System.out.println("hjypypsc printsignal phone mac - "+this.mac);
            System.out.println("RSSI     "+this.rssi[0]+"    "+this.sigSize);
            System.out.println("TXP     "+this.txp[0]+"    "+this.sigSize);

        }
        public void stackSignal(int rssiw, int txpw){
            if(sigSize<100) {
                this.rssi[sigSize] = rssiw;
                this.txp[sigSize] = txpw;
                sigSize ++;
            }else{
                Log.d("hjhj signal", " signal stack full");
            }
        }



    }

    Context thisContext=null;
    FragmentActivity thisFA;

    public BleScanner(BluetoothManager bm,Context ctx, FragmentActivity fa){

        this.thisContext=ctx;
        this.thisFA=fa;
        ble_adapter_=bm.getAdapter();
        this.myResult= new HashMap<>();
        this.scdSize=0;
        this.ids=new String[100];
    }
    public void stopScan(){
        //ble_scanner_.stopScan(scan_cb_);
    }
    public ScanCallback scanCB(){
        return this.scan_cb_;
    }

    public void startScan() {
        // 스캔 가능한지 체크
        if(ble_adapter_==null) {
        Log.d("hjhjsc","adapter null");
        }
        if(!ble_adapter_.isEnabled()){
            Log.d("hjhjsc","adapter unable");
        }
        if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {
            Log.d("hjhjsc","adapter not enable hjhj");
            requestEnableBLE(this.thisFA);
            return;
        }else{
            Log.d("hjhjsc","adapter okok hjhj");

        }
        // 블루투스 권한 체크
        if (thisContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("hjhjsc","bluetooth not enable hjhj");
            requestLocationPermission(this.thisFA);
            return;
        }else{
            Log.d("hjhjsc","bluetooth okok hjhj");

        }
        byte[] mask=new byte[25];
        for(int i=0; i<25; i++){
            if(i<5){
                mask[i]=(byte)0xff;
            }else{
                mask[i]=0x00;
            }
        }

        byte[] mdt=new byte[25];
        for(int i=0; i<25; i++){
            if(i<5){
                mdt[i]=(byte)0x29;
            }else{
                mdt[i]=0x00;
            }
        }
        System.out.println("hjhjsc mask"+mask);
        System.out.println("hjhjsc data" +mdt);

        sfb=new ScanFilter.Builder();
        //sfb.setServiceUuid(ParcelUuid.fromString(UUID_tempt));
        sfb.setServiceData(ParcelUuid.fromString(UUID_tempt),new byte[]{0x00},new byte[]{0x00});
        ScanFilter sf1=sfb.build();
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(sf1);
        /*ScanFilter mscanfilter = new ScanFilter.Builder().setServiceUuid(pUuid).build();
        filters.add(mscanfilter);*/


        ///t


        ScanSettings settings= new ScanSettings.Builder().setScanMode( ScanSettings.SCAN_MODE_LOW_POWER ).setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        //ScanSettings st= new ScanSettings.Builder().
        //ble 스캐너 설정
        scan_results_= new HashMap<>();
        scan_cb_= new BLEScanCallback( scan_results_ );


        ble_scanner_ =BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();


        ble_scanner_.startScan( filters, settings, scan_cb_ );
        is_scanning_= true;
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
            Log.e(TAG, "hjhjsc BLE scan failed with code " + _error);
        }

        //스캔 결과값 출력을 위해 리스트에 저장
        //원하는 값을 빼내서 리스트에 저장하면 됨
        private void addScanResult(ScanResult _result) {
            // get scanned device
            BluetoothDevice device = _result.getDevice();
            // get scanned device MAC address
            String device_address = device.getAddress(); //mac
            String device_name=device.getName();
            //device.getUuids().
            // add the device to the result list
            cb_scan_results_.put(device_name, device);

            int rs=_result.getRssi();
           //int tx= _result.getTxPower();
           int yy=_result.getScanRecord().getTxPowerLevel();
           int tx=txLevelToPower(yy);
           System.out.println("hjhjsc txl  "+yy);
           String ad= _result.getDevice().getAddress();
            byte[] sdata=_result.getScanRecord().getServiceData().get(ParcelUuid.fromString(UUID_tempt));
            String sdatatoString=new String(sdata, StandardCharsets.US_ASCII);



            //byte[] resultdata= _result.getScanRecord().getManufacturerSpecificData(1);
            //byte[] scannedIDByte=new byte[20];
            /*
            for(int k=0; k<20; k++){

                scannedIDByte[k]=resultdata[k+5];

            }
            String scannedID=scannedIDByte.toString();

            */
            int phnum=scdSize+1;
            String name="phn"+phnum;
            String macadr=ad;

            String tempID=name;
            if(sdata!=null){
                System.out.println("hjhjsc sdata   bytecode"+sdata+"   toString    "+sdatatoString);
                tempID=sdatatoString;
                ad=tempID;
            }

            if(myResult.containsKey(ad)){
                myResult.get(ad).stackSignal(rs,tx);

            }else{

                ids[scdSize]=ad;
                INFO info= new INFO(macadr);
                scdSize++;
                info.stackSignal(rs,tx);
                myResult.put(ad,info);

                if(myResult.size()==scdSize) {
                    for (int u = 0; u < myResult.size(); u++) {
                        myResult.get(ids[u]).printSignal();
                    }
                }else {
                }


            }

            Log.d("hjhjsc id(name)",name);
            Log.d("hjhjsc myResult Size : ",""+myResult.size());


            // log
            Log.d("hjhjsc ", String.valueOf(cb_scan_results_.size()));
            Log.d("hjhjsc addr ", String.valueOf(device_address));
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

    private void requestEnableBLE(FragmentActivity thisfa) {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        thisfa.startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );

    }

    private void requestLocationPermission(FragmentActivity thisfa) {
        thisfa.requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
    }





}
