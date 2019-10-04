package com.ang.acb.youtubelearningbuddy.ui.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ang.acb.youtubelearningbuddy.ui.topic.TopicsFragment;
import com.ang.acb.youtubelearningbuddy.ui.video.SearchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    private String[] tabTitles = new String[] {"Search", "Topics"};

    ViewPagerAdapter(@NonNull FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment =null;
        switch (position) {
            case 0:
                fragment = new SearchFragment();
            case 1:
                fragment = new TopicsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
