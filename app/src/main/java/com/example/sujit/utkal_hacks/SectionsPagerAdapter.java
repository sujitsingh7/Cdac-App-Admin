package com.example.sujit.utkal_hacks;

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
            case 3:
                return new termFourFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){

            case 0:
                return " Ist Year";


            case 1:
                return "IInd Year";
            case 2:
                return "IIIrd Year";
            case 3:
                return "IVth Year";
        }
        return null;


    }
}
