package com.github.maxcriser.qrscanner.adapter;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;

import com.github.maxcriser.qrscanner.fragment.PageFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 2;
    private final Context context;
    private final String[] tabTitles = new String[] { "Scanning", "DATABASE" };
    private final LoaderManager mLoaderManager;
    private final Application mApplication;

    public SampleFragmentPagerAdapter(final FragmentManager fm, final Context context, final Application pApplication, final LoaderManager pLoaderManager) {
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
    public Fragment getItem(final int position) {
        return PageFragment.newInstance(position + 1, mLoaderManager, mApplication, context);
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return tabTitles[position];
    }
}
