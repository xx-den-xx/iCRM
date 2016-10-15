package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Price;

/**
 * Created by User on 12.10.2016.
 */

public class RecyclerPriceAdapter extends RecyclerView.Adapter<RecyclerPriceAdapter.ViewHolder> {

    private List<Price> mPriceList;

    public RecyclerPriceAdapter(List<Price> list) {
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
        final Price price = mPriceList.get(position);
        String title = price.getTitle();
        String coast = price.getPrice();
        holder.title.setText(title);
        holder.price.setText(coast);
    }

    @Override
    public int getItemCount() {
        return mPriceList.size();
    }

    public void setPriceList(List<Price> list) {
        this.mPriceList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView price;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}
