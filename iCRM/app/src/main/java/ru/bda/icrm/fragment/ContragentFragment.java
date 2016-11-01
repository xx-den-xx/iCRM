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
import android.util.Log;
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
import ru.bda.icrm.enums.SearchMode;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.json.ResponseParser;
import ru.bda.icrm.listener.EndlessScrollListener;
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
    private int startProgressInt = 0;
    private int countProgressInt = 50;
    private SearchMode searchMode = SearchMode.LOAD;

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
        mAgentRV.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (searchMode == SearchMode.LOAD) {
                    new ContragentRequestTask().execute();
                }
            }
        });

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
                searchMode = SearchMode.SEARCH;
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
            //new AgentFromBDTask().execute();
        }
        return view;
    }

    private void setSearch(String text) {
        if (text.equals("")) {
            searchMode = SearchMode.LOAD;
            new ContragentRequestTask().execute();
        } else {
            searchMode = SearchMode.SEARCH;
            new ContragentSearchTask().execute(text);
        }
    }

    @Override
    public void onContragentClick(String uid) {
        contrListener.onContragentClick(uid);
    }

    public void setOnContragentClickListener(OnContragentClickListener listener) {
        this.contrListener = listener;
    }

    private class ContragentRequestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Contragent> list = ApiController.getInstance()
                    .getContragentList(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()),
                        startProgressInt, countProgressInt);
            if (list != null) {
                if (startProgressInt == 0) {
                    mListContragent = list;
                } else {
                    mListContragent.addAll(list);
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
            if (mListContragent != null) {
                startProgressInt += countProgressInt;
                mAgentAdapter.setAgentList(mListContragent);
                mAgentAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ContragentSearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            List<Contragent> list = ApiController.getInstance()
                    .searchContragent(AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), params[0]);
            if (list != null) {
                mListContragent = list;
                Log.d("myLog", mListContragent.toString());
            }
            Log.d("myLog", mListContragent.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
            if (mListContragent != null) {
                startProgressInt = 0;
                mAgentAdapter.setAgentList(mListContragent);
                mAgentAdapter.notifyDataSetChanged();
            }
        }
    }
}
