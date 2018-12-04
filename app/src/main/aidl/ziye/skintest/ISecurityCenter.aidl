// ISecurityCenter.aidl
package ziye.skintest;

// binder连接池 加解密
interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
