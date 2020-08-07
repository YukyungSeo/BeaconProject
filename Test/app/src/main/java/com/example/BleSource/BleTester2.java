package com.example.BleSource;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import java.util.Iterator;
import java.util.Set;

public class BleTester2 {


    private BleAdvertiser bleAdvr;
    private BleScannnerForPHP bleScr;
    private BluetoothManager bleMgr;
    String TAG="LOG --- ";

    private String myPhoneID;  //내 폰 ID
    private TextView mainView;

    public BleTester2(Context thisctx, FragmentActivity fa){

        bleAdvr= new BleAdvertiser(thisctx);
        bleMgr=(BluetoothManager)thisctx.getSystemService( Context.BLUETOOTH_SERVICE );
        bleScr= new BleScannnerForPHP(bleMgr,thisctx, fa);
    }

    public void BleStart(String myID,TextView datatxt) {  //메인 기능 함수


        setMyID(myID);
        setTextView(datatxt);
        showScanning(datatxt);
        System.out.println("ble test id:"+myID);
       // myAdvertiser().advertising1(getMyID());
        //myScanner().startScan(getMyID());

        Handler timer = new Handler(); //Handler 생성
        timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
            public void run() {
                myScanner().stopScan();
                myAdvertiser().stopAdvertising();
                showResult(getTextView());

            }
        }, 10000); //60000 == 1분 18 6 36 = 36 24 = 60
    }

    public void showResult(TextView tv){    //MainActivity 에 BLE TEST 결과 출력

        Set<String> re = myScanner().getScanned().keySet();
        Iterator<String> it = re.iterator();
        String txt="끝: ";
        while(it.hasNext()){
            String thiss=it.next();
            BleScannnerForPHP.INFO info = myScanner().getScanned().get(thiss);
            txt+=thiss+" "+ info.sigSize+" and ";
        }
        tv.setText(txt);
    }

    private void showScanning(TextView tv){
        tv.setText("SCANNING, ADVERTISING");
    }
    public void setMyID(String id){this.myPhoneID=id; }
    public String getMyID(){
        return this.myPhoneID;
    }
    public BleAdvertiser myAdvertiser(){
        return this.bleAdvr;
    }
    public BleScannnerForPHP myScanner(){
        return this.bleScr;
    }
    public void setTextView(TextView tv){
        this.mainView=tv;
    }
    public TextView getTextView(){
        return this.mainView;
    }

}
