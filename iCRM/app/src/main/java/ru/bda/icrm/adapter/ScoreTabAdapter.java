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
            "Описание"
            //"Вложение"
    };

    private Score score;

    public ScoreTabAdapter(FragmentManager fm, Score score) {
        super(fm);
        this.score = score;
        mFragmentTabScore = new TabScoreFragment(score);
        mFragmentScreen = new TabScreenFragment();
        mFragmentNotation = new TabNotationFragment();
    }

    public void setScore(Score score) {
        this.score = score;
        mFragmentTabScore.setScore(score);
        mFragmentScreen.setUrl(score);
        mFragmentNotation.setScore(score);
    }

    public Score getScore() {
        score.setContactFace(mFragmentTabScore.getScore().getContactFace());
        score.setContract(mFragmentTabScore.getScore().getContract());
        score.setInitialConditions(mFragmentNotation.getScore().getInitialConditions());
        score.setOrderWorks(mFragmentNotation.getScore().getOrderWorks());
        score.setAnnotation(mFragmentNotation.getScore().getAnnotation());
        return score;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragmentTabScore.setScore(score);
                return mFragmentTabScore;
            case 1:
                mFragmentScreen.setUrl(score);
                return mFragmentScreen;
            case 2:
                mFragmentNotation.setScore(score);
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
        return 3;
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
            /**case 3:
                return textTab[3];*/

        }
        return super.getPageTitle(position);
    }
}
