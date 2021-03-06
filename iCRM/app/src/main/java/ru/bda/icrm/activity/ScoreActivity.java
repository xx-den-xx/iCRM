package ru.bda.icrm.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.ScoreTabAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.dialog.MyDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class ScoreActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ScoreTabAdapter mScoreAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private String mNumberAccount;
    private Score mScore = new Score();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        if (getIntent() != null) {
            mNumberAccount = getIntent().getStringExtra(Constants.INTENT_SCORE);
        }
        initContent();
        new GetScoreTask().execute();
    }

    private void initContent() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("№ " + mNumberAccount);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateTask().execute();
            }
        });
    }

    private void initPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mScoreAdapter = new ScoreTabAdapter(getSupportFragmentManager(), mScore);
        mViewPager.setAdapter(mScoreAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return ApiController.getInstance().updateFullScore(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ScoreActivity.this), mScore, 0
            );
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mProgressBar.setVisibility(View.GONE);
            if (result) {
                MyDialog dialog = new MyDialog();
                dialog.init("Успешно сохранено", "Сохранение счета произошло успешно.",
                        "Выйти", "Продолжить", new MyDialog.OnClickListener() {
                            @Override
                            public void onLeftBtnClick() {
                                finish();
                            }

                            @Override
                            public void onRightBtnClick() {

                            }
                        });
                dialog.show(ScoreActivity.this);
            } else {
                MyDialog dialog = new MyDialog();
                dialog.init("Ошибка сохранения", "При сохранении произошла ошибка, попробуйте ещё раз сохранить",
                        null, "Ok", new MyDialog.OnClickListener() {
                    @Override
                    public void onLeftBtnClick() {
                    }

                    @Override
                    public void onRightBtnClick() {
                    }
                });
                dialog.show(ScoreActivity.this);
            }
        }
    }

    private class GetScoreTask extends AsyncTask<Void, Void, Score> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Score doInBackground(Void... params) {
            return ApiController.getInstance().getScore(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getApplicationContext()), mNumberAccount
            );
        }

        @Override
        protected void onPostExecute(Score score) {
            super.onPostExecute(score);
            mProgressBar.setVisibility(View.GONE);
            if (score != null) {
                //Log.d("myLog", score.toString());
                mScore = score;
                mViewPager = (ViewPager) findViewById(R.id.view_pager);
                mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
                mScoreAdapter = new ScoreTabAdapter(getSupportFragmentManager(), mScore);
                mViewPager.setAdapter(mScoreAdapter);
                mViewPager.setCurrentItem(0);
                mViewPager.setOffscreenPageLimit(3);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        }
    }
}
