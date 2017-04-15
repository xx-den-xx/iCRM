package ru.bda.icrm.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.bda.icrm.R;
import ru.bda.icrm.activity.MainActivity;
import ru.bda.icrm.activity.ScoreActivity;
import ru.bda.icrm.adapter.RecyclerScoreAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.dialog.AddScoreDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddScoreClickListener;
import ru.bda.icrm.listener.OnClickScoreListener;
import ru.bda.icrm.listener.OnFilterScoreClickListener;
import ru.bda.icrm.model.Score;

public class ScoresFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerScoreAdapter mAdapter;
    private List<Score> mScoreList = new ArrayList<>();

    private LinearLayout mLayoutFilter;
    //private TextView mTvFilter;
    private Spinner mFilterSpinner;
    private ProgressBar mProgressBar;
    private Score mScore = new Score();
    private String idContragent;
    private int mFilter = 0;
    private String[] mFilterArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, null);
        mFilterArray = getResources().getStringArray(R.array.score_filter_array);
        initContent(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFilterSpinner.setSelection(0);
        new GetScoreListTask().execute();
    }

    private void initContent(View view) {
        mLayoutFilter = (LinearLayout) view.findViewById(R.id.layout_filter);
        mFilterSpinner = (Spinner) view.findViewById(R.id.filter_spinner);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.score_filter_array, android.R.layout.simple_spinner_dropdown_item);
        mFilterSpinner.setAdapter(adapter);
        mFilterSpinner.setSelection(mFilter);
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilter = position - 1;
                if (mFilter > -1 && mScoreList != null && mScoreList.size() > 0) {
                    List<Score> list = new ArrayList<Score>();
                    for (int i = 0; i < mScoreList.size(); i++) {
                        Score score = mScoreList.get(i);
                        int state = score.getStatus();
                        if (state == mFilter) {
                            list.add(score);
                        }
                    }
                    mAdapter.setListScore(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.setListScore(mScoreList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void setContragentId(String id) {
        idContragent = id;
        Log.d("myLog", idContragent);
        new AddScoreTask().execute();
    }

    private void startScoreActivity(Score score){
        Intent intent = new Intent(getActivity(), ScoreActivity.class);
        intent.putExtra(Constants.INTENT_SCORE, score.getNumberAccount());
        startActivity(intent);
    }

    private class AddScoreTask extends AsyncTask<Void, Void, Void>{

        private String score;

        @Override
        protected Void doInBackground(Void... params) {
            score = ApiController.getInstance().saveScore(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), idContragent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AddScoreDialog dialog = new AddScoreDialog();
            dialog.init(idContragent, score, new AddScoreClickListener() {
                @Override
                public void onLeftBtnClick() {
                }
                @Override
                public void onRightBtnClick(Score score) {
                    mScore = score;
                    new SendScoreTask().execute();

                }
            });
            dialog.show(getActivity());
        }
    }

    private class SendScoreTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return ApiController.getInstance().updateScore(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), mScore, 0);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mScore != null) {
                mScoreList.add(mScore);
                for (int i = 0; i < mScoreList.size(); i++) {

                }
                mAdapter.setListScore(mScoreList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class GetScoreListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mScoreList = ApiController.getInstance().getScoreList(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mScoreList != null) {
                mAdapter.setListScore(mScoreList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
