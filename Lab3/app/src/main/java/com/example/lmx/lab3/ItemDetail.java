package com.example.lmx.lab3;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        findview();
        //获取包
        final Intent intent=getIntent();
        //获取包中的信息
        final String names=intent.getStringExtra("name");
        final String prices=intent.getStringExtra("price");
        final String types=intent.getStringExtra("type");
        final String details=intent.getStringExtra("detail");
        final String photos=intent.getStringExtra("imageid");
        //进行相应设置
        name.setText(names);
        price.setText(prices);
        type.setText(types);
        detail.setText(details);
        photo.setImageResource(Integer.parseInt(photos));

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
                Intent intent2=new Intent();
                intent2.putExtra("firstletter",names.substring(0,1));
                intent2.putExtra("name",names);
                intent2.putExtra("price",prices);
                intent2.setClass(ItemDetail.this,ItemList.class);
                setResult(1,intent2);
                Toast.makeText(getApplicationContext(),"商品已添加到购物车",Toast.LENGTH_SHORT).show();
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
