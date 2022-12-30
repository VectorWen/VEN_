package com.vector.ven.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vector.ven.R;
import com.vector.ven.adapter.WordListAdapter;
import com.vector.ven.entity.YouWord;
import com.vector.ven.model.WordModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 主界面 -- 单词列表
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class WordListFragment extends BaseFragment{

    private ViewHolder mViewHolder;
    private List<YouWord> mWords;
    private WordModel mModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.word_list_fragment,inflater,container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mViewHolder = new ViewHolder(mParent);
        mModel = new WordModel();
        mWords = mModel.query(0);
        mViewHolder.bindData(mWords);
    }

    private void asyncLoading(boolean append){
        if(append){

        }else {
            List<YouWord> words = mModel.query(0);
            mWords.clear();
            mWords.addAll(words);
            mViewHolder.mAdapter.notifyDataSetInvalidated();
        }
    }

    class ViewHolder{
        @BindView(R.id.pull_list_view)  PullToRefreshListView mPullListView;
        private View mEmptyView;
        private WordListAdapter mAdapter;

        ViewHolder(final View view){
            ButterKnife.bind(this, view);
            mEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.word_list_empty_view,null,false);
            mPullListView.setEmptyView(mEmptyView);
            mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    asyncLoading(false);
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullListView.onRefreshComplete();
                        }
                    },1000);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    toast("没有更多内容了");
                    mPullListView.onRefreshComplete();
                }
            });
        }

        public void bindData(List<YouWord> words) {
            mAdapter = new WordListAdapter(getActivity(),words);
            mPullListView.setAdapter(mAdapter);
        }
    }
}