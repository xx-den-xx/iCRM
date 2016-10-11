package ru.bda.icrm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.ContragentRecyclerAdapter;
import ru.bda.icrm.adapter.RecyclerEventAdapter;
import ru.bda.icrm.database.DBController;

/**
 * Created by User on 31.08.2016.
 */
public class EventsFragment extends Fragment implements View.OnClickListener{

    private TextView mTvNow;
    private ImageView mIvBack;
    private ImageView mIvNext;
    private ImageView mIvCalendar;
    private LinearLayout mLlMore;
    private ProgressBar mProgressBar;

    private RecyclerView mRVEvent;
    private RecyclerEventAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mTvNow = (TextView) view.findViewById(R.id.tv_now);
        mTvNow.setOnClickListener(this);
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvNext = (ImageView) view.findViewById(R.id.iv_next);
        mIvNext.setOnClickListener(this);
        mIvCalendar = (ImageView) view.findViewById(R.id.iv_calendar);
        mIvCalendar.setOnClickListener(this);
        mLlMore = (LinearLayout) view.findViewById(R.id.ll_more);
        mLlMore.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    private void initRecyclerContent(View view) {

    }

    @Override
    public void onClick(View v) {

    }
}
