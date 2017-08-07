package ru.bda.icrm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.icrm.R;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.dialog.MyDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Phone;

public class AddContragentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Context mContext;
    @Nullable
    @Bind(R.id.add_toolbar)
    Toolbar mToolbar;

    private Contragent mContragent;
    @Nullable
    @Bind(R.id.et_name)
    EditText mEtName;
    @Nullable
    @Bind(R.id.sp_type_organization)
    Spinner mSpTypeOrganization;
    @Nullable
    @Bind(R.id.sp_type_recognition)
    Spinner mSpTypeRecognition;
    @Nullable
    @Bind(R.id.et_inn)
    EditText mEtInn;
    @Nullable
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Nullable
    @Bind(R.id.et_code_pookpo)
    EditText mEtCodePookpo;
    @Nullable
    @Bind(R.id.et_jur_address)
    EditText mEtJurAddress;
    @Nullable
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Nullable
    @Bind(R.id.et_site)
    EditText mEtSite;
    @Nullable
    @Bind(R.id.fab_save)
    FloatingActionButton mFabSave;
    private ArrayAdapter<CharSequence> orgSpinAdapter;
    private ArrayAdapter<CharSequence>  recSpinAdapter;
    @Nullable
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contragent);
        ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        initContent();
        initSpinners();
        mContragent = new Contragent();
    }

    private void initSpinners() {
        orgSpinAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_organization, android.R.layout.simple_spinner_dropdown_item);
        mSpTypeOrganization.setAdapter(orgSpinAdapter);
        mSpTypeOrganization.setOnItemSelectedListener(this);
        mSpTypeOrganization.setSelection(1);

        recSpinAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_relation, android.R.layout.simple_spinner_dropdown_item);
        mSpTypeRecognition.setAdapter(recSpinAdapter);
        mSpTypeRecognition.setOnItemSelectedListener(this);
        mSpTypeRecognition.setSelection(0);
    }

    private void initContent() {
        mFabSave.setOnClickListener(v -> {
            MyDialog myDialog = new MyDialog();
            if (mEtName.getText().toString().equals("")) {
                String message = getResources().getString(R.string.error_empty_name_contragent);
                myDialog.init(getString(R.string.user_empty_error), message, null, getString(R.string.ok), new MyDialog.OnClickListener() {
                    @Override
                    public void onLeftBtnClick() {
                    }

                    @Override
                    public void onRightBtnClick() {
                    }
                });
                myDialog.show(AddContragentActivity.this);
            } else {
                String message = getString(R.string.dialog_save_message);
                myDialog.init(getString(R.string.dialog_save_title), message, getString(R.string.no), getString(R.string.ok), new MyDialog.OnClickListener() {

                    @Override
                    public void onLeftBtnClick() {

                    }

                    @Override
                    public void onRightBtnClick() {
                        initContragent();
                        new UpdateContragentTask().execute();
                    }
                });
                myDialog.show(AddContragentActivity.this);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initContragent() {
        mContragent.setNameContragent(mEtName.getText().toString());
        mContragent.setCode(mEtCode.getText().toString());
        mContragent.setInn(mEtInn.getText().toString());
        mContragent.setUrFace(String.valueOf(mSpTypeOrganization.getSelectedItemPosition()));
        mContragent.setRelations(String.valueOf(mSpTypeRecognition.getSelectedItemPosition()));
        mContragent.setCodePoOkpo(mEtCodePookpo.getText().toString());
        mContragent.setSite(mEtSite.getText().toString());
        mContragent.setEmail(mEtEmail.getText().toString());
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("Новый контрагент");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class UpdateContragentTask extends AsyncTask<Void, Void, Contragent> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Contragent doInBackground(Void... params) {
            Log.d("myLog", "start_update.....");
            List<Phone> phones = new ArrayList<>();
            mContragent.setPhones(phones);
            return ApiController.getInstance().addContragent(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, mContext),
                    "new", mContragent);
        }

        @Override
        protected void onPostExecute(Contragent result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {
                Intent intent = new Intent(AddContragentActivity.this, ContragentActivity.class);
                intent.putExtra(Constants.INTENT_ID_CONTRAGENT, result.getId());
                startActivity(intent);
                finish();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
