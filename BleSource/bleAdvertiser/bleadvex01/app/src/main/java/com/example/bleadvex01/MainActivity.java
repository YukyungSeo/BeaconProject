package com.example.bleadvex01;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button b1,b2;
    advertiser ba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ba= new advertiser(this.getApplicationContext());
        Log.d("click button 12 :","log test");

        Log.d("click button 13 :","log test");
        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);

        Log.d("click button 14 :","log test");


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.d("click button 15 :","log test");
                ba.adv1();
                Log.d("click button 16:","log test");



            }


        });

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.d("click button 17 :","log test");
                ba.adv2();
                Log.d("click button 18:","log test");



            }


        });
    }
}
