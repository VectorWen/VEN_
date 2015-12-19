package com.vector.ven.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.entity.YouWord;
import com.vector.ven.media.Media;
import com.vector.ven.model.WordModel;
import com.vector.ven.network.TeamDownloader;
import com.vector.ven.network.TeamListener;
import com.vector.ven.network.api.TranslationAPI;
import com.vector.ven.table.Word;
import com.vector.ven.util.FileUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面 -- 翻译
 * 1，翻译
 * 2，保存
 * 3，删除
 * 4，发音
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class TranslationFragment extends BaseFragment implements View.OnClickListener, TeamListener {

    private ViewHolder mViewHolder;
    private WordModel mModel;
    private YouWord mYouWord; //显示的单词，绑定数据之后才初始化或改变

    private String mQuery; //想要查询的单词，点击按钮之后会改变
    private String mJson;  //查询回来的json
    private boolean mHas; //数据库是否保存了,同样是绑定数据才改变的

    private Media mMedia;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.translation_fragment, inflater, container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mViewHolder = new ViewHolder(mParent);
        mModel = new WordModel();
        mMedia = new Media(getActivity());
    }

    private void saveOrDelete(){
        if(mYouWord == null){
            //刚进来，没有搜索过单词
            return;
        }

        //删除
        if(mHas){
            mModel.delete(mQuery);
            mHas = false;
            mViewHolder.updateAddBtn();
            return;
        }

        //保存
        if(mModel.save(mQuery,mJson)){
            mHas = true;
            mViewHolder.updateAddBtn();
        }else{
            toast("保存失败");
        }

    }

    private void search(){
        String work = mViewHolder.mWorkEdit.getText().toString().trim();
        if (work.isEmpty()) {
            return;
        }
        //相同的单词不管了
        if(work.equals(mQuery)){
            return;
        }

        mQuery = work;
        YouWord youWord = mModel.query(work,0,this);
        if(youWord != null){
            mViewHolder.bindData(youWord, true);
        }else{
            mViewHolder.mSearchBtn.setEnabled(false);
            mViewHolder.mSearchBtn.setText(R.string.translating);
        }
    }

    private void play(){
        if(!FileUtils.ifFileExist(mYouWord.getQuery()+".mp3", Constants.SYS_PATH)){
            toast("获取发音");
            mModel.downloadAudio(mYouWord.getQuery(), new TeamListener() {
                @Override
                public void onOK(int requestCode, String json) {
                    mMedia.play(mYouWord.getQuery());
                }

                @Override
                public void onError(int requestCode, int errorCode, String msg) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("下载发音出错了");
                        }
                    });
                }
            });
        }
        mMedia.play(mYouWord.getQuery());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                saveOrDelete();
                break;
            case R.id.search_btn:
                search();
                break;
            case R.id.video_btn:
                play();
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
                YouWord youWord = YouWord.createByJson(json);
                if(youWord == null){
                    toast(getString(R.string.request_update));
                    return;
                }
                mJson = json;
                mViewHolder.bindData(youWord,false);
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
        @Bind(R.id.add_btn)
        ImageButton mAddBtn;
        @Bind(R.id.video_btn)
        ImageButton mVideoBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mSearchBtn.setOnClickListener(TranslationFragment.this);
            mAddBtn.setOnClickListener(TranslationFragment.this);
            mVideoBtn.setOnClickListener(TranslationFragment.this);
        }

        /**
         * 更新按钮状态
         */
        void updateAddBtn(){
            if(mHas){
                mAddBtn.setImageResource(R.drawable.ic_added);
            }else{
                mAddBtn.setImageResource(R.drawable.ic_add);
            }
        }

        void bindData(YouWord word,boolean has) {
            mYouWord = word;
            mHas = has;
            mViewHolder.updateAddBtn();
            if (word == null) {
                mWorkTxt.setText(R.string.translation_error);
                mTranslationTxt.setText("");
                return;
            }

            String query = word.getQuery();
            if (word.getBasic() != null) {
                query += " /" + word.getBasic().getPhonetic() + "/";
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