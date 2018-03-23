package com.example.lmx.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

public class FileEdit extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private Button save;
    private Button load;
    private  Button clear_text;
    private  Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        init();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_in=title.getText().toString();
                String content_in=content.getText().toString();
                FileOutputStream out=null;
                BufferedWriter writer=null;
                if(title_in.isEmpty()) {//如果文件名为空
                    Toast.makeText(FileEdit.this, "Fail to save file", Toast.LENGTH_SHORT).show();
                }
                else {//文件名不为空
                    try {
                        out = openFileOutput(title_in, Context.MODE_PRIVATE);//获得一个FileOutputStream对象
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                        writer.write(content_in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (writer != null) {
                                writer.close();
                                Toast.makeText(FileEdit.this, "Save successfully.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(FileEdit.this, "Fail to save file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_in=title.getText().toString();
                FileInputStream in=null;
                BufferedReader reader=null;
                StringBuilder content_in=new StringBuilder();
                if(title_in.isEmpty()) {//如果文件名为空
                    Toast.makeText(FileEdit.this, "Fail to load file", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        in=openFileInput(title_in);
                        reader=new BufferedReader(new InputStreamReader(in));
                        String line="";
                        while((line=reader.readLine())!=null){
                            content_in.append(line);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(FileEdit.this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                    }finally {
                        if(reader!=null){
                            try{
                                reader.close();
                                Toast.makeText(FileEdit.this, "Load successfully.", Toast.LENGTH_SHORT).show();
                                content.setText(content_in.toString());
                            }catch (IOException e){
                                e.printStackTrace();
                                Toast.makeText(FileEdit.this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_in=title.getText().toString();
                if(!TextUtils.isEmpty(title_in)){
                    deleteFile(title_in);
                    Toast.makeText(FileEdit.this, "Delete successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        title=(EditText)findViewById(R.id.title);
        content=(EditText)findViewById(R.id.content);
        save=(Button)findViewById(R.id.save);
        load=(Button)findViewById(R.id.load);
        clear_text=(Button)findViewById(R.id.clear_text);
        delete=(Button)findViewById(R.id.delete);
    }
}
