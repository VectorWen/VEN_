package com.vector.ven.network;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.Buffer;

/**
 *
 * Post 请求参数
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class TeamParameter {
    private FormEncodingBuilder mBuilder;

    private TeamParameter() {
        mBuilder = new FormEncodingBuilder();
    }

    public TeamParameter add(String key, String value) {
        mBuilder.add(key, value);
        return this;
    }

    public RequestBody getRequestBody() {
        return mBuilder.build();
    }

    /**
     * e.g key=value&key=value
     *
     * @return key=value&key=value
     */
    @Override
    public String toString() {
        Buffer buffer = new Buffer();
        try {
            mBuilder.build().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}