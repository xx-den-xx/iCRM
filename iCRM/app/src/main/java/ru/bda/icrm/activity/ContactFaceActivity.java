package ru.bda.icrm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import ru.bda.icrm.R;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Contact;

/**
 * Created by User on 04.11.2016.
 */

public class ContactFaceActivity extends AppCompatActivity {

    private Contact mContact = new Contact();
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private EditText mEtName;
    private EditText mEtRole;
    private EditText mEtComments;
    private EditText mEtWorkPhone;
    private EditText mEtEmail;
    private EditText mEtMobPhone;
    private ImageView mIvWorkPhone;
    private ImageView mIvMobPhone;
    private String mIdContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_face);
        mIdContact = getIntent().getStringExtra(Constants.INTENT_ID_CONTRAGENT);
        setToolbar();
        initContent();
        new GetContactTask().execute();
    }

    private void initContent() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtRole = (EditText) findViewById(R.id.et_role);
        mEtComments = (EditText) findViewById(R.id.et_comments);
        mEtWorkPhone = (EditText) findViewById(R.id.et_work_phone);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtMobPhone = (EditText) findViewById(R.id.et_mob_phone);
        mIvWorkPhone = (ImageView) findViewById(R.id.iv_work_phone);
        mIvMobPhone = (ImageView) findViewById(R.id.iv_mob_phone);
        mIvWorkPhone.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mIvMobPhone.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mIvWorkPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtWorkPhone.getText().toString();
                if (!phone.equals("")) {
                    Uri number = Uri.parse("tel:" + phone);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                }
            }
        });
        mIvMobPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtMobPhone.getText().toString();
                if (!phone.equals("")) {
                    Uri number = Uri.parse("tel:" + phone);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                }
            }
        });
    }

    private void setDataContent() {
        mEtName.setText(mContact.getName());
        mEtRole.setText(mContact.getRole());
        mEtComments.setText(mContact.getComments());
        mEtWorkPhone.setText(mContact.getWorkPhone());
        mEtEmail.setText(mContact.getWorkEmail());
        mEtMobPhone.setText(mContact.getMobilePhone());
        mToolbar.setTitle(mContact.getName());
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
        mToolbar.inflateMenu(R.menu.activity_contact_face);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    mContact.setName(mEtName.getText().toString());
                    mContact.setRole(mEtRole.getText().toString());
                    mContact.setComments(mEtComments.getText().toString());
                    mContact.setWorkPhone(mEtWorkPhone.getText().toString());
                    mContact.setWorkEmail(mEtEmail.getText().toString());
                    mContact.setMobilePhone(mEtMobPhone.getText().toString());
                    new SaveContactFace().execute();
                }
                return false;
            }
        });
    }

    private class GetContactTask extends AsyncTask<Void, Void, Contact>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Contact doInBackground(Void... params) {
            return ApiController.getInstance().getContact(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ContactFaceActivity.this), mIdContact);
        }

        @Override
        protected void onPostExecute(Contact contact) {
            super.onPostExecute(contact);
            mProgressBar.setVisibility(View.GONE);
            if (contact != null) {
                mContact = contact;
                setDataContent();
            }
        }
    }

    private class SaveContactFace extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return ApiController.getInstance().updateContact(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ContactFaceActivity.this), mContact, ""
            );
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
