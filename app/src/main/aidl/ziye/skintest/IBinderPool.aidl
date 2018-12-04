// IBinderPool.aidl
package ziye.skintest;

// binder连接池  池接口

interface IBinderPool {
    IBinder queryBinder(int code);
}
