package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.bda.icrm.R;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.listener.OnPriorityClickListener;

/**
 * Created by User on 03.11.2016.
 */

public class PriorityDialog extends DialogFragment implements View.OnClickListener{

    private TextView mTvLow;
    private TextView mTvMedium;
    private TextView mTvHigh;
    private TextView mTvTitle;
    private LinearLayout mLayoutLow;
    private LinearLayout mLayoutMedium;
    private LinearLayout mLayoutHigh;
    private String title;
    private int mState;
    private String[] stateArray;
    private OnPriorityClickListener priorityListener;

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
        View rootView = inflater.inflate(R.layout.dialog_priority_layout, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View view) {
        mTvLow = (TextView) view.findViewById(R.id.tv_low);
        mTvMedium = (TextView) view.findViewById(R.id.tv_medium);
        mTvHigh = (TextView) view.findViewById(R.id.tv_high);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mLayoutLow = (LinearLayout) view.findViewById(R.id.layout_low);
        mLayoutLow.setOnClickListener(this);
        mLayoutMedium = (LinearLayout) view.findViewById(R.id.layout_medium);
        mLayoutMedium.setOnClickListener(this);
        mLayoutHigh = (LinearLayout) view.findViewById(R.id.layout_high);
        mLayoutHigh.setOnClickListener(this);
        setTextStatusContent();
        setBackground();
    }

    private void setBackground() {
        Resources res = getActivity().getResources();
        int active = res.getColor(R.color.client_background);
        int inactive = res.getColor(R.color.cardview_light_background);
        switch(mState) {
            case 0:
                mLayoutLow.setBackgroundColor(active);
                mLayoutMedium.setBackgroundColor(inactive);
                mLayoutHigh.setBackgroundColor(inactive);
                return;
            case 1:
                mLayoutLow.setBackgroundColor(inactive);
                mLayoutMedium.setBackgroundColor(active);
                mLayoutHigh.setBackgroundColor(inactive);
                return;
            case 2:
                mLayoutLow.setBackgroundColor(inactive);
                mLayoutMedium.setBackgroundColor(inactive);
                mLayoutHigh.setBackgroundColor(active);
                return;
        }
    }

    private void setTextStatusContent() {
        mTvTitle.setText(title);
        mTvLow.setText(stateArray[0]);
        mTvMedium.setText(stateArray[1]);
        mTvHigh.setText(stateArray[2]);
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public PriorityDialog init(String title, String[] priority, int state, OnPriorityClickListener listener) {
        this.title = title;
        this.stateArray = priority;
        mState = state;
        priorityListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int state = 0;
        if (v.getId() == R.id.layout_low){
            state = 0;
        } else if (v.getId() == R.id.layout_medium) {
            state = 1;
        } else if (v.getId() == R.id.layout_high) {
            state = 2;
        }
        state = title.equals(Constants.PRIORITY_TITLE) ? state : state - 1;
        priorityListener.onPriorityClick(state);
        dismiss();
    }
}
