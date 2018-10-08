package ziye.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ziye.skin.attr.SkinAttr;
import ziye.skin.attr.SkinType;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinAttrSupport {

    public final String TAG = "SkinAttrSupport";

    /**
     * @author 张子扬
     * @time 2018/9/29 0029 17:22
     * @desc 获取skinAttr的属性
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {

        //background,src,textColor需要解析
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int length = attrs.getAttributeCount();
        for (int i = 0; i < length; i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);

            //Log.e(TAG, "attributeName:" + attributeName + "    attributeValue=" + attributeValue);

            //只抓取重要的属性
            SkinType type = getSkinType(attributeName);
            if (type != null) {
                //传入资源名称, 目前只有value , 为@int类型
                String resName = getResName(context, attributeValue);

                if (TextUtils.isEmpty(resName)) {
                    //非@开头的硬编码 不予处理
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName, type);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }


    /**
     * @author 张子扬
     * @time 2018/10/8 0008 11:26
     * @desc 获取资源的名称
     */
    private static String getResName(Context context, String attributeValue) {
        if (attributeValue.startsWith("@")) {
            //以@开头,可以进行处理
            attributeValue = attributeValue.substring(1);
            int resId = Integer.parseInt(attributeValue);

            //通过资源ID获取属性名
            return context.getResources().getResourceEntryName(resId);
        }

        return null;
    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 11:21
     * @desc 通过名称获取skinType
     */
    private static SkinType getSkinType(String attributeName) {
        SkinType[] types = SkinType.values();

        for (SkinType type : types) {
            if (type.getResName().equals(attributeName)) {

                //
                return type;
            }
        }
        return null;
    }

}
