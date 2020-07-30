package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.BleSource.BleTester;

import java.util.concurrent.ExecutionException;

//*** import for BLE
//*** import for BLE
//import for BLE ***//
//import for BLE ***//
public class MainActivity extends AppCompatActivity {
    TextView id_text;
    Button startButton;
    TextView BLEDataText;
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

        startButton = (Button)findViewById(R.id.start_button);
        BLEDataText = (TextView) findViewById(R.id.BLEDataText);

        // BleTester 객체 생성 :
        bleTester=new BleTester(this.getApplicationContext(), this);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id_text= (TextView) findViewById(R.id.idInputText);  //입력받은 id 값
                //1단계 부분입니다.
                Intent intent = new Intent(MainActivity.this, BeaconActivity.class) ;
                startActivityForResult(intent, 0);

                //2단계 부분입니다.
                //BLEDataText에 같은 영역의 BLE를 출력하면 됩니다.
                id=id_text.getText().toString();

                boolean BLETESTMODE=false;

                if(BLETESTMODE==true){  //서버에서 값 안가져오고 테스트 할때
                    bleTester.setVirtualID();
                    System.out.println("BLE TEST MODE");

                }else {
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
                    if (result == null) { // web error로 오류가 생겼을 경우
                        toast_cant_find_location();
                    } else {  // 정상의 경우
                        if (result.length() == 2) {
                            String x = result.split("")[0];
                            String y = result.split("")[1];
                            URL = "http://168.188.129.191/test_find_others.php?x=" + x + "&y=" + y;
                        } else {
                            toast_error();

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

                        if (others == null) {
                            toast_cant_find_location();
                        } else {
                            String temp = "";
                            for (int l = 0; l < others.length(); l++) {
                                temp += String.valueOf(others.charAt(l));
                            }
                            otherPhones = temp.split("<br>");
                            bleTester.setIDFromServer(otherPhones);
                        }

                    }
                }

                //Ble Test 시작 :
                //안드로이드 9와 10 에서 실행 확인한 코드입니다.
//                bleTester.BleTestFunc(id,BLEDataText2);
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
                    BLEDataText.setText("regionXY: (" + x + ", " + y + "), point2D: (" + regionXY[2] + ", " + regionXY[3] + ")");
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
