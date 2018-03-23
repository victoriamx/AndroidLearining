package com.example.lmx.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {
    private static final String STACTICACTION="com.example.lmx.lab3.MyStaticFilter";//静态广播字符串
    private static final  String DYNAMICACTION="com.example.lmx.lab3.MyDynamicFilter";//动态广播字符串
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

       // CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
       // views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);

        //lab5
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        Intent i=new Intent(context,ItemList.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0, i,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.mywidget,pendingIntent);
        ComponentName me=new ComponentName(context,MyWidget.class);
        appWidgetManager.updateAppWidget(me, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent)
    {
        super.onReceive(context,intent);
        if(intent.getAction().equals(STACTICACTION))
        {
            Goods good=(Goods)intent.getExtras().get("goods");
            RemoteViews updateview=new RemoteViews(context.getPackageName(),R.layout.my_widget);
            updateview.setImageViewResource(R.id.appwidget_image,good.getImageId());
            updateview.setTextViewText(R.id.appwidget_text,good.getName()+"仅售"+good.getPrice()+"！");

            Intent i=new Intent(context,ItemDetail.class);
           i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtras(intent.getExtras());
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            updateview.setOnClickPendingIntent(R.id.mywidget,pendingIntent);

            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            ComponentName me=new ComponentName(context,MyWidget.class);
            appWidgetManager.updateAppWidget(me,updateview);
        }
        else if(intent.getAction().equals(DYNAMICACTION))
        {
            Goods good=(Goods)intent.getExtras().get("goods");
            RemoteViews updateview=new RemoteViews(context.getPackageName(),R.layout.my_widget);
            updateview.setTextViewText(R.id.appwidget_text,good.getName()+"已添加到购物车");
            updateview.setImageViewResource(R.id.appwidget_image,good.getImageId());

            Intent i=new Intent(context,ItemList.class);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtras(intent.getExtras());
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            updateview.setOnClickPendingIntent(R.id.mywidget,pendingIntent);

            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            ComponentName me=new ComponentName(context,MyWidget.class);
            appWidgetManager.updateAppWidget(me,updateview);
        }
    }

}

