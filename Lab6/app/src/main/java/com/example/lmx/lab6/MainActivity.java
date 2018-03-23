package com.example.lmx.lab6;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MusicService.MyBinder mBinder;
    private ImageView cover;
    private TextView state,time1,time2;
    private SeekBar seekbar;
    private Button play,stop,quit;
    private ObjectAnimator animator;//图片旋转动画
    public static final int UPDATE_UI=1;
    private boolean hasPermission;//是否拥有权限
    public static final int PLAY=1,PAUSE=2,STOP=3;//三种状态
    private ServiceConnection sc=new ServiceConnection() {
        @Override
        //活动与服务成功绑定时调用
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("service","connected");
            mBinder=(MusicService.MyBinder)service;//向下转型得到MyBinder实例
        }
        @Override
        //活动与服务连接断开时调用
        public void onServiceDisconnected(ComponentName name) {
            sc=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hasPermission=false;
        vertifyStoragePermission();//检查权限
        init();//初始化
        final Handler mHandler= new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case UPDATE_UI:
                        //100ms后进行UI更新
                        SimpleDateFormat time=new SimpleDateFormat("mm:ss");
                        Parcel data=Parcel.obtain();
                        Parcel reply=Parcel.obtain();
                        try{
                            mBinder.transact(104,data,reply,0);
                        }catch (RemoteException e){
                            e.printStackTrace();
                        }
                        int current=reply.readInt();
                        int duration=reply.readInt();
                        seekbar.setProgress(current);
                        seekbar.setMax(duration);
                        time1.setText(time.format(current));
                        time2.setText(time.format(duration));
                        break;
                    default:
                        break;
                }
            }
        };
        //定义一个新的线程
        Thread mthread=new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(100);//睡眠100ms
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(sc!=null && hasPermission==true)
                        //定义Message对象，并将消息放到消息队列中去
                        mHandler.obtainMessage(UPDATE_UI).sendToTarget();
                }
            }
        };
        mthread.start();
    }

    //权限检查
    public  void vertifyStoragePermission() {
        try{
            //检查是否有读取权限，如果没有就申请（PERMISSION_GRANTED = 0，PERMISSION_DENIED = -1）
            if(ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE")!= PackageManager.PERMISSION_GRANTED){
               //没有权限，申请，弹出对话框
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},1);
            }
            else
                hasPermission=true;
                 connect();//绑定开启服务
             }catch (Exception e){
                e.printStackTrace();
                 }
    }

    //请求权限会弹出询问框，用户选择后，系统回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        if(requestCode==1 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            hasPermission=true;
            connect();//绑定开启服务
        }
        else
            System.exit(0);//用户拒绝权限后无法使用程序
    }

    private void connect() {
        Intent intent=new Intent(this,MusicService.class);
        startService(intent);//开启服务
        bindService(intent,sc, Context.BIND_AUTO_CREATE);//活动绑定服务
    }

    //初始化
    private void init() {
        cover=(ImageView)findViewById(R.id.cover);
        state=(TextView)findViewById(R.id.state);
        time1=(TextView)findViewById(R.id.time1);
        time2=(TextView)findViewById(R.id.time2);
        seekbar=(SeekBar)findViewById(R.id.progress);
        play=(Button) findViewById(R.id.play);
        stop=(Button)findViewById(R.id.stop);
        quit=(Button)findViewById(R.id.quit);
        //图片旋转
        animator=ObjectAnimator.ofFloat(cover,"rotation",0f,360.0f);
        animator.setInterpolator(new LinearInterpolator());//动画插值
        animator.setRepeatCount(-1);//设置动画重复次数
        animator.setDuration(50000);//动画时间
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复的模式
        //按钮的监听
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        quit.setOnClickListener(this);
        //seekbar设置监听器
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度发生变化
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //用户开始拖动seekbar
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户结束拖动seekbar
                Parcel data=Parcel.obtain();
                Parcel reply=Parcel.obtain();
                data.writeInt(seekBar.getProgress());//写入当前拖到的进度位置
                try{
                    mBinder.transact(105,data,reply,0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //点击事件处理
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v){
        Parcel data=Parcel.obtain();
        Parcel reply=Parcel.obtain();
        int cur_state;
        switch (v.getId()){
            //播放/暂停按钮
            case R.id.play:
                try{
                    mBinder.transact(101,data,reply,0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                cur_state=reply.readInt();
                if(cur_state==PLAY){
                    state.setText("Playing");
                    play.setText("PAUSED");
                    if (animator.isPaused() ) animator.resume();
                    else animator.start();
                }
                else if(cur_state==PAUSE){
                    state.setText("Paused");
                    play.setText("PLAY");
                    animator.pause();
                }
                break;
            //停止按钮
            case R.id.stop:
                try{
                    mBinder.transact(102,data,reply,0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                cur_state=reply.readInt();
                if(cur_state==STOP){
                    state.setText("Stop");
                    play.setText("PLAY");
                    animator.end();
                }
                break;
            //退出按钮
            case R.id.quit:
                try{
                    mBinder.transact(103,data,reply,0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                unbindService(sc);//服务
                sc=null;
                try{
                    MainActivity.this.finish();
                    System.exit(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Intent intent=new Intent(this,MusicService.class);
        stopService(intent);
        unbindService(sc);//活动被销毁时解绑服务
        sc = null;
        try {
            MainActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
         }
    }
