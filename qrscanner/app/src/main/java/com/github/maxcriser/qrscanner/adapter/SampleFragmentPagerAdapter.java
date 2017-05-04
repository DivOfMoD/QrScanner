package com.github.maxcriser.qrscanner.adapter;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;

import com.github.maxcriser.qrscanner.fragment.PageFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private Context context;
    private String tabTitles[] = new String[] { "Scanning", "DATABASE" };
    private LoaderManager mLoaderManager;
    private Application mApplication;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, Application pApplication, LoaderManager pLoaderManager) {
        super(fm);
        mApplication = pApplication;
        mLoaderManager = pLoaderManager;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, mLoaderManager, mApplication, context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
