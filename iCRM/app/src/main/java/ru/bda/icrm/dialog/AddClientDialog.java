package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.AddClientClickListener;
import ru.bda.icrm.model.Clients;
import ru.bda.icrm.model.Phone;

/**
 * Created by User on 13.09.2016.
 */
public class AddClientDialog extends DialogFragment{

    private TextView mTvTitle;
    private EditText mEtName;
    private EditText mEtRole;
    private EditText mEtComments;
    private EditText mEtWorkPhone;
    private EditText mEtMobPhone;
    private EditText mEtEmail;
    private Button mBtnLeft;
    private Button mBtnRight;
    private Clients mContacts;
    private Phone mWorkPhone;
    private Phone mMobPhone;
    private AddClientClickListener clickListener;


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
        View rootView = inflater.inflate(R.layout.dialog_add_client, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitle.setText(getActivity().getResources().getString(R.string.add_contacts_title));
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtRole = (EditText) view.findViewById(R.id.et_role);
        mEtComments = (EditText) view.findViewById(R.id.et_comments);
        mEtWorkPhone = (EditText) view.findViewById(R.id.et_work_phone);
        mEtMobPhone = (EditText) view.findViewById(R.id.et_mob_phone);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mBtnLeft = (Button) view.findViewById(R.id.btn_left);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onLeftBtnClick();
                }
                dismiss();
            }
        });
        mBtnRight = (Button) view.findViewById(R.id.btn_right);
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null && !mEtName.getText().toString().equals("")) {
                    mContacts = new Clients();
                    mContacts.setName(mEtName.getText().toString());
                    mContacts.setRole(mEtRole.getText().toString());
                    mContacts.setComments(mEtComments.getText().toString());
                    mWorkPhone = new Phone();
                    mWorkPhone.setType(0);
                    mWorkPhone.setmNumber(mEtWorkPhone.getText().toString());
                    mContacts.setWorkPhone(mWorkPhone);
                    mMobPhone = new Phone();
                    mMobPhone.setType(1);
                    mMobPhone.setmNumber(mEtMobPhone.getText().toString());
                    mContacts.setMobilePhone(mMobPhone);
                    mContacts.setWorkEmail(mEtEmail.getText().toString());
                    clickListener.onRightBtnClick(mContacts);
                } else if (mEtName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Поле \"Фамилия, имя и отчество\" не может быть пустым!!!", Toast.LENGTH_SHORT).show();
                    clickListener.onRightBtnClick(null);
                }
                dismiss();
            }
        });
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public AddClientDialog init(AddClientClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        clickListener = null;
    }
}
