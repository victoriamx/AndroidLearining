package com.example.lmx.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private MyDataBaseHelper myDataBaseHelper;
    private static final String DB_NAME="Contacts.db";
    private static final int  DB_VERSION=1;
    private Button additem_main;
    private ListView list;
    private TextView dialog_name;
    private EditText dialog_birthday;
    private  EditText dialog_gift;
    private  TextView dialog_phone;
    private List<Map<String,Object>> content_list;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vertifyStoragePermission();//检查权限
    }

    //权限检查
    public  void vertifyStoragePermission() {
        try{
            //检查是否有读取权限，如果没有就申请（PERMISSION_GRANTED = 0，PERMISSION_DENIED = -1）
            if(ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.READ_CONTACTS")!= PackageManager.PERMISSION_GRANTED){
                //没有权限，申请，弹出对话框
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.READ_CONTACTS",},1);
            }
            else doing();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        if(requestCode==1 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) doing();
        else System.exit(0);//用户拒绝权限
    }

    //主体内容
    private void doing() {
        init();

        //增加条目按钮的跳转
        additem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Additem.class);
                startActivity(intent);
            }
        });

        myDataBaseHelper=new MyDataBaseHelper(this,DB_NAME,null,DB_VERSION);//创建数据库
        final SQLiteDatabase db=myDataBaseHelper.getWritableDatabase();//打开数据库
        Cursor cursor=db.rawQuery("SELECT * FROM Contacts",null);//获得数据库中Contacts表中的内容，cursor-所返回的数据集的指针
        //列表内容的初始化
        if(cursor.moveToFirst())//将数据指针移到第一行
        {
            int name_ColumnIndex=cursor.getColumnIndex("name");
            int birthday_ColumnIndex=cursor.getColumnIndex("birthday");
            int gift_ColumnIndex=cursor.getColumnIndex("gift");
            do{//遍历Cursor对象，取出数据加到列表中
                String name=cursor.getString(name_ColumnIndex);
                String birthday=cursor.getString(birthday_ColumnIndex);
                String gift=cursor.getString(gift_ColumnIndex);
                Map<String,Object> item=new LinkedHashMap<>();
                item.put("name",name);
                item.put("birthday",birthday);
                item.put("gift",gift);
                content_list.add(item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        simpleAdapter=new SimpleAdapter(
                this,
                content_list,
                R.layout.item,
                new String[] {"name","birthday","gift"},
                new int[]{R.id.name,R.id.birthday,R.id.gift}
        );
        list.setAdapter(simpleAdapter);

        //长按item
        final AlertDialog.Builder alertDialog1=new AlertDialog.Builder(MainActivity.this);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position==0)return true;
                alertDialog1.setMessage("是否删除？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //从数据库中移除
                                db.execSQL("DELETE FROM Contacts WHERE name=?",new String[]{content_list.get(position).get("name").toString()});
                                //从列表内容中移除
                                content_list.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                return true;
                //如果返回false那么click仍然会被调用。而且是先调用Long click，然后调用click。
                // 如果返回true,click就不会再被调用了
            }
        });

        //点击item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position==0)return;
                //载入弹出框的布局
                //View dialog_view=getLayoutInflater().inflate(R.layout.dialoglayout, (ViewGroup) findViewById(R.id.dialog));
                View dialog_view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialoglayout,null);
                dialog_name=(TextView)dialog_view.findViewById(R.id.dialog_name);
                dialog_birthday=(EditText)dialog_view.findViewById(R.id.dialog_birthday);
                dialog_gift=(EditText)dialog_view.findViewById(R.id.dialog_gift);
                dialog_phone=(TextView)dialog_view.findViewById(R.id.dialog_phone);

                final Map<String,Object> item=content_list.get(position);//得到对应的item记录的信息
                dialog_name.setText(item.get("name").toString());
                dialog_birthday.setText(item.get("birthday").toString());
                dialog_gift.setText(item.get("gift").toString());
                
                Cursor dialog_cursor=null;
                String phonenumber="无";
                try{
                    //查询联系人信息
                    dialog_cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                    int id_ColumnIndex=dialog_cursor.getColumnIndex(ContactsContract.Contacts._ID);//id对应的列下标
                    int  name_ColumnIndex=dialog_cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);//name对应的列下标
                    int phonenumbercount_ColumnIndex=dialog_cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    if(dialog_cursor!=null&&dialog_cursor.moveToFirst()){
                        do{
                            String contact_id=dialog_cursor.getString(id_ColumnIndex);
                            String name=dialog_cursor.getString(name_ColumnIndex);
                            if(name.equals(dialog_name.getText())){
                               int phonenumbercount=Integer.parseInt(dialog_cursor.getString(phonenumbercount_ColumnIndex));//看联系人是否有电话号码
                                if(phonenumbercount>0){
                                    Cursor phone_cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contact_id,null,null);
                                    phonenumber="";
                                    while (phone_cursor.moveToNext())
                                        phonenumber += phone_cursor.getString(phone_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";//取出电话号码
                                    phone_cursor.close();
                                    }
                                }
                        }while (dialog_cursor.moveToNext());
                    }
                   dialog_phone.setText(phonenumber);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(dialog_cursor!=null)
                        dialog_cursor.close();
                }

                final AlertDialog.Builder alertDialog2=new AlertDialog.Builder(MainActivity.this);
                alertDialog2.setTitle("Happy Birthday!")
                        .setView(dialog_view)
                        .setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //更新数据库内容
                               db.execSQL("UPDATE Contacts SET birthday=?,gift=? WHERE name=?",new String[]{dialog_birthday.getText().toString(),dialog_gift.getText().toString(),dialog_name.getText().toString()});
                                //更新主页的显示内容
                                content_list.remove(item);
                                Map<String,Object> new_item=new LinkedHashMap<>();
                                new_item.put("name",dialog_name.getText().toString());
                                new_item.put("birthday",dialog_birthday.getText().toString());
                                new_item.put("gift",dialog_gift.getText().toString());
                                content_list.add(new_item);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        }).create().show();
            }
        });
    }

    private void init() {
        additem_main=(Button) findViewById(R.id.additem_main);
        list=(ListView)findViewById(R.id.list);
        //列表的首行
        content_list=new ArrayList<>();
        Map<String,Object> item=new LinkedHashMap<>();
        item.put("name","姓名");
        item.put("birthday","生日");
        item.put("gift","礼物");
        content_list.add(item);
    }
}
