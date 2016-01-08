package com.vector.ven.ui;

import android.app.Activity;
import android.content.Intent;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2016/1/3
 * Description:<p>{TODO: 用一句话描述}
 */
public class Display {

    /**
     * 显示一个Fragment
     * @param activity 只有activity 才能打开activity
     * @param fragmentClassName v4.Fragment.class.getName();
     */
    public static void showFragment(Activity activity,String fragmentClassName){
        Intent intent = new Intent(activity,ShowFragmentActivity.class);
        intent.putExtra(ShowFragmentActivity.FRAGMENT_NAME,fragmentClassName);
        activity.startActivity(intent);
    }
}
