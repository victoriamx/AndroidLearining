package com.example.lmx.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;

public class MusicService extends Service {
    public MyBinder mBinder=new MyBinder();
    public static MediaPlayer mediaPlayer=new MediaPlayer();
    private int state;//播放器所处的状态
    public static final int PLAY=1,PAUSE=2,STOP=3;//三种状态
    public File file=new File(Environment.getExternalStorageDirectory(),"/Music/melt.mp3");
    public MusicService() {
            try{
                mediaPlayer.setDataSource(file.getPath());//指定音频的路径
                mediaPlayer.prepare();//让播放器进入准备状态
                mediaPlayer.setLooping(true);//循环播放
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    //播放和暂停
    public void play_And_pause(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();//开始播放
            state=PLAY;
        }
        else{
            mediaPlayer.pause();//暂停播放
            state=PAUSE;
        }
    }
    //停止
    public void stop(){
        if(mediaPlayer!=null){
            mediaPlayer.reset();//停止播放
            state=STOP;
            try{
                mediaPlayer.setDataSource(file.getPath());//指定音频的路径
                mediaPlayer.prepare();//进入准备状态
                //mediaPlayer.seekTo(0);//从头开始播放
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //停止服务
    public  void quit(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class MyBinder extends Binder {
        @Override
        protected  boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        //code:远程操作的命令；data和reply参数相当于普通java方法里的调用参数和返回值。Parcel类型是可以跨进程的数据
                throws RemoteException{
            switch (code){
                case 101://播放
                    play_And_pause();
                    reply.writeInt(state);//返回状态
                    break;
                case 102://停止
                    stop();
                    reply.writeInt(state);//返回状态
                    break;
                case 103://退出
                    quit();
                    break;
                case 104://刷新，返回数据
                    reply.writeInt(mediaPlayer.getCurrentPosition());//回传当前播放时长
                    reply.writeInt(mediaPlayer.getDuration());//回传总时长
                    reply.writeInt(state);//返回播放器的状态
                    break;
                case 105://拖动进度条，处理
                    mediaPlayer.seekTo(data.readInt());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
