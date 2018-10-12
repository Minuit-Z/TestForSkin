package ziye.skintest;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatViewInflater;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ziye.callback.ISkinChangeInterface;
import ziye.skin.SkinAttrSupport;
import ziye.skin.SkinManager;
import ziye.skin.SkinResource;
import ziye.skin.SkinViewInflater;
import ziye.skin.attr.SkinAttr;
import ziye.skin.attr.SkinView;
import ziye.utils.SpHelper;
import ziye.widget.DownloadProgressView;

public class MainActivity extends AppCompatActivity implements LayoutInflaterFactory, ISkinChangeInterface {

    TextView btnChange, btnRecover, btnNext;
    ImageView iv;
    private SkinViewInflater mAppCompatViewInflater;
    private Context mContext;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    private DownloadProgressView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);
        SkinManager.getInstance(this).init();
        LayoutInflaterCompat.setFactory(inflater, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        btnChange = findViewById(R.id.btn);
        btnRecover = findViewById(R.id.btn_recover);
        btnNext = findViewById(R.id.btn_next);
        iv = findViewById(R.id.iv);

        progress = findViewById(R.id.download_progress_bar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i=0;;) {
                        i++;
                        Thread.sleep(100);
                        progress.setProgress(i % 100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
        progress.setProgress(70);

//        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    //点击 读取本地的.skin文件中的资源
//                    Resources superResource = getResources();
//                    //AssetsManager创建实例
//                    AssetManager manager = AssetManager.class.newInstance();
//                    //添加资源包
//                    Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
//
//                    String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.apk";
//                    //反射执行方法
//                    method.invoke(manager, skinPath);
//                    Resources resources = new Resources(manager, superResource.getDisplayMetrics(), superResource.getConfiguration());
//                    int drawableID2 = resources.getIdentifier("ic_test", "drawable", "ziye.skinplugin");
//                    Drawable drawable = resources.getDrawable(drawableID2);
//
//                    iv.setImageDrawable(drawable);
//
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        // 使用框架换肤
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "red.zip";

                //换肤
                int result = SkinManager.getInstance(mContext).loadSkin(path);
            }
        });

        //恢复默认
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = SkinManager.getInstance(mContext).loadDefault();

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        btnNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                return true;
            }
        });
    }


    //拦截View的创建
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        //1. 创建View
        View v = createView(parent, name, context, attrs);
        Log.e("onCreateView: ", v + "");

        //2. 解析属性
        //2.1 一个Activity布局对应多个SkinView
        if (v != null) {
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(v, skinAttrs);

            //3. 统一去管理,交给Manager处理
            managerSkinView(skinView);

            //4. 判断是否初始就需要换肤
            SkinManager.getInstance(this).checkChangeSkin(skinView);
        }


        return v;

    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 10:30
     * @desc 统一管理skinView
     */
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance(this).getSkinViews(this);

        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance(this).register(this, skinViews);
        }

        skinViews.add(skinView);
    }


    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            TypedArray a = obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme);
            String viewInflaterClassName =
                    a.getString(android.support.v7.appcompat.R.styleable.AppCompatTheme_viewInflaterClass);
            if ((viewInflaterClassName == null)
                    || AppCompatViewInflater.class.getName().equals(viewInflaterClassName)) {
                // Either default class name or set explicitly to null. In both cases
                // create the base inflater (no reflection)
                mAppCompatViewInflater = new SkinViewInflater();
            } else {
                try {
                    Class viewInflaterClass = Class.forName(viewInflaterClassName);
                    mAppCompatViewInflater =
                            (SkinViewInflater) viewInflaterClass.getDeclaredConstructor()
                                    .newInstance();
                } catch (Throwable t) {
                    Log.i("TAG", "Failed to instantiate custom view inflater "
                            + viewInflaterClassName + ". Falling back to default.", t);
                    mAppCompatViewInflater = new SkinViewInflater();
                }
            }
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }
        View view = mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                false, /* Read read app:theme as a fallback at all times for legacy reasons */
                false /* Only tint wrap the context if enabled */
        );

        return view;
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    private void interruptLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//                Log.e( "onCreateView: ", "拦截了");
//                if (name.equals("Button")){
//                    TextView tv=new TextView(MainActivity.this);
//                    tv.setText("你好");
//                    return tv;
//                }

                //1. 创建View


                //2. 解析属性

                //3. 统一去管理,交给Manager处理

                return null;
            }
        });

    }

    @Override
    public void changeSkin(SkinResource resource) {

    }

    @Override
    protected void onDestroy() {
        //防止内存泄漏
        SkinManager.getInstance(this).unregister(this);
        super.onDestroy();
    }
}
