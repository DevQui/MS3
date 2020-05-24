package com.example.ms3codingchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readInfoData();
    }

    private List<InfoData> infoData = new ArrayList<>();

    private void readInfoData() {
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try{
            //step over  headers
            reader.readLine();
            while((line = reader.readLine()) != null){
                Log.d("MyActivity", "Line is : " +line);

                //split by commas
                String[] tokens = line.split(",");


                //read the data
                InfoData info = new InfoData();
                Integer i;

                if(tokens.length >= 10){
                    i = 0;
                    while (i >= 10){
                        info.setColA(tokens[i]); //0
                        info.setColB(tokens[++i]); //1
                        info.setColC(tokens[++i]); //2
                        info.setColD(tokens[++i]); //3
                        info.setColE(tokens[++i]); //4
                        info.setColF(tokens[++i]); //5
                        info.setColG(tokens[++i]); //6
                        info.setColH(tokens[++i].toUpperCase()); //7
                        info.setColH(tokens[++i].toUpperCase()); //8
                        info.setColJ(tokens[++i]); //9
                        ++i; //10
                    }
                }
                infoData.add(info);

                Log.d("MyActivity", "Just created: " +info);

            }

        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
    }

}
