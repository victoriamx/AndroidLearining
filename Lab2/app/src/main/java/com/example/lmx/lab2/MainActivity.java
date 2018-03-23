package com.example.lmx.lab2;

import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ImageView sysuimg;
    private TextInputLayout idtext;
    private TextInputLayout passwordtext;
    private AutoCompleteTextView idedit;
    private EditText passwordedit;
    private RadioGroup choice;
    private RadioButton student;
    private RadioButton teacher;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//引入布局
        init();
        //点击图片出现对话框
        final AlertDialog.Builder  alertDialog=new AlertDialog.Builder(this);//创建一个AlertDialog
        final String [] items =new String []{"拍摄","从相册选择"};
        alertDialog.setTitle("上传头像").setNegativeButton("取消",
                new DialogInterface.OnClickListener(){                  //为取消按钮设置监听器
                    @Override
                    public void onClick (DialogInterface dialogInterface,int i){                //重写onclick方法
                        Toast.makeText(MainActivity.this, "您选择了取消", Toast.LENGTH_SHORT).show();//toast：上下文，显示内容，toast显示的时间
                    }
                }).setItems(items,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Toast.makeText(MainActivity.this, "您选择了"+items[i], Toast.LENGTH_SHORT).show();
                     }
        }).create();
        sysuimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        //单选框
        choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//为单选框设置监听器
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==student.getId()){
                    Snackbar.make(choice,"您选择了学生",Snackbar.LENGTH_SHORT)
                            .setAction("确认",new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(MainActivity.this,"Snackbar的确认按钮被点击了",Toast.LENGTH_SHORT).show();//点击Snackbar的确认按钮后弹出的toast
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else if(checkedId==teacher.getId()){
                    Snackbar.make(choice,"您选择了教职工",Snackbar.LENGTH_SHORT)//view控件，显示文字内容，显示时间长度
                            .setAction("确认",new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(MainActivity.this,"Snackbar的确认按钮被点击了",Toast.LENGTH_SHORT).show();//点击Snackbar的确认按钮后弹出的toast
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))//“确认”的颜色
                            .setDuration(5000)//持续时间，以ms为单位
                            .show();
                }
            }
        });
        //AutoCompleteTextView实现
        String [] ids={"123456","123333","654321","999999","66666"};//内容
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,ids);
        idedit.setAdapter(adapter);
        //通过textinputlayout获取edittext的内容
        //idedit=idtext.getEditText();
        passwordedit=passwordtext.getEditText();
        //登录按钮
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=idedit.getText().toString();//getText的返回值是一个Charsequence的接口,而toString()是Java.Lang.Object的一个方法
                String password=passwordedit.getText().toString();
                idtext.setErrorEnabled(false);
                passwordtext.setErrorEnabled(false);
                if(id.isEmpty()){
                    idtext.setErrorEnabled(true);
                    idtext.setError("学号不能为空");
                }
                else if(password.isEmpty()){
                    passwordtext.setErrorEnabled(true);
                    passwordtext.setError("密码不能为空");
                }
                else if(id.equals("123456")&&password.equals("6666")){
                    //Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT);
                    Snackbar.make(login,"登录成功",Snackbar.LENGTH_SHORT)
                            .setAction("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {}
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else{
                    Snackbar.make(login,"学号或密码错误",Snackbar.LENGTH_SHORT)
                            .setAction("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {}
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });
        //注册按钮
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student.isChecked()){
                    Snackbar.make(register,"学生注册功能尚未启用",Snackbar.LENGTH_SHORT)
                            .setAction("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {}
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else if(teacher.isChecked()){
                    Toast.makeText(MainActivity.this,"教职工注册功能尚未启用",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //引入与事件相关的控件
    private void init() {
        sysuimg=(ImageView) findViewById(R.id.imagesysu);
        idtext=(TextInputLayout) findViewById(R.id.studentidlayout);
        passwordtext=(TextInputLayout) findViewById(R.id.passwordlayout);
        idedit=(AutoCompleteTextView) findViewById(R.id.studentid_input);
        passwordedit=(EditText) findViewById(R.id.passwordinput);
        choice=(RadioGroup)findViewById(R.id.groups);
        student=(RadioButton)findViewById(R.id.studentbutton);
        teacher=(RadioButton)findViewById(R.id.teacherbutton);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
    }
}
//getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
// Activity.this的context 返回当前activity的上下文，属于activity ，activity 摧毁他就摧毁
// getBaseContext() 返回由构造函数指定或setBaseContext()设置的上下文