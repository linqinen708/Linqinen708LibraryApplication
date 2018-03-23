package com.linqinen.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        /*如果需要动态添加，则需要刷新*/
//        notifyDataSetChanged();
    }

    public void addFragment(int index, Fragment fragment, String title) {
        mFragmentList.add(index, fragment);
        mFragmentTitleList.add(index, title);
    }

    public void removeFragment(Fragment fragment, String title) {
        mFragmentList.remove(fragment);
        mFragmentTitleList.remove(title);
    }
    public void removeFragment(int index) {
        mFragmentList.remove(index);
        mFragmentTitleList.remove(index);
    }

    public void set(int index,Fragment fragment, String title){
        mFragmentList.set(index,fragment );
        mFragmentTitleList.set(index,title);
    }

    /**在直接调用addFragment可以成功的添加Fragment。但是调用remove方法并不能移除fragment。
     * viewpager调用setAdapter时会首先执行instantiateItem(ViewGroup container,
     * int position)寻找是否添加过，如果添加过会直接使用，没添加过会执行getItem(int position)。
     * 所以当我们仅仅移除列表中的Fragment没生效，因为在adapter已经记录了fragment。
     *
     * 所以我们重写getItemId(int position)方法，根据fragment的id来查找。
     * */
    @Override
    public long getItemId(int position) {
        // 获取当前数据的hashCode
        int hashCode = mFragmentList.get(position).hashCode();
//        LogT.i("hashCode:" + hashCode);
        return hashCode;
    }

    @Override
    public int getItemPosition(Object object) {
//        LogT.i("object:" + object);
        if (!mFragmentList.isEmpty()) {
            if (!mFragmentList.contains(object)) {
                return POSITION_NONE;
            }
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < mFragmentList.size()) {

            return mFragmentList.get(position);

        }

        return null;
    }


    @Override
    public int getCount() {
//        LogT.i("mFragmentList.size():" + mFragmentList.size());
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
