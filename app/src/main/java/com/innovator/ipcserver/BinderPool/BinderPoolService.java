package com.innovator.ipcserver.BinderPool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.innovator.ipcclient.IBinderPool;

import static com.innovator.ipcserver.MyConstants.BINDER_COMPUTE;
import static com.innovator.ipcserver.MyConstants.BINDER_SECURITY_CENTER;


/**
 * BinderPool 的服务端
 * Created by innovator on 2018/2/13.
 */

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPool";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"BinderPool Service onCreate");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"BinderPool Service onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回对外的统一 AIDL 接口，有点像工厂方法
        Log.i(TAG,"onBind");
        return new BinderPoolImpl();
    }


    public static class BinderPoolImpl extends IBinderPool.Stub{

        public BinderPoolImpl(){
            super();
        }


        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            //根据不同的请求 Code 返回不同的 Binder 对象
            switch (binderCode){
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }
}
