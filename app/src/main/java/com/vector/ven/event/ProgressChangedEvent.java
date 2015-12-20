package com.vector.ven.event;

import de.greenrobot.event.EventBus;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/20
 * Description:<p>{TODO: 用一句话描述}
 */
public class ProgressChangedEvent {
    //下载路径，用来识别是谁下载的
    private String url;
    //总长度
    private long contentLength;
    //下载进度
    private long progress;
    //下载状态
    private int state;

    public static final int STATE_OK = 1;
    public static final int STATE_ERROR = 2;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void post(){
        EventBus.getDefault().post(this);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}

