package com.vector.ven.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.entity.YouWord;
import com.vector.ven.media.Media;
import com.vector.ven.model.WordModel;
import com.vector.ven.network.TeamListener;
import com.vector.ven.util.FileUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2016/1/8
 * Description:<p>{TODO: 用一句话描述}
 */
public class WordListAdapter extends BaseAdapter {

    private Context mContext;
    private List<YouWord> mWords;

    private WordModel mModel;
    private Media mMedia;
    private boolean downloading;

    public WordListAdapter(Context context, List<YouWord> words) {
        mWords = words;
        mContext = context;
        mMedia = new Media(context);
        mModel = new WordModel();
    }

    @Override
    public int getCount() {
        return mWords == null ? 0 : mWords.size();
    }

    @Override
    public Object getItem(int position) {
        return mWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.word_list_item, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindData(mWords.get(position));
        return convertView;

    }


    class ViewHolder implements View.OnClickListener {
        @Bind(R.id.work_txt)
        TextView mWorkTxt;
        @Bind(R.id.translation_txt)
        TextView mTranslationTxt;
        @Bind(R.id.video_btn)
        View mVideoBtn;
        YouWord mYouWord;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bindData(YouWord youWord) {
            mYouWord = youWord;
            mVideoBtn.setOnClickListener(this);
            String query = youWord.getQuery();
            if (youWord.getBasic() != null) {
                query += " /" + youWord.getBasic().getPhonetic() + "/";
            }
            mWorkTxt.setText(query);


            StringBuilder builder = new StringBuilder();
            String[] translations = youWord.getTranslation();
            if (translations != null) {
                for (String tran : youWord.getTranslation()) {
                    builder.append(tran).append("\n");
                }
            }

            if (youWord.getBasic() != null) {
                translations = youWord.getBasic().getExplains();
                if (translations != null) {
                    String[] tans = youWord.getBasic().getExplains();
                    int total = tans.length;
                    for (int i = 0; i < total; i++) {
                        String tran = tans[i];
                        builder.append(tran);
                        if (i < total - 1) {
                            builder.append("\n");
                        }
                    }
                }
            }
            mTranslationTxt.setText(builder.toString());
        }

        @Override
        public void onClick(final View v) {
            if (mYouWord == null || downloading) {
                return;
            }
            if (!FileUtils.existes(Constants.SYS_PATH + mYouWord.getQuery() + ".mp3")) {
                downloading = true;
                Toast.makeText(mContext, "获取发音", Toast.LENGTH_SHORT).show();
                mModel.downloadAudio(mYouWord.getQuery(), new TeamListener() {
                    @Override
                    public void onOK(int requestCode, String json) {
                        downloading = false;
                        mMedia.play(Constants.SYS_PATH + mYouWord.getQuery() + ".mp3");
                    }

                    @Override
                    public void onError(int requestCode, int errorCode, String msg) {
                        downloading = false;
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "下载发音出错了", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            mMedia.play(Constants.SYS_PATH + mYouWord.getQuery() + ".mp3");
        }
    }
}









