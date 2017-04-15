package ru.bda.icrm.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.bda.icrm.R;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.holders.AppPref;
import ru.bda.icrm.listener.AddEventClickListener;
import ru.bda.icrm.model.Event;

/**
 * Created by User on 06.10.2016.
 */

public class AddEventDialog extends DialogFragment  implements View.OnClickListener{

    private final static int DIALOG_TIME = 1;
    private final static int DIALOG_DATE = 2;

    private AddEventClickListener listener;
    private TextView mTvTimeBegin;
    private TextView mTvDateBegin;
    private TextView mTvTimeEnd;
    private TextView mTvDateEnd;
    private int mIdView;
    private Button mBtnLeft;
    private Button mBtnRight;
    private EditText mEtMessage;

    private Calendar date;
    private int hour;
    private int hourBegin;
    private int hourEnd;
    private int minute;
    private int minuteBegin;
    private int minuteEnd;
    private int day;
    private int dayBegin;
    private int dayEnd;
    private int month;
    private int monthBegin;
    private int monthEnd;
    private int year;
    private int yearBegin;
    private int yearEnd;
    private String dateEvent;
    private String message;

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
        View rootView = inflater.inflate(R.layout.dialog_add_event, container, false);

        date = Calendar.getInstance();
        hour = hourBegin = hourEnd = date.get(Calendar.HOUR_OF_DAY);
        minute = minuteBegin = minuteEnd = date.get(Calendar.MINUTE);
        day = dayBegin = dayEnd = date.get(Calendar.DAY_OF_MONTH);
        month = monthBegin = monthEnd = date.get(Calendar.MONTH);
        year = yearBegin = yearEnd = date.get(Calendar.YEAR);

        initContent(rootView);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return rootView;
    }

    private void initContent(View rootView) {
        mEtMessage = (EditText) rootView.findViewById(R.id.et_message);
        mBtnLeft = (Button) rootView.findViewById(R.id.btn_left);
        mBtnLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeftBtnClick();
            }
            dismiss();
        });

        mBtnRight = (Button) rootView.findViewById(R.id.btn_right);
        mBtnRight.setOnClickListener(v -> {
            Event event = new Event();
            if (listener != null) {
                message = mEtMessage.getText().toString();
                long timeBegin = timeMilliseconds(hourBegin, minuteBegin, dayBegin, monthBegin + 1, yearBegin);
                long timeEnd = timeMilliseconds(hourEnd, minuteEnd, dayEnd, monthEnd + 1, yearEnd);
                Log.d("myLog", "time begin = " + timeBegin + "; time end = " + timeEnd);
                if (timeEnd > timeBegin && !message.equals("")) {
                    event.setUser(AppPref.getInstance().getStringPref(AppPref.PREF_HEX_LOGIN, getActivity()));
                    event.setTimeBegin(timeBegin);
                    event.setTimeEnd(timeEnd);
                    event.setDate(dateEvent);
                    event.setMessage(message);
                    event.setId(0);
                    listener.onRightBtnClick(event);
                    dismiss();
                } else {
                    Context ctx = getActivity();
                    String errorTime = ctx.getResources().getString(R.string.add_event_error_time);
                    String errorMessage = ctx.getResources().getString(R.string.add_event_error_message);
                    String errorTitle = ctx.getResources().getString(R.string.dialog_error_title);

                    if (timeEnd <= timeBegin) {
                        MyDialog dialog = new MyDialog();
                        dialog.init(errorTitle, errorTime, null, "Ok", new MyDialog.OnClickListener() {
                            @Override
                            public void onLeftBtnClick() {

                            }

                            @Override
                            public void onRightBtnClick() {

                            }
                        });
                        dialog.show(getActivity());

                    }  else if (message.equals("")){
                        MyDialog dialog = new MyDialog();
                        dialog.init(errorTitle, errorMessage, null, "Ok", new MyDialog.OnClickListener() {
                            @Override
                            public void onLeftBtnClick() {

                            }

                            @Override
                            public void onRightBtnClick() {

                            }
                        });
                        dialog.show(getActivity());
                    }
                }
            }
        });
        String timeString = (String.valueOf(hour).length() < 2 ? "0" + hour : "" + hour) +
                ":" + (String.valueOf(minute).length() < 2 ? "0" + minute : "" + minute);

        int convertMonth = month + 1;

        String dateString = (String.valueOf(day).length() < 2 ? "0" + day : "" + day) +
                "/" + (String.valueOf(convertMonth).length() < 2 ? "0" + (convertMonth) : "" + (convertMonth)) +
                "/" + (String.valueOf(year).length() < 2 ? "0" + year : "" + year);

        dateEvent =  dateString;

        mTvTimeBegin = (TextView) rootView.findViewById(R.id.tv_time_begin);
        mTvTimeBegin.setOnClickListener(this);
        mTvTimeBegin.setText(timeString);
        mTvTimeEnd = (TextView) rootView.findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mTvTimeEnd.setText(timeString);
        mTvDateBegin = (TextView) rootView.findViewById(R.id.tv_date_begin);
        mTvDateBegin.setOnClickListener(this);
        mTvDateBegin.setText(dateString);
        mTvDateEnd = (TextView) rootView.findViewById(R.id.tv_date_end);
        mTvDateEnd.setOnClickListener(this);
        mTvDateEnd.setText(dateString);
    }

    private long timeMilliseconds(int hour, int minute, int day, int month, int year) {
        long milliseconds = 0;
        String dateString = (String.valueOf(hour).length()<2 ? "0" + hour : "" + hour)+ ":"
                + (String.valueOf(minute).length()<2 ? "0" + minute : "" + minute) + ":00 "
                + (String.valueOf(day).length()<2 ? "0" + day : "" + day) + "-"
                + (String.valueOf(month).length()<2 ? "0" + month : "" + month) + "-"+ year;
        Log.d("myLog", "time String :: " + dateString);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        try {
            Date mDate = sdf.parse(dateString);
            milliseconds = mDate.getTime();
        } catch(ParseException e){}
        return milliseconds;
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public AddEventDialog init(AddEventClickListener clickListener) {
        this.listener = clickListener;
        return this;
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = new Dialog(getActivity());
        if(id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(getActivity(), myTimeCallBack, hour, minute, true);
            return tpd;
        } else if (id == DIALOG_DATE) {
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), myDateCallBack, year, month, day);
            return dpd;
        }
        return null;
    }

    TimePickerDialog.OnTimeSetListener myTimeCallBack = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = (String.valueOf(hourOfDay).length() < 2 ? "0" + hourOfDay : "" + hourOfDay) +
                    ":" + (String.valueOf(minute).length() < 2 ? "0" + minute : "" + minute);

            if (mIdView == R.id.tv_time_begin) {
                hourBegin = hourOfDay;
                minuteBegin = minute;
                mTvTimeBegin.setText(timeString);
            } else if(mIdView == R.id.tv_time_end) {
                hourEnd = hourOfDay;
                minuteEnd = minute;
                mTvTimeEnd.setText(timeString);
            }

        }
    };

    DatePickerDialog.OnDateSetListener myDateCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dateString = AppControl.getInstance().getDateString(year, monthOfYear + 1, dayOfMonth);

            if (mIdView == R.id.tv_date_begin) {
                yearBegin = year;
                monthBegin = monthOfYear;
                dayBegin = dayOfMonth;
                dateEvent =  dateString;
                mTvDateBegin.setText(dateString);
            } else if(mIdView == R.id.tv_date_end) {
                yearEnd = year;
                monthEnd = monthOfYear;
                dayEnd = dayOfMonth;
                mTvDateEnd.setText(dateString);
            }
        }
    };

    public void setAddEventClickListener(AddEventClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener = null;
    }

    @Override
    public void onClick(View v) {
        mIdView = v.getId();
        int id = DIALOG_TIME;
        switch (mIdView) {
            case R.id.tv_time_begin:
                id = DIALOG_TIME;
                break;
            case R.id.tv_date_begin:
                id = DIALOG_DATE;
                break;
            case R.id.tv_time_end:
                id = DIALOG_TIME;
                break;
            case R.id.tv_date_end:
                id = DIALOG_DATE;
                break;
            default:
                break;
        }
        onCreateDialog(id).show();
    }
}
