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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class ItemList extends AppCompatActivity{
    private ListView shoppinglist;
    private SimpleAdapter simpleAdapter;
    private RecyclerView mRecyclerView;
    private CommonAdapter<Map<String,Object>> commonAdapter;//修改
    private FloatingActionButton fab;
    private List<Map<String, Object>> cartlists;
    private List<Goods> shoppings = new ArrayList<>();;
    private List<Map<String,Object>> onsalelists;
    private List<Goods> data = new ArrayList<>();;
    private static final String STACTICACTION="com.example.lmx.lab3.MyStaticFilter";//静态广播字符串
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        findView();
        //数据
        data.add(new Goods("Enchated Forest", "¥ 5.00", "作者", "Johanna Basford", R.drawable.enchatedforest));
        data.add(new Goods("Arla Milk", "¥ 59.00", "产地", "德国", R.drawable.arla));
        data.add(new Goods("Devondale Milk", "¥ 79.00", "产地", "澳大利亚", R.drawable.devondale));
        data.add(new Goods("Kindle Oasis", "¥ 2399.00", "版本", "8GB", R.drawable.kindle));
        data.add(new Goods("waitrose 早餐麦片", "¥179.00", "重量", "2Kg", R.drawable.waitrose));
        data.add(new Goods("Mcvitie's 饼干", "¥14.90 ", "产地", "英国", R.drawable.mcvitie));
        data.add(new Goods("Ferrero Rocher", "¥ 132.59", "重量", "300g", R.drawable.ferrero));
        data.add(new Goods("Maltesers", "¥ 141.43", "重量", "118g", R.drawable.maltesers));
        data.add(new Goods("Lindt", "¥ 139.43", "重量", "249g", R.drawable.lindt));
        data.add(new Goods("Borggreve", "¥ 28.90", "重量", "640g", R.drawable.borggreve));

        //物品列表
        onsalelists=new ArrayList<>();
        for (Goods c : data){
            Map<String,Object> temp=new LinkedHashMap<>();
            temp.put("firstletter",c.getFirstLetter());
            temp.put("name",c.getName());
            onsalelists.add(temp);
        }
        //购物车列表
        cartlists = new ArrayList<>();
        Goods c = new Goods("购物车", "价格  ", null,null,0);
        Map<String, Object> temp2 = new LinkedHashMap<>();
        temp2.put("firstletter", "*");
        temp2.put("name", c.getName());
        temp2.put("price", c.getPrice());
        shoppings.add(c);
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
                bundle.putSerializable("goods", data.get(position));
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
                bundle.putSerializable("goods", shoppings.get(position));
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

        //lab4___静态广播
        Random random=new Random();
        int rposition=random.nextInt(10);//返回位置的随机数 0-9
        Bundle bundle2=new Bundle();
        bundle2.putSerializable("goods",data.get(rposition));
        Intent intentBrocast =new Intent(STACTICACTION);
        intentBrocast.putExtras(bundle2);
        sendBroadcast(intentBrocast);//发送静态广播（文字和内容也附带）

        //lab4 evenbus
        EventBus.getDefault().register(this);
    }

    //从新的intent切换到活动时
    @Override
    protected  void onNewIntent(Intent intent){
        mRecyclerView.setVisibility(View.GONE);
        shoppinglist.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.mainpage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//在主线程完成
    public void onMessageEvent(Goods c){
        Map<String, Object> listItem = new LinkedHashMap<>();
        assert c != null;
        listItem.put("firstletter", c.getFirstLetter());
        listItem.put("name", c.getName());
        listItem.put("price", c.getPrice());
        shoppings.add(c);
        cartlists.add(listItem);
        simpleAdapter.notifyDataSetChanged();
    }
/*
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                Goods c= (Goods) data.getExtras().get("goods");
                Map<String, Object> listItem = new LinkedHashMap<>();
                assert c != null;
                listItem.put("firstletter", c.getFirstLetter());
                listItem.put("name", c.getName());
                listItem.put("price", c.getPrice());
                shoppings.add(c);
                cartlists.add(listItem);
                simpleAdapter.notifyDataSetChanged();
            }
            //从商品列表切换过来不添加物品
        }
    }*/

    private void findView()
    {
        shoppinglist=(ListView)findViewById(R.id.ShoppingList);
        mRecyclerView=(RecyclerView)findViewById(R.id.itemlist);
        fab=(FloatingActionButton)findViewById(R.id.floating);
    }
}
