package com.vector.ven.network;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class StatusCode {

    /**
     * 网络出错，无法连接服务器
     */
    public static final int CODE_NETWORK_ERROR = -1;

    /**
     * 请求被取消，可能因为自己取消，可能因为这次请求还没有响应新的请求又发出了
     */
    public static final int CODE_CANCEL = -2;

    /**
     * 位置错误，就是现在还不知道的错误
     */
    public static final int CODE_UNKNOWN = -3;

}
