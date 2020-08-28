package com.example.bletest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    BluetoothManager bmgr;
    BleScanner scr;
    BleAdvertiser advr;
    Button firstButton;
    Button benchButton;
    Button nonbenchButton;
    Button stopButton;
    TextView tv;
    String MYID;
    boolean benchmark;
    Button idsetButton;
    EditText idinputText;
    EditText timeEditText;
    Button timeButton;
    EditText distText;
    Button distButton;
    Thread adThread;
    Thread scThread;
    int Mtime;
    String sTime;
    String eTime;
    String distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set My ID//
        //Set My ID//
        MYID="hj";
        //Set My ID//
        //Set My ID//
        bmgr=(BluetoothManager)this.getApplicationContext().getSystemService( Context.BLUETOOTH_SERVICE );
        scr= new BleScanner(bmgr,this.getApplicationContext(), this);
        advr = new BleAdvertiser(this.getApplicationContext());
        benchmark=false;
        firstButton = (Button)findViewById(R.id.firstButton);
        benchButton=(Button)findViewById(R.id.benchButton);
        nonbenchButton=(Button)findViewById(R.id.nonbenchButton);
        stopButton=(Button)findViewById(R.id.stopButton);
        idinputText=(EditText)findViewById(R.id.idInputText);
        idsetButton=(Button)findViewById(R.id.idsetButton);
        timeEditText = (EditText)findViewById(R.id.time_edit_text);
        timeButton = (Button)findViewById(R.id.time_button);
        distText = (EditText)findViewById(R.id.distance_edit_text);
        distButton = (Button)findViewById(R.id.distance_button);
        sTime = "";
        eTime = "";

        tv=(TextView)findViewById(R.id.textview1);

        idinputText.setText(MYID);
        tv.setText("기준아님//ID-"+MYID);
        idsetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MYID=idinputText.getText().toString();
                tv.setText("ID-"+MYID);
            }
        });

        benchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                benchmark=true;
                tv.setText("기준임//ID-"+MYID);
            }
        });
        nonbenchButton.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View v) {
                                                  benchmark=false;
                                                  tv.setText("기준아님//ID-"+MYID);
                                              }
                                          }
        );

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = Integer.parseInt(timeEditText.getText().toString());
                Mtime = time*1000;
            }
        });

        distButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance = distText.getText().toString();
            }
        });


        firstButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                firstButton.setEnabled(false);
                adThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        advr.advertising1(MYID,benchmark);
                    }
                });

                scThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long now = System.currentTimeMillis();
                        Date mDate = new Date(now);
                        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
                        sTime = simpleDate.format(mDate);
                        scr.startScan(MYID,benchmark);
                    }
                });

                if(adThread.isAlive()){
                    Toast.makeText(getApplicationContext(),"AD : 여기 오류인가요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(scThread.isAlive()){
                    Toast.makeText(getApplicationContext(),"SC : 여기 오류인가요",Toast.LENGTH_SHORT).show();
                    return;
                }
                adThread.start();
                scThread.start();
//                advr.advertising1(MYID,benchmark);
//                scr.startScan(MYID,benchmark);
                tv.setText(tv.getText().toString()+" !START!");
                Handler timer = new Handler(); //Handler 생성
                timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
                    public void run() {
                        adThread.interrupt();
                        scThread.interrupt();
                        adThread = null;
                        scThread = null;
                        advr.stopAdvertising();
                        scr.stopScan();
                        long now = System.currentTimeMillis();
                        Date mDate = new Date(now);
                        SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:ss");
                        eTime = simpleDate.format(mDate);
                        if(benchmark){
                            PHPConnect connect = new PHPConnect();
                            String URL = "http://168.188.129.191/send_ble_benchmark_data.php?bm_id="+MYID+"&sTime="+sTime+"&eTime="+eTime+"&dist="+distance;
                            connect.execute(URL);
                        }
                        scr.setResutText(tv);
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone ringtone=
                                RingtoneManager.getRingtone(getApplicationContext(),notification);
                        ringtone.play();
                        firstButton.setEnabled(true);
                        sTime = null;
                        eTime = null;
                    }
                }, Mtime); //60000 == 1분 //660000 == 11분 //240000 == 4분
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                adThread.interrupt();
                scThread.interrupt();
                advr.stopAdvertising();
                scr.stopScan();
                scr.setResutText(tv);
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone=
                        RingtoneManager.getRingtone(getApplicationContext(),notification);
                ringtone.play();
                firstButton.setEnabled(true);
            }
        });

    }
}