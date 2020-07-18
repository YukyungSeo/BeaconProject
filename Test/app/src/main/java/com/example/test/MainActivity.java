package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BleSource.BleTester;

//*** import for BLE
//*** import for BLE

//import for BLE ***//
//import for BLE ***//

public class MainActivity extends AppCompatActivity {
    TextView id_text;
    Button firstButton;
    Button secondButton;
    TextView BLEDataText;
    //추가로 변수가 필요할 경우 단톡방에 꼭 말해주세요.
    //push 할때도 꼭 단톡방에 말해주세요.


    //**************************************
    //*** BLE advertsing and scanner SOURCE
    //*** BLE advertsing and scanner SOURCE
    BleTester bleTester;
    //BLE advertsing and scanner SOURCE ***//
    //BLE advertsing and scanner SOURCE ***//
    //**************************************




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstButton = (Button)findViewById(R.id.firstButton);
        secondButton = (Button)findViewById(R.id.secondButton);
        BLEDataText = (TextView) findViewById(R.id.BLEDataText);


        //**************************************
        //*** BLE advertsing and scanner SOURCE
        //*** BLE advertsing and scanner SOURCE
        bleTester=new BleTester(this.getApplicationContext(), this);
        //BLE advertsing and scanner SOURCE ***//
        //BLE advertsing and scanner SOURCE ***//
        //**************************************



        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //1단계 부분입니다.
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //2단계 부분입니다.
                //BLEDataText에 같은 영역의 BLE를 출력하면 됩니다.



                //**************************************
                //*** BLE advertsing and scanner SOURCE
                //*** BLE advertsing and scanner SOURCE

                //advertising, scanning, setting datas :
                bleTester.BleTestMainFunc();

                //get needed datas from bleTester :


                //BLE advertsing and scanner SOURCE ***//
                //BLE advertsing and scanner SOURCE ***//
                //**************************************





                
            }
        });


    }
}
