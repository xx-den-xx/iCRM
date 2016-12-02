package ru.bda.icrm.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.CallRecyclerAdapter;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Call;
import ru.bda.icrm.receiver.CallReceiver;

/**
 * Created by User on 31.08.2016.
 */
public class CallFragment extends Fragment {

    private RecyclerView mRvCall;
    private LinearLayoutManager mLayoutManager;
    private DBController mDBController;
    private CallRecyclerAdapter mAdapter;
    private List<Call> mListCall = new ArrayList<>();
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, null);
        mDBController = new DBController(getActivity());
        initContent(view);
        return view;
    }

    private void initContent(View v) {
        mRvCall = (RecyclerView) v.findViewById(R.id.rv_call);
        mAdapter = new CallRecyclerAdapter(mListCall);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvCall.setLayoutManager(mLayoutManager);
        mRvCall.setAdapter(mAdapter);
        mRvCall.setHasFixedSize(true);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetDbCallTask().execute();
    }

    private class GetDbCallTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mListCall = mDBController.getCallList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mListCall != null) {
                mAdapter.setListCall(mListCall);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
