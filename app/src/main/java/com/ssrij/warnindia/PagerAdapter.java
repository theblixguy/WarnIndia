package com.ssrij.warnindia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<EarthquakeObject> earthquakeObjectArrayList;
    ArrayList<TsunamiObject> tsunamiObjectArrayList;
    ArrayList<String> cycloneObjectArrayList;
    ArrayList<CycloneObject> detailedCycloneObjectArrayList;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<EarthquakeObject> quakeDataList,
                        ArrayList<TsunamiObject> tsunamiDataList, ArrayList<String> cycloneDataList,
                        ArrayList<CycloneObject> detailedCycloneDataList) {
        super(fm);
        this.earthquakeObjectArrayList = quakeDataList;
        this.tsunamiObjectArrayList = tsunamiDataList;
        this.cycloneObjectArrayList = cycloneDataList;
        this.detailedCycloneObjectArrayList = detailedCycloneDataList;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EarthquakeMapFragment tab1 = new EarthquakeMapFragment().newInstance(earthquakeObjectArrayList);
                return tab1;
            case 1:
                TsunamiFragment tab2 = new TsunamiFragment().newInstance(tsunamiObjectArrayList);
                return tab2;
            case 2:
                CycloneFragment tab3 = new CycloneFragment().newInstance(cycloneObjectArrayList, detailedCycloneObjectArrayList);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}