package com.dmrjkj.remotecontroller.module.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.module.fragment.DeviceListFragment;
import com.dmrjkj.remotecontroller.module.fragment.SearchDeviceFragment;
import com.dmrjkj.remotecontroller.weight.XViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xinchen on 19-4-16.
 */

public class DeviceListActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;
    @BindView(R.id.tab_tl_indicator)
    TabLayout tabTlIndicator;
    @BindView(R.id.viewPager)
    XViewPager viewPager;

    private DeviceListFragment deviceListFragment;
    private SearchDeviceFragment searchDeviceFragment;

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        toolbarTitle.setText("Add Device");

        deviceListFragment = new DeviceListFragment();
        searchDeviceFragment = new SearchDeviceFragment();

        TabFragmentPageAdapter adapter = new TabFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabTlIndicator.setupWithViewPager(viewPager);

        tabTlIndicator.getTabAt(1).select();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    searchDeviceFragment.beginSearch();
                } else {
                    searchDeviceFragment.cancleScan();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_all_device_list;
    }

    @OnClick({R.id.iv_back, R.id.search_iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.search_iv_search:
                break;
        }
    }

    /******************inner class*****************/
    class TabFragmentPageAdapter extends FragmentPagerAdapter {

        public TabFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? searchDeviceFragment : deviceListFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? searchDeviceFragment.getBaseTitle() : deviceListFragment.getBaseTitle();
        }
    }
}
