package ru.bda.icrm.view.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.icrm.R;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.presenter.LoginPresenter;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.receiver.TimeReceiver;
import ru.bda.icrm.services.NotificationService;
import ru.bda.icrm.view.LoginActivityView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginActivityView{

    @Bind(R.id.et_login)
    EditText mEtLogin;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.btn_ok)
    Button mBtnOk;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.im_logo)
    ImageView mIvLogo;
    @Bind(R.id.login_layout)
    LinearLayout mLoginLayout;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    private String sLogin = "";
    private String sPassword = "";
    private String hexLogin = "";
    private String hexPassword = "";
    private Context mContext;
    private int requestCode = 333;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppPref.getInstance().setNotifCount(0, this);
        //if (!isMyServiceRunning(NotificationService.class)) {
            //startService(new Intent(this, NotificationService.class));
        handleNotification();
        //}

        mContext = this;
        initContent();
        if (getIntent().getStringExtra(Constants.INTENT_EXIT) == null) {
            new LogoLayout().execute();
        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            mIvLogo.setVisibility(View.GONE);
        }
    }

    private void handleNotification() {
        Intent alarmIntent = new Intent(this, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initContent() {
        mToolbar.setTitle(R.string.app_name);
        mBtnOk.setOnClickListener(v -> onClick(v));
        mProgressBar.setVisibility(View.GONE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                sLogin = mEtLogin.getText().toString();
                sPassword = mEtPassword.getText().toString();
                if (!sLogin.equals("") && !sPassword.equals("")) {
                    hexPassword = getMD5(sPassword);
                    hexLogin = getMD5(sLogin);
                    loadPresenter(hexLogin, hexPassword);
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
            //digest.update(s.getBytes(Charset.forName("utf-8")),0,s.length());
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

    public void startApp() {
        if (!AppPref.getInstance().getStringPref(AppPref.PREF_HEX_LOGIN, mContext).equals("")
                && !AppPref.getInstance().getStringPref(AppPref.PREF_HEX_PASSWORD, mContext).equals("")) {
            hexLogin = AppPref.getInstance().getStringPref(AppPref.PREF_HEX_LOGIN, mContext);
            hexPassword = AppPref.getInstance().getStringPref(AppPref.PREF_HEX_PASSWORD, mContext);
            loadPresenter(hexLogin, hexPassword);
        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void loadPresenter(String login, String pass) {
        presenter = new LoginPresenter(this, login, pass);
        presenter.loadData();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveToken(Token token) {
        if (token.getToken().equals("null") || token.getManager().equals("null")) {
            Toast.makeText(mContext, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            return;
        }
        AppPref.getInstance().setHexAuth(sLogin, hexLogin, hexPassword, token.getManager(), mContext);
        AppPref.getInstance().setToken(token.getToken(), mContext);
        long time = AppPref.getInstance().getDate(mContext);

        if (time == 0) AppPref.getInstance().setDateAuth(Calendar.getInstance().getTimeInMillis(), mContext);

        Log.d("log_login", "Time = " + time + ";");
        if (presenter != null) presenter.onStop();
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(mContext, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
        mLoginLayout.setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.VISIBLE);
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
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED)
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.PROCESS_OUTGOING_CALLS,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.WRITE_CALL_LOG,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_NETWORK_STATE
                }, requestCode);
            } else {
                startApp();
            }
        }
    }
}
