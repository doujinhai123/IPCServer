package com.innovator.ipcserver.BinderPool;

import android.os.RemoteException;

import com.innovator.ipcclient.ISecurityCenter;

/**
 * 实现 ISecurityCenter AIDL 接口的业务类
 * Created by innovator on 2018/2/11.
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub{

    public static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for(int i=0;i<chars.length;i++){
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decript(String password) throws RemoteException {
        return encrypt(password);
    }
}
