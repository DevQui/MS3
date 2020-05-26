package com.example.ms3codingchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    private List<InfoData> invalidData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        readInfoData();
    }

    private void readInfoData() {
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        StringBuilder badData = new StringBuilder();
        StringBuilder counter = new StringBuilder();
        Integer received = 0, success = 0, failed = 0;

        badData.append("A,B,C,D,E,F,G,H,I,J");
        try{
            //step over  headers
            reader.readLine();
            while((line = reader.readLine()) != null){
                Log.d("MyActivity", "Line is : " +line);

                //split by commas
                String[] tokens = line.split(",");

                //verify data
                Boolean valid = verifyData(tokens);

                //read the data
                InfoData info = new InfoData();
                Integer i;
                if(valid == true){
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

                    //add data here to the db
                    AddData(info);

                    ++success;
                }else{
                    //store to bad
                    badData.append("\n" + line);
                    ++failed;
                }
                ++received;
            }
            createCSVFile(badData);
            createLogger(received, success, failed);
        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
    }

    private void createLogger(Integer received, Integer success, Integer failed) {

        Context context = getApplicationContext();
        File filedir = new File(context.getFilesDir(),"ms3.log");
        if(!filedir.exists()){
            filedir.mkdir();
        }

        try{
            File file = new File(filedir, "ms3.log");
            FileWriter writer = new FileWriter(file);
            writer.append("Number of received data is: " + received + "\n");
            writer.append("Number of success data is: " + success + "\n");
            writer.append("Number of failed data is: " + failed);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createCSVFile(StringBuilder tokens) {
        try {
            //saving
            FileOutputStream out = openFileOutput("data-bad.csv", Context.MODE_PRIVATE);
            out.write((tokens.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(),"data-bad.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.ms3codingchallenge.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Bad Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //to access the file attached
            fileIntent.putExtra(Intent.EXTRA_STREAM, path); //knows where to get the file attached
            startActivity(Intent.createChooser(fileIntent, "Send Mail"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Boolean verifyData(String[] dataLine) {
        InfoData invalid = new InfoData();
        Integer i, x;
        for (i = 0; i < 10; i++){
            if(dataLine.length >= 10) {
                if (dataLine[i].length() <= 0) {
                    x = 0;
                    invalid.setColA(dataLine[x]); //0
                    invalid.setColB(dataLine[++x]); //1
                    invalid.setColC(dataLine[++x]); //2
                    invalid.setColD(dataLine[++x]); //3
                    invalid.setColE(dataLine[++x]); //4
                    invalid.setColF(dataLine[++x]); //5
                    invalid.setColG(dataLine[++x]); //6
                    invalid.setColH(dataLine[++x].toUpperCase()); //7
                    invalid.setColH(dataLine[++x].toUpperCase()); //8
                    invalid.setColJ(dataLine[++x]); //9

                    //store invalid data to invalidData
                    invalidData.add(invalid);
                    return false;
                }
            }
        }
        return true;
    }

    private void AddData(InfoData info) {
        //getting the values
        boolean insertData = myDb.addData(info.getColA(), info.getColB(), info.getColC(),
                info.getColD(), info.getColE(), info.getColF(), info.getColG(), info.getColH(),
                info.getColI(), info.getColJ());

        Context context = getApplicationContext();
        if(insertData == true){
            Toast.makeText(context, "Successfully Entered Data!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error! Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
