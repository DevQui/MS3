package com.example.ms3codingchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnView = (Button) findViewById(R.id.btnView);
        myDb = new DatabaseHelper(this);

        readInfoData();

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewListContents.class);
                startActivity(intent);
            }
        });

    }

    private List<InfoData> infoData = new ArrayList<>();
    private List<InfoData> invalidData = new ArrayList<>();

    private void readInfoData() {
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        StringBuilder badData = new StringBuilder();
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
                    infoData.add(info);

                    Log.d("MyActivity", "Just created: " +info);

                    //add data here to the db
                    AddData(info);
                }else{
                    //store to -bad
                    badData.append("\n" + line);
                }
            }
            //create csv file
            createBadCSV(badData);

        }catch (IOException e){
            Log.wtf("MyActivity", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
    }

    private void createBadCSV(StringBuilder tokens) {
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
            startActivity(Intent.createChooser(fileIntent, "Send mail"));

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
        boolean insertData = myDb.addData(info.getColA(), info.getColB(), info.getColC(),
                info.getColD(), info.getColE(), info.getColF(), info.getColG(), info.getColH(),
                info.getColI(), info.getColJ());

        if(insertData == true){
            Toast.makeText(MainActivity.this, "Successfully Entered Data!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, "Error! Something Went Wrong", Toast.LENGTH_LONG).show();
        }
    }

}
