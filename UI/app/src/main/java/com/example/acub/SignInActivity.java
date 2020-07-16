package com.example.acub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class SignInActivity extends AppCompatActivity {
    TextView id_text;
    TextView pswd_text;
    Button sign_in_button;
    Button sign_up_button;
    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        showPermissionDialog();

        sign_in_button = (Button)findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                id_text = (TextView) findViewById(R.id.id_text);
                pswd_text = (TextView) findViewById(R.id.pswd_text);
                PHPConnect connect = new PHPConnect();
                String URL = "http://168.188.129.191/sign_in.php?id="+id_text.getText().toString()+"&pswd="+pswd_text.getText().toString();
//                connect.execute(URL);
                String result = null;
                try {
                    result = connect.execute(URL).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(result.equals("success")){
                    toast_sign_in_result_success();
                    startActivityForResult(intent,1);
                }else if(result.equals("fail")){
                    toast_sign_in_result_failure();
                }else if(result.equals("")) {
                    toast_sign_in_result_nothing();
                }
            }
        });

        sign_up_button = (Button)findViewById(R.id.sign_up_button);

        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    //permission 추가하면 아래 코드 참고
    /*
    private void showPermissionDialog() {

        for(String permission : permission_list){
            int chk = ContextCompat.checkSelfPermission(this,permission);
            if(chk != PackageManager.PERMISSION_GRANTED){
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==0){
            for(int i=0; i<grantResults.length; i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(getApplicationContext(),"앱 권한을 설정하십시오.",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
     */

    public void toast_sign_in_result_nothing(){
        Toast.makeText(this,"아무것도 안들어옴", Toast.LENGTH_SHORT).show();
    }

    public void toast_sign_in_result_success(){
        Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show();
    }

    public void toast_sign_in_result_failure(){
        Toast.makeText(this,"로그인 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "뒤로가기는 안돼요!", Toast.LENGTH_SHORT).show();
    }
}
