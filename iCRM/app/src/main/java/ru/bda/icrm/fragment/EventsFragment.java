package ru.bda.icrm.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.activity.LoginActivity;
import ru.bda.icrm.adapter.ContragentRecyclerAdapter;
import ru.bda.icrm.adapter.RecyclerEventAdapter;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;
import ru.yandex.m;

/**
 * Created by User on 31.08.2016.
 */
public class EventsFragment extends Fragment implements View.OnClickListener{

    private TextView mTvNow;
    private TextView mTvAll;
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
    private boolean isAddToDb = false;
    private Calendar mChangeDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        mDbController = new DBController(getActivity());
        mChangeDate = Calendar.getInstance();
        initContent(view);
        initRecyclerContent(view);
        new EventSendTask().execute();
        return view;
    }

    private void initContent(View view) {
        mTvNow = (TextView) view.findViewById(R.id.tv_now);
        mTvNow.setOnClickListener(this);
        mTvAll = (TextView) view.findViewById(R.id.tv_all);
        mTvAll.setOnClickListener(this);
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
        mAdapter = new RecyclerEventAdapter(getActivity(), mEventList, Calendar.getInstance());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVEvent.setLayoutManager(mLayoutManager);
        mRVEvent.setAdapter(mAdapter);
        mRVEvent.setHasFixedSize(true);
    }

    public void setEvent(Event event) {
        mEvent = event;
        isAddToDb = true;
        if (mEvent != null) {
            new EventSendTask().execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_now:
                Calendar calendar = Calendar.getInstance();
                mChangeDate = calendar;
                setDateCalendar(getDate(mChangeDate.getTimeInMillis(), "dd.MM.yyyy"));
                break;
            case R.id.iv_back:
                mChangeDate = getChangedDate(mChangeDate, false);
                setDateCalendar(getDate(mChangeDate.getTimeInMillis(), "dd.MM.yyyy"));
                break;
            case R.id.iv_next:
                mChangeDate = getChangedDate(mChangeDate, true);
                setDateCalendar(getDate(mChangeDate.getTimeInMillis(), "dd.MM.yyyy"));
                break;
            case R.id.iv_calendar:
                DatePickerDialog dateDialog = new DatePickerDialog();
                dateDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        mChangeDate = calendar;
                        String dayCalendar = getDate(calendar.getTimeInMillis(), "dd.MM.yyyy");
                        setDateCalendar(dayCalendar);
                    }
                });
                dateDialog.show(getActivity().getFragmentManager().beginTransaction(), "");
                break;
            case R.id.tv_all:
                mAdapter.setEventList(mEventList);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private Calendar getChangedDate(Calendar calendar, boolean top) {
        Calendar newDate = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        if (!top) date.setDate(date.getDay() - 1);
        else date.setDate(date.getDay() + 1);
        newDate.setTime(date);
        Log.d("event_log", getDate(newDate.getTimeInMillis(),"dd.MM.yyyy"));
        return newDate;
    }

    private void setDateCalendar(String dayCalendar) {
        List<Event> events = new ArrayList<Event>();
        for (Event event : mEventList) {
            long date = event.getTimeBegin();
            String dayEvent = getDate(date, "dd.MM.yyyy");
            if (dayEvent.equals(dayCalendar)) {
                events.add(event);
            }
        }
        if (events != null && events.size() > 0) {
            mAdapter.setEventList(events);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "На " + dayCalendar + " нет событий", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDate(long milliseconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }

    private List<Event> refreshEventList(List<Event> events) {
        List<Event> eventList = new ArrayList<>();
        Calendar nowDay = Calendar.getInstance();
        Calendar preDay = nowDay;
        boolean isFirstCycle = true;
        for (Event event : events) {
            Calendar eventDayCalendar = Calendar.getInstance();
            eventDayCalendar.setTimeInMillis(event.getTimeBegin());
            String eventDay = getDate(event.getTimeBegin(), "dd.MM.yyyy");
            String preDayString = getDate(preDay.getTimeInMillis(), "dd.MM.yyyy");
            boolean isFirstDay = false;
            boolean isNowDay = false;
            if (eventDay.equals(getDate(nowDay.getTimeInMillis(), "dd.MM.yyyy"))) {
                if (isFirstCycle) {
                    isFirstDay = true;
                    isNowDay = true;
                }
            } else if (!eventDay.equals(getDate(nowDay.getTimeInMillis(), "dd.MM.yyyy"))) {
                if (isFirstCycle) {
                    isFirstDay = true;
                    isNowDay = false;
                } else {
                    if (!eventDay.equals(preDayString)) {
                        isFirstDay = true;
                        isNowDay = false;
                    }
                }
            }
            isFirstCycle = false;
            preDay.setTimeInMillis(event.getTimeBegin());

            event.setFirstDay(isFirstDay);
            event.setNowDay(isNowDay);
            eventList.add(event);
        }
        return eventList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbController.closeDb();
    }

    private class EventSendTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isAddToDb) {
                synchronized (DBController.class) {
                    mDbController.addEventToDB(mEvent);
                }
            }
            mEventList = new ArrayList<>();
            mEventList.addAll(mDbController.getEvent());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (mEventList != null && mEventList.size() > 0) {
                mEventList = refreshEventList(mEventList);
            }
            mAdapter.setEventList(mEventList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
