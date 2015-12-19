package com.vector.ven.application;

import com.vector.ven.network.TImageLoader;

import org.litepal.LitePalApplication;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/19
 * Description:<p>{TODO: 用一句话描述}
 */
public class App extends LitePalApplication {

    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        TImageLoader.init(this);
    }
}
