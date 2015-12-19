package com.vector.ven.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.entity.Sentence;
import com.vector.ven.media.Media;
import com.vector.ven.model.SentenceModel;
import com.vector.ven.network.TImageLoader;
import com.vector.ven.network.TeamListener;
import com.vector.ven.util.FileUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面 -- 每天一句
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class SentenceFragment extends BaseFragment implements View.OnClickListener, TeamListener {

    private ViewHolder mViewHolder;
    private SentenceModel mModel;
    private Sentence mSentence;

    private boolean mHas;
    private Media mMedia;
    private boolean downloading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.sentence_fragment, inflater, container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mViewHolder = new ViewHolder(mParent);
        mModel = new SentenceModel();
        mMedia = new Media(getActivity());
        mSentence = mModel.getTodaySentence(0, this);
        if (mSentence != null) {
            mViewHolder.bind(mSentence);
            if(!mModel.isEmpty(mSentence)){
                mHas = true;
                mViewHolder.updateAddBtn();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                addBtn();
                break;
            case R.id.video_btn:
                play();
                break;
        }
    }

    private void play() {
        if(mSentence == null||downloading){
            return;
        }

        if(!FileUtils.existes(Constants.SYS_PATH_SENTENCE +mSentence.getDateline()+".mp3")){
            toast("获取发音");
            downloading = true;
            mModel.downloadAudio(mSentence, new TeamListener() {
                @Override
                public void onOK(int requestCode, String json) {
                    downloading = false;
                    mMedia.play(Constants.SYS_PATH+mSentence.getDateline() + ".mp3");
                }

                @Override
                public void onError(int requestCode, int errorCode, String msg) {
                    downloading = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("下载发音出错了");
                        }
                    });
                }
            });
        }
        mMedia.play(Constants.SYS_PATH+mSentence.getDateline() + ".mp3");

    }

    private void addBtn() {
        if (mSentence == null) {
            return;
        }
        if(mHas){
            mModel.delete(mSentence);
            mHas = false;
            mViewHolder.updateAddBtn();
            return;
        }

        mModel.save(mSentence);
        mHas = true;
        mViewHolder.updateAddBtn();

    }

    @Override
    public void onOK(int requestCode, String json) {
        mSentence = Sentence.createByJson(json);
        if (mSentence != null) {
            mModel.cacheSentence(mSentence);
            mViewHolder.bind(mSentence);
        }
    }

    @Override
    public void onError(int requestCode, int errorCode, String msg) {

    }

    class ViewHolder {
        @Bind(R.id.time_txt)
        TextView mTimeTxt;
        @Bind(R.id.sentence_txt)
        TextView mSentenceTxt;
        @Bind(R.id.video_btn)
        ImageButton mVideoBtn;
        @Bind(R.id.add_btn)
        ImageButton mAddBtn;
        @Bind(R.id.sentence_translation_txt)
        TextView mSentenceTranslationTxt;
        @Bind(R.id.sentence_img)
        ImageView mSentenceImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mAddBtn.setOnClickListener(SentenceFragment.this);
            mVideoBtn.setOnClickListener(SentenceFragment.this);
        }

        /**
         * 更新按钮状态
         */
        void updateAddBtn() {
            if (mHas) {
                mAddBtn.setImageResource(R.drawable.ic_added);
            } else {
                mAddBtn.setImageResource(R.drawable.ic_add);
            }
        }

        void bind(Sentence sentence) {
            mTimeTxt.setText(sentence.getDateline());
            mSentenceTranslationTxt.setText(sentence.getNote());
            mSentenceTxt.setText(sentence.getContent());
            TImageLoader.display(sentence.getPicture2(), mSentenceImg, R.drawable.ic_sentence);
        }
    }
}
