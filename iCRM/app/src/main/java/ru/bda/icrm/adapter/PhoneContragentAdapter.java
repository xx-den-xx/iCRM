package ru.bda.icrm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnCallClickListener;
import ru.bda.icrm.model.Phone;

/**
 * Created by User on 04.11.2016.
 */

public class PhoneContragentAdapter extends RecyclerView.Adapter<PhoneContragentAdapter.ViewHolder> {

    private List<Phone> mPhoneList;
    private Context ctx;
    private OnCallClickListener listener;

    public PhoneContragentAdapter(Context ctx, List<Phone> list){
        this.mPhoneList = list;
        this.ctx = ctx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Phone phone = mPhoneList.get(position);
        if (phone.getType() > 0) {
            holder.tvType.setText("Мобильный");
        } else {
            holder.tvType.setText("Рабочий");
        }
        holder.tvNumber.setText(phone.getNumber());
        holder.ivCall.setColorFilter(ctx.getResources().getColor(R.color.colorPrimaryDark));
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallClick(phone.getNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    public void setPhoneList(List<Phone> list) {
        this.mPhoneList = list;
    }

    public void setOnCallClickListener (OnCallClickListener listener) {
        this.listener = listener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvNumber;
        ImageView ivCall;
        ImageView ivSms;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_phone);
            ivCall = (ImageView) itemView.findViewById(R.id.iv_call);
        }
    }
}
