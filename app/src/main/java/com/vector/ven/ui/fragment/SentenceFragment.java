package com.vector.ven.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vector.ven.R;

/**
 *
 * 主界面 -- 每天一句
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class SentenceFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.sentence_fragment,inflater,container);
        return mParent;
    }
}
