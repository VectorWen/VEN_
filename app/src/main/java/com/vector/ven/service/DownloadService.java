package com.vector.ven.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.vector.ven.event.ProgressChangedEvent;
import com.vector.ven.logger.Logger;
import com.vector.ven.network.TeamDownloader;
import com.vector.ven.network.TeamUrl;
import com.vector.ven.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/20
 * Description:<p>{TODO: 用一句话描述}
 */
public class DownloadService extends Service {
    private Logger logger = Logger.getLogger();
    private TeamDownloader mDownloader = new TeamDownloader();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        logger.d("onBind()");
        return new DownloadBinder();
    }

    public class DownloadBinder extends Binder {

        public DownloadService getService() {
            logger.d("getService()");
            return DownloadService.this;
        }

    }

    public void download(String url,String filePath, String fileName){
        mDownloader.download(new TeamUrl(url),new DownloadRunnable(url,filePath,fileName));
    }

    public class DownloadRunnable implements Callback {

        private String filePath;
        private String fileName;
        private ProgressChangedEvent mEvent;


        public DownloadRunnable(String url,String filePath, String fileName) {
            this.filePath = filePath;
            this.fileName = fileName;

            mEvent = new ProgressChangedEvent();
            mEvent.setUrl(url);
        }

        @Override
        public void onFailure(Request request, IOException e) {
            logger.d("onFailure()");
            mEvent.setState(ProgressChangedEvent.STATE_ERROR);
            mEvent.post();
        }

        @Override
        public void onResponse(Response response) {
            int code = response.code();
            if (code == 200) {
                try {
                    long length = response.body().contentLength();
                    mEvent.setContentLength(length);
                    mEvent.post();
                    writeFile(filePath, fileName, response.body().byteStream());
                    mEvent.setState(ProgressChangedEvent.STATE_OK);
                    mEvent.post();
                }catch (IOException e){
                    mEvent.setState(ProgressChangedEvent.STATE_ERROR);
                    mEvent.post();
                }
            } else {
                logger.d("code != 200");
                mEvent.setState(ProgressChangedEvent.STATE_ERROR);
                mEvent.post();
            }
        }

        private void writeFile(String path, String fileName, InputStream input) throws IOException {
            File file = null;
            OutputStream output = null;
            file = FileUtils.createFile(fileName, path);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int temp;
            long progress = 0;
            while ((temp = input.read(buffer)) != -1) {
                progress += temp;
                output.write(buffer, 0, temp);
                mEvent.setProgress(progress);
                mEvent.post();
            }
            output.flush();
            output.close();
        }
    }

}
