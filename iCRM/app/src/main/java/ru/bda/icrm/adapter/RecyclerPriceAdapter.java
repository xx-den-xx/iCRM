package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.AddPriceClickListener;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;

/**
 * Created by User on 12.10.2016.
 */

public class RecyclerPriceAdapter extends RecyclerView.Adapter<RecyclerPriceAdapter.ViewHolder> {

    private List<PriceSum> mPriceList;
    private AddPriceClickListener priceListener;

    public RecyclerPriceAdapter(List<PriceSum> list) {
        this.mPriceList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PriceSum price = mPriceList.get(position);
        String title = price.getTitle();
        double coast = price.getPrice();
        holder.title.setText(title);
        holder.price.setText(coast + " RUB");
        holder.priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               priceListener.addPriceListener(price);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPriceList.size();
    }

    public void setPriceList(List<PriceSum> list) {
        this.mPriceList = list;
    }

    public void addPriceClickListener(AddPriceClickListener priceListener) {
        this.priceListener = priceListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout priceLayout;
        public TextView title;
        public TextView price;
        public ViewHolder(View itemView) {
            super(itemView);
            priceLayout = (LinearLayout) itemView.findViewById(R.id.price_layout);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}
