package com.example.BleSource;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class BleTester {

    private BleAdvertiser bleAdvr;
    private BleScanner bleScr;
    private BluetoothManager bleMgr;
    private Build.VERSION api;

    private String myPhoneID;  //내 폰 ID

    private TextView mainView;
    private double comparedDistance=4; // ble신호로 산출한 거리와 비교할 거리 범위의 반지름 m

    private int PhoneIDSizeFromServer;
    private int PhoneSizeFromBleScanned;

    private String[] PhoneIDFromServer;
    private phoneInfo[] PhonesFromBleScanned; //ble신호로 받은 같은 어플을 사용하는 폰들

    private class phoneInfo{

        public phoneInfo(String id, int[] rssiv, int[] txlv,double Dist){
            this.phoneID=id;
            this.nearByBeacon=false;
            this.nearByBle=false;
            this.DistByBle= Dist;
            this.rssi=rssiv;
            this.txLEVEL=txlv;
        }

        int[] rssi;
        int[] txLEVEL;

        String phoneID;
        boolean nearByBeacon = false; // 비콘정보로 '나'와 같은 범위내이면 true, 그렇지 않으면 false
        boolean nearByBle = false;  // nearByBeacon이 true인 phone들 중, ble신호와 비교하여 가까이
        double DistByBle=0.0;   // ble 신호로 추정한 거리 값

        private int[] getRssi(){return this.rssi;}
        private int[] getTxlv(){return this.txLEVEL;}
        private String getID(){
            return this.phoneID;
        }
        private double getDist(){
            return this.DistByBle;
        }
        public void setNearByBLE(boolean b) {
            this.nearByBle=b;
        }
        public boolean getNearByBLE(){
            return this.nearByBle;
        }
        public boolean getNearByBeacon(){
            return this.nearByBeacon;
        }
        public void setNearByBeacon(boolean b) {
            this.nearByBeacon=b;
        }
        private String getRssiText(){
            String tr="";
            for(int l=0; l<this.rssi.length; l++){
                tr+=this.rssi[l]+", ";
            }
            return tr;
        }
        private String getTXText(){
            String tr="";
            for(int l=0; l<this.txLEVEL.length; l++){
                tr+=this.txLEVEL[l]+", ";
            }
            return tr;
        }
    }
    public void BleTestFunc(String myID, final TextView datatxt) {  //메인 기능 함수

        setCompdDist(2.0);
        setMyID(myID);
        setTextView(datatxt);

        myAdvertiser().advertising1(getMyID());//
        myScanner().startScan();

        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run() {
                myScanner().stopScan();
                myAdvertiser().stopAdvertising();
                BleTestFunc2(getTextView());

            }
        }, 15000); //60000 == 1분
    }
    public void BleTestFunc2(TextView resultView){
        PhonesFromBleScanned=scanfilteredPhoneDatas();
        PhoneSizeFromBleScanned=PhonesFromBleScanned.length;

        for(int v=0; v<this.PhoneSizeFromBleScanned; v++) {
            Log.d("hjhj re ",PhonesFromBleScanned[v].phoneID+"  는 ");
            if (ifNear(PhonesFromBleScanned[v].getDist())) {
                PhonesFromBleScanned[v].setNearByBLE(true);
                Log.d("hjhj re ","가까이잇소 ");
            } else {
                Log.d("hjhj re ","멀리잇소 ");
            }
        }
        this.checkPhonesNearBeacon(PhoneIDFromServer,PhonesFromBleScanned);
        showResult(resultView);
    }

    public void setIDFromServer(String[] fromServer){   //서버에서 같은 영역 내의 아이디 목록 가져옴
        if(fromServer.length==0){
            System.out.println("from server data size 0");
        }else {
            for (int k = 0; k < fromServer.length; k++) {
                System.out.println("hjhjt sv data  " + fromServer[k]);
            }
        }
        if(fromServer==null) {
            System.out.println("hjhj from server null");
        }else {
            this.PhoneIDFromServer = new String[fromServer.length];
            this.PhoneIDFromServer = fromServer;
            PhoneIDSizeFromServer = PhoneIDFromServer.length;
        }
    }

    public void showResult(TextView tv){    //MainActivity 에 BLE TEST 결과 출력
        TextView bletxt = tv;

        String[] temp= new String[PhoneSizeFromBleScanned];
        int n=PhoneSizeFromBleScanned;
        if(n==0){
            System.out.println("hjhj Ble scanned size 0");
        }
        String txtView="BLE TEST FINISH - "+n+"개 스캔됨\n";
        for(int i=0; i<n; i++) {
            if (PhonesFromBleScanned[i] != null) {
                temp[i] = "[ID:" + PhonesFromBleScanned[i].phoneID + "/TXlevel: "+PhonesFromBleScanned[i].getTxlv()[0]+" /nearBeacon:" + PhonesFromBleScanned[i].getNearByBeacon() + " /nearBLE:" + PhonesFromBleScanned[i].getNearByBLE() + " /DistByBLE:" + PhonesFromBleScanned[i].getDist() + "]";
                txtView+=temp[i]+"\n";
                txtView+="ㄴ> RSSI: "+PhonesFromBleScanned[i].getRssiText()+"\n";
            }
        }
        tv.setText(txtView);
    }



    public BleTester(Context thisctx, FragmentActivity fa){
        bleAdvr= new BleAdvertiser(thisctx);
        bleMgr=(BluetoothManager)thisctx.getSystemService( Context.BLUETOOTH_SERVICE );
        bleScr= new BleScanner(bleMgr,thisctx, fa);

        this.PhoneSizeFromBleScanned=0;
        this.PhoneIDSizeFromServer=0;
        this.api=new Build.VERSION();
        System.out.println("hjhjtt api"+this.api.SDK_INT);
    }

    private phoneInfo[] VirtualPhones(){    //가상의 핸드폰 만들기
        phoneInfo[] pi= new phoneInfo[10];
        for(int i=0; i<10; i++){
            pi[i]=new phoneInfo("ph"+(i+1),new int[]{-50, -56,-50, -56,-50, -56,-50, -56,-50, -56,-50, -56},new int[]{1, 1,1, 1,1, 1,1, 1,1, 1,1, 1},0.7 );
        }
        return pi;
    }

    private void checkPhonesNearBeacon(String[] idFromServer, phoneInfo[] phonesBLE){
        if(idFromServer.length==this.PhoneIDSizeFromServer) {

            for (int i = 0; i < this.PhoneIDSizeFromServer; i++) {
                System.out.println("hjhjsv in check func"+idFromServer[i]);
                boolean findSameID=false;
                for(int j=0; j<this.PhoneSizeFromBleScanned; j++){
                    if(idFromServer[i].equals(phonesBLE[j].getID())){   //중복아이디 고려 안함
                        findSameID=true;
                        phonesBLE[j].setNearByBeacon(true);
                        j=this.PhoneSizeFromBleScanned;
                    }
                }
                if(findSameID==true) {
                }else{
                    System.out.println("hjhj error: can't find SAME PHONE ID from ble scanned LIST");
                }
            }
        }else{
           System.out.println("hjhj error , size not correct");
        }
    }
    private phoneInfo[] scanfilteredPhoneDatas(){   //BleScanner에서 스캔된 결과 가져옴

        int sz= myScanner().getAddrsSize();
        phoneInfo[] temppi=new phoneInfo[sz];

        for(int g=0; g<sz; g++){
            String tpid =myScanner().ids()[g];
            double tempDist=this.computeDist1(myScanner().getScanned().get(tpid).RSSI(),myScanner().getScanned().get(tpid).TX(),myScanner().getScanned().get(tpid).sigSize);
            temppi[g]=new phoneInfo(tpid,myScanner().getScanned().get(tpid).RSSI(),myScanner().getScanned().get(tpid).TX(),tempDist);
        }
        return temppi;
    }

    private boolean ifNear(double DistanceByBLE/** 미터로 환산한 거리값**/){
        if(DistanceByBLE>=0&&DistanceByBLE<= getCompdDist()){
            return true;
        }else{
            return false;
        }
    }
    private Double computeDist2(){

        return 0.0;
    }
    private Double computeDist1(int[] rssi, int[] txP, int signalsize){
        double DIST= 0.0;
        int n=signalsize;

        String printedrssi="";
        String printedtxp="";

        for(int m=0; m<n; m++){
            printedrssi+=" "+rssi[m];
            printedtxp+=" "+txP[m];

        }
        double meanTX=0;
        double meanRS=0;
            for(int p=0; p<n; p++){
                meanTX+=txP[p];
                meanRS+=rssi[p];
            }
            meanTX=meanTX/n;
            meanRS=meanRS/n;
            double val1=meanTX-meanRS;
            double val2=val1/20;
            DIST=Math.pow(10.0,val2);

        Log.d("hjypypre","DIST"+DIST);
        Log.d("hjypypre","rssis"+printedrssi);
        Log.d("hjypypre","txps"+printedtxp);
        return DIST;
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
    public void setCompdDist(double radius){ this.comparedDistance=radius;}
    private double getCompdDist(){
        return this.comparedDistance;
    }
    private void setTextView(TextView tv){
        this.mainView=tv;
    }
    private TextView getTextView(){
        return this.mainView;
    }
}
