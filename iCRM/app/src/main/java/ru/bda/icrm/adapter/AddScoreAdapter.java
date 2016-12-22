package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        holder.etCoast.setText(price.getPrice() + "");
        final double coast = Double.parseDouble(holder.etCoast.getText().toString());
        final double number = Double.parseDouble(holder.etNumberProducts.getText().toString());
        holder.etNumberProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    PriceSum priceSum = price;
                    double sum = Integer.parseInt(s.toString()) * Double.parseDouble(holder.etCoast.getText().toString());
                    priceSum.setSum(Integer.parseInt(s.toString()));
                    priceSum.setTotlalCoast(new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    priceSum.setPrice(Double.parseDouble(holder.etCoast.getText().toString()));
                    listener.onChangeCoast(priceSum);
                    holder.tvSumm.setText(sum+"");
                    Log.d("log-score", "price = " + priceSum.getPrice() + "; total coast = " + priceSum.getTotalCoast());
                } catch (Exception e) {
                    holder.tvSumm.setText("Общая стоимость: " + Double.parseDouble(holder.etCoast.getText().toString()) + " RUB");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etCoast.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    PriceSum priceSum = price;
                    int number = !s.toString().equals("") ? Integer.parseInt(s.toString()) : 0;
                    double sum = Double.parseDouble(s.toString()) * Double.parseDouble(holder.etNumberProducts.getText().toString());
                    priceSum.setSum(number);
                    priceSum.setTotlalCoast(new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    if (!s.toString().equals("")) priceSum.setPrice(Double.parseDouble(s.toString()));
                    listener.onChangeCoast(priceSum);
                    holder.tvSumm.setText("Общая стоимость: " + sum + " RUB");
                    Log.d("log-score", "price = " + priceSum.getPrice() + "; total coast = " + priceSum.getTotalCoast());
                } catch (Exception e) {
                    double summer = (!s.toString().equals("")) ? Double.parseDouble(holder.etCoast.getText().toString()) : 0;
                    holder.tvSumm.setText("Общая стоимость: " + summer + " RUB");
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
        EditText etCoast;
        TextView tvSumm;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            etNumberProducts = (EditText) itemView.findViewById(R.id.et_number_products);
            etCoast = (EditText) itemView.findViewById(R.id.et_coast);
            tvSumm = (TextView) itemView.findViewById(R.id.tv_summ);
        }
    }
}
