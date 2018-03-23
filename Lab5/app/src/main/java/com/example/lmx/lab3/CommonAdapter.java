package com.example.lmx.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
    protected Context mcontext;
    protected int mlayoutID;
    protected List<T> mDatas;
    private OnItemClickListener mOnItemClickListener=null;

    //构造函数
    public CommonAdapter(Context context,int layoutID, List<T>datas)
    {
        mcontext=context;
        mlayoutID=layoutID;
        mDatas=datas;
    }
    //创建item视图，返回相应的viewholder
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,int viewType)
    {
        ViewHolder viewHolder=ViewHolder.get(mcontext,parent,mlayoutID);
        return viewHolder;
    }
    //绑定数据到正确的item视图上
    @Override
    public void onBindViewHolder(final ViewHolder holder,int position)
    {
        //设置holder内view内容
        convert(holder,mDatas.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    //获取item的个数
    @Override
    public int getItemCount()
    {
        return mDatas.size();
    };

    public abstract void convert(ViewHolder holder, T t);

    public  void removeItem(int position)
    {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    //实现recyclerview的监听接口及函数
    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
    this.mOnItemClickListener=onItemClickListener;
    }
}



