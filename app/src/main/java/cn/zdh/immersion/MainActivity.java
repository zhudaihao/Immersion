package cn.zdh.immersion;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 沉浸式 实现方法 有两种
 * <p>
 * 1：设置主题(styles)--》在res--》values--->styles-->
 * <p>
 * <p>
 * 2：代码设置
 */
public class MainActivity extends AppCompatActivity {

    //占位控件
    private View statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusBar = findViewById(R.id.view);
        statusBar.setVisibility(View.GONE);

        //沉浸式
        initStatus();
//
//        //修改状态栏背景颜色
        setStatusBarColor();
//
//        //设置状态栏上的字体为黑色-因为本页面是白色必须设置（4.4不兼容）
        UtilsStyle.statusBarLightMode(this);
    }

    /**
     * 修改状态栏背景颜色
     */
    private void setStatusBarColor() {
        //方法1
//        setOne();

        //方法2
        setTwo();

    }


    /**
     * 方法1 不然内容填充状态栏 在布局添加个view作为占位
     */
    private void setOne() {
        //设置占位控件显示
        statusBar.setVisibility(View.VISIBLE);
        //获取到它的Params对象
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        //设置它的高度
        layoutParams.height = getStatusHeight();
        //设置layoutParams
        statusBar.setLayoutParams(layoutParams);
        //设置背景颜色
        statusBar.setBackgroundColor(Color.RED);
    }

    /**
     * 方法2 不然内容填充状态栏 根据版本 调用系统 和控件占位
     */
    private void setTwo() {
        //获取到根布局的view
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        //给根布局设置padding值
        rootView.setPadding(0, getStatusHeight(), 0, getNavigationBarHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //第二种 5.0以上
            getWindow().setStatusBarColor(Color.RED);
        } else {
            //第一种 4.4-5.0
            //获取到根布局
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            View statusBar = new View(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight());
            statusBar.setBackgroundColor(Color.RED);
            statusBar.setLayoutParams(layoutParams);
            decorView.addView(statusBar);
        }
    }


    /**
     * Java代码实现沉浸式
     */
    private void initStatus() {
        //版本大于等于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获取到状态栏设置的两条属性
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //在4.4之后又有两种情况  第一种 4.4-5.0   第二种 5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //第二种 5.0以上
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                window.setStatusBarColor(0);
            } else {
                //第一种 4.4-5.0
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

    }


    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public int getStatusHeight() {
        //获取到状态栏的资源ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        //如果获取到了
        if (resourceId > 0) {
            //就返回它的高度
            return getResources().getDimensionPixelSize(resourceId);
        }
        //否则返回0
        return 0;
    }


    /**
     * 获取到底部虚拟按键的高度
     *
     * @return
     */
    public int getNavigationBarHeight() {
        //获取到虚拟按键的资源ID
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        //如果获取到了
        if (resourceId > 0) {
            //就返回它的高度
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
