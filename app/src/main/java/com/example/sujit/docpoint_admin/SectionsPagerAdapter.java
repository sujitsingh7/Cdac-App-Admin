package com.example.sujit.docpoint_admin;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return new termOneFragment();


            case 1:
                return new termTwoFragment();

            case 2:
                return new termThreeFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){

            case 0:
                return "TERM I";


            case 1:
                return "TERM II";
            case 2:
                return "TERM III";
        }
        return null;


    }
}
