package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.tvPhone.setText(call.getPhone());
        holder.tvDate.setText(call.getType() == 0 ? "Исходящий" : "Входящий");
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

        public ViewHolder(View itemView) {
            super(itemView);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
