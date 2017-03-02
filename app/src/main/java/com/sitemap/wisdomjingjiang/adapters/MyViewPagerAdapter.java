package com.sitemap.wisdomjingjiang.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * com.sitemap.wisdomjingjiang.adapters.MyViewPagerAdapter
 * @author zhang
 * create at 2016年5月5日 上午10:22:26
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mMenuList;//fragment集合
	public MyViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> mMenuList) {
		super(fm);
		this.mMenuList=mMenuList;
	}

	@Override
	public Fragment getItem(int position) {
		return mMenuList.get(position);
	}

	@Override
	public int getCount() {
		return mMenuList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

}
