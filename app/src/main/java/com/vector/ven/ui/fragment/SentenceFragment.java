package com.vector.ven.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.entity.Sentence;
import com.vector.ven.media.Media;
import com.vector.ven.model.SentenceModel;
import com.vector.ven.network.TImageLoader;
import com.vector.ven.network.TeamListener;
import com.vector.ven.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

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
            if (!mModel.isEmpty(mSentence)) {
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
        if (mSentence == null || downloading) {
            return;
        }

        if (!FileUtils.existes(Constants.SYS_PATH_SENTENCE + mSentence.getDateline() + ".mp3")) {
            toast("获取发音");
            downloading = true;
            mModel.downloadAudio(mSentence, new TeamListener() {
                @Override
                public void onOK(int requestCode, String json) {
                    downloading = false;
                    mMedia.play(Constants.SYS_PATH + mSentence.getDateline() + ".mp3");
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
        mMedia.play(Constants.SYS_PATH + mSentence.getDateline() + ".mp3");

    }

    private void addBtn() {
        if (mSentence == null) {
            return;
        }
        if (mHas) {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.bind(mSentence);
                }
            });
        }
    }

    @Override
    public void onError(int requestCode, int errorCode, String msg) {

    }

    class ViewHolder {
        @BindView(R.id.time_txt)
        TextView mTimeTxt;
        @BindView(R.id.sentence_txt)
        TextView mSentenceTxt;
        @BindView(R.id.video_btn)
        ImageButton mVideoBtn;
        @BindView(R.id.add_btn)
        ImageButton mAddBtn;
        @BindView(R.id.sentence_translation_txt)
        TextView mSentenceTranslationTxt;
        @BindView(R.id.sentence_img)
        ImageView mSentenceImg;

        Sentence mSentence;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mAddBtn.setOnClickListener(SentenceFragment.this);
            mVideoBtn.setOnClickListener(SentenceFragment.this);
        }

        @OnLongClick(R.id.sentence_img)
        public boolean onLongClick() {
            if (mSentence == null) {
                return false;
            }
            File file = ImageLoader.getInstance().getDiskCache().get(mSentence.getPicture2());
            if (file != null) {
                String filePath = Constants.SYS_PATH_VEN_IMAGE;
                String[] names = mSentence.getPicture2().split("/");
                String fileName = names[names.length-1];
                /* 保存图片 */
                FileUtils.copyFile(file, filePath, fileName);
                /* 把文件插入到系统图库 */
                try {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            filePath, fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                        /* 通知图库更新 */
                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePath + fileName)));

                toast("已经保存到：" + filePath+fileName);
            } else {
                toast("保存失败");
            }
            return true;
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
            mSentence = sentence;
            mTimeTxt.setText(sentence.getDateline());
            mSentenceTranslationTxt.setText(sentence.getNote());
            mSentenceTxt.setText(sentence.getContent());
            TImageLoader.display(sentence.getPicture2(), mSentenceImg, R.drawable.ic_sentence);
        }
    }
}
