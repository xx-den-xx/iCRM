package ru.bda.icrm.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.bda.icrm.R;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.dialog.AddClientDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddClientClickListener;
import ru.bda.icrm.model.Clients;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 01.09.2016.
 */
public class ContragentActivity extends AppCompatActivity {

    private String mUid;
    private Contragent mContragent;
    private Context mContext;
    private Toolbar mToolbar;
    private LinearLayout mLlMainContent;
    private ProgressBar mProgressBar;
    private TextView mTvJurFace;
    private TextView mTvFizFace;
    private EditText mEtClientName;
    private TextView mTvClientRelation;
    private LinearLayout mLlAddContactFace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contragent);
        mUid = getIntent().getStringExtra(Constants.INTENT_UID_CONTRAGENT);
        mContext = this;
        setToolbar();
        setContent();
        new ContragentTask().execute();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setContent() {
        mLlMainContent = (LinearLayout) findViewById(R.id.ll_main_content);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvJurFace = (TextView) findViewById(R.id.tv_jur_face);
        mTvFizFace = (TextView) findViewById(R.id.tv_fiz_face);
        mEtClientName = (EditText) findViewById(R.id.et_client_name);
        mTvClientRelation = (TextView) findViewById(R.id.et_client_relation);
        mLlAddContactFace = (LinearLayout) findViewById(R.id.ll_add_contact_face);
        mLlAddContactFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClientDialog dialog = new AddClientDialog();
                dialog.init(new AddClientClickListener() {
                    @Override
                    public void onLeftBtnClick() {

                    }

                    @Override
                    public void onRightBtnClick(Clients clients) {
                        if (clients != null) {

                        }
                    }
                });
                dialog.show(ContragentActivity.this);
            }
        });
    }

    private class ContragentTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLlMainContent.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mContragent = ApiController.getInstance().getContragent(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, mContext), mUid);
            boolean answer = mContragent != null ? true : false;
            return answer;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Log.d("myLog", mContragent.toString());
                mToolbar.setTitle(mContragent.getNameContragent());
                mProgressBar.setVisibility(View.GONE);
                mLlMainContent.setVisibility(View.VISIBLE);
                if (mContragent.getUrFace().equals("1")){
                    mTvJurFace.setTextColor(getResources().getColor(R.color.client_text_active));
                    mTvFizFace.setTextColor(getResources().getColor(R.color.client_text_inactive));
                } else {
                    mTvJurFace.setTextColor(getResources().getColor(R.color.client_text_inactive));
                    mTvFizFace.setTextColor(getResources().getColor(R.color.client_text_active));
                }
                mEtClientName.setText(mContragent.getNameContragent());
                mTvClientRelation.setText(mContragent.getRelations().equals("1") ? "Поставщик" : "Покупатель");
            } else {
                Log.d("myLog", "ошибка получения контрагента");
            }
        }
    }
}
