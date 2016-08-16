package ru.bda.icrm.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.ContragentRecyclerAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.json.ResponseParser;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 29.06.2016.
 */
public class ContragentFragment extends Fragment {

    private ProgressBar mProgressBar;
    private RecyclerView mAgentRV;
    private ContragentRecyclerAdapter mAgentAdapter;
    private LinearLayoutManager mLayoutManager;
    private DBController mDBController;

    private List<Contragent> mListContragent = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contragent, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mAgentRV = (RecyclerView) view.findViewById(R.id.agent_recycler_view);
        mAgentAdapter = new ContragentRecyclerAdapter(mListContragent);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAgentRV.setLayoutManager(mLayoutManager);
        mAgentRV.setAdapter(mAgentAdapter);
        mAgentRV.setHasFixedSize(true);

        mDBController = new DBController(getActivity());
        if (isOnline()) {
            new ContragentRequestTask().execute();
        } else {
            Toast.makeText(getActivity(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
            new AgentFromBDTask().execute();
        }
        return view;
    }

    private boolean isOnline () {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    private class ContragentRequestTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean answerUrl = ApiController.getInstance()
                    .addContragent(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()))
                    .equals("error") ? false : true;
            mListContragent = ResponseParser.sContragentList;
            return answerUrl;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
            if (mListContragent != null) mAgentAdapter.setAgentList(mListContragent);
            mAgentAdapter.notifyDataSetChanged();
            new AgentFromBDTask().execute();
        }
    }

    private class AgentFromBDTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mDBController.insertContragentDB(mListContragent);
            mListContragent = new ArrayList<>();
            mListContragent = mDBController.getContragentList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAgentAdapter.setAgentList(mListContragent);
            mAgentAdapter.notifyDataSetChanged();
            if (ResponseParser.sContragentList != null) ResponseParser.sContragentList.clear();
        }
    }
}
