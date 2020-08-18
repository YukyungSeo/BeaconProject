package com.example.bletest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        tv=(TextView)findViewById(R.id.textview1);



        idinputText.setText(MYID);
        tv.setText("ID-"+MYID);
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

        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                advr.advertising1(MYID,benchmark);
                scr.startScan(MYID,benchmark);
                Handler timer = new Handler(); //Handler 생성
                timer.postDelayed(new Runnable() { //2초후 쓰레드를 생성하는 postDelayed 메소드
                    public void run() {
                        advr.stopAdvertising();
                        scr.stopScan();
                        scr.setResutText(tv);

                    }
                }, 240000); //60000 == 1분 //660000 == 11분 //240000 == 4분

            }
        });
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                advr.stopAdvertising();
                scr.stopScan();
                scr.setResutText(tv);
            }
        });

    }
}