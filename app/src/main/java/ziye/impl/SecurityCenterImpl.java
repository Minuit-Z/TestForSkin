package ziye.impl;

import android.os.RemoteException;

import ziye.skintest.ISecurityCenter;

/**
 * Created by Administrator on 2018/12/4 0004.
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub {
    private static final char SECRET_CODE = 'w';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
