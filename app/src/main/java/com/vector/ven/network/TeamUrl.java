package com.vector.ven.network;

import com.squareup.okhttp.HttpUrl;

/**
 *
 * get请求URL & 参数
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class TeamUrl {

    private HttpUrl.Builder mUrlBuilder;

    public TeamUrl(String url){
        mUrlBuilder = HttpUrl.parse(url).newBuilder();
    }

    public TeamUrl add(String key,String value){
        mUrlBuilder.addQueryParameter(key,value);
        return this;
    }

    HttpUrl getHttpUrl(){
        return mUrlBuilder.build();
    }

    /**
     * e.g. http://www.xxx.com/cus?key=value&key=value
     * @return
     */
    @Override
    public String toString() {
        return mUrlBuilder.toString();
    }
}