package ru.bda.icrm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.bda.icrm.fragment.TabIncludeFragment;
import ru.bda.icrm.fragment.TabNotationFragment;
import ru.bda.icrm.fragment.TabScoreFragment;
import ru.bda.icrm.fragment.TabScreenFragment;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class ScoreTabAdapter extends FragmentPagerAdapter {

    private TabScoreFragment mFragmentTabScore;
    private TabScreenFragment mFragmentScreen;
    private TabNotationFragment mFragmentNotation;
    private TabIncludeFragment mFragmentInclude;

    private String[] textTab = {
            "Счёт",
            "Печать",
            "Описание",
            "Вложение"
    };

    private Score score;

    public ScoreTabAdapter(FragmentManager fm, Score score) {
        super(fm);
        this.score = score;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragmentTabScore = new TabScoreFragment(score);
                return mFragmentTabScore;
            case 1:
                mFragmentScreen = new TabScreenFragment();
                return mFragmentScreen;
            case 2:
                mFragmentNotation = new TabNotationFragment();
                return mFragmentNotation;
            case 3:
                mFragmentInclude = new TabIncludeFragment();
                return mFragmentInclude;
            default:
                mFragmentTabScore = new TabScoreFragment(score);
                return mFragmentTabScore;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return textTab[0];
            case 1:
                return textTab[1];
            case 2:
                return textTab[2];
            case 3:
                return textTab[3];

        }
        return super.getPageTitle(position);
    }
}
