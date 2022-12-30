package com.vector.ven.adapter;

/**
 * 主界面Fragment的适配器，不销毁Fragment
 * <p/>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vector.ven.logger.Logger;

import java.util.List;

/**
 * 定义适配器
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private Logger logger = Logger.getLogger();
    private List<Fragment> mFragments;

    public MainFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.mFragments = fragmentList;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

}
