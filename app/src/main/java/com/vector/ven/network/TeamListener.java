package com.vector.ven.network;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public interface TeamListener {

    /**
     * @param requestCode 请求代码，区分使用同一个监听器的不同请求
     * @param json 响应的json 数据
     */
    void onOK(final int requestCode, final String json);

    /**
     * @param errorCode
     * @param msg
     */
    void onError(final int requestCode, final int errorCode, final String msg);

}