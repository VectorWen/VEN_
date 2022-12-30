package com.vector.ven.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.event.ProgressChangedEvent;
import com.vector.ven.service.DownloadService;
import com.vector.ven.ui.Display;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 主界面 -- 每天一课，视频教程
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class VideoFragment extends BaseFragment implements View.OnClickListener {

    private ViewHolder mViewHolder;
    private DownloadService mService;

    private String mFilePath = "http://cnak2.englishtown.com/juno/school/videos/3.5%20scene%201.f4v";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.video_fragment, inflater, container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mViewHolder = new ViewHolder(mParent);
        //绑定Service
        Intent intent = new Intent("com.vector.ven.action.DOWNLOAD");
        intent.setPackage(getActivity().getPackageName());
        getActivity().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //java.lang.ClassCastException: android.os.BinderProxy cannot be cast to com.vector.ven.service.DownloadService$DownloadBinder
                if (service instanceof DownloadService.DownloadBinder) {
                    mService = ((DownloadService.DownloadBinder) service).getService();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        if (true) {
            Display.showFragment(getActivity(), VideoPlayFragment.class.getName());
            return;
        }
        if (mService != null) {
            mService.download("http://cnak2.englishtown.com/juno/school/videos/3.5%20scene%201.f4v"
                    , Constants.SYS_PATH_VIDEO
                    , "3.5.201.f4v");
        }
    }

    public void onEventMainThread(ProgressChangedEvent event) {
        mViewHolder.bindData(event);
    }

    class ViewHolder {
        @BindView(R.id.progress_bar)
        TextView mProgressBar;
        @BindView(R.id.cur_length)
        TextView mCurLength;
        @BindView(R.id.content_length)
        TextView mContentLength;
        @BindView(R.id.download_btn)
        Button mDownloadBtn;
        @BindView(R.id.test_image_view)
        ImageView mImageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mDownloadBtn.setOnClickListener(VideoFragment.this);
//            TImageLoader.display("http://cnc1.ef-cdn.com/_imgs/community/dailylesson/lessons/lesson359.jpg",mImageView);
        }

        public void bindData(ProgressChangedEvent event) {
            mProgressBar.setText(event.getProgress() + "");
            mCurLength.setText(event.getProgress() + "");
            mContentLength.setText(event.getContentLength() + "");
        }
    }
}