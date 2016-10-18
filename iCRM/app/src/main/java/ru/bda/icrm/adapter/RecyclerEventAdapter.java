package ru.bda.icrm.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
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
    private Date mBeginDate;
    private Date mEndDate;
    private Calendar mDate;
    private int countEvent = 0;

    public RecyclerEventAdapter (List<Event> eventList, Calendar date) {
        this.mEventList = eventList;
        this.mDate = date;
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
        if (!event.getDate().equals("null") && countEvent == 0 && event.getDate().equals(dateEvent)) {
            holder.layoutDate.setVisibility(View.VISIBLE);
            String timeFormat = getDate(event.getTimeBegin(), "EEEE, dd MMMM");
            dateTitle = "Сегодня, " + timeFormat;
            holder.tvDate.setText(dateTitle);
        }
        holder.tvEvent.setText(getDate(event.getTimeBegin(), "HH:mm") + " - " + getDate(event.getTimeEnd(), "HH:mm, ")
                + event.getMessage());
        countEvent++;
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
