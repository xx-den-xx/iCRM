package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnChangeCoastListener;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;

/**
 * Created by User on 26.10.2016.
 */

public class AddScoreAdapter extends RecyclerView.Adapter<AddScoreAdapter.ViewHolder> {

    private List<PriceSum> priceList;
    private OnChangeCoastListener listener;

    public AddScoreAdapter (List<PriceSum> priceList) {
        this.priceList = priceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_score_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PriceSum price = priceList.get(position);
        holder.tvProductName.setText(price.getTitle());
        holder.etNumberProducts.setText("" + 1);
        final double coast = price.getPrice();
        holder.etNumberProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    PriceSum priceSum = price;
                    double sum = Integer.parseInt(s.toString()) * coast;
                    priceSum.setSum(Integer.parseInt(s.toString()));
                    priceSum.setTotlalCoast(new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    listener.onChangeCoast(priceSum);
                    holder.tvSumm.setText(sum + " RUB");
                } catch (Exception e) {
                    holder.tvSumm.setText(coast + " RUB");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.tvSumm.setText(price.getPrice() + " RUB");
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public void setPriceList(List<PriceSum> priceList) {
        this.priceList = priceList;
    }

    public void setOnChangeCoastListener (OnChangeCoastListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        EditText etNumberProducts;
        TextView tvSumm;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            etNumberProducts = (EditText) itemView.findViewById(R.id.et_number_products);
            tvSumm = (TextView) itemView.findViewById(R.id.tv_summ);
        }
    }
}
