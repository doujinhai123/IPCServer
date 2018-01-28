package com.innovator.ipcserver.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.innovator.ipcserver.MyConstants;

/**
 *
 * 使用 Messenger 方式的服务端
 * Created by innovator on 2018/1/24.
 */

public class MessengerService extends Service{


    private static final String TAG = "TAG";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG,"通过 Messenger 收到客户端发来的消息："+msg.getData().getString("msg"));

                    //接收 客户端 传过来的 Messenger，用来回复 客户端
                    Messenger clientMessenger = msg.replyTo;
                    Message message = Message.obtain(null,MyConstants.MSG_REPLY_TO_CLIENT);
                    Bundle data = new Bundle();
                    data.putString("msg","你好，我是服务器，很高兴能和你对话");
                    message.setData(data);

                    try{
                        clientMessenger.send(message);
                    }catch (RemoteException e){
                        Log.i(TAG,"服务端回应客户端异常："+e.getMessage());
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    //构造服务器的 Messenger
    private final Messenger messenger = new Messenger(new MessengerHandler());


    //连接客户端的时候返回服务器的Binder对象给客户端
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
