package com.example.tabletest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {
    ImageView imgGif00;
    ImageView imgGif01;
    ImageView imgGif02;
    ImageView imgGif10;
    ImageView imgGif11;
    ImageView imgGif12;
    ImageView imgGif20;
    ImageView imgGif21;
    ImageView imgGif22;
    EditText editText;
    Button selectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        selectButton = (Button)findViewById(R.id.button);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(editText.getText().toString());
            }
        });

        imgGif00 = (ImageView)findViewById(R.id.user00);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif00);

        imgGif01 = (ImageView)findViewById(R.id.user01);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif01);

        imgGif02 = (ImageView)findViewById(R.id.user02);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif02);

        imgGif10 = (ImageView)findViewById(R.id.user10);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif10);

        imgGif11 = (ImageView)findViewById(R.id.user11);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif11);

        imgGif12 = (ImageView)findViewById(R.id.user12);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif12);

        imgGif20 = (ImageView)findViewById(R.id.user20);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif20);

        imgGif21 = (ImageView)findViewById(R.id.user21);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif21);

        imgGif22 = (ImageView)findViewById(R.id.user22);

        Glide.with(this)
                .asGif()
                .load(R.raw.user)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imgGif22);

        setINVISIBLE();
    }

    private void select(String string){
        setINVISIBLE();
        switch (string){
            case "00":
                imgGif00.setVisibility(View.VISIBLE);
                break;
            case "01":
                imgGif01.setVisibility(View.VISIBLE);
                break;
            case "02":
                imgGif02.setVisibility(View.VISIBLE);
                break;
            case "10":
                imgGif10.setVisibility(View.VISIBLE);
                break;
            case "11":
                imgGif11.setVisibility(View.VISIBLE);
                break;
            case "12":
                imgGif12.setVisibility(View.VISIBLE);
                break;
            case "20":
                imgGif20.setVisibility(View.VISIBLE);
                break;
            case "21":
                imgGif21.setVisibility(View.VISIBLE);
                break;
            case "22":
                imgGif22.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setINVISIBLE(){
        imgGif00.setVisibility(View.INVISIBLE);
        imgGif01.setVisibility(View.INVISIBLE);
        imgGif02.setVisibility(View.INVISIBLE);
        imgGif10.setVisibility(View.INVISIBLE);
        imgGif11.setVisibility(View.INVISIBLE);
        imgGif12.setVisibility(View.INVISIBLE);
        imgGif20.setVisibility(View.INVISIBLE);
        imgGif21.setVisibility(View.INVISIBLE);
        imgGif22.setVisibility(View.INVISIBLE);
    }
}