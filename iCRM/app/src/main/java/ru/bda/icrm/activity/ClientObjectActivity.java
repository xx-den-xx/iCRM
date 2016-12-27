package ru.bda.icrm.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.PhotoRecyclerAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.OnPhotoClickListener;
import ru.bda.icrm.model.ClientObject;
import ru.bda.icrm.model.Photo;

/**
 * Created by User on 22.12.2016.
 */

public class ClientObjectActivity extends AppCompatActivity {

    private static final int MAX_PICTURE_WIDTH = 600;
    private Toolbar mToolbar;
    private EditText mEtObjectName;
    private EditText mEtObjectContact;
    private EditText mEtObjectComments;
    private EditText mEtObjectAddress;
    private EditText mEtObjectPhone;
    private ImageView ivCall;
    private LinearLayout mLlPhoto;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;
    private MenuItem mMapItem;

    private RecyclerView mRvPhoto;
    private StaggeredGridLayoutManager mLMPhoto;
    private PhotoRecyclerAdapter mPhotoAdapter;
    private List<Photo> mPhotoList = new ArrayList<>();
    private ClientObject mObject = new ClientObject();
    private Bitmap mBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_object);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Карточка объекта");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.inflateMenu(R.menu.activity_contragent);
        mMapItem = (MenuItem) mToolbar.getMenu().findItem(R.id.action_maps);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mMapItem.getItemId() == R.id.action_maps) {
                    Intent intent = new Intent(ClientObjectActivity.this, MapActivity.class);
                    intent.putExtra(Constants.INTENT_ID_CONTRAGENT, String.valueOf(mObject.getId()));
                    intent.putExtra(Constants.INTENT_MAP_TYPE, Constants.MAP_TYPE_OBJECT);
                    startActivity(intent);
                }
                return false;
            }
        });

        if (getIntent() != null) {
            mObject.setId(getIntent().getIntExtra(Constants.INTENT_OBJECT_ID, 0));
        }
        initContent();
        new GetObjectTask().execute(mObject.getId());
    }

    private void initContent() {
        mEtObjectName = (EditText) findViewById(R.id.et_object_name);
        mEtObjectContact = (EditText) findViewById(R.id.et_object_contact);
        mEtObjectComments = (EditText) findViewById(R.id.et_object_comments);
        mEtObjectAddress = (EditText) findViewById(R.id.et_object_adress);
        mEtObjectPhone = (EditText) findViewById(R.id.et_object_phone);
        mEtObjectPhone.setText("+7");
        ivCall = (ImageView) findViewById(R.id.iv_call);
        ivCall.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtObjectPhone.getText().toString();
                if (!phone.equals("")) {
                    Uri number = Uri.parse("tel:" + phone);
                    Intent intentCall = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ClientObjectActivity.this, "Поле телефона не должно быть пустым!!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mLlPhoto = (LinearLayout) findViewById(R.id.layout_photo);
        mLlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST);
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObject.setName(mEtObjectName.getText().toString());
                mObject.setPhone(mEtObjectPhone.getText().toString());
                mObject.setContact(mEtObjectContact.getText().toString());
                mObject.setComments(mEtObjectComments.getText().toString());
                mObject.setAddress(mEtObjectAddress.getText().toString());
                new UpdateObjectTask().execute(mObject);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRvPhoto = (RecyclerView) findViewById(R.id.rw_photo);
        initRvPhoto(mPhotoList);
    }

    private void initRvPhoto(List<Photo> photos) {
        mLMPhoto = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRvPhoto.setLayoutManager(mLMPhoto);
        mPhotoAdapter = new PhotoRecyclerAdapter(this, photos);
        mPhotoAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onPhotoClick(Photo photo) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(photo.getLink()), "image/*");
                startActivity(intent);
            }
        });
        mRvPhoto.setAdapter(mPhotoAdapter);
        mRvPhoto.setHasFixedSize(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("my-log", "onActivityResult");

        if(resultCode == RESULT_OK){
            Bitmap tmpBitmap = null;
            switch(requestCode) {
                case Constants.CAMERA_REQUEST:
                    tmpBitmap = (Bitmap) data.getExtras().get("data");
                    this.mBitmap = getResizedBitmap(tmpBitmap);
                    new SendPhotoTask().execute(this.mBitmap);
            }
        } else {
            Log.d("mylog", "Canceled");
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        if(width > MAX_PICTURE_WIDTH) {
            int height = bm.getHeight();
            float aspectRatio = (float) width / height;
            float newWidth = (float) Math.min(width, MAX_PICTURE_WIDTH);
            float newHeight = newWidth / aspectRatio;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);
            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        } else {
            return bm;
        }
    }

    private void setContent(ClientObject object) {
        mEtObjectName.setText(object.getName());
        mEtObjectContact.setText(object.getContact());
        mEtObjectComments.setText(object.getComments());
        mEtObjectAddress.setText(object.getAddress());
        mEtObjectPhone.setText(object.getPhone());
        if (object.getPhotoUrl() != null && object.getPhotoUrl().size() > 0) {
            mPhotoList.addAll(object.getPhotoUrl());
            mPhotoAdapter.setPhotoList(mPhotoList);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    private class SendPhotoTask extends AsyncTask<Bitmap, Void, Void> {

        private Photo photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            photo = ApiController.getInstance().postUploadPicture(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ClientObjectActivity.this),
                    mObject.getId(), params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (photo != null) {
                mPhotoList.add(photo);
                mPhotoAdapter.setPhotoList(mPhotoList);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    }

    private class GetObjectTask extends AsyncTask<Integer, Void, ClientObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ClientObject doInBackground(Integer... params) {
            return ApiController.getInstance().getObject(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ClientObjectActivity.this), params[0]
            );
        }

        @Override
        protected void onPostExecute(ClientObject object) {
            super.onPostExecute(object);
            if (object != null) {
                setContent(object);
            } else {
                Toast.makeText(ClientObjectActivity.this, "Ошибка получения объекта!!!", Toast.LENGTH_LONG).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class UpdateObjectTask extends AsyncTask<ClientObject, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ClientObject... params) {
            return ApiController.getInstance().updateObject(
                    AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, ClientObjectActivity.this),
                    params[0]
            );
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
                Toast.makeText(ClientObjectActivity.this, "Объект сохранён", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ClientObjectActivity.this, "Ошибка обновления объекта!!!", Toast.LENGTH_LONG).show();
        }
    }
}
