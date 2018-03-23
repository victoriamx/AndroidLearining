package com.example.lmx.lab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class ItemList extends AppCompatActivity{
    private ListView shoppinglist;
    private SimpleAdapter simpleAdapter;
    private RecyclerView mRecyclerView;
    private CommonAdapter<Map<String,Object>> commonAdapter;//修改
    private FloatingActionButton fab;
    private List<Map<String, Object>> cartlists;
    private List<Map<String,Object>> onsalelists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        findView();

        final String[] firstletter=new String[]{"E","A","D","K","w","M","F","M","L","B"};
        final String[] itemname =new String[]{"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};

        //intent包内容
        final Map<String,Object> name=new LinkedHashMap<>();
        name.put("Enchated Forest","Enchated Forest");
        name.put("Arla Milk","Arla Milk");
        name.put("Devondale Milk","Devondale Milk");
        name.put("Kindle Oasis","Kindle Oasis");
        name.put("waitrose 早餐麦片","waitrose 早餐麦片");
        name.put("Mcvitie's 饼干","Mcvitie's 饼干");
        name.put("Ferrero Rocher","Ferrero Rocher");
        name.put("Maltesers","Maltesers");
        name.put("Lindt","Lindt");
        name.put("Borggreve","Borggreve");

        final Map<String,Object> price=new LinkedHashMap<>();
        price.put("Enchated Forest","¥ 5.00");
        price.put("Arla Milk","¥ 59.00");
        price.put("Devondale Milk","¥ 79.00");
        price.put("Kindle Oasis","¥ 2399.00");
        price.put("waitrose 早餐麦片","¥ 179.00");
        price.put("Mcvitie's 饼干","¥ 14.90");
        price.put("Ferrero Rocher","¥ 132.59");
        price.put("Maltesers","¥ 141.43");
        price.put("Lindt","¥ 139.43");
        price.put("Borggreve","¥ 28.90");

        final Map<String,Object> type=new LinkedHashMap<>();
        type.put("Enchated Forest","作者");
        type.put("Arla Milk","产地");
        type.put("Devondale Milk","产地");
        type.put("Kindle Oasis","版本");
        type.put("waitrose 早餐麦片","重量");
        type.put("Mcvitie's 饼干","产地");
        type.put("Ferrero Rocher","重量");
        type.put("Maltesers","重量");
        type.put("Lindt","重量");
        type.put("Borggreve","重量");

        final Map<String,Object> detail=new LinkedHashMap<>();
        detail.put("Enchated Forest","Johanna Basford");
        detail.put("Arla Milk","德国");
        detail.put("Devondale Milk","澳大利亚");
        detail.put("Kindle Oasis","8GB");
        detail.put("waitrose 早餐麦片","2Kg");
        detail.put("Mcvitie's 饼干","英国");
        detail.put("Ferrero Rocher","300g");
        detail.put("Maltesers","118g");
        detail.put("Lindt","249g");
        detail.put("Borggreve","重640g");

        final Map<String,Object> imageid=new LinkedHashMap<>();
        imageid.put("Enchated Forest",R.drawable.enchatedforest);
        imageid.put("Arla Milk",R.drawable.arla);
        imageid.put("Devondale Milk",R.drawable.devondale);
        imageid.put("Kindle Oasis",R.drawable.kindle);
        imageid.put("waitrose 早餐麦片",R.drawable.waitrose);
        imageid.put("Mcvitie's 饼干",R.drawable.mcvitie);
        imageid.put("Ferrero Rocher",R.drawable.ferrero);
        imageid.put("Maltesers",R.drawable.maltesers);
        imageid.put("Lindt",R.drawable.lindt);
        imageid.put("Borggreve",R.drawable.borggreve);

        //数据
        //物品列表
        onsalelists=new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String,Object> temp=new LinkedHashMap<>();
            temp.put("firstletter",firstletter[i]);
            temp.put("name",itemname[i]);
            onsalelists.add(temp);
        }
        //购物车列表
        cartlists = new ArrayList<>();
        Map<String, Object> temp2 = new LinkedHashMap<>();
        temp2.put("firstletter", "*");
        temp2.put("name", "购物车");
        temp2.put("price", "价格");
        cartlists.add(temp2);

        /*--------listview-----------*/
        //建立simpleadapter,填充数据
        simpleAdapter=new SimpleAdapter(
                this,
                cartlists,//集合对象
                R.layout.item_in_cart,//列表项布局
                new String[]{"firstletter","name","price"},//提取key值生成列表项
                new int[]{R.id.FirstLetter_in_cart,R.id.ItemName_in_cart,R.id.ItemPrice_in_cart});//填充组件
        shoppinglist.setAdapter(simpleAdapter);

        /*------recyclerview-------*/
        //选择线性显示方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //新建一个adapter
        commonAdapter=new CommonAdapter<Map<String,Object>>(this,R.layout.item_on_sale,onsalelists) {
            @Override
            //通过ViewHolder修改视图内容
            public void convert(ViewHolder holder, Map<String, Object> stringObjectMap) {
                TextView first=holder.getView(R.id.FirstLetter_on_sale);
                first.setText(stringObjectMap.get("firstletter").toString());
                TextView name=holder.getView(R.id.ItemName_on_sale);
                name.setText(stringObjectMap.get("name").toString());
            }
        };
        //为适配器设置监听
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(ItemList.this,ItemDetail.class);
                //将数据都放到bundle包中
                Bundle bundle=new Bundle();
                String onclickname=onsalelists.get(position).get("name").toString();
                bundle.putString("name",name.get(onclickname).toString());
                bundle.putString("price",price.get(onclickname).toString());
                bundle.putString("type",type.get(onclickname).toString());
                bundle.putString("detail",detail.get(onclickname).toString());
                bundle.putString("imageid",imageid.get(onclickname).toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
            @Override
            public void onLongClick(int position) {
                commonAdapter.removeItem(position);
                Toast.makeText(getApplicationContext(),"移除第"+position+"个商品",Toast.LENGTH_SHORT).show();
            }
        });
        //添加动画
        //mRecyclerView.setAdapter(commonAdapter);
        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        //添加默认分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        /*---------浮标------------*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView.getVisibility()==View.VISIBLE)
                {
                    mRecyclerView.setVisibility(View.GONE);
                    shoppinglist.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.mipmap.mainpage);//切换浮标图片
                }
                else
                {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    shoppinglist.setVisibility(View.GONE);
                    fab.setImageResource(R.mipmap.shoplist);//切换浮标图片
                }
            }
        });

        /*购物车监听器*/
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        shoppinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)return;//点击的是第一个——标题
                //跳转至详情
                Intent intent=new Intent(ItemList.this,ItemDetail.class);
                Bundle bundle=new Bundle();
                String onclickname=cartlists.get(position).get("name").toString();
                bundle.putString("name",name.get(onclickname).toString());
                bundle.putString("price",price.get(onclickname).toString());
                bundle.putString("type",type.get(onclickname).toString());
                bundle.putString("detail",detail.get(onclickname).toString());
                bundle.putString("imageid",imageid.get(onclickname).toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        shoppinglist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position==0)return true;//点击的是第一个——标题
                alertDialog.setTitle("移除商品")
                        .setMessage("从购物车移除"+cartlists.get(position).get("name").toString())
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cartlists.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                String rfirstletter=data.getStringExtra("firstletter");
                String rname=data.getStringExtra("name");
                String rprice=data.getStringExtra("price");
                Map<String,Object> item=new LinkedHashMap<>();
                item.put("firstletter",rfirstletter);
                item.put("name",rname);
                item.put("price",rprice);
                cartlists.add(item);
                simpleAdapter.notifyDataSetChanged();
            }
            //从商品列表切换过来不添加物品
        }
    }

    private void findView()
    {
        shoppinglist=(ListView)findViewById(R.id.ShoppingList);
        mRecyclerView=(RecyclerView)findViewById(R.id.itemlist);
        fab=(FloatingActionButton)findViewById(R.id.floating);
    }

}
