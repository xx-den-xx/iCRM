package ru.bda.icrm.fragment;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

@SuppressLint("ValidFragment")
public class TabScoreFragment extends Fragment {

    private Score mScore;
    private TextView mTvClient;
    private TextView mTvContact;
    private ImageView mIvInfoClient;
    private ImageView mIvInfoContact;
    private ImageView mIvPriority;
    private ImageView mIvStatus;

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
        mTvContact = (TextView) view.findViewById(R.id.tv_contact);
        mIvInfoClient = (ImageView) view.findViewById(R.id.iv_info_client);
        mIvInfoContact = (ImageView) view.findViewById(R.id.iv_info_contact);
        mIvPriority = (ImageView) view.findViewById(R.id.iv_priority);
        mIvStatus = (ImageView) view.findViewById(R.id.iv_status);
        setColor();
    }

    private void setColor() {
        mIvPriority.setColorFilter(getResources().getColor(getColor(mScore.getPriority())), PorterDuff.Mode.SRC_ATOP);
        mIvStatus.setColorFilter(getResources().getColor(getColor(mScore.getStatus())), PorterDuff.Mode.SRC_ATOP);
        mIvInfoClient.setColorFilter(getResources().getColor(R.color.color_green), PorterDuff.Mode.SRC_ATOP);
        mIvInfoContact.setColorFilter(getResources().getColor(R.color.color_green), PorterDuff.Mode.SRC_ATOP);
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

}
