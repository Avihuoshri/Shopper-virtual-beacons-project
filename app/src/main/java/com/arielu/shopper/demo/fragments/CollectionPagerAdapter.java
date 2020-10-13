package com.arielu.shopper.demo.fragments;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> pages;
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<>();
        pages.add(UserListsFragment.newInstance());
        pages.add(UserListsSharedFragment.newInstance());
    }
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "MY LISTS";
            case 1:
                return "SHARED LISTS";
        }
        return null;
    }
}
