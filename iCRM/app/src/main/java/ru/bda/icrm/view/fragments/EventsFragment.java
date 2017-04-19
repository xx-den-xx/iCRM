package ru.bda.icrm.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.bda.icrm.R;
import ru.bda.icrm.adapter.RecyclerEventAdapter;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.EventDTO;
import ru.bda.icrm.presenter.EventFragmentPresenter;
import ru.bda.icrm.view.EventFragmentView;

public class EventsFragment extends Fragment implements View.OnClickListener, EventFragmentView{

    @Bind(R.id.tv_now)
    TextView mTvNow;

    @Bind(R.id.tv_all)
    TextView mTvAll;

    @Bind(R.id.iv_back)
    ImageView mIvBack;

    @Bind(R.id.iv_next)
    ImageView mIvNext;

    @Bind(R.id.iv_calendar)
    ImageView mIvCalendar;

    @Bind(R.id.ll_more)
    LinearLayout mLlMore;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.recycler_view_event)
    RecyclerView mRVEvent;

    private RecyclerEventAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Event mEvent;
    private List<Event> mEventList = new ArrayList<>();
    private DBController mDbController;
    private boolean isAddToDb = false;
    private Calendar mChangeDate;
    private EventFragmentPresenter presenter;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        ButterKnife.bind(this, view);
        presenter = new EventFragmentPresenter(this, getContext());
        token = AppPref.getInstance().getStringPref(AppPref.PREF_TOKEN, getContext());
        mDbController = new DBController(getActivity());
        mChangeDate = Calendar.getInstance();
        initContent();
        initRecyclerContent();
        presenter.getEventList(new Token(token));
        //new EventSendTask().execute();
        return view;
    }

    private void initContent() {
        mTvNow.setOnClickListener(this);
        mTvAll.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvCalendar.setOnClickListener(this);
        mLlMore.setOnClickListener(this);
    }

    private void initRecyclerContent() {
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
            presenter.makeEvent(new EventDTO(mEvent, token));
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
                dateDialog.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year, monthOfYear, dayOfMonth);
                    mChangeDate = calendar1;
                    String dayCalendar = getDate(calendar1.getTimeInMillis(), "dd.MM.yyyy");
                    setDateCalendar(dayCalendar);
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

    @Override
    public void showError(String error) {
        Snackbar.make(mProgressBar, error, Snackbar.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void makeEvent(List<Event> events) {
        mProgressBar.setVisibility(View.GONE);
        mEventList = new ArrayList<>();
        mEventList.addAll(events);
        if (mEventList != null && mEventList.size() > 0) {
            mEventList = refreshEventList(mEventList);
        }

        mAdapter.setEventList(mEventList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateEvent(Event event) {

    }

    @Override
    public void deleteEvent(Event event) {

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
                    mDbController.closeDb();
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
