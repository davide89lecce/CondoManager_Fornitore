package com.gambino_serra.condomanager_fornitore.View.DrawerMenu.Menu.Home.InterventiInCorso;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterInterventoInCorso extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterInterventoInCorso(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DettaglioInterventoInCorso tab1 = new DettaglioInterventoInCorso();
                return tab1;
            case 1:
                DettaglioInterventoInCorso tab2 = new DettaglioInterventoInCorso();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}