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
import android.widget.Toast;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.AddObjectClickListener;
import ru.bda.icrm.model.ClientObject;

/**
 * Created by User on 22.12.2016.
 */

public class AddObjectDialog extends DialogFragment {

    private AddObjectClickListener clickListener;
    private EditText mEtName;
    private Button mButtonLeft;
    private Button mButtonRight;

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
        View rootView = inflater.inflate(R.layout.dialog_add_object, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View rootView) {
        mEtName = (EditText) rootView.findViewById(R.id.et_name);
        mButtonLeft = (Button) rootView.findViewById(R.id.btn_left);
        mButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mButtonRight = (Button) rootView.findViewById(R.id.btn_right);
        mButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                if (!name.equals("")) {
                    ClientObject object = new ClientObject();
                    object.setName(name);
                    clickListener.addClickListener(object);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Наименование объекта не может быть пустым", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public AddObjectDialog init(AddObjectClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        clickListener = null;
    }
}
