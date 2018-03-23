package com.example.lmx.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver{
    private static final String STACTICACTION="com.example.lmx.lab3.MyStaticFilter";//静态广播字符串
    private static final  String DYNAMICACTION="com.example.lmx.lab3.MyDynamicFilter";//动态广播字符串
    private int id=0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收到的是静态广播时
        if(intent.getAction().equals(STACTICACTION)){
            Goods c= (Goods) intent.getExtras().get("goods");
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),c.getImageId());
            NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);//通过getSystemService获得系统服务类实例
            Notification.Builder builder=new Notification.Builder(context);
            //对builder进行配置
            builder.setContentTitle("新商品热卖")
                    .setContentText(c.getName()+"仅售"+c.getPrice()+'!')
                    .setTicker("您有一条新消息")
                    .setSmallIcon(c.getImageId())
                    .setLargeIcon(bm)
                    .setAutoCancel(true);
            //绑定intent，点击时进入对应活动
            Intent mintent=new Intent(context,ItemDetail.class);//进入商品详情
            mintent.putExtras(intent.getExtras());
            PendingIntent pendingintent=PendingIntent.getActivities(context,0, new Intent[]{mintent},PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingintent);
            //绑定notification，发送通知请求
            Notification notification=builder.build();
            manager.notify(0,notification);
        }
        else if(intent.getAction().equals(DYNAMICACTION)){
            Goods c= (Goods) intent.getExtras().get("goods");
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),c.getImageId());
            NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);//通过getSystemService获得系统服务类实例
            Notification.Builder builder=new Notification.Builder(context);
            //对builder进行配置
            builder.setContentTitle("马上下单")
                    .setContentText(c.getName()+"已添加到购物车")
                    .setTicker("您有一条新消息")
                    .setSmallIcon(c.getImageId())
                    .setLargeIcon(bm)
                    .setAutoCancel(true);
            //绑定intent，点击时进入对应活动
            Intent mintent=new Intent(context,ItemList.class);//进入购物车
            mintent.putExtras(intent.getExtras());
            PendingIntent pendingintent=PendingIntent.getActivities(context,0, new Intent[]{mintent},0);
            builder.setContentIntent(pendingintent);
            //绑定notification，发送通知请求
            Notification notification=builder.build();
            id=id+1;
            manager.notify(id+c.getImageId(),notification);
        }
    }
}
