package com.example.lmx.lab8;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Additem extends AppCompatActivity {
    private MyDataBaseHelper myDataBaseHelper;
    private static final String DB_NAME="Contacts.db";
    private static final int  DB_VERSION=1;
    private EditText add_name;
    private EditText add_birthday;
    private EditText add_gift;
    private Button add_item2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        init();
        myDataBaseHelper=new MyDataBaseHelper(this,DB_NAME,null,DB_VERSION);
       add_item2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name_in=add_name.getText().toString();
               String birthday_in=add_birthday.getText().toString();
               String gift_in=add_gift.getText().toString();
               if(name_in.isEmpty())
                   Toast.makeText(Additem.this,"名字为空，请完善！",Toast.LENGTH_SHORT).show();
               else{
                   SQLiteDatabase db=myDataBaseHelper.getWritableDatabase();//数据库如果已经存在就不会再创建一次
                   Cursor check_cursor=db.rawQuery("SELECT * FROM Contacts WHERE name=?",new String[]{name_in});
                   if(check_cursor.getCount()>0)    //The number of rows in the cursor
                   Toast.makeText(Additem.this,"名字重复啦！请检查",Toast.LENGTH_SHORT).show();
                   else {
                       db.execSQL("INSERT INTO Contacts (name,birthday,gift) values(?,?,?)", new String[]{name_in, birthday_in, gift_in});
                       check_cursor.close();
                       Intent intent=new Intent(Additem.this,MainActivity.class);
                       startActivity(intent);
                   }
               }
           }
       });
    }

    private void init() {
        add_name=(EditText)findViewById(R.id.add_name);
        add_birthday=(EditText)findViewById(R.id.add_birthday);
        add_gift=(EditText)findViewById(R.id.add_gift);
        add_item2=(Button)findViewById(R.id.additem_2);
    }
}
