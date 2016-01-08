package com.vector.ven.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vector.ven.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2016/1/3
 * Description:<p>{TODO: 用一句话描述}
 */
public class VideoPlayFragment extends BaseFragment {

    private ViewHolder mViewHolder;
    private String mPath = "http://cnak2.englishtown.com/juno/school/videos/3.5%20scene%201.f4v";
    private MediaController mMediaController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Vitamio.isInitialized(getActivity().getApplicationContext());
        init(R.layout.video_play_fragment,inflater,container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mMediaController = new MediaController(getActivity());
        mViewHolder = new ViewHolder(mParent);
    }

    class ViewHolder{
        @Bind(R.id.video_view)  VideoView mVideoView;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
            mVideoView.setMediaController(mMediaController);
            mVideoView.setVideoPath(mPath);
            mVideoView.requestFocus();
        }
    }
}
