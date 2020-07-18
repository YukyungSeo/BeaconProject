package com.example.BleSource;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class BleTester {
    /**

     // 2 - get filtered List - 스캔 과정에서 직접 필터링하는게 나음
                                그 후, phoneID와 거리 값을 산출하여 filteredListByID에

     // 2.5 - get phone info in same area from Server

     // 3 - stack ble datas (need time) -

     // 4 - compute mean value of data

     // 5 - get distance from each phone and make Distance List

     // 6 - compare BLE data and Server data

     // 7 - get  RESULT

     **/

    private BleAdvertiser bleAdvr;
    private BleScanner bleScr;
    private BluetoothManager bleMgr;
    private BluetoothAdapter bleAdtr;
    private int myPhoneNumber;

    private double comparedDistance=0.6; // ble신호로 산출한 거리와 비교할 거리 범위의 반지름 m

    private phoneInfo[] Phones; //
    private int phoneSize;

    private class phoneInfo{

        String phoneID;     //패킷식별문자
        String phoneIDNumber;  //패킷식별문자에서 기계를 구분하는 숫자만 잘라낸것

        boolean nearByBeacon = false; // 비콘정보로 '나'와 같은 범위내이면 true, 그렇지 않으면 false
        boolean nearByBle = false;  // nearByBeacon이 true인 phone들 중, ble신호와 비교하여 가까이
                                    //있는걸로 최종 확인되면 true, 그렇지 않으면 false
        double[] rssi;              //scancallback에서 받아서 저장할 값
        double[] txP;               // 위와같은내용

        double phoneDist;           //rssi와 txp의 평균값으로 계산한 거리값 하나.



    }

    //private Button secondButtonOnMainActivity;
    public void BleTestMainFunc(){

        // 0 -start BLE advertising
        myAdvertiser().advertising1(010777);// param : 핸드폰 식별할 정수값(고유번호)직접 입력

        // 1 - start  BLE scanning
        myScanner().startScan();

        // 2 - get filtered List

        // 2.5 - get phone info in same area from Server

        // 3 - stack ble datas (need time)

        // 4 - compute mean value of data

        // 5 - get distance from each phone and make Distance List

        // 6 - compare BLE data and Server data

        // 7 - get  RESULT
    }

    public BleTester(Context thisctx, FragmentActivity fa){

        bleAdvr= new BleAdvertiser(thisctx);
        bleScr= new BleScanner(thisctx, fa);
        bleMgr=(BluetoothManager)thisctx.getSystemService( Context.BLUETOOTH_SERVICE );
        bleAdtr=bleMgr.getAdapter();

        //secondButtonOnMainActivity=sb;
        this.phoneSize=0;
        Phones = new phoneInfo[30];


    }

    private BluetoothAdapter myAdapter(){
        return this.bleAdtr;
    }
    private BluetoothManager myManager(){
        return this.bleMgr;
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
    private Double RSSItoDist(double rssi, double txP){


        return 0.5;
    }



}
