package com.linqinen.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林 on 2017/6/5.
 */

public class CourseEnrolledFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public CourseEnrolledFragmentAdapter(FragmentManager fm) {

        super(fm);

    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override

    public Fragment getItem(int i) {

        if (i < mFragmentList.size()) {

            return mFragmentList.get(i);

        }

        return null;

    }

    @Override

    public int getCount() {

        return mFragmentList.size();

    }


    @Override

    public int getItemPosition(Object object) {

        if (!mFragmentList.isEmpty()) {

            if (!mFragmentList.contains(object)) {

                return POSITION_NONE;

            }

        }

        return POSITION_UNCHANGED;

    }

    @Override

    public long getItemId(int position) {

        return mFragmentList.get(position).getId();

    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (mFragmentTitleList.size() > position) {

            return mFragmentTitleList.get(position);

        }

        return super.getPageTitle(position);

    }

}
