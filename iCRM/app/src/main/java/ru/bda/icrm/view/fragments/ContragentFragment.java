package ru.bda.icrm.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.icrm.R;
import ru.bda.icrm.view.adapters.ContragentRecyclerAdapter;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.enums.SearchMode;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.EndlessScrollListener;
import ru.bda.icrm.listener.OnContragentClickListener;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentListDTO;
import ru.bda.icrm.presenter.ContragentFragmentPresenter;
import ru.bda.icrm.view.ContragentFragmentView;

public class ContragentFragment extends Fragment implements OnContragentClickListener, ContragentFragmentView{

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.agent_recycler_view)
    RecyclerView mAgentRV;
    @Bind(R.id.iv_search)
    ImageView mIvSearch;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.iv_cancel)
    ImageView mIvCancel;

    private ContragentRecyclerAdapter mAgentAdapter;
    private LinearLayoutManager mLayoutManager;
    private DBController mDBController;
    private OnContragentClickListener contrListener;

    private int startProgressInt = 0;
    private int countProgressInt = 50;
    private SearchMode searchMode = SearchMode.LOAD;

    private ContragentFragmentPresenter presenter;

    private List<Contragent> mListContragent = new ArrayList<>();
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contragent, null);
        ButterKnife.bind(this, view);
        token = AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity());
        presenter = new ContragentFragmentPresenter(this, token);
        mAgentAdapter = new ContragentRecyclerAdapter(mListContragent);
        mAgentAdapter.setOnContragentClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAgentRV.setLayoutManager(mLayoutManager);
        mAgentRV.setAdapter(mAgentAdapter);
        mAgentRV.setHasFixedSize(true);
        mAgentRV.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("log_contragent", "page = " + page + "; totalItemCount = " + totalItemsCount);
                if (searchMode == SearchMode.LOAD) {
                    presenter.loadData(new TakeContragentListDTO(token, startProgressInt, countProgressInt));
                }
            }
        });

        mIvSearch.setOnClickListener(v -> {
            if (mEtSearch.getVisibility() == View.GONE && mIvCancel.getVisibility() == View.GONE) {
                mEtSearch.setVisibility(View.VISIBLE);
                mIvCancel.setVisibility(View.VISIBLE);
            } else {
                mEtSearch.setVisibility(View.GONE);
                mIvCancel.setVisibility(View.GONE);
            }
        });

        mIvCancel.setOnClickListener(v -> {
            setSearch("");
            mEtSearch.setText("");
            mEtSearch.setVisibility(View.GONE);
            mIvCancel.setVisibility(View.GONE);
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppControl.getInstance().isOnline(getActivity())) {
            presenter.loadData(new TakeContragentListDTO(token, startProgressInt, countProgressInt));
        } else {
            Toast.makeText(getActivity(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
        }
    }

    private void setSearch(String text) {
        if (text.equals("")) {
            searchMode = SearchMode.LOAD;
            presenter.loadData(new TakeContragentListDTO(token, startProgressInt, countProgressInt));
        } else {
            searchMode = SearchMode.SEARCH;
            presenter.searchData(new SearchContragentDTO(token, text));
        }
    }

    @Override
    public void onContragentClick(String uid) {
        contrListener.onContragentClick(uid);
    }

    public void setOnContragentClickListener(OnContragentClickListener listener) {
        this.contrListener = listener;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mProgressBar, error, Snackbar.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadList(List<Contragent> list) {
        if (list != null) {
            if (startProgressInt == 0) {
                mListContragent = list;
            } else {
                mListContragent.addAll(list);
            }
            startProgressInt += countProgressInt;
            mAgentAdapter.setAgentList(mListContragent);
            mAgentAdapter.notifyDataSetChanged();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void searchList(List<Contragent> list) {
        mProgressBar.setVisibility(View.GONE);
        if (list != null) {
            mListContragent = list;
            startProgressInt = 0;
            mAgentAdapter.setAgentList(mListContragent);
            mAgentAdapter.notifyDataSetChanged();
        }
    }
}
