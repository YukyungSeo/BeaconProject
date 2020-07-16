package com.example.acub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {
    TextView id_text;
    TextView pswd_text;
    TextView email_text;
    Button sign_up_button;
    Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);

                id_text = (TextView) findViewById(R.id.id_text);
                pswd_text = (TextView) findViewById(R.id.pswd_text);
                email_text = (TextView) findViewById(R.id.email_text);

                PHPConnect connect = new PHPConnect();
                String URL = "http://168.188.129.191/sign_up.php?id="+id_text.getText().toString()+"&pswd="+pswd_text.getText().toString()+"&email="+email_text.getText().toString();
                connect.execute(URL);
                toast_sign_up_success();
            }
        });

        back_button = (Button)findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    private void toast_sign_up_success(){
        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "뒤로가기는 안돼요!", Toast.LENGTH_SHORT).show();
    }
}
