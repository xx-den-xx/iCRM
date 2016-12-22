package ru.bda.icrm.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.PhoneContragentAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.dialog.AddClientDialog;
import ru.bda.icrm.dialog.AddContactInfoDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddClientClickListener;
import ru.bda.icrm.listener.AddPhoneClickListener;
import ru.bda.icrm.listener.OnCallClickListener;
import ru.bda.icrm.model.Contact;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Phone;

/**
 * Created by User on 01.09.2016.
 */
public class ContragentActivity extends AppCompatActivity {

    private String mUid;
    private Contragent mContragent;
    private Contact mContact;
    private Context mContext;
    private Toolbar mToolbar;
    private LinearLayout mLlMainContent;
    private ProgressBar mProgressBar;
    private TextView mTvJurFace;
    private TextView mTvFizFace;
    private EditText mEtClientName;
    private EditText mEtJurAddress;
    private TextView mTvClientRelation;
    private TextView mTvContactFace;
    private LinearLayout mLlAddContactFace;
    private MenuItem mMapItem;
    private EditText mEtEmail;
    private EditText mEtSite;
    private FloatingActionButton mFab;
    private LinearLayout mLayoutContactInfo;

    private RecyclerView mRwPhone;
    private LinearLayoutManager mLayoutManager;
    private PhoneContragentAdapter mPhoneAdapter;
    private List<Phone> mPhoneList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contragent);
        mUid = getIntent().getStringExtra(Constants.INTENT_ID_CONTRAGENT);
        mContext = this;
        setToolbar();
        setContent();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        mToolbar.inflateMenu(R.menu.activity_contragent);
        mMapItem = (MenuItem) mToolbar.getMenu().findItem(R.id.action_maps);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mMapItem.getItemId() == R.id.action_maps) {
                    Intent intent = new Intent(ContragentActivity.this, MapActivity.class);
                    intent.putExtra(Constants.INTENT_ID_CONTRAGENT, mContragent.getId());
                    startActivity(intent);
                }
                return false;
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
        mTvContactFace = (TextView) findViewById(R.id.tv_contact_face);
        mEtEmail = (EditText)  findViewById(R.id.et_email);
        mEtSite = (EditText) findViewById(R.id.et_site);
        mEtJurAddress = (EditText) findViewById(R.id.et_jur_address);

        mLlAddContactFace = (LinearLayout) findViewById(R.id.ll_add_contact_face);
        mLlAddContactFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContragent.getIdContact().equals("")) {
                    AddClientDialog dialog = new AddClientDialog();
                    dialog.init(new AddClientClickListener() {
                        @Override
                        public void onLeftBtnClick() {

                        }

                        @Override
                        public void onRightBtnClick(Contact clients) {
                            if (clients != null) {
                                mContact = clients;
                                mContact.setId(mContragent.getIdContact());
                                mTvContactFace.setText(clients.getName());
                                new UpdateContactTask().execute();
                            }
                        }
                    });
                    dialog.show(ContragentActivity.this);
                } else {
                    Intent intent = new Intent(ContragentActivity.this, ContactFaceActivity.class);
                    intent.putExtra(Constants.INTENT_ID_CONTRAGENT, mContragent.getIdContact());
                    startActivity(intent);
                }
            }
        });

        mLayoutContactInfo = (LinearLayout) findViewById(R.id.layout_contact_info);
        mLayoutContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactInfoDialog dialog = new AddContactInfoDialog();
                dialog.init(new AddPhoneClickListener() {
                    @Override
                    public void addPhoneClick(Phone phone) {
                        Phone mPhone = phone;
                        mPhone.setContactsId(mContragent.getId());
                        new AddPhoneTask().execute(mPhone);
                    }
                });
                dialog.show(ContragentActivity.this);
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContragent.setNameContragent(mEtClientName.getText().toString());
                mContragent.setPhones(mPhoneList);
                mContragent.setEmail(mEtEmail.getText().toString());
                mContragent.setSite(mEtSite.getText().toString());
                mContragent.setJurAddress(mEtJurAddress.getText().toString());
                new UpdateContragentTask().execute(mContragent);
            }
        });
    }

    private void initRecyclerView() {
        mRwPhone = (RecyclerView) findViewById(R.id.rw_phone);
        mLayoutManager = new LinearLayoutManager(this);
        mPhoneAdapter = new PhoneContragentAdapter(this, mPhoneList);
        mPhoneAdapter.setOnCallClickListener(new OnCallClickListener() {
            @Override
            public void onCallClick(String phone) {
                Uri number = Uri.parse("tel:" + phone);
                Intent intentCall = new Intent(Intent.ACTION_DIAL, number);
                startActivity(intentCall);
            }
        });
        mRwPhone.setAdapter(mPhoneAdapter);
        mRwPhone.setLayoutManager(mLayoutManager);
        mRwPhone.setHasFixedSize(true);
    }

    private class ContragentTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLlMainContent.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
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
                if (mContragent.getUrFace().equals("1")) {
                    mTvJurFace.setTextColor(getResources().getColor(R.color.client_text_active));
                    mTvFizFace.setTextColor(getResources().getColor(R.color.client_text_inactive));
                } else {
                    mTvJurFace.setTextColor(getResources().getColor(R.color.client_text_inactive));
                    mTvFizFace.setTextColor(getResources().getColor(R.color.client_text_active));
                }
                mEtClientName.setText(mContragent.getNameContragent());
                mEtEmail.setText(mContragent.getEmail());
                mEtSite.setText(mContragent.getSite());
                if (mContragent.getContacts() != null)
                    if (!mContragent.getContacts().equals("")) mTvContactFace.setText(mContragent.getContacts());
                mTvClientRelation.setText(mContragent.getRelations().equals("1") ? "Поставщик" : "Покупатель");
                if (mContragent.getPhones() != null && mContragent.getPhones().size() > 0) {
                    mPhoneList = mContragent.getPhones();
                    mPhoneAdapter.setPhoneList(mPhoneList);
                    mPhoneAdapter.notifyDataSetChanged();
                }
            } else {
                Log.d("myLog", "ошибка получения контрагента");
            }
        }
    }

    private class UpdateContactTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return ApiController.getInstance().updateContact(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ContragentActivity.this), mContact, mContragent.getId());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class UpdateContragentTask extends AsyncTask<Contragent, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Contragent... params) {
             ApiController.getInstance().addContragent(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ContragentActivity.this),
                     params[0].getId(), params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class AddPhoneTask extends AsyncTask<Phone, Void, Boolean> {

        private Phone phone;

        @Override
        protected Boolean doInBackground(Phone... params) {
            phone = params[0];
            return ApiController.getInstance().addPhone(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ContragentActivity.this), params[0]
            );
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //if (aBoolean) {
                mPhoneList.add(phone);
                mPhoneAdapter.setPhoneList(mPhoneList);
                mPhoneAdapter.notifyDataSetChanged();
            //}
        }
    }
}
