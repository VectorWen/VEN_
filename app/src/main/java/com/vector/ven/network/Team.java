package com.vector.ven.network;

import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vector.ven.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 设计理念：一个API 对应一个Pocket 实例。同一个API 不能同一时间发出两次，就是说一个Pocket 只能发出一个请求，后发出的请求
 * 会覆盖上次的请求。
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class Team {

    private Logger logger = Logger.getLogger();

    private static OkHttpClient mHttpClient; //全部请求使用同一个OkHttpClient
    private Call mCall; //保存最后一次请求的Call
    private Call mCancelCall; //保存最后一次请求的Call

    private HttpUrl mHttpUrl;//API 的url
    private int mRequestCode;//请求代码，区分同一个API 的不同请求
    private TeamListener mListener;

    public Team() {
        if (mHttpClient == null) {
            mHttpClient = new OkHttpClient();
            mHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        }
    }

    /**
     * 取消请求
     */
    public void cancel() {
        if (mCall != null) {
            if (!mCall.isCanceled()) {
                mCall.cancel();
                mCancelCall = mCall;
            }
        }
    }

    /**
     * post 请求
     *
     * @param listener    监听器
     * @param param       请求参数
     * @param requestCode 请求代码
     */
    public void post(TeamListener listener, TeamParameter param, int requestCode) {
        cancel();//取消上次的请求
        mListener = listener;
        mRequestCode = requestCode;
        Request request = new Request.Builder()
                .url(mHttpUrl)
                .post(param.getRequestBody())
                .build();
        mCall = mHttpClient.newCall(request);
        mCall.enqueue(new CustomCallback());

        logger.d("url -- " + mHttpUrl.toString() + "?" + param.toString());
    }

    /**
     * post 请求
     *
     * @param listener    监听器
     * @param body        请求Body
     * @param requestCode 请求代码
     */
    public void post(TeamListener listener, RequestBody body, int requestCode) {
        cancel();//取消上次的请求
        mListener = listener;
        mRequestCode = requestCode;
        Request request = new Request.Builder()
                .url(mHttpUrl)
                .post(body)
                .build();
        mCall = mHttpClient.newCall(request);
        mCall.enqueue(new CustomCallback());

        logger.d("url -- " + mHttpUrl.toString() + "?" + body.toString());
    }

    /**
     * get 请求，参数已经在setUrl 的时候包含在url 里面了
     *
     * @param listener    监听器
     * @param requestCode 请求代码
     */
    public void get(int requestCode,TeamListener listener) {
        cancel();//取消上次的请求
        mListener = listener;
        mRequestCode = requestCode;
        Request request = new Request.Builder()
                .url(mHttpUrl)
                .get()
                .build();
        mCall = mHttpClient.newCall(request);
        mCall.enqueue(new CustomCallback());
        logger.d("url -- " + mHttpUrl.toString());
    }

    public String getUrl() {
        return mHttpUrl.toString();
    }

    public void setUrl(String url) {
        this.mHttpUrl = HttpUrl.parse(url);
    }

    public void setUrl(TeamUrl url) {
        this.mHttpUrl = url.getHttpUrl();
    }

    /**
     * 不是静态的，就是说，每一个Pocket 有一个CustomCallback
     */
    private class CustomCallback implements Callback {

        @Override
        public void onFailure(final Request request, final IOException e) {
            if(mCancelCall!=null&&mCancelCall.isCanceled()){
                mCancelCall = null;
                mListener.onError(mRequestCode, StatusCode.CODE_CANCEL, e.getMessage());
            }else{
                mListener.onError(mRequestCode, StatusCode.CODE_NETWORK_ERROR, e.getMessage());
            }
        }

        @Override
        public void onResponse(final Response response) throws IOException {

            final int code = response.code(); //响应代码，我们都是希望是200
            switch (code){
                case 200:
                    String json = response.body().string();
                    logger.d("url -- "+json);
                    mListener.onOK(mRequestCode, json);
                    break;
                default:
                    mListener.onError(mRequestCode,StatusCode.CODE_UNKNOWN,"Unknown Error");
            }
        }

    }
}