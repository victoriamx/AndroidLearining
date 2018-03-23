package com.example.lmx.lab3;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemDetail extends AppCompatActivity {
    private ImageView back;
    private ImageView star;
    private  ImageView cart;
    private ImageView photo;
    private TextView name;
    private  TextView price;
    private  TextView type;
    private TextView detail;
    private ListView morelist;
    private  TextView moredetail;
    private IntentFilter intentFilter,intentFilter2;
    private Receiver receiver;
    private MyWidget myWidget;
    private static final  String DYNAMICACTION="com.example.lmx.lab3.MyDynamicFilter";//动态广播字符串
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        findview();
        ResgisterBroadcast();
        //获取包中的信息
        final Goods goods= (Goods) getIntent().getExtras().get("goods");
        //进行相应设置
        name.setText(goods.getName());
        price.setText(goods.getPrice());
        type.setText(goods.getType());
        detail.setText(goods.getInfomation());
        photo.setImageResource(goods.getImageId());

        //更多列表的实现
        String[] info =new String[]{"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,info);
        morelist.setAdapter(arrayAdapter);

        //点击返回，结束
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击星星图标
        star.setTag(0);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Integer) v.getTag()==0)
                {
                    v.setBackgroundResource(R.mipmap.full_star);
                    v.setTag(1);
                }
                else
                {
                    v.setBackgroundResource(R.mipmap.empty_star);
                    v.setTag(0);
                }
            }
        });

        //点击购物车
        cart.setClickable(true);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(1,new Intent(ItemDetail.this,ItemList.class).putExtra("goods",goods));
                EventBus.getDefault().post(goods);
                Toast.makeText(getApplicationContext(),"商品已添加到购物车",Toast.LENGTH_SHORT).show();

                //lab4 发送动态广播部分
                Bundle bundle=new Bundle();
                bundle.putSerializable("goods",goods);
                Intent intentbroadcast=new Intent(DYNAMICACTION);
                intentbroadcast.putExtras(bundle);
                sendBroadcast(intentbroadcast);
            }
        });

        //点击进入京东
        moredetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Intent.ACTION_VIEW);//系统内部指定动作
                intent2.setData(Uri.parse("https://www.jd.com/"));
                startActivity(intent2);
            }
        });

    }
    //动态广播注册
    private void ResgisterBroadcast() {
        intentFilter=new IntentFilter();
        receiver=new Receiver();
        intentFilter.addAction(DYNAMICACTION);
        registerReceiver(receiver,intentFilter);
        //lab5 widget
        intentFilter2=new IntentFilter();
        myWidget=new MyWidget();
        intentFilter2.addAction(DYNAMICACTION);
        registerReceiver(myWidget,intentFilter2);
    }
    //取消注册
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(myWidget);
    }

    private void findview() {
        back=(ImageView)findViewById(R.id.back);
        star=(ImageView)findViewById(R.id.star);
        cart=(ImageView)findViewById(R.id.cart);
        photo=(ImageView)findViewById(R.id.photo);
        name=(TextView)findViewById(R.id.name);
        price=(TextView)findViewById(R.id.price);
        type=(TextView)findViewById(R.id.type);
        detail=(TextView)findViewById(R.id.detail);
        morelist=(ListView)findViewById(R.id.morelist);
        moredetail=(TextView)findViewById(R.id.moredetail);
    }
}
