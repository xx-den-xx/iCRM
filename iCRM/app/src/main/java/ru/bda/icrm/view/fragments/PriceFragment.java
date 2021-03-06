package ru.bda.icrm.view.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.icrm.R;
import ru.bda.icrm.activity.ChildPriceActivity;
import ru.bda.icrm.adapter.RecyclerPriceAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.enums.SearchMode;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddPriceClickListener;
import ru.bda.icrm.listener.EndlessScrollListener;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.dto.TakeNomenclatureDTO;
import ru.bda.icrm.presenter.PriceFragmentPresenter;
import ru.bda.icrm.view.PriceFragmentView;

public class PriceFragment extends Fragment implements PriceFragmentView{

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.iv_search)
    ImageView mIvSearch;

    @Bind(R.id.et_search)
    EditText mEtSearch;

    @Bind(R.id.iv_cancel)
    ImageView mIvCancel;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private RecyclerPriceAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String mParentCode = "root";
    private List<PriceSum> mPriceList = new ArrayList<>();
    private int startProgressInt = 0;
    private int countProgressInt = 50;
    private SearchMode searchMode = SearchMode.LOAD;
    private String token;
    private PriceFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price, null);
        //getSHA1();
        ButterKnife.bind(this, view);
        presenter = new PriceFragmentPresenter(this);
        initContent();
        token = AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getContext());
        presenter.loadPrice(new TakeNomenclatureDTO(token, mParentCode, startProgressInt, countProgressInt));
        //new NomenclatureTask().execute();
        return view;
    }

    private void initContent() {
        mAdapter = new RecyclerPriceAdapter(mPriceList);
        mAdapter.addPriceClickListener(price -> {
            if (price.isGroup()) {
                Intent intent = new Intent(getActivity(), ChildPriceActivity.class);
                intent.putExtra(Constants.INTENT_PARENT_CODE, price.getCode());
                startActivity(intent);
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
                    if (totalItemsCount > countProgressInt)
                        presenter.loadPrice(new TakeNomenclatureDTO(token, mParentCode, startProgressInt, countProgressInt));
                    //new NomenclatureTask().execute();
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
            presenter.loadPrice(new TakeNomenclatureDTO(token, mParentCode, startProgressInt, countProgressInt));
            //new NomenclatureTask().execute();
            mEtSearch.setText("");
            mEtSearch.setVisibility(View.GONE);
            mIvCancel.setVisibility(View.GONE);
            searchMode = SearchMode.LOAD;
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
    }

    public void setParentCode(String mParentCode) {
        this.mParentCode = mParentCode;

    }

    private void setSearch(String text) {
        if (text.equals("")) {
            searchMode = SearchMode.LOAD;
            presenter.loadPrice(new TakeNomenclatureDTO(token, mParentCode, startProgressInt, countProgressInt));
            //new NomenclatureTask().execute();
        } else {
            searchMode = SearchMode.SEARCH;
            new SearchTask().execute(text);
        }
    }

    private void getSHA1() {
        PackageInfo info;
        try {

            info = getActivity().getPackageManager().getPackageInfo(
                    "ru.bda.icrm", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("myLog", something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void showError(String error) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mProgressBar, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadPriceList(List<PriceSum> list) {
        mProgressBar.setVisibility(View.GONE);
        if (list != null) {
            if (startProgressInt == 0) {
                mPriceList = list;
            } else {
                mPriceList.addAll(list);
            }
        }
        if (mPriceList != null) {
            startProgressInt += countProgressInt;
            mAdapter.setPriceList(mPriceList);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class NomenclatureTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<PriceSum> list = (ApiController.getInstance().getNomenclatureTree(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), mParentCode, startProgressInt, countProgressInt));

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
            mProgressBar.setVisibility(View.GONE);
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
            mProgressBar.setVisibility(View.VISIBLE);
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
            mProgressBar.setVisibility(View.GONE);
            if (mPriceList != null) {
                startProgressInt = 0;
                mAdapter.setPriceList(mPriceList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}