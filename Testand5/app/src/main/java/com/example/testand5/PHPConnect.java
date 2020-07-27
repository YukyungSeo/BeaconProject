package com.example.testand5;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PHPConnect extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... params){
        String output = "";
        try{
            //setting connection url
            URL url = new URL(params[0]);

            //create connection Object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //if connect,
            if(conn != null){
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

                //if connetion code return,
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    int i = 0;
                    for(;;){
                        //store text on web by the line
                        String line = br.readLine();
                        if(line==null){
                            System.out.println("stop -> "+i);
                            break;
                        }
                        System.out.println("success ->"+line);
                        i++;
                        output += line;
                    }
                    br.close();
                }
                conn.disconnect();
            }else{
                System.out.println("failure");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return output;
    }
}
