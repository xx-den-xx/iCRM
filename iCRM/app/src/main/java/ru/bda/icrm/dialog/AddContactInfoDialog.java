package ru.bda.icrm.dialog;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.AddPhoneClickListener;
import ru.bda.icrm.model.Phone;

/**
 * Created by User on 04.11.2016.
 */

public class AddContactInfoDialog extends DialogFragment {

    private String[] data = {"Рабочий", "Мобильный"};
    private EditText mEtPhone;
    private Spinner mTypeSpinner;
    private int typeInt = 0;
    private Button mBtnLeft;
    private Button mBtnRight;
    private Phone mPhone = new Phone();
    private AddPhoneClickListener listener;

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
        View rootView = inflater.inflate(R.layout.dialog_add_contact_info, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View v) {
        mEtPhone = (EditText) v.findViewById(R.id.et_phone);
        mTypeSpinner = (Spinner) v.findViewById(R.id.type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
        mTypeSpinner.setSelection(0);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeInt = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBtnLeft = (Button) v.findViewById(R.id.btn_left);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnRight = (Button) v.findViewById(R.id.btn_right);
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEtPhone.getText().toString();
                if (!phone.equals("")) {
                    mPhone.setType(typeInt);
                    mPhone.setmNumber(mEtPhone.getText().toString());
                    listener.addPhoneClick(mPhone);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Вы не ввели номер телефона!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public AddContactInfoDialog init(AddPhoneClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }
}
