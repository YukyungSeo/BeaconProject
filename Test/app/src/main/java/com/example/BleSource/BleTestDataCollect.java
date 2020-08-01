package com.example.BleSource;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

public class BleTestDataCollect {


    private BleAdvertiser bleAdvr;
    private BleScanner bleScr;
    private BluetoothManager bleMgr;
    private Build.VERSION api;
    String TAG="LOG --- ";

    private String myPhoneID;  //내 폰 ID

    private TextView mainView;
    private int PhoneSizeFromBleScanned;

    private String[] PhoneIDFromServer;
    private phoneInfo[] PhonesFromBleScanned; //ble신호로 받은 같은 어플을 사용하는 폰들

    public BleTestDataCollect(Context thisctx, FragmentActivity fa){
        bleAdvr= new BleAdvertiser(thisctx);
        bleMgr=(BluetoothManager)thisctx.getSystemService( Context.BLUETOOTH_SERVICE );
        bleScr= new BleScanner(bleMgr,thisctx, fa);



        this.PhoneSizeFromBleScanned=0;
        this.api=new Build.VERSION();
        System.out.println("ANDROID API : "+this.api.SDK_INT);
    }

    public void BleStart(String myID,TextView datatxt) {  //메인 기능 함수


        setMyID(myID);
        setTextView(datatxt);
        showScanning(datatxt);

        myAdvertiser().advertising1(getMyID());
        myScanner().startScan();

        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run() {
                myScanner().stopScan();
                myAdvertiser().stopAdvertising();
                //BleDataCollect(getTextView());

            }
        }, 180000); //60000 == 1분
    }
    public void BleDataCollect(TextView resultView){
        PhonesFromBleScanned=scanfilteredPhoneDatas();
        PhoneSizeFromBleScanned=PhonesFromBleScanned.length;
        showResult(resultView);
    }



    private phoneInfo[] scanfilteredPhoneDatas(){   //BleScanner에서 스캔된 결과 가져옴

        int sz= myScanner().getAddrsSize();
        phoneInfo[] temppi=new phoneInfo[sz];
        BleScanner.INFO temp;

        for(int g=0; g<sz; g++){
            String tpid =myScanner().ids()[g];
            temp=myScanner().getScanned().get(tpid);

            temppi[g]=new phoneInfo(tpid,temp.RSSI(),temp.sigSize);
        }
        return temppi;
    }

    public void showResult(TextView tv){    //MainActivity 에 BLE TEST 결과 출력
        TextView bletxt = tv;

        String[] temp= new String[PhoneSizeFromBleScanned];
        int n=PhoneSizeFromBleScanned;
        int ss=0;
        if(n==0){
        }
        String txtView="BLE TEST FINISH - "+n+"개 스캔됨\n";
        for(int i=0; i<n; i++) {
            if (PhonesFromBleScanned[i] != null) {
                temp[i] = "[ID:" + PhonesFromBleScanned[i].phoneID +"]";
                txtView+=temp[i]+"\n";
                ss=PhonesFromBleScanned[i].sigsize-15;
                txtView+="ㄴ> RSSI 신호개수:"+ss+"\n";
            }
        }
        tv.setText(txtView);
    }

    public class phoneInfo{

        public phoneInfo(String id, int[] rssiv,int sigsz){
            this.phoneID=id;
            this.rssi=rssiv;
            this.sigsize=sigsz;
        }
        int sigsize;
        int[] rssi;

        String phoneID;

        private int[] getRssi(){return this.rssi;}
        private String getID(){
            return this.phoneID;
        }

        private String getRssiText(){
            String tr="";
            for(int l=0; l<this.rssi.length; l++){
                tr+=this.rssi[l]+", ";
            }
            return tr;
        }
    }
    private void showScanning(TextView tv){
        tv.setText("SCANNING, ADVERTISING");
    }

    public void setMyID(String id){this.myPhoneID=id; }
    public String getMyID(){
        return this.myPhoneID;
    }
    private BleAdvertiser myAdvertiser(){
        return this.bleAdvr;
    }
    private BleScanner myScanner(){
        return this.bleScr;
    }
    private void setTextView(TextView tv){
        this.mainView=tv;
    }
    private TextView getTextView(){
        return this.mainView;
    }

}
