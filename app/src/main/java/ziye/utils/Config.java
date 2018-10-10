package ziye.utils;

/**
 * Created by Administrator on 2018/10/9 0009.
 */

public interface Config {

    String SP_SKIN_FILE = "skinName";
    String SP_SKIN_VALUE = "skinValue";


    Integer STATUS_NO_CHANGE = 0x00; //无需换肤
    Integer STATUS_CHANGE_SUCCESS = 0x01; //换肤成功
    Integer STATUS_FILE_NOT_EXISTS = 0x02; //皮肤文件不存在
    Integer STATUS_FILE_EXCEPTION = 0x03; //皮肤文件异常


}
