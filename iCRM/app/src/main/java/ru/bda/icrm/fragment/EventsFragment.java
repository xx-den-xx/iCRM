package ru.bda.icrm.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.ContragentRecyclerAdapter;
import ru.bda.icrm.adapter.RecyclerEventAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;

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
    private LinearLayoutManager mLayoutManager;
    private Event mEvent;
    private List<Event> mEventList = new ArrayList<>();
    private DBController mDbController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        mDbController = new DBController(getActivity());
        initContent(view);
        initRecyclerContent(view);
        new EventSendTask().execute();
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
        mRVEvent = (RecyclerView) view.findViewById(R.id.recycler_view_event);
        mAdapter = new RecyclerEventAdapter(mEventList, Calendar.getInstance());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVEvent.setLayoutManager(mLayoutManager);
        mRVEvent.setAdapter(mAdapter);
        mRVEvent.setHasFixedSize(true);
    }

    public void setEvent(Event event) {
        mEvent = event;
        if (event != null) {
            new EventSendTask().execute();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class EventSendTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (DBController.class){
                mDbController.addEventToDB(mEvent);
            }
            mEventList.addAll(mDbController.getEvent());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
