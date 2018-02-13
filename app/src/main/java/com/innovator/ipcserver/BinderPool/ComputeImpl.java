package com.innovator.ipcserver.BinderPool;

import android.os.RemoteException;

import com.innovator.ipcclient.ICompute;

/**
 * ICompute AIDL 的实现类
 * Created by innovator on 2018/2/11.
 */

public class ComputeImpl extends ICompute.Stub{

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
