package ru.bda.icrm.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.bda.icrm.R;
import ru.bda.icrm.activity.ScoreActivity;
import ru.bda.icrm.adapter.RecyclerScoreAdapter;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.listener.OnClickScoreListener;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 31.08.2016.
 */
public class ScoresFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerScoreAdapter mAdapter;
    private List<Score> mScoreList = new ArrayList<>();

    private LinearLayout mLayoutFilter;
    private TextView mTvFilter;
    private ProgressBar mProgressBar;
    private Score mScore = new Score();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mLayoutFilter = (LinearLayout) view.findViewById(R.id.layout_filter);
        mTvFilter = (TextView) view.findViewById(R.id.tv_filter);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerScoreAdapter(mScoreList);
        mAdapter.setOnClickScoreListener(new OnClickScoreListener() {
            @Override
            public void onClickItemScore(Score score) {
                startScoreActivity(score);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    public void setScore(Score score) {
        this.mScore = score;
        new SendScoreTask().execute();
    }

    private void startScoreActivity(Score score){
        Intent intent = new Intent(getActivity(), ScoreActivity.class);
        intent.putExtra(Constants.INTENT_SCORE, score.getNumberAccount());
        startActivity(intent);
    }

    private class SendScoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mScore != null) {
                mScoreList.add(mScore);
                mAdapter.setListScore(mScoreList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
