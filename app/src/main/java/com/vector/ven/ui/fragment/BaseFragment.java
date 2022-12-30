package com.vector.ven.ui.fragment;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vector.ven.logger.Logger;

/**
 *
 * Fragment 的基类
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class BaseFragment extends Fragment {

    private Logger logger = Logger.getLogger();
    protected View mParent;

    /**
     * 初始化parent,如果parent 已经在Activity 的View 容器里面，那么就移除这个parent，让onCreateView 返回的View 再次加入view 容器
     *
     * @param inflater
     * @param container
     */
    public void init(int layoutId, LayoutInflater inflater, ViewGroup container) {
        if (mParent == null) {
            mParent = inflater.inflate(layoutId, container, false);
            onInitData();
        }
        if (mParent != null) {
            ViewGroup parentContainer = (ViewGroup) mParent.getParent();
            if (parentContainer != null) {
                parentContainer.removeView(mParent);
            }
        }
    }

    /**
     * 初始化数据，在parent 为空的时候回调
     */
    public void onInitData() {
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private Toast mToast;

    /**
     * 封装了Toast，直接toast（String content）
     *
     * @param content
     *            content of your want to Toast
     */
    public void toast(CharSequence content) {
        if(mToast == null){
            mToast = Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(content);
        }
        mToast.show();
    }
}
