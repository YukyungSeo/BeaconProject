package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    //TextView BLEDataText;
    //추가로 변수가 필요할 경우 단톡방에 꼭 말해주세요.
    //push 할때도 꼭 단톡방에 말해주세요.

    //추가 변수들 :
    BleTester bleTester;
    String id;
    String[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstButton = (Button)findViewById(R.id.firstButton);
        secondButton = (Button)findViewById(R.id.secondButton);
        TextView BLEDataText = (TextView) findViewById(R.id.BLEDataText);
        //BLEDataText.
        BLEDataText.setText("왜안나와1");

        // BleTester 객체 생성 :
        bleTester=new BleTester(this.getApplicationContext(), this);

        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //1단계 부분입니다.
                Intent intent = new Intent(MainActivity.this, BeaconActivity.class) ;
                startActivityForResult(intent, 0);
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                TextView BLEDataText2 = (TextView) findViewById(R.id.BLEDataText);
                //2단계 부분입니다.
                //BLEDataText에 같은 영역의 BLE를 출력하면 됩니다.
                id=id_text.getText().toString();

                //server에서  자기 포함 값 가져오기 :
                PHPConnect connect1 = new PHPConnect();
                String URL = "http://168.188.129.191/test_find_location.php?id="+id_text.getText().toString();
                String result = null;
                String[] otherPhones;
                try {
                    result = connect1.execute(URL).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(result == null){ // web error로 오류가 생겼을 경우
                    toast_cant_find_location();
                }else{  // 정상의 경우
                    if(result.length()==2) {
                        String x = result.split("")[0];
                        String y = result.split("")[1];
                        URL = "http://168.188.129.191/test_find_others.php?x="+x+"&y="+y;
                    }else{
                        toast_error();
                        return;
                    }
                    PHPConnect connect2 = new PHPConnect();
                    String others = null;
                    try {
                        others = connect2.execute(URL).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(others==null){
                        toast_cant_find_location();
                    }else{
                        String temp="";
                        for(int l=0; l<others.length(); l++){
                            temp+=String.valueOf(others.charAt(l));
                        }
                        otherPhones=temp.split("<br>");
                        bleTester.setIDFromServer(otherPhones);
                    }
                }

                //Ble Test 시작 :
                bleTester.BleTestFunc(id,BLEDataText2);

                //Ble Test 결과 return :
                /*
                results=bleTester.getResult();
                String DataText = "";

                for(int i = 0; i<results.length;i++){
                    DataText += (i+1)+"번째 결과 값"+results[i]+"\n";
                    System.out.println("hjhj result2 "+ results[i]);
                }
                BLEDataText2.setText("LOL BLE");
                 */
                //BLEDataText2.setText(DataText);

            }
        });


    }

    private void toast_error() {
        Toast.makeText(this,"x,y 좌표로 출력이 안됨", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView BLEDataText3 = (TextView) findViewById(R.id.BLEDataText);
        switch(requestCode){
            case 0: {
                PHPConnect connect = new PHPConnect();
                int[] regionXY = (int[]) data.getSerializableExtra("regionXY");

                String x = "";
                String y = "";
                if(regionXY != null) {
                    x += regionXY[0];   // x값 삽입 해주세요.
                    y += regionXY[1];   // y값 삽입 해주세요.
                    BLEDataText3.setText("regionXY: (" + x + ", " + y + "), point2D: (" + regionXY[2] + ", " + regionXY[3] + ")");
                }

                String URL = "http://168.188.129.191/test_save_location.php?id=" + id_text.getText().toString() + "&x=" + x + "&y=" + y;
                connect.execute(URL);
                break;
            }
            case 1:
                break;
            default:
                break;
        }
    }

    public void toast_cant_find_location(){
        Toast.makeText(this,"error", Toast.LENGTH_SHORT).show();
    }
}
