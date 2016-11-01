package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.AddScoreAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddContragentClickListener;
import ru.bda.icrm.listener.AddPriceClickListener;
import ru.bda.icrm.listener.AddScoreClickListener;
import ru.bda.icrm.listener.OnChangeCoastListener;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class AddScoreDialog extends DialogFragment {

    private AddScoreClickListener listener;
    private Button mBtnClient;
    private Button mBtnProduct;
    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView mTvCoast;
    private TextView mTvClient;
    private TextView mTvTitle;
    private Score mScore = new Score();
    private List<PriceSum> mPriceList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutMangaer;
    private AddScoreAdapter mAdapter;
    private String mIdContragent;
    private Contragent mContragent;
    private double mTotalScore;
    private String numberScore;

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
        View rootView = inflater.inflate(R.layout.dialog_add_score, container, false);
        initContent(rootView);
        new ContragentTask().execute();
        return rootView;
    }

    private void initContent(View view) {
        mLeftBtn = (Button) view.findViewById(R.id.btn_left);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftBtnClick();
                    mPriceList = null;
                }
                dismiss();
            }
        });
        mRightBtn = (Button) view.findViewById(R.id.btn_right);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && mContragent != null && mPriceList.size() > 0) {
                    listener.onRightBtnClick(getScore());
                    mPriceList = null;
                    dismiss();
                } else {

                }
            }
        });

        mBtnClient = (Button) view.findViewById(R.id.btn_client);
        mBtnClient.setVisibility(View.GONE);
        mBtnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContragentDialog dialog = new GetContragentDialog();
                dialog.init(new AddContragentClickListener() {
                    @Override
                    public void addContragentListener(Contragent contragent) {
                        mContragent = contragent;
                        mTvClient.setText(contragent.getNameContragent());
                    }
                });
                dialog.show(getActivity());
            }
        });
        mBtnProduct = (Button) view.findViewById(R.id.btn_products);
        mBtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPriceDialog dialog = new GetPriceDialog();
                dialog.init(new AddPriceClickListener() {
                    @Override
                    public void addPriceListener(PriceSum price) {
                        if (price != null) {
                            double totalCoast = 0;
                            boolean isHavePrice = true;
                            if (mPriceList != null && mPriceList.size() > 0) {
                                for (int i = 0; i < mPriceList.size(); i++) {
                                    PriceSum sum = mPriceList.get(i);
                                    if (!sum.getTitle().equals(price.getTitle())) {
                                        isHavePrice = false;
                                    } else {
                                        isHavePrice = true;
                                        break;
                                    }
                                }
                                if (!isHavePrice) mPriceList.add(price);
                            } else if (mPriceList.size() == 0) {
                                mPriceList.add(price);
                            }
                            mAdapter.setPriceList(mPriceList);
                            mAdapter.notifyDataSetChanged();
                            for (PriceSum sum : mPriceList) {
                                totalCoast = new BigDecimal(totalCoast + sum.getTotalCoast())
                                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                            mTvCoast.setText(totalCoast + "");
                        }
                    }
                });
                dialog.show(getActivity());
            }
        });

        mTvCoast = (TextView) view.findViewById(R.id.tv_coast);
        mTvClient = (TextView) view.findViewById(R.id.tv_client);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitle.setText("№" + numberScore);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_products);
        mLayoutMangaer = new LinearLayoutManager(getActivity());
        mAdapter = new AddScoreAdapter(mPriceList);
        mAdapter.setOnChangeCoastListener(new OnChangeCoastListener() {
            @Override
            public void onChangeCoast(PriceSum coast) {
                double totalCoast = 0;
                for (int i=0; i < mPriceList.size(); i++) {
                    PriceSum priceSum = mPriceList.get(i);
                    if (priceSum.getTitle().equals(coast.getTitle())) {
                        priceSum.setTotlalCoast(coast.getTotalCoast());
                        mPriceList.remove(i);
                        mPriceList.add(i, priceSum);
                    }
                }
                for (PriceSum priceSum : mPriceList) {
                    totalCoast = new BigDecimal(totalCoast + priceSum.getTotalCoast())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
                mTvCoast.setText(""+totalCoast);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutMangaer);
        mRecyclerView.setHasFixedSize(true);
    }

    private Score getScore() {
        mScore.setClient(mContragent);
        mScore.setDateAccount(Calendar.getInstance().getTimeInMillis());
        mScore.setProductList(mPriceList);
        mScore.setNumberAccount(numberScore);
        mScore.setSumScore(Double.parseDouble(mTvCoast.getText().toString()));
        Log.d("myLog", "Client ID = " + mContragent.getId() + "\n" + " date = " + mScore.getDateAccount()
                + "\n" + mScore.getProductList().toString());
        return mScore;
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public AddScoreDialog init (String idContragent, String numberScore, AddScoreClickListener listener) {
        this.numberScore = numberScore;
        this.mIdContragent = idContragent;
        this.listener = listener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener = null;
        mPriceList = null;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mPriceList = null;
    }

    private class ContragentTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mContragent = ApiController.getInstance().getContragent(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getActivity()), mIdContragent);
            boolean answer = mContragent != null ? true : false;
            return answer;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Log.d("myLog", mContragent.toString());
                mTvClient.setText(mContragent.getNameContragent());
                mTvTitle.setText("№" + numberScore);
            } else {
                Log.d("myLog", "ошибка получения контрагента");
            }
        }
    }
}
