package com.example.lmx.lab3;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewHolder extends RecyclerView.ViewHolder {
    //使用一个SparseArrayS数组存储list_item的子VIEW
    private SparseArray<View> mViews;//存储list_item的子view
    private  View mConvertView;//存储list_item

    //构造函数
    public ViewHolder(Context context,View item_View,ViewGroup parent)
    {
        super(item_View);
        mConvertView=item_View;
        mViews=new SparseArray<View>();
    }

    //获取viewholder实例
    public static ViewHolder get(Context context,ViewGroup parent,int layoutId)
    {
        // LayoutInflater.from(context)获取LayoutInflater的实例
        //layoutInflater.inflate(resourceId, root);加载布局
        View item_View= LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder holder =new ViewHolder(context,item_View,parent);
        return holder;
    }
    //viewholder未将子view缓存到sparsearray数组时，需要用findviewbyid创建view对象，
    // 如果已经缓存，则直接返回
    public <T extends View> T getView(int viewid)
    {
        View view=mViews.get(viewid);
        if(view==null)
        {
            //创建view
            view=mConvertView.findViewById(viewid);
            //将view存入mview
            mViews.put(viewid,view);
        }
        return (T) view;
    }
}
