package com.vector.ven.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.vector.ven.R;
import com.vector.ven.adapter.MainFragmentPagerAdapter;
import com.vector.ven.ui.fragment.SentenceFragment;
import com.vector.ven.ui.fragment.TranslationFragment;
import com.vector.ven.ui.fragment.VideoFragment;
import com.vector.ven.ui.fragment.WordListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
,RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.radio_group) RadioGroup mRadioGroup;

    private List<Fragment> mFragments;
    private MainFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragments = new ArrayList<>(4);
        mFragments.add(new WordListFragment());
        mFragments.add(new TranslationFragment());
        mFragments.add(new SentenceFragment());
        mFragments.add(new VideoFragment());
        mAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                mRadioGroup.check(R.id.word_list_btn);
                break;
            case 1:
                mRadioGroup.check(R.id.translation_btn);
                break;
            case 2:
                mRadioGroup.check(R.id.sentence_btn);
                break;
            case 3:
                mRadioGroup.check(R.id.video_btn);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.word_list_btn:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.translation_btn:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.sentence_btn:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.video_btn:
                mViewPager.setCurrentItem(3);
                break;
        }
    }
}
