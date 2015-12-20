package com.vector.ven.network;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vector.ven.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/19
 * Description:<p>{TODO: 用一句话描述}
 */
public class TeamDownloader {
    private static OkHttpClient mHttpClient;

    public TeamDownloader() {
        if (mHttpClient == null) {
            mHttpClient = new OkHttpClient();
            mHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        }
    }

    public void download(TeamUrl url, String filePath,String fileName, TeamListener listener) {
        Request request = new Request.Builder()
                .url(url.getHttpUrl())
                .build();
        mHttpClient.newCall(request).enqueue(new DownloadCallback(filePath,fileName, listener));
    }

    public void download(TeamUrl url, Callback callback) {
        Request request = new Request.Builder()
                .url(url.getHttpUrl())
                .build();
        mHttpClient.newCall(request).enqueue(callback);
    }


    private class DownloadCallback implements Callback {

        private String filePath;
        private String fileName;
        private TeamListener listener;

        public DownloadCallback(String filePath,String fileName, TeamListener listener) {
            this.filePath = filePath;
            this.listener = listener;
            this.fileName = fileName;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            listener.onError(0,StatusCode.CODE_NETWORK_ERROR,"");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            int code = response.code();
            if(code == 200){
                FileUtils.writeFile(filePath,fileName,response.body().byteStream());
                listener.onOK(0,null);
            }else{
                listener.onError(0,StatusCode.CODE_UNKNOWN,"");
            }
        }
    }

}
