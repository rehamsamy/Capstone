package com.example.mohamed.capstone;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return new FriendsFragment();
           case 1:
               return new ChatFragment();
           case 2:
               return new RequestsFragment();
           default:
               return null;

       }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Friends";
            case 1:
                return "Chat";
            case 2:
                return "Requests";
                default: return null;
        }
    }
}
