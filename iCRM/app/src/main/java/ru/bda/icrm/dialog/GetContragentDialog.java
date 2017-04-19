package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.view.adapters.ContragentRecyclerAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.SearchMode;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddContragentClickListener;
import ru.bda.icrm.listener.EndlessScrollListener;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 27.10.2016.
 */

public class GetContragentDialog extends DialogFragment {

    private Button mRightBtn;
    private RecyclerView mAgentRV;
    private ContragentRecyclerAdapter mAgentAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageView mIvSearch;
    private EditText mEtSearch;
    private ImageView mIvCancel;
    private int startProgressInt = 0;
    private int countProgressInt = 50;
    private SearchMode searchMode = SearchMode.LOAD;
    private List<Contragent> mListContragent = new ArrayList<>();
    private AddContragentClickListener addListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT ,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(root);
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_get_contragent, container, false);
        initContent(rootView);
        new ContragentRequestTask().execute();
        return rootView;
    }

    private void initContent(View view) {
        mRightBtn = (Button) view.findViewById(R.id.btn_right);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAgentRV = (RecyclerView) view.findViewById(R.id.agent_recycler_view);
        mAgentAdapter = new ContragentRecyclerAdapter(mListContragent);
        mAgentAdapter.addContragentClickListener(new AddContragentClickListener() {
            @Override
            public void addContragentListener(Contragent contragent) {
                addListener.addContragentListener(contragent);
                dismiss();
            }
        });
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

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public GetContragentDialog init(AddContragentClickListener addListener) {
        this.addListener = addListener;
        return this;
    }

    private class ContragentRequestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (mListContragent != null) {
                startProgressInt = 0;
                mAgentAdapter.setAgentList(mListContragent);
                mAgentAdapter.notifyDataSetChanged();
            }
        }
    }
}
