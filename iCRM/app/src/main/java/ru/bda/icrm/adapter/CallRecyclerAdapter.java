package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Call;

/**
 * Created by User on 17.11.2016.
 */

public class CallRecyclerAdapter extends RecyclerView.Adapter<CallRecyclerAdapter.ViewHolder> {

    private List<Call> mListCall;

    public CallRecyclerAdapter (List<Call> list) {
        this.mListCall = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Call call = mListCall.get(position);
        holder.tvPhone.setText(call.getPhone() + " (" + call.getType() +")");
        String date = getDate(call.getTime(), "dd.MM.yyyy hh:mm:ss");
        holder.tvDate.setText(date);

        holder.tvDuration.setText("Длительность:" + call.getDuration() + " сек.");
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
        return mListCall.size();
    }

    public void setListCall (List<Call> list) {
        this.mListCall = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPhone;
        TextView tvDate;
        TextView tvDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }
}
