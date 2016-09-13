package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.bda.icrm.R;

/**
 * Created by User on 07.09.2016.
 */
public class MyDialog extends DialogFragment {

    private String titleText;
    private String text;
    private String btnLeftText;
    private String btnRightText;
    private OnClickListener clickListener;
    private boolean isFullWidthInLandscapeMode;

    public void setFullWidthInLandscapeMode() {
        isFullWidthInLandscapeMode = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT ,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_text, container, false);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        TextView txt = (TextView) rootView.findViewById(R.id.text);

        Button btnLeft = (Button) rootView.findViewById(R.id.btn_left);
        Button btnRight = (Button) rootView.findViewById(R.id.btn_right);
        View divider3 = rootView.findViewById(R.id.divider3);
        View divider4 = rootView.findViewById(R.id.divider4);

        txtTitle.setText(titleText);
        txt.setText(text);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( clickListener!=null){
                    clickListener.onLeftBtnClick();
                }
                dismiss();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( clickListener!=null){
                    clickListener.onRightBtnClick();
                }
                dismiss();

            }
        });

        if (btnLeftText == null) {
            btnLeft.setVisibility(View.GONE);
            divider3.setVisibility(View.GONE);
            divider4.setVisibility(View.GONE);
        }
        return rootView;
    }

    public MyDialog init(String titleText, String text,
                         String btnLeftText, String btnRightText, OnClickListener onClickListener) {
        this.titleText = titleText;
        this.text = text;
        this.btnLeftText = btnLeftText;
        this.btnRightText = btnRightText;
        this.clickListener = onClickListener;
        return this;
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public void cancel() {
        dismiss();
    }

    public void cancelWithoutAnimation() {
        if (getDialog() != null) {
            getDialog().getWindow().getAttributes().alpha = 1.0f;
            getDialog().getWindow().getAttributes().windowAnimations = 0;
        }
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        clickListener = null;
    }

    public interface OnClickListener {
        void onLeftBtnClick();
        void onRightBtnClick();
    }
}
