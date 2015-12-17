package com.vector.ven.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vector.ven.R;
import com.vector.ven.entity.YouWord;
import com.vector.ven.network.TeamListener;
import com.vector.ven.network.api.TranslationManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面 -- 翻译
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class TranslationFragment extends BaseFragment implements View.OnClickListener, TeamListener {

    private ViewHolder mViewHolder;
    private TranslationManager mManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.translation_fragment, inflater, container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mViewHolder = new ViewHolder(mParent);
        mManager = new TranslationManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                String work = mViewHolder.mWorkEdit.getText().toString().trim();
                if (work.isEmpty()) {
                    return;
                }
                mManager.translation(work, 0, this);
                mViewHolder.mSearchBtn.setEnabled(false);
                mViewHolder.mSearchBtn.setText(R.string.translating);
                break;
        }
    }

    @Override
    public void onOK(int requestCode, final String json) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewHolder.mSearchBtn.setEnabled(true);
                mViewHolder.mSearchBtn.setText(R.string.translation);
                YouWord word = YouWord.createByJson(json);
                mViewHolder.bindData(word);
            }
        });
    }

    @Override
    public void onError(int requestCode, int errorCode, final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewHolder.mSearchBtn.setEnabled(true);
                mViewHolder.mSearchBtn.setText(R.string.translation);
                toast(msg);
            }
        });
    }

    class ViewHolder {
        @Bind(R.id.work_edit)
        EditText mWorkEdit;
        @Bind(R.id.search_btn)
        TextView mSearchBtn;
        @Bind(R.id.work_txt)
        TextView mWorkTxt;
        @Bind(R.id.translation_txt)
        TextView mTranslationTxt;
        @Bind(R.id.web_txt)
        TextView mWebTxt;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mSearchBtn.setOnClickListener(TranslationFragment.this);
        }

        void bindData(YouWord word) {
            if (word == null) {
                mWorkTxt.setText(R.string.translation_error);
                mTranslationTxt.setText("");
                return;
            }

            String query = word.getQuery();
            if (word.getBasic() != null) {
                query += " /" + word.getBasic().getUsPhonetic() + "/";
            }
            mWorkTxt.setText(query);

            StringBuilder builder = new StringBuilder();
            String[] translations = word.getTranslation();
            if (translations != null) {
                for (String tran : word.getTranslation()) {
                    builder.append(tran).append("\n");
                }
            }

            if(word.getBasic()!=null){
                translations = word.getBasic().getExplains();
                if(translations!=null){
                    for (String tran : word.getBasic().getExplains()) {
                        builder.append(tran).append("\n");
                    }
                }
            }

            mTranslationTxt.setText(builder.toString());
            List<YouWord.Web> webs = word.getWeb();

            builder = new StringBuilder();
            if(webs!=null){
                for(YouWord.Web web : webs){
                    builder.append(web.getKey()).append("\n    ");
                    String[] values = web.getValue();
                    if(values !=null){
                        for (int i=0;i<values.length;i++){
                            builder.append(values[i]);
                            if(i==values.length - 1){
                                builder.append("\n\n");
                            }else{
                                builder.append("\n    ");
                            }
                        }
                    }
                }
            }

            mWebTxt.setText(builder.toString());
        }
    }
}