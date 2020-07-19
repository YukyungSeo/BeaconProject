package com.example.BleSource;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.Charset;


public class BleAdvertiser {
    public String LOG_TAG="hjhjad";
    ConnectivityManager mng;
    Context thisContext;



    public static String UUID_tempt ="CDB7950D-73F1-4D4D-8E47-C090502DBD63";
    BluetoothAdapter adapter;
    BluetoothLeAdvertiser advertiser;


    AdvertiseData.Builder dataBuilder;
    AdvertiseData data;

    AdvertisingSetParameters.Builder bld;
    AdvertisingSetParameters parameters;

    AdvertisingSet currentAdvertisingSet;

    AdvertiseSettings st;


    /////////////////////////////////



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
        Log.d(LOG_TAG,"hjhjad myaddr" +adapter.getAddress());

    }
    private void setAdvParameter(){
        bld= new AdvertisingSetParameters.Builder();
        bld.setLegacyMode(true); // True by default, but set here as a reminder.
        bld.setScannable(true);
        bld.setConnectable(true);
        bld.setInterval(AdvertisingSetParameters.INTERVAL_HIGH);
        bld.setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM);

        parameters = bld.build();

    }
    byte[] md;
    private void setData(String id){
        //
        dataBuilder= new AdvertiseData.Builder();
        //dataBuilder.setIncludeDeviceName(true);
        //dataBuilder.setIncludeTxPowerLevel(true); //tx파워값을 패킷에 넣어 보냄~


        int datalength=1;
        md=new byte[datalength];
        byte[] idtobyte=id.getBytes(Charset.forName("UTF-8"));
        System.out.println("hjhjad id  "+id);
        System.out.println("hjhjad idto byte length  "+idtobyte.length);

        for(int i=0; i<datalength; i++){
           if(i<5) {
               md[i] = (byte) 0x29;   //filtered
           }else{
               md[i]=(byte)0x00;
           }

        }
        /*
        int idlen=idtobyte.length;
        if(idtobyte.length>datalength){
            idlen=datalength;
        }

        for(int j=5; j<5+idlen; j++){
            md[j]=idtobyte[j-5];         //id
        }
        */

       Log.d("hjhjad adv data len",""+md.length);
        System.out.println("hjhjad adv data len       "+md.length);
        //set data tester

        String uid = "hajoo";

        //설정한 uuid와 edittext 값을 바탕으로 광고 데이터 생성
        //mAdvData = new AdvertiseData.Builder();
        //dataBuilder.addServiceUuid(ParcelUuid.fromString(UUID_tempt));
       // mAdvData.addServiceData(pUuid, uid.getBytes(Charset.forName("UTF-8")));


        //dataBuilder.addServiceData(ParcelUuid.fromString(UUID_tempt), new byte[]{0x00,0x00});
        dataBuilder.addServiceData(ParcelUuid.fromString(UUID_tempt), id.getBytes(Charset.forName("UTF-8")));
        dataBuilder.setIncludeTxPowerLevel(true);
        //dataBuilder.addManufacturerData(1,md);  //MANUFACTURER DATA SIZE : 25 BYTES.
       // adapter.getMa
       // dataBuilder.addServiceData();
       // dataBuilder.addServiceUuid();

        //

        data = dataBuilder.build();

    }
    private void settingAdvertising(){
        advertiser.startAdvertisingSet(parameters, data, null, null, null, callback);

        st = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();


    }
    private void startAdv(){

        advertiser.startAdvertising(st,data,advertisingCallback);
    }
    private void stopAdvSet(){
        advertiser.stopAdvertisingSet(callback);

    }
    AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(LOG_TAG, "LE Advertise success.");

        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e(LOG_TAG, "Advertising onStartFailure: " + errorCode);
            super.onStartFailure(errorCode);
        }
    };
    AdvertisingSetCallback callback = new AdvertisingSetCallback() {
        @Override
        public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
            Log.i(LOG_TAG, "hjhjad onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                    + status);
            currentAdvertisingSet = advertisingSet;
        }

        @Override
        public void onAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
            Log.i(LOG_TAG, "hjhjad onAdvertisingDataSet() :status:" + status);
        }

        @Override
        public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
            Log.i(LOG_TAG, "hjhjad onScanResponseDataSet(): status:" + status);
        }

        @Override
        public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
            Log.i(LOG_TAG, "hjhjad onAdvertisingSetStopped():");
        }
    };

    public void advertising1(String myphoneID) {   //user this function
        this.setAdapter();
        this.setAdvParameter();
        this. setData(myphoneID);
        this.settingAdvertising();

        this.startAdv();
        this.stopAdvSet();


    }
    public void stopAdvertising(){
        advertiser.stopAdvertising(advertisingCallback);
    }










    //










    /***
    void advertising2(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeAdvertiser advertiser =
                BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        // Check if all features are supported
        if (!adapter.isLe2MPhySupported()) {
            Log.e(LOG_TAG, "2M PHY not supported!");
            return;
        }
        if (!adapter.isLeExtendedAdvertisingSupported()) {
            Log.e(LOG_TAG, "LE Extended Advertising not supported!");
            return;
        }

        int maxDataLength = adapter.getLeMaximumAdvertisingDataLength();

        AdvertisingSetParameters.Builder parameters = (new AdvertisingSetParameters.Builder())
                .setLegacyMode(false)
                .setInterval(AdvertisingSetParameters.INTERVAL_HIGH)
                .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
                .setPrimaryPhy(BluetoothDevice.PHY_LE_2M)
                .setSecondaryPhy(BluetoothDevice.PHY_LE_2M);

        AdvertiseData data = (new AdvertiseData.Builder()).addServiceData(new
                        ParcelUuid(UUID.randomUUID()),
                "You should be able to fit large amounts of data up to maxDataLength. This goes up to 1650 bytes. For legacy advertising this would not work".getBytes()).build();

        AdvertisingSetCallback callback = new AdvertisingSetCallback() {
            @Override
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                Log.i(LOG_TAG, "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status);
                currentAdvertisingSet = advertisingSet;
            }

            @Override
            public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                Log.i(LOG_TAG, "onAdvertisingSetStopped():");
            }
        };

        advertiser.startAdvertisingSet(parameters.build(), data, null, null, null, callback);

        // After the set starts, you can modify the data and parameters of currentAdvertisingSet.
        currentAdvertisingSet.setAdvertisingData((new
                AdvertiseData.Builder()).addServiceData(new ParcelUuid(UUID.randomUUID()),
                "Without disabling the advertiser first, you can set the data, if new data is less than 251 bytes long.".getBytes()).build());

        // Wait for onAdvertisingDataSet callback...

        // Can also stop and restart the advertising
        currentAdvertisingSet.enableAdvertising(false, 0, 0);
        // Wait for onAdvertisingEnabled callback...
        currentAdvertisingSet.enableAdvertising(true, 0, 0);
        // Wait for onAdvertisingEnabled callback...

        // Or modify the parameters - i.e. lower the tx power
        currentAdvertisingSet.enableAdvertising(false, 0, 0);
        // Wait for onAdvertisingEnabled callback...
        currentAdvertisingSet.setAdvertisingParameters(parameters.setTxPowerLevel
                (AdvertisingSetParameters.TX_POWER_LOW).build());
        // Wait for onAdvertisingParametersUpdated callback...
        currentAdvertisingSet.enableAdvertising(true, 0, 0);
        // Wait for onAdvertisingEnabled callback...

        // When done with the advertising:
        advertiser.stopAdvertisingSet(callback);


    }
     ***/

}
