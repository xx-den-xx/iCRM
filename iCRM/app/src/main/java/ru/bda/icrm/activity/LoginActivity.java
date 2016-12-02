package ru.bda.icrm.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.bda.icrm.R;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.yandex.yandexmapkit.utils.Utils;

/**
 * Created by User on 28.07.2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEtLogin;
    private EditText mEtPassword;
    private Button mBtnOk;
    private Toolbar mToolbar;
    private ImageView mIvLogo;
    private LinearLayout mLoginLayout;
    private ProgressBar mProgressBar;
    private String sLogin = "";
    private String sPassword = "";
    private String hexLogin = "";
    private String hexPassword = "";
    private Context mContext;
    private int requestCode = 333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContext = this;
        initContent();
        if (getIntent().getStringExtra(Constants.INTENT_EXIT) == null) {
            new LogoLayout().execute();
        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            mIvLogo.setVisibility(View.GONE);
        }
    }

    private void initContent() {
        mToolbar.setTitle(R.string.app_name);
        mEtLogin = (EditText) findViewById(R.id.et_login);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);
        mIvLogo = (ImageView) findViewById(R.id.im_logo);
        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                sLogin = mEtLogin.getText().toString();
                sPassword = mEtPassword.getText().toString();
                if (!sLogin.equals("") && !sPassword.equals("")) {
                    hexPassword = getMD5(sPassword);
                    hexLogin = getMD5(sLogin);
                    new AuthTask().execute();
                } else {
                    Toast.makeText(LoginActivity.this,
                            LoginActivity.this.getResources().getString(R.string.empty_field),
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    //функция для создания хэш строки в MD5
    public String getMD5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            //создаем hex-строку
            StringBuilder hexString = new StringBuilder();
            for (int i=0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            String hexResult = hexString.toString();
            return hexResult;
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCode) {
            startApp();
        } else {
            startApp();
        }
    }

    private void startApp() {
        if (!AppPref.getInstance().getStringPref(AppPref.PREF_HEX_LOGIN, mContext).equals("")
                && !AppPref.getInstance().getStringPref(AppPref.PREF_HEX_PASSWORD, mContext).equals("")) {
            hexLogin = AppPref.getInstance().getStringPref(AppPref.PREF_HEX_LOGIN, mContext);
            hexPassword = AppPref.getInstance().getStringPref(AppPref.PREF_HEX_PASSWORD, mContext);
            new AuthTask().execute();
        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    private class LogoLayout extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoginLayout.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mIvLogo.setVisibility(View.GONE);
            if (AppControl.getInstance().isMarshmallowDevice()
                    && (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED)) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS}, requestCode);
            } else {
                startApp();
            }
        }
    }

    private class AuthTask extends AsyncTask<Void, Void, Boolean> {

        String token = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            token = ApiController.getInstance().auth(hexLogin, hexPassword);
            if (!token.equals("error")) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            if (result) {
                AppPref.getInstance().setHexAuth(sLogin, hexLogin, hexPassword, mContext);
                AppPref.getInstance().setToken(token, mContext);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(mContext, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                mLoginLayout.setVisibility(View.VISIBLE);
                mToolbar.setVisibility(View.VISIBLE);
            }
        }
    }
}
