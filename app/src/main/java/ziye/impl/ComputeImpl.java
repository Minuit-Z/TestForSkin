package ziye.impl;

import android.os.RemoteException;
import ziye.skintest.IComputer;

public class ComputeImpl extends IComputer.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
