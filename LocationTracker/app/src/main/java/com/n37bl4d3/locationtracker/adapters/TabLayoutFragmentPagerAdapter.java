package com.n37bl4d3.locationtracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.n37bl4d3.locationtracker.interfaces.TabLayoutInterface;

import java.util.ArrayList;

public class TabLayoutFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<TabLayoutInterface> mTabLayoutInterfaceArrayList;

    public TabLayoutFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<TabLayoutInterface> tabLayoutInterfaceArrayList) {
        super(fragmentManager);

        mTabLayoutInterfaceArrayList = tabLayoutInterfaceArrayList;
    }

    @Override
    public int getCount() {
        return mTabLayoutInterfaceArrayList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mTabLayoutInterfaceArrayList.get(position).getItem();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabLayoutInterfaceArrayList.get(position).getPageTitle();
    }
}
