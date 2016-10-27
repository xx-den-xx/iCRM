package ru.bda.icrm.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.bda.icrm.R;
import ru.bda.icrm.holders.AppControl;
import ru.bda.icrm.model.Event;

/**
 * Created by User on 04.10.2016.
 */

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.ViewHolder>{

    private List<Event> mEventList;
    private Context mContext;
    private Date mBeginDate;
    private Date mEndDate;
    private Calendar mDate;
    private boolean isNewDay = true;
    private int countEvent = 0;
    private int dayPrevious = 0;
    private int monthPrevious = 0;
    private int yearPrevious = 0;

    public RecyclerEventAdapter (Context ctx, List<Event> eventList, Calendar date) {
        this.mEventList = eventList;
        this.mDate = date;
        this.mContext = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEventList.get(position);
        String dateEvent = AppControl.getInstance().getDateString(mDate.get(Calendar.YEAR),
                mDate.get(Calendar.MONTH) + 1, mDate.get(Calendar.DAY_OF_MONTH));
        String dateTitle = "";
        int dayNow = mDate.get(Calendar.DAY_OF_MONTH);
        int monthNow = mDate.get(Calendar.MONTH) + 1;
        int yearNow = mDate.get(Calendar.YEAR);
        StringBuilder sbDay = new StringBuilder();
        sbDay.append(event.getDate().charAt(0));
        sbDay.append(event.getDate().charAt(1));
        StringBuilder sbMonth = new StringBuilder();
        sbMonth.append(event.getDate().charAt(3));
        sbMonth.append(event.getDate().charAt(4));
        StringBuilder sbYear = new StringBuilder();
        sbYear.append(event.getDate().charAt(6));
        sbYear.append(event.getDate().charAt(7));
        sbYear.append(event.getDate().charAt(8));
        sbYear.append(event.getDate().charAt(9));
        int dayEvent = Integer.parseInt(sbDay.toString());
        int monthEvent = Integer.parseInt(sbMonth.toString());
        int yearEvent = Integer.parseInt(sbYear.toString());
        if ((dayNow != dayEvent) || (monthNow != monthEvent) || (yearNow != yearEvent)) {
            if (dayPrevious == 0 || monthPrevious == 0 || yearPrevious == 0) {
                isNewDay = true;
            }
        }
        if (dayPrevious != dayEvent || monthPrevious != monthEvent || yearPrevious != yearEvent) {
            if (dayPrevious != 0 || monthPrevious != 0 || yearPrevious != 0) {
                isNewDay = true;
            }
        }
        if (!event.getDate().equals("null") && countEvent == 0 && event.getDate().equals(dateEvent)) {
            holder.layoutDate.setVisibility(View.VISIBLE);
            holder.layoutDate.setBackgroundColor(mContext.getResources().getColor(R.color.color_event_date_now_background));
            String timeFormat = getDate(event.getTimeBegin(), "EEEE, dd MMMM");
            dateTitle = "Сегодня, " + timeFormat;
            holder.tvDate.setText(dateTitle);
            isNewDay = false;
            countEvent++;
        } else if (isNewDay && !event.getDate().equals(dateEvent)) {
            holder.layoutDate.setVisibility(View.VISIBLE);
            holder.layoutDate.setBackgroundColor(mContext.getResources().getColor(R.color.color_event_date_background));
            String monthFormat = firstUpperCase(getDate(event.getTimeBegin(), "EEEE, "));
            String timeFormat = getDate(event.getTimeBegin(), "dd MMMM");
            holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.divider_black));
            holder.tvDate.setText(monthFormat + timeFormat);
            isNewDay = false;
        }
        holder.tvEvent.setText(getDate(event.getTimeBegin(), "HH:mm") + " - " + getDate(event.getTimeEnd(), "HH:mm, ")
                + event.getMessage());
        dayPrevious = dayEvent;
        monthPrevious = monthEvent;
        yearPrevious = yearEvent;
    }

    private String firstUpperCase (String word) {
        if (word == null || word.isEmpty() || word.equals("null")) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        //formatter.setTimeZone(TimeZone.getTimeZone("GMS"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void setEventList(List<Event> list) {
        this.mEventList = list;
        dayPrevious = 0;
        monthPrevious = 0;
        yearPrevious = 0;
        countEvent = 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutDate;
        TextView tvDate;
        View topDivider;
        TextView tvEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutDate = (LinearLayout) itemView.findViewById(R.id.ll_date);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            topDivider = itemView.findViewById(R.id.top_divider);
            tvEvent = (TextView) itemView.findViewById(R.id.tv_event);
        }
    }
}
