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
import ru.bda.icrm.adapter.RecyclerPriceAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.SearchMode;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddPriceClickListener;
import ru.bda.icrm.listener.EndlessScrollListener;
import ru.bda.icrm.model.PriceSum;

/**
 * Created by User on 27.10.2016.
 */

public class GetPriceDialog extends DialogFragment {

    private RecyclerView mRecyclerView;
    private RecyclerPriceAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageView mIvSearch;
    private EditText mEtSearch;
    private ImageView mIvCancel;
    private Button mOkBtn;
    private List<PriceSum> mPriceList = new ArrayList<>();
    private int startProgressInt = 0;
    private int countProgressInt = 50;
    private SearchMode searchMode = SearchMode.LOAD;

    private AddPriceClickListener priceListener;

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
        View rootView = inflater.inflate(R.layout.dialog_get_price, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerPriceAdapter(mPriceList);
        mAdapter.addPriceClickListener(new AddPriceClickListener() {
            @Override
            public void addPriceListener(PriceSum price) {
                if (priceListener != null) {
                    price.setTotlalCoast(price.getPrice());
                    priceListener.addPriceListener(price);
                }
                dismiss();
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (searchMode == SearchMode.LOAD) {
                    new NomenclatureTask().execute();
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
                new NomenclatureTask().execute();
                mEtSearch.setText("");
                mEtSearch.setVisibility(View.GONE);
                mIvCancel.setVisibility(View.GONE);
                searchMode = SearchMode.LOAD;
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMode = SearchMode.SEARCH;
                Log.d("myLog", s.toString());
                setSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mOkBtn = (Button) view.findViewById(R.id.btn_ok);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        new NomenclatureTask().execute();
    }

    private void setSearch(String text) {
        if (text.equals("")) {
            searchMode = SearchMode.LOAD;
            new NomenclatureTask().execute();
        } else {
            searchMode = SearchMode.SEARCH;
            new SearchTask().execute(text);
        }
    }


    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public GetPriceDialog init(AddPriceClickListener priceListener) {
        this.priceListener = priceListener;
        return this;
    }

    private class NomenclatureTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<PriceSum> list = (ApiController.getInstance().getNomenclatureSum(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), startProgressInt, countProgressInt));
            if (list != null) {
                if (startProgressInt == 0) {
                    mPriceList = list;
                } else {
                    mPriceList.addAll(list);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mPriceList != null) {
                startProgressInt += countProgressInt;
                mAdapter.setPriceList(mPriceList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class SearchTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            List<PriceSum> list = ApiController.getInstance().searchNomenclatureSum(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), params[0]);
            if (list != null) {
                mPriceList = list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mPriceList != null) {
                startProgressInt = 0;
                mAdapter.setPriceList(mPriceList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
