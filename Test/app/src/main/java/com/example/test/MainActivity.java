package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
//*** import for BLE
//*** import for BLE
import com.example.BleSource.BleTester;
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
    EditText idedittext;
    String id;
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
        idedittext= (EditText)findViewById(R.id.idInputText);

        //BLE advertsing and scanner SOURCE ***//
        //BLE advertsing and scanner SOURCE ***//
        //**************************************


        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //1단계 부분입니다.
                Intent intent = new Intent(MainActivity.this, BeaconActivity.class) ;
                startActivity(intent) ;
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
                id=idedittext.getText().toString();
                bleTester.BleTestFunc(id);

                //get needed datas from bleTester :

                //BLE advertsing and scanner SOURCE ***//
                //BLE advertsing and scanner SOURCE ***//
                //**************************************


            }
        });


    }
}
