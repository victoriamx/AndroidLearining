package com.example.lmx.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText new_password;
    private EditText confirm_password;
    private Button ok;
    private Button clear_password;
    private SharedPreferences pref;
    private Boolean isRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        pref=getSharedPreferences("info",MODE_PRIVATE);//获得SharedPreferences对象
        isRemember=pref.getBoolean("isRemember",false);//判断是否之前已经记住密码，没有找到对应值时用默认值false
        if(isRemember==true){
            new_password.setVisibility(View.GONE);//新密码框不可见
            confirm_password.setHint("Password");//更改确认密码框提示
        }
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isRemember==false){//初始密码界面
                    String new_password_in=new_password.getText().toString();
                    String confirm_password_in=confirm_password.getText().toString();
                    if(new_password_in.isEmpty() || confirm_password_in.isEmpty()){
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(new_password_in.equals(confirm_password_in)==false){
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SharedPreferences.Editor editor=pref.edit();//获取SharedPreferences.Editor对象
                        editor.putString("password",new_password_in);//保存密码
                        editor.putBoolean("isRemember",true);//保存是否已经输入过新密码的选择
                        editor.apply();//提交
                        Intent intent=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{//非初始密码界面
                    String password_in=confirm_password.getText().toString();
                    String password=pref.getString("password","");
                    if(password_in.isEmpty()){
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(password_in.equals(password)==false){
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        clear_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new_password.setText("");
                confirm_password.setText("");
            }
        });
    }

    private void init() {
        new_password=(EditText)findViewById(R.id.new_password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        ok=(Button)findViewById(R.id.ok);
        clear_password=(Button)findViewById(R.id.clear_password);
    }
}
