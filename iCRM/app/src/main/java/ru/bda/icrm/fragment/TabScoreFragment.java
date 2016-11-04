package ru.bda.icrm.fragment;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.bda.icrm.R;
import ru.bda.icrm.dialog.PriorityDialog;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.listener.OnPriorityClickListener;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

@SuppressLint("ValidFragment")
public class TabScoreFragment extends Fragment implements View.OnClickListener{

    private Score mScore;
    private TextView mTvClient;
    private EditText mTvContact;
    private EditText mTvContract;
    private TextView mTvResponsible;
    private ImageView mIvPriority;
    private ImageView mIvStatus;
    private LinearLayout mLayoutPriority;
    private LinearLayout mLayoutStatus;
    private int statusInt = 1;
    private int priorityInt = 1;

    public TabScoreFragment (Score mScore) {
        this.mScore = mScore;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_score, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mTvClient = (TextView) view.findViewById(R.id.tv_client);
        mTvContact = (EditText) view.findViewById(R.id.tv_contact);
        mTvContract = (EditText) view.findViewById(R.id.tv_contract);
        mTvResponsible = (TextView) view.findViewById(R.id.tv_responsible);
        mIvPriority = (ImageView) view.findViewById(R.id.iv_priority);
        mIvStatus = (ImageView) view.findViewById(R.id.iv_status);
        mLayoutPriority = (LinearLayout) view.findViewById(R.id.layout_priority);
        mLayoutPriority.setOnClickListener(this);
        mLayoutStatus = (LinearLayout) view.findViewById(R.id.layout_status);
        mLayoutStatus.setOnClickListener(this);
        setColor();
        setDataContent();
    }

    private void setDataContent() {
        mTvClient.setText(mScore.getClient().getNameContragent());
        mTvContact.setText(mScore.getContactFace());
        mTvContract.setText(mScore.getContract());
        mTvResponsible.setText(mScore.getResponsible());

    }

    private void setColor() {
        mIvPriority.setColorFilter(getResources().getColor(getColor(priorityInt)), PorterDuff.Mode.SRC_ATOP);
        mIvStatus.setColorFilter(getResources().getColor(getColor(statusInt)), PorterDuff.Mode.SRC_ATOP);
    }

    private int getColor(int status) {
        int color = R.color.color_green;
        if (mScore != null) {
            if (status == 0) {
                color = R.color.color_orange;
            } else if (status == 1) {
                color = R.color.color_green;
            } else if (status == 2) {
                color = R.color.color_red;
            }
        }
        return color;
    }

    public void setScore(Score score) {
        this.mScore = score;
        statusInt = score.getStatus() + 1;
        priorityInt = score.getPriority();
        //setDataContent();
    }

    public Score getScore() {
        mScore.setContract(mTvContract.getText().toString());
        mScore.setContactFace(mTvContact.getText().toString());
        return mScore;
    }

    @Override
    public void onClick(final View v) {
        PriorityDialog dialog = new PriorityDialog();
        String title = "";
        String[] statusArray = {};
        int status = 1;
        if (v.getId() == R.id.layout_priority) {
            title = Constants.PRIORITY_TITLE;
            statusArray = getActivity().getResources().getStringArray(R.array.score_priority);
            status = priorityInt;
        } else if (v.getId() == R.id.layout_status) {
            title = Constants.STATUS_TITLE;
            statusArray = getActivity().getResources().getStringArray(R.array.score_status);
            status = statusInt;
        }

        dialog.init(title, statusArray, status, new OnPriorityClickListener() {
            @Override
            public void onPriorityClick(int priority) {
                if (v.getId() == R.id.layout_priority) {
                    priorityInt = priority;
                    mScore.setPriority(priority);
                }
                else if (v.getId() == R.id.layout_status) {
                    Log.d("myLog", "status = " + priority);
                    statusInt = priority + 1;
                    mScore.setStatus(priority);
                }
                setColor();
            }
        });
        dialog.show(getActivity());
    }
}
