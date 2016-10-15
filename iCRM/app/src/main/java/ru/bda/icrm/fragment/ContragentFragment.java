package ru.bda.icrm.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.ContragentRecyclerAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.json.ResponseParser;
import ru.bda.icrm.listener.OnContragentClickListener;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 29.06.2016.
 */
public class ContragentFragment extends Fragment implements OnContragentClickListener{

    private ProgressBar mProgressBar;
    private RecyclerView mAgentRV;
    private ContragentRecyclerAdapter mAgentAdapter;
    private LinearLayoutManager mLayoutManager;
    private DBController mDBController;
    private OnContragentClickListener contrListener;
    private ImageView mIvSearch;
    private EditText mEtSearch;
    private ImageView mIvCancel;
    private String search;
    private final Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {
            refreshList();
        }
    };

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
        mAgentAdapter.setOnContragentClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAgentRV.setLayoutManager(mLayoutManager);
        mAgentRV.setAdapter(mAgentAdapter);
        mAgentRV.setHasFixedSize(true);

        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mIvCancel = (ImageView) view.findViewById(R.id.iv_cancel);

        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtSearch.getVisibility() == View.GONE && mIvCancel.getVisibility() == View.GONE) {
                    mEtSearch.setVisibility(View.VISIBLE);
                    mIvCancel.setVisibility(View.VISIBLE);
                } else {
                    mEtSearch.setVisibility(View.GONE);
                    mIvCancel.setVisibility(View.GONE);
                }
            }
        });
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearch("");
                mEtSearch.setText("");
                mEtSearch.setVisibility(View.GONE);
                mIvCancel.setVisibility(View.GONE);
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDBController = new DBController(getActivity());
        if (AppControl.getInstance().isOnline(getActivity())) {
            new ContragentRequestTask().execute();
        } else {
            Toast.makeText(getActivity(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
            new AgentFromBDTask().execute();
        }
        return view;
    }

    private void setSearch(String text) {
        search = text;
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    private void refreshList() {
        List<Contragent> contragentList = mListContragent;
        List<Contragent> items = new ArrayList<>();
        for (Contragent contragent : contragentList) {
            if (search != null && search.length() > 0) {
                String agent = "";
                try {
                    agent = contragent.getNameContragent();
                }catch(NullPointerException e){}
                if (agent != null && agent.length() > 0) {
                    if (agent.toLowerCase().contains(search)) {
                        items.add(contragent);
                    }
                }
            } else {
                items.add(contragent);
            }
            mAgentAdapter.setAgentList(items);
            mAgentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContragentClick(String uid) {
        contrListener.onContragentClick(uid);
    }

    public void setOnContragentClickListener(OnContragentClickListener listener) {
        this.contrListener = listener;
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
                    .getContragent(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()))
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
