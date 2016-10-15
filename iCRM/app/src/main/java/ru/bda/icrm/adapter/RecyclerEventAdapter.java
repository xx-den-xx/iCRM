package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Event;

/**
 * Created by User on 04.10.2016.
 */

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.ViewHolder>{

    private List<Event> mEventList;
    private Date mBeginDate;
    private Date mEndDate;
    private Calendar mDate;

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
        //mBeginDate;
    }

    private Date getDate(long milliSeconds, String dateFormat)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return (calendar.getTime());
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
