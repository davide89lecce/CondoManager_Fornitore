package com.gambino_serra.condomanager_fornitore.View.Home.InterventiInCorso.InterventoInCorso;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gambino_serra.condomanager_fornitore.View.Home.InterventiInCorso.InterventoInCorso.RapportiIntervento.RapportiInterventoInCorso;

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
                RapportiInterventoInCorso tab2 = new RapportiInterventoInCorso();
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