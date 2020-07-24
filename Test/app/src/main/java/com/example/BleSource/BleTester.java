package com.example.BleSource;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;


import com.example.test.PHPConnect;

import java.util.concurrent.ExecutionException;

public class BleTester {
    public void BleTestFunc2(){
        // 2 - get filtered List and stack ble data
        //PhonesFromBleScanned=VirtualPhones();   //test data
        PhonesFromBleScanned=scanfilteredPhoneDatas();
        PhoneSizeFromBleScanned=PhonesFromBleScanned.length;


        // 2.5 - get phone info in same area from Server
        //PhoneIDFromServer= new String[]{"ph5","ph2","ph7","ph4","ph3"}; //test data
        //PhoneIDFromServer= getIDFromServer();
        //PhoneIDSizeFromServer=PhoneIDFromServer.length;


        // ble 스캔 리스트의 모든 기기의 거리를 비교범위와 비교함
        for(int v=0; v<this.PhoneSizeFromBleScanned; v++) {
            Log.d("hjhj re ",PhonesFromBleScanned[v].phoneID+"  는 ");
            if (ifNear(PhonesFromBleScanned[v].getDist())) {
                PhonesFromBleScanned[v].setNearByBLE(true);
                Log.d("hjhj re ","가까이잇소 ");

            } else {
                Log.d("hjhj re ","멀리잇소 ");
            }

        }


        //ble로 스캔된 기기들중 아이디를 확인하여 서버에서 받은 기기들과 같은 기기들을 걸러냄.
        // 6 - compare BLE data and Server data and set nearByBeacon:
        this.checkPhonesNearBeacon(PhoneIDFromServer,PhonesFromBleScanned);


        // 7 - get  RESULT
        // nearbybeacon==true&&nearbyble==true 인 기기가 비콘측량과 ble측량에서 유사정확도안에 포함되는 기기임 ***
        // nearbybeacon==false&&nearbyble==true 서버에서 같은 영역안의 기기라고 정해지진 않았지만, ble거리 측정에서 유사범위안에 포함되는 기기임
        //nearbybeacon==true&&nearbyble==false 서버에서 같은 영역안 기기라고 결정했지만, ble 측정결과 아니라고 결정한 기기 ***
        //nearbybeacon==falsee&&nearbyble==false

    }
    public void setIDFromServer(String[] fromServer){
        // 서버에서 받아온 같은 영역에 있는 phone들의 ID string 목록임

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
    public String[] getResult(){
        String[] temp= new String[PhoneSizeFromBleScanned];
        int n=PhoneSizeFromBleScanned;

        for(int i=0; i<n; i++) {
            if (temp[i] != null) {
                temp[i] = "[ID:" + PhonesFromBleScanned[i].phoneID + " nearBeacon:" + PhonesFromBleScanned[i].getNearByBeacon() + " nearBLE:" + PhonesFromBleScanned[i].getNearByBLE() + " DistByBLE:" + PhonesFromBleScanned[i].getDist()+"]";
            }
        }



        return temp;
    }
    public void BleTestFunc(String myID) {

        //set compared Distance:
        setCompdDist(2.0);
        //set my id from UI textView
        setMyID(myID);

        // 0 -start BLE advertising
        myAdvertiser().advertising1(getMyID());//

        // 1 - start  BLE scanning
        myScanner().startScan();
        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run() {

                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");


                BleTestFunc2();
                myScanner().stopScan();
                myAdvertiser().stopAdvertising();

            }
        }, 15000); //60000 == 1분
    }


    private BleAdvertiser bleAdvr;
    private BleScanner bleScr;
    private BluetoothManager bleMgr;
    //private BluetoothAdapter bleAdtr;

    private String myPhoneID;  //내 폰 ID



    private int myX;    //내가 있는 좌표 x
    private int myY;    //내가 있는 좌표 y


    private double comparedDistance=0.6; // ble신호로 산출한 거리와 비교할 거리 범위의 반지름 m



    private int PhoneIDSizeFromServer;
    private int PhoneSizeFromBleScanned;
    private int PhoneSizeCheckedBOTH;

    private String[] PhoneIDFromServer;

    private phoneInfo[] PhonesFromBleScanned; //ble신호로 받은 같은 어플을 사용하는 폰들


    public BleScanner.INFO getScannedINFOByID(String id){

        return null;
    }
    private phoneInfo[] scanfilteredPhoneDatas(){

        int sz= myScanner().getAddrsSize();
        phoneInfo[] temppi=new phoneInfo[sz];

        for(int g=0; g<sz; g++){
            String tpid =myScanner().ids()[g];



            double tempDist=this.RSSItoDist(myScanner().getScanned().get(tpid).RSSI(),myScanner().getScanned().get(tpid).TX(),myScanner().getScanned().get(tpid).sigSize);

            temppi[g]=new phoneInfo(tpid,false,false,tempDist);



        }









        return temppi;
    }

    private class phoneInfo{

        public phoneInfo(String id, boolean nbBeacon, boolean nebBle, double Dist){
            this.phoneID=id;
            this.nearByBeacon=nbBeacon;
            this.nearByBle=nebBle;
           // this.rssi= new double[]{-60, -50, -80,-80,-80,-80,-80,-88,-88,-80,-80,-80,-50,-50,-50,-50,-55-50,-50};
            //this.txP=2.0;
            this.DistByBle= Dist;
        }

        String phoneID;

        boolean nearByBeacon = false; // 비콘정보로 '나'와 같은 범위내이면 true, 그렇지 않으면 false
        boolean nearByBle = false;  // nearByBeacon이 true인 phone들 중, ble신호와 비교하여 가까이
                                    //있는걸로 최종 확인되면 true, 그렇지 않으면 false
        //double[] rssi;              //scancallback에서 받아서 저장할 값
        //double txP;               // 위와같은내용

        double DistByBle=0.0;   // ble 신호로 추정한 거리 값



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
    }

    public BleTester(Context thisctx, FragmentActivity fa){

        bleAdvr= new BleAdvertiser(thisctx);

        bleMgr=(BluetoothManager)thisctx.getSystemService( Context.BLUETOOTH_SERVICE );
        //bleAdtr=bleMgr.getAdapter();
        bleScr= new BleScanner(bleMgr,thisctx, fa);

        this.PhoneSizeFromBleScanned=0;
        this.PhoneIDSizeFromServer=0;
        this.PhoneSizeCheckedBOTH=0;
    }




    private phoneInfo[] VirtualPhones(){
        phoneInfo[] pi= new phoneInfo[10];
        for(int i=0; i<10; i++){
            pi[i]=new phoneInfo("ph"+(i+1),false,false,0.7 );
        }
        return pi;
    }


    private void checkPhonesNearBeacon(String[] idFromServer, phoneInfo[] phonesBLE){
        if(idFromServer.length==this.PhoneIDSizeFromServer) {

            for (int i = 0; i < this.PhoneIDSizeFromServer; i++) {
                System.out.println("hjhj sv in check func"+idFromServer[i]);
                boolean findSameID=false;
                for(int j=0; j<this.PhoneSizeFromBleScanned; j++){
                    if(idFromServer[i].equals(phonesBLE[j].getID())){   //중복아이디 고려 안함
                        findSameID=true;
                        phonesBLE[j].setNearByBeacon(true);
                        j=this.PhoneSizeFromBleScanned;
                    }
                }
                if(findSameID==true) {
                    this.PhoneSizeCheckedBOTH++;
                }else{
                    System.out.println("hjhj error: can't find SAME PHONE ID from ble scanned LIST");
                }
            }
        }else{
           System.out.println("hjhj error , size not correct");
        }
    }

    //private Button secondButtonOnMainActivity;










    /**
    private BluetoothAdapter myAdapter(){
        return this.bleAdtr;
    }
    private BluetoothManager myManager(){
        return this.bleMgr;
    }
     ***/
    public void setMyID(String id){
        this.myPhoneID=id;
    }
    public String getMyID(){
        return this.myPhoneID;
    }
    private BleAdvertiser myAdvertiser(){
        return this.bleAdvr;
    }
    private BleScanner myScanner(){
        return this.bleScr;
    }
    public void setCompdDist(double radius){
        this.comparedDistance=radius;

    }
    private double getCompdDist(){
        return this.comparedDistance;
    }

    private boolean ifNear(double DistanceByBLE/** 미터로 환산한 거리값**/){
        if(DistanceByBLE>=0&&DistanceByBLE<= getCompdDist()){
            return true;
        }else{
            return false;
        }
    }
    private Double RSSItoDist(int[] rssi, int[] txP, int signalsize){
       // D = 10 ^ ( (TXpower-RSSI) / (10*n) )
        double DIST= 0.0;
        int n=signalsize;
        String printedrssi="";
        String printedtxp="";
        //System.out.println("hjhj re"+rssi);
        for(int m=0; m<n; m++){
            printedrssi+=" "+rssi[m];
            printedtxp+=" "+txP[m];

        }
        //System.out.println("hjhj re"+txP);

        double meanTX=0;
        double meanRS=0;

            for(int p=0; p<n; p++){
                meanTX+=txP[p];
                meanRS+=rssi[p];
                DIST+=10^((txP[p]-rssi[p])/(10*2));

            }
            meanTX=meanTX/n;
            meanRS=meanRS/n;
            double val1=meanTX-meanRS;
            double val2=val1/20;
            DIST=Math.pow(10.0,val2);


        Log.d("hjypyp re","DIST"+DIST);
           // DIST=((int)(DIST*100))/100;





        Log.d("hjypyp re","rssis"+printedrssi);
        Log.d("hjypyp re","txps"+printedtxp);
        return DIST;
    }



}
