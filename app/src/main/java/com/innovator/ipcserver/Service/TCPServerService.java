package com.innovator.ipcserver.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * Socket 服务端
 * Created by innovator on 2018/2/5.
 */

public class TCPServerService extends Service {

    private boolean mIsServiceDestoyed = false;
    private String[] mDefinedMessages = new String[] {
            "你好啊，🙃\n",
            "请问你叫什么名字呀？你成功引起了我的注意\n",
            "今天真冷，什么时候才能回暖啊\n",
            "你知道吗？我可是可以和多个人同时聊天的哦\n",
            "给你讲个笑话吧，据说爱笑的人运气都不会太差，不知道是不是真的\n"
    };

    @Override
    public void onCreate() {
        Log.i("TCP","正在启动服务端的Service");
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable{

        @SuppressWarnings("resource")
        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                //监听 8688 端口
                serverSocket = new ServerSocket(8687);
            }catch (IOException i){
                Log.i("TCP","establish tcp server failed,port 8688，"+i.getMessage());
                i.printStackTrace();
                return;
            }

            while (!mIsServiceDestoyed){
                try{
                  //接收客户端请求
                    final Socket client  = serverSocket.accept();
                    Log.i("TCP","accept");

                    //新建一个线程，因此可以和多个客户端连接
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Log.i("TCP","11111111111111111111");
                               responseClient(client);
                            }catch (IOException o){
                                o.printStackTrace();
                            }
                        }
                    }.start();
                }catch (IOException e){
                    e.printStackTrace();

                }
            }
        }
    }


    /**
     * 服务端回应客户端
     * @param client
     * @throws IOException
     */
    private void responseClient(Socket client) throws IOException{
        //接收客户端的消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())),true);

        out.println("Weclome to the TCP Server\n");

        Log.i("TCP","222222222222222222");
        while (!mIsServiceDestoyed){
            Log.i("TCP","33333333333333");
            String str = in.readLine();
            Log.i("TCP","444444444444444");
            if (str == null){
                //客户端断开连接
                break;
            }

            Log.i("TCP","正在读取客户端发送的消息: "+str);
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            //回复客户端，进行聊天
            out.println(msg);
            Log.i("TCP","发送: "+str+"给客户端");

        }

        //客户端断开连接后需要关闭流
        Log.i("TCP","客户端断开连接");

        try {
            if (null != out) {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (null != in) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
