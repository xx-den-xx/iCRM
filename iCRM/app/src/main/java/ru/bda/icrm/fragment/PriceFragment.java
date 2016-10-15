package ru.bda.icrm.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.RecyclerPriceAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.EndlessScrollListener;
import ru.bda.icrm.model.Price;

/**
 * Created by User on 31.08.2016.
 */
public class PriceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerPriceAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar mProgressBar;
    private List<Price> mPriceList = new ArrayList<>();
    private int startProgressInt = 0;
    private int countProgressInt = 50;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price, null);
        //getSHA1();
        initContent(view);
        new NomenclatureTask().execute();
        return view;
    }

    private void initContent(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerPriceAdapter(mPriceList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new NomenclatureTask().execute();
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
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

    private class NomenclatureTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Price> list = new ArrayList<>();
            list = (ApiController.getInstance().getNomenclature(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), startProgressInt, countProgressInt));
            if (list != null) {
                mPriceList.addAll(list);
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
            Log.d("myLog", mPriceList.toString());
        }
    }
}